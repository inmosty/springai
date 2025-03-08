package com.cloit.springai.service;

import com.cloit.springai.utils.PdfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PdfService {
    private final ChromaService chromaService;

    /**
     * PDF 파일을 처리하여 텍스트를 추출한 후, 500자 단위로 분할하여 ChromaDB에 저장
     */
    public void processAndStorePdf(MultipartFile file) throws IOException {
        String text = PdfUtil.extractTextFromPDF(file);
        List<String> chunks = splitTextIntoChunks(text, 500);

        for (String chunk : chunks) {
            chromaService.saveTextToChroma(chunk);
        }
    }

    /**
     * 긴 텍스트를 일정한 크기(500자)로 나누는 메서드
     */
    private List<String> splitTextIntoChunks(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += chunkSize) {
            chunks.add(text.substring(i, Math.min(length, i + chunkSize)));
        }
        return chunks;
    }
}
