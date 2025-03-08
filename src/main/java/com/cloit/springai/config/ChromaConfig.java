package com.cloit.springai.config;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromaConfig {

    @Bean
    public ChromaApi chromaApi() {
        return new ChromaApi("http://localhost:8000");
    }

    @Bean
    public ChromaVectorStore vectorStore(ChromaApi chromaApi, EmbeddingModel embeddingModel) {
        try {
            return ChromaVectorStore.builder(chromaApi, embeddingModel)
                    .collectionName("my_collection")
                    //.initializeSchema(true)  // 기존 컬렉션을 찾도록 설정
                    //.initializeImmediately(true)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("ChromaVectorStore 초기화 실패: " + e.getMessage(), e);
        }
    }


}
