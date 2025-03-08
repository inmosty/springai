package com.cloit.springai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChromaQueryService {
    private final RestTemplate restTemplate;
    private final EmbeddingModel embeddingModel;
    private final String chromaCollectionUrl = "http://localhost:8000/api/v1/collections/"; // ✅ 검색용 API
    private final String collectionId = "aa0093f5-56c8-4c56-96cf-8c4a70a20a91"; // ✅ Chroma에서 확인한 UUID


    public List<String> search(String query) {
        log.info("🔍 검색 요청: {}", query);

        // 1️⃣ 입력 텍스트를 벡터로 변환 (벡터 생성 확인)
        float[] queryEmbedding = embeddingModel.embed(query);

        // 벡터가 정상적으로 생성되었는지 확인
        log.info("🔢 변환된 벡터: {}", Arrays.toString(queryEmbedding));

        // float[] -> List<Double> 변환
        List<Double> embeddingList = new ArrayList<>();
        for (float value : queryEmbedding) {
            embeddingList.add((double) value);
        }

        // 2️⃣ ChromaDB 검색 요청 구성
        Map<String, Object> requestBody = Map.of(
                "query_embeddings", Collections.singletonList(embeddingList),
                "n_results", 5,
                "include", Arrays.asList("documents", "embeddings") // 벡터 포함 검색
        );

        String chromaSearchUrl = chromaCollectionUrl + collectionId + "/query";
        ResponseEntity<Map> response = restTemplate.postForEntity(chromaSearchUrl, requestBody, Map.class);

        log.info("🔍 검색 응답: {}", response.getBody());

        // 3️⃣ 검색 결과 처리 (ChromaDB의 중첩 리스트 구조 해결)
        Object documentsObject = response.getBody().get("documents");

        if (documentsObject instanceof List) {
            List<?> rawList = (List<?>) documentsObject;
            if (!rawList.isEmpty() && rawList.get(0) instanceof List) {
                // 중첩 리스트 풀기
                List<List<String>> results = (List<List<String>>) rawList;
                List<String> searchResults = results.stream()
                        .flatMap(List::stream)
                        .distinct() // 중복 제거
                        .collect(Collectors.toList());

                log.info("🔎 최종 검색 결과: {}", searchResults);
                return searchResults;
            }
        }

        log.warn("⚠ 검색 결과가 없음");
        return Collections.emptyList();
    }


    public Map<String, Object> getAllDocuments() {
        String url = chromaCollectionUrl + collectionId + "/get";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디에 벡터 데이터 포함하도록 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("include", Arrays.asList("embeddings", "documents"));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        log.info("🔍 ChromaDB 응답: {}", response.getBody());
        return response.getBody();
    }

}
