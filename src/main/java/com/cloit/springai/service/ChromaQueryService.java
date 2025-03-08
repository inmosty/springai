package com.cloit.springai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChromaQueryService {
    private final RestTemplate restTemplate;
    private final EmbeddingModel embeddingModel;
    private final String chromaCollectionUrl = "http://localhost:8000/api/v1/collections/"; // ✅ 검색용 API
    private final String collectionId = "c643bb7c-6157-4659-854b-ebb082086a2a"; // ✅ Chroma에서 확인한 UUID

    /**
     * 사용자의 입력을 벡터로 변환하여 ChromaDB에서 유사한 문서를 검색
     */
    public List<String> search(String query) {
        log.info("🔍 검색 요청: {}", query);

        // 1️⃣ 입력 텍스트를 벡터로 변환
        float[] queryEmbedding = embeddingModel.embed(query);

        // 2️⃣ float[] → List<Double> 변환
        List<Double> embeddingList = new ArrayList<>();
        for (float value : queryEmbedding) {
            embeddingList.add((double) value);
        }

        log.info("🔢 검색 벡터: {}", embeddingList);

        // 3️⃣ JSON 데이터 생성
        Map<String, Object> requestBody = Map.of(
                "query_embeddings", Collections.singletonList(embeddingList),
                "n_results", 5
        );

        // 4️⃣ ChromaDB에 검색 요청
        String chromaSearchUrl = chromaCollectionUrl + collectionId + "/query";
        ResponseEntity<Map> response = restTemplate.postForEntity(chromaSearchUrl, requestBody, Map.class);

        // 5️⃣ 응답 데이터 로깅
        log.info("🔍 검색 응답: {}", response.getBody());

        // 6️⃣ 검색 결과 처리
        Object documentsObject = response.getBody().get("documents");

        if (documentsObject instanceof List) {
            List<?> rawList = (List<?>) documentsObject;
            if (!rawList.isEmpty() && rawList.get(0) instanceof Map) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) rawList;
                List<String> searchResults = results.stream()
                        .map(doc -> (String) doc.get("text"))
                        .collect(Collectors.toList());

                log.info("🔎 검색 결과: {}", searchResults);
                return searchResults;
            }
        }
        return Collections.emptyList();
    }


}
