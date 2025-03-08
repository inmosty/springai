package com.cloit.springai.controller;

import com.cloit.springai.service.ChromaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChromaController {
    private final ChromaQueryService chromaQueryService;

    @GetMapping("/documents")
    @ResponseBody
    public String showDocuments(Model model) {
        Map<String, Object> response = chromaQueryService.getAllDocuments();

        // documents 리스트만 따로 추출하여 모델에 전달
        List<Map<String, Object>> documents = (List<Map<String, Object>>) response.get("documents");

        model.addAttribute("documents", documents);
        return documents.toString();
    }
}
