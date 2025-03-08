package com.cloit.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChromaService {
    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;

    public ChromaService(VectorStore vectorStore, EmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    /**
     * 텍스트 데이터를 벡터로 변환하여 ChromaDB에 저장
     */
    public void saveTextToChroma(String text) {
        log.info("🔵 ChromaDB 저장 시작: {}", text);

        // 텍스트를 벡터 임베딩으로 변환
        float[] embeddingArray = embeddingModel.embed(text);

        // float[] → List<Double> 변환
        List<Double> embedding = new ArrayList<>();
        for (float value : embeddingArray) {
            embedding.add((double) value);
        }

        // Document 객체 생성 (벡터를 metadata로 저장)
        Document document = new Document(text, Map.of("embedding", embedding));
        log.info("🟢 저장할 Document: {}", document);

        // ChromaDB에 저장
        vectorStore.add(List.of(document));  // ✅ 임베딩을 메타데이터에 저장하여 추가

        log.info("✅ ChromaDB 저장 완료: {}", text);
    }

}

