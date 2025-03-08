# Spring AI Chroma 프로젝트

## 📌 프로젝트 개요

이 프로젝트는 OpenAI 기반의 AI 챗봇과 PDF 문서 검색 기능을 제공하는 **Spring Boot 애플리케이션**입니다. 사용자의 입력을 **ChromaDB**에 벡터로 저장하고, 유사한 문서를 검색하여 AI 응답을 생성합니다.

## 🚀 주요 기능

- **AI 챗봇**: 사용자의 입력을 ChromaDB에 저장하고, 유사한 문서를 기반으로 AI 응답 생성
- **PDF 업로드 및 검색**: PDF 파일을 업로드하여 텍스트를 벡터화하고 검색 가능하도록 저장
- **ChromaDB 벡터 저장**: 입력된 데이터는 벡터 임베딩으로 변환 후 ChromaDB에 저장됨

---

## 📂 프로젝트 구조

```
📦 src/main/java/com/cloit/springai
┣ 📂 config  # 설정 관련 클래스
┃ ┣ 📜 AppConfig.java       # RestTemplate 설정
┃ ┗ 📜 ChromaConfig.java    # ChromaDB 및 벡터 스토어 설정
┣ 📂 controller  # API 엔드포인트
┃ ┣ 📜 ChatController.java     # 채팅 페이지 및 API
┃ ┗ 📜 PdfUploadController.java  # PDF 업로드 API
┣ 📂 request  # 요청 객체
┃ ┗ 📜 ChatRequest.java
┣ 📂 service  # 비즈니스 로직
┃ ┣ 📜 ChatService.java     # AI 챗봇 로직
┃ ┣ 📜 ChromaQueryService.java  # ChromaDB 검색 서비스
┃ ┣ 📜 ChromaService.java  # 벡터 임베딩 및 저장 서비스
┃ ┗ 📜 PdfService.java    # PDF 파일 처리 서비스
┣ 📂 utils  # 유틸리티 클래스
┃ ┗ 📜 PdfUtil.java  # PDF에서 텍스트 추출 기능
```

---

## 🛠️ 설치 및 실행 방법

### 1️⃣ **프로젝트 클론**

```sh
git clone https://github.com/your-repo/spring-ai-chroma.git
cd spring-ai-chroma
```

### 2️⃣ **환경 설정**

1. **ChromaDB 실행 (Docker 필요)**

   ```sh
   docker run -p 8000:8000 ghcr.io/chroma-core/chroma:latest
   ```

2. ``** 설정**

   ```yaml
   spring:
     ai:
       chroma:
         url: http://localhost:8000
   ```

### 3️⃣ **애플리케이션 실행**

```sh
./gradlew bootRun
```

---

## 📌 API 사용법

### 1️⃣ **AI 챗봇 요청**

- **URL:** `POST /api/chat`
- **Request Body:**
  ```json
  {
    "userMessage": "5000만원일 때 소득세는?"
  }
  ```
- **Response:**
  ```json
  "소득세 계산 결과는..."
  ```

### 2️⃣ **PDF 파일 업로드**

- **URL:** `POST /api/upload-pdf`
- **Request Body:** `multipart/form-data`
    - **파일 필드:** `file`
- **Response:**
  ```json
  "파일 업로드 성공!"
  ```

---

## 🛠️ 기술 스택

- **Spring Boot 3.4.3**
- **Spring AI** (OpenAI API 활용)
- **ChromaDB** (벡터 저장 및 검색)
- **Thymeleaf** (웹 UI)
- **H2 Database** (테스트용 DB)
- **PDFBox** (PDF 텍스트 추출)

---