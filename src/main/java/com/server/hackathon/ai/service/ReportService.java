package com.server.hackathon.ai.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.server.hackathon.ai.model.ChatMessage;
import com.server.hackathon.ai.model.ChatRoom;
import com.server.hackathon.ai.repository.ChatMessageRepository;
import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.repository.ModelInstanceRepository;
import com.server.hackathon.note.model.Memo;
import com.server.hackathon.note.repository.MemoRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ModelInstanceRepository modelInstanceRepository;
    private final MemoRepository memoRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatClient.Builder chatClientBuilder;

    @Transactional
    public byte[] generatePdfReport(String memberUuid, String modelUuid) {
        // 1. 대화 내역 가져오기
        ModelInstance modelInstance = modelInstanceRepository.findByMemberShortUuidAndModelShortUuid(memberUuid, modelUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "모델과 채팅 내역을 가지고 있지 않습니다"));

        ChatRoom chatRoom = modelInstance.getChatRoom();

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());

        if (messages.isEmpty()) {
            throw new CustomException(HttpStatus.NO_CONTENT,"대화 내역이 없어 보고서를 만들 수 없습니다.");
        }

        String fullDialog = messages.stream()
                .map(m -> m.getRole() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));

        Memo memo = memoRepository.findByModelInstanceId(modelInstance.getId())
                .orElse(null);

        String memoContext = "";
        if(memo != null){
            memoContext = memo.getContent().title() + "\n" + memo.getContent().body();
        }

        // 2. AI 요약 요청
        String summaryText = """
                아래는 사용자와의 학습 대화 내역입니다.
                이 내용을 바탕으로 잘 정리된 '학습 결과 보고서'를 작성해주세요.
                서식은 일반 줄글 형태로 작성하되, 핵심 주제, 세부 내용, 결론으로 나누어 요약해주세요.
                
                [대화 내용]
                {context}
                
                [메모(Memo)]
                {memo}
                """;

        PromptTemplate template = new PromptTemplate(summaryText);
        Prompt prompt = template.create(Map.of(
                "context", fullDialog,
                "memo", memoContext
        ));

        ChatClient chatClient = chatClientBuilder.build();
        String summaryContent = chatClient.prompt(prompt).call().content();

        // 3. PDF 생성
        return createPdfDocument(summaryContent);
    }

    private byte[] createPdfDocument(String content) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // 한글 폰트 설정
            BaseFont baseFont = BaseFont.createFont(
                    new ClassPathResource("fonts/NotoSansCJKkr-Regular.otf").getURL().toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            Font titleFont = new Font(baseFont, 20, Font.BOLD);
            Font bodyFont = new Font(baseFont, 12, Font.NORMAL);

            // 문서 작성
            Paragraph title = new Paragraph("학습 정리 보고서", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph(content, bodyFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 실패", e);
        }
    }
}
