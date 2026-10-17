package com.actest.server.controller;

import com.actest.server.model.Exam;
import com.actest.server.model.Question;
import com.actest.server.repository.ExamRepository;
import com.actest.server.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExamHandler {
    private final ExamRepository examRepository = new ExamRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HttpHandler getExamHandler() {
        return exchange -> {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("POST".equals(method)) {
                handleUploadExam(exchange);
            } else if ("GET".equals(method)) {
                if (path.equals("/api/exams")) {
                    handleGetAllExams(exchange);
                } else if (path.matches("/api/exams/\\d+")) {
                    handleGetExamById(exchange);
                } else if (path.matches("/api/exams/\\d+/questions")) {
                    handleGetQuestionsByExamId(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        };
    }

    private void handleGetAllExams(HttpExchange exchange) throws IOException {
        List<Exam> exams = examRepository.getAllExams();
        String response = objectMapper.writeValueAsString(exams);
        sendResponse(exchange, 200, response, "application/json; charset=UTF-8");
    }

    private void handleGetExamById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
        Exam exam = examRepository.getExamById(id);
        if (exam != null) {
            String response = objectMapper.writeValueAsString(exam);
            sendResponse(exchange, 200, response, "application/json; charset=UTF-8");
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private void handleGetQuestionsByExamId(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[3]);
        List<Question> questions = questionRepository.getQuestionsByExamId(id);
        String response = objectMapper.writeValueAsString(questions);
        sendResponse(exchange, 200, response, "application/json; charset=UTF-8");
    }

    private void handleUploadExam(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            UploadRequest request = objectMapper.readValue(body, UploadRequest.class);

            int creatorId = -1;
            if (request.getEmail() != null) {
                com.actest.server.repository.UserRepository userRepository = new com.actest.server.repository.UserRepository();
                creatorId = userRepository.getUserIdByEmail(request.getEmail());
            }

            if (creatorId == -1) {
                sendResponse(exchange, 400, "User not found for email: " + request.getEmail(), "text/plain; charset=UTF-8");
                return;
            }

            Exam exam = request.getExam();
            exam.setCreatorId(creatorId);

            int examId = examRepository.createExam(exam);
            if (examId != -1) {
                if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
                    questionRepository.addQuestionsToExam(examId, request.getQuestions());
                }
                sendResponse(exchange, 200, "Exam uploaded successfully", "text/plain; charset=UTF-8");
            } else {
                sendResponse(exchange, 500, "Failed to create exam", "text/plain; charset=UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 400, "Error processing request: " + e.getMessage(), "text/plain; charset=UTF-8");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // DTO for upload request
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private static class UploadRequest {
        private Exam exam;
        private List<Question> questions;
        private String email;

        public Exam getExam() {
            return exam;
        }

        public void setExam(Exam exam) {
            this.exam = exam;
        }

        public List<Question> getQuestions() {
            return questions;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
