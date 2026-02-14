package com.server.hackathon.ai.service;

import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelPart;
import com.server.hackathon.model.model.ModelTheory;
import com.server.hackathon.model.model.ModelUsage;
import com.server.hackathon.model.repository.ModelRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DataInjectionService {

    private final ModelRepository modelRepository;
    private final VectorStore vectorStore;

    @Transactional(readOnly = true)
    public void ingestData() {
        List<Model> models = modelRepository.findAll();
        List<Document> documents = new ArrayList<>();

        for (Model model : models) {
            // 모델  저장
            String summaryContent = String.format("모델명: %s\n 개요: %s", model.getName(), model.getDescription());
            Document summaryDoc = new Document(summaryContent, Map.of(
                    "type", "summary",
                    "model_id", model.getId(),
                    "model_name", model.getName()
            ));
            documents.add(summaryDoc);

            // Parts 정보 개별 저장
            for (ModelPart part : model.getModelParts()) {
                // 검색에 유리하도록 텍스트를 재구성 (Context Enrichment)
                String partContent = String.format(
                        "모델명: %s\n 부품명: %s\n 재질: %s\n 설명: %s",
                        model.getName(), // 상위 모델명을 포함해야 검색 정확도 상승
                        part.getName(),
                        part.getMaterial(),
                        part.getDescription()
                );

                Document partDoc = new Document(partContent, Map.of(
                        "type", "part",
                        "model_id", model.getId(),
                        "modelPart_id", part.getId(),
                        "part_name", part.getName(),
                        "model_name", model.getName()
                ));
                documents.add(partDoc);
            }

            // Theory 정보 개별 저장
            for (ModelTheory theory : model.getTheory()) {
                String theoryContent = String.format(
                        "모델명: %s\n 이론 제목: %s\n 내용: %s\n 상세: %s",
                        model.getName(),
                        theory.getTitle(),
                        theory.getContent(),
                        theory.getDetails()
                );

                Document theoryDoc = new Document(theoryContent, Map.of(
                        "type", "theory",
                        "model_id", model.getId(),
                        "theory_id", theory.getId(),
                        "theory_title", theory.getTitle(),
                        "model_name", model.getName()
                ));
                documents.add(theoryDoc);
            }

            // Usage 정보 개별 저장
            for (ModelUsage usage : model.getUsage()) {
                String usageContent = String.format(
                        "모델명: %s\n  용도: %s\n 내용: %s\n",
                        model.getName(),
                        usage.getTitle(),
                        usage.getContent()
                );

                Document usageDoc = new Document(usageContent, Map.of(
                        "type", "usage",
                        "model_id", model.getId(),
                        "usage_id", usage.getId(),
                        "usage_title", usage.getTitle(),
                        "model_name", model.getName()
                ));
                documents.add(usageDoc);
            }
        }

        // Pinecone에 일괄 저장 (Embeddings API 호출)
        vectorStore.add(documents);
        System.out.println("데이터 주입 완료: " + documents.size() + "개의 문서가 생성됨.");
    }

}

