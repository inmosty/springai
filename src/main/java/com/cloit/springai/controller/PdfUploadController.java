package com.cloit.springai.controller;

import com.cloit.springai.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PdfUploadController {

    private final PdfService pdfService;

    /**
     * PDF 파일을 업로드하고 ChromaDB에 저장하는 API 엔드포인트
     */
    @PostMapping("/upload-pdf")
    public ResponseEntity<String> uploadPDF(@RequestParam("file") MultipartFile file) {
        // 10MB 제한
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("파일 크기가 10MB를 초과했습니다.");
        }

        // 파일 확장자 확인
        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("PDF 파일만 업로드 가능합니다.");
        }

        try {
            pdfService.processAndStorePdf(file);
            return ResponseEntity.ok("파일 업로드 성공!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류 발생: " + e.getMessage());
        }
    }
}
