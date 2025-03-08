package com.cloit.springai.config;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class ChromaConfig {

    @Bean
    public ChromaApi chromaApi() {
        return new ChromaApi("http://localhost:8000");
    }


    @Bean
    public EmbeddingModel embeddingModel() {
        return new EmbeddingModel() {
            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                List<String> instructions = request.getInstructions();
                // 각 텍스트에 대해 임의의 임베딩 벡터와 인덱스를 생성
                List<Embedding> embeddings = IntStream.range(0, instructions.size())
                        .mapToObj(i -> new Embedding(new float[]{0.1f, 0.2f, 0.3f}, i))
                        .collect(Collectors.toList());
                return new EmbeddingResponse(embeddings);
            }

            @Override
            public float[] embed(Document document) {
                // 단일 문서의 임베딩 벡터 반환 (예제 값)
                return new float[]{0.1f, 0.2f, 0.3f};
            }
        };
    }


    @Bean
    public ChromaVectorStore vectorStore(ChromaApi chromaApi, EmbeddingModel embeddingModel) {
        try {
            return ChromaVectorStore.builder(chromaApi, embeddingModel)
                    .collectionName("my_collection")
                    .initializeSchema(true)  // 기존 컬렉션을 찾도록 설정
                    //.initializeImmediately(true)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("ChromaVectorStore 초기화 실패: " + e.getMessage(), e);
        }
    }


}
