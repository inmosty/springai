package com.cloit.springai.controller;

import com.cloit.springai.request.ChatRequest;
import com.cloit.springai.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 채팅 페이지를 반환
     */
    @GetMapping("/")
    public String chatPage(Model model) {
        return "chat";  // Thymeleaf 템플릿 반환
    }

    /**
     * PDF 업로드 페이지를 반환
     */
    @GetMapping("/pdf")
    public String pdfUpload(Model model) {
        return "pdf_upload";  // Thymeleaf 템플릿 반환
    }

    /**
     * 사용자의 채팅 요청을 처리하는 API 엔드포인트
     */
    @PostMapping("/api/chat")
    @ResponseBody
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request.getUserMessage());
    }
}