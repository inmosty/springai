# Spring AI Chroma 프로젝트

## 📌 프로젝트 개요

이 프로젝트는 OpenAI 기반의 AI 챗봇과 PDF 문서 검색 기능을 제공하는 **Spring Boot 애플리케이션**입니다. 사용자의 입력을 **ChromaDB**에 벡터로 저장하고, 유사한 문서를 검색하여 AI 응답을 생성합니다.

## 🚀 주요 기능

- **AI 챗봇**: 사용자의 입력을 ChromaDB에 저장하고, 유사한 문서를 기반으로 AI 응답 생성
- **PDF 업로드 및 검색**: PDF 파일을 업로드하여 텍스트를 벡터화하고 검색 가능하도록 저장
- **ChromaDB 벡터 저장**: 입력된 데이터는 벡터 임베딩으로 변환 후 ChromaDB에 저장됨
---

## 🛠️ 설치 및 실행 방법

### 2️⃣ **환경 설정**

1. **ChromaDB 실행 (Docker 필요)**

   ```sh
   docker run -d --name chromadb -p 8000:8000 ghcr.io/chroma-core/chroma:0.4.24
   ```

   ```
   설치확인
   curl -X GET "http://localhost:8000/api/v1/collections"
   
   db 생성
   curl -X POST "http://localhost:8000/api/v1/collections"  -H "Content-Type: application/json" -d '{"name": "my_collection", "dimension": 1536}'
   ```
   


2. ``** 설정**

   ```yaml
   spring:
     ai:
       chroma:
         url: http://localhost:8000
   ```

### 3️⃣ **애플리케이션 실행**

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