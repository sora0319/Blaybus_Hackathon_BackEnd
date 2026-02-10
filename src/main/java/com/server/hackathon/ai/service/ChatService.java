package com.server.hackathon.ai.service;

import com.server.hackathon.ai.controller.dto.response.ChatMessageResponse;
import com.server.hackathon.ai.model.ChatMessage;
import com.server.hackathon.ai.model.ChatRoom;
import com.server.hackathon.ai.model.vo.MessageRole;
import com.server.hackathon.ai.repository.ChatMessageRepository;
import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.repository.ModelInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final VectorStore vectorStore;

    private final ModelInstanceRepository  modelInstanceRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public String processMessage(String memberUuid, String modelUuid, String userMessageText) {
        ModelInstance modelInstance = modelInstanceRepository.findByMemberShortUuidAndModelShortUuid(memberUuid, modelUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "모델을 가지고 있지 않습니다"));

        ChatRoom chatRoom = modelInstance.getChatRoom();
        Model model = modelInstance.getModel();

        // 사용자 질문 DB 저장
        chatMessageRepository.save(new ChatMessage(MessageRole.USER, userMessageText, chatRoom));

        // Pinecone 벡터 검색 (RAG)
        List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userMessageText)
                        .topK(10)
                        .filterExpression(new FilterExpressionBuilder().eq("model_id", model.getId()).build())
                        .build()
        );

        String context = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        // 이전 대화 내역 조회
        List<ChatMessage> history = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());
        String conversationHistory = history.stream()
                .map(msg -> msg.getRole() + ": " + msg.getContent())
                .collect(Collectors.joining("\n"));

        // 프롬프트 엔지니어링
        String promptText = """
                당신은 사용자의 학습을 돕는 친절한 AI 튜터입니다.
                아래 정보를 바탕으로 답변하세요.
                
                [지식 베이스(Context)]
                {context}
                
                [이전 대화 내역(History)]
                {history}
                
                [사용자 질문]
                {question}
                
                답변은 명확하고 친절하게 한국어로 작성해주세요.
                """;

        PromptTemplate template = new PromptTemplate(promptText);
        Prompt prompt = template.create(Map.of(
                "context", context,
                "history", conversationHistory,
                "question", userMessageText
        ));

        // AI 응답 생성
        ChatClient chatClient = chatClientBuilder.build();
        String aiResponse = chatClient.prompt(prompt).call().content();

        // 6. AI 응답 DB 저장
        chatMessageRepository.save(new ChatMessage(MessageRole.ASSISTANT, aiResponse, chatRoom));

        return aiResponse;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(String memberUuid, String modelUuid) {
        ModelInstance modelInstance = modelInstanceRepository.findByMemberShortUuidAndModelShortUuid(memberUuid, modelUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "모델과 채팅 내역을 가지고 있지 않습니다"));

        ChatRoom chatRoom = modelInstance.getChatRoom();

        List<ChatMessage> entities = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());


        return entities.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }
}
