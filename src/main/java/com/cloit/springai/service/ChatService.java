package com.cloit.springai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final OpenAiChatModel chatModel;
    private final ChromaQueryService chromaQueryService;
    private final ChromaService chromaService;
    private final VectorStore vectorStore;

    public String chat(String userMessage) {
        try {
            // 1️⃣ 사용자의 채팅 메시지를 먼저 ChromaDB에 저장
            Document document = new Document(userMessage);
            //vectorStore.add(List.of(document));  // ✅ ChromaDB에 저장
            chromaService.saveTextToChroma(userMessage);
            //log.info("✅ 사용자 입력 저장 완료: {}", userMessage);

            // 2️⃣ ChromaDB에서 유사한 문서 검색
            List<String> relevantDocs = chromaQueryService.search(userMessage);
            String context = String.join("\n", relevantDocs);

            // 3️⃣ AI 프롬프트 구성
            List<Message> messages = List.of(
                    new SystemMessage("너는 AI 챗봇이야. 사용자의 질문에 친절하게 답변해."),
                    new UserMessage("참고 문서:\n" + context + "\n\n질문: " + userMessage)
            );

            // 4️⃣ AI 모델 호출
            Prompt prompt = new Prompt(messages);
            ChatResponse response = chatModel.call(prompt);
            return response.getResults().get(0).getOutput().getText(); // AI 응답 반환

        } catch (Exception e) {
            log.error("❌ AI 호출 실패: ", e);
            return "현재 AI 서비스가 불가능합니다. 나중에 다시 시도해주세요.";
        }
    }


}
