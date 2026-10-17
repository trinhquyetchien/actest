package com.actest.server;

import com.actest.server.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerModelTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test User model getters and setters")
    void testUserModel() {
        User user = new User(1, "test@example.com", "password123", "Alice", "ADMIN");
        assertEquals(1, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Alice", user.getName());
        assertEquals("password123", user.getPassword());
        assertEquals("ADMIN", user.getRole());

        user.setName("Alice Updated");
        assertEquals("Alice Updated", user.getName());
    }

    @Test
    @DisplayName("Test Exam model and serialization")
    void testExamModel() throws Exception {
        Exam exam = new Exam(10, "Java Core Final", 60, 1);
        exam.setStatus("ACTIVE");
        exam.setAllowReview(true);

        assertEquals(10, exam.getId());
        assertEquals("Java Core Final", exam.getName());
        assertEquals(60, exam.getDuration());
        assertEquals(1, exam.getCreatorId());
        assertEquals("ACTIVE", exam.getStatus());
        assertTrue(exam.isAllowReview());

        String json = objectMapper.writeValueAsString(exam);
        assertNotNull(json);

        Exam deserialized = objectMapper.readValue(json, Exam.class);
        assertEquals(exam.getId(), deserialized.getId());
        assertEquals(exam.getName(), deserialized.getName());
        assertEquals(exam.getDuration(), deserialized.getDuration());
        assertEquals(exam.getStatus(), deserialized.getStatus());
    }

    @Test
    @DisplayName("Test Question model and options serialization")
    void testQuestionModel() throws Exception {
        Question question = new Question();
        question.setId(101);
        question.setContent("What is JVM?");
        question.setOptions(List.of("Java Virtual Machine", "Java Very Much", "Just Virtual Machine", "None"));
        question.setCorrectOptionIndex(0);

        assertEquals(101, question.getId());
        assertEquals("What is JVM?", question.getContent());
        assertEquals(4, question.getOptions().size());
        assertEquals(0, question.getCorrectOptionIndex());

        String json = objectMapper.writeValueAsString(question);
        assertTrue(json.contains("Java Virtual Machine"));

        Question deserialized = objectMapper.readValue(json, Question.class);
        assertEquals(question.getContent(), deserialized.getContent());
        assertEquals(question.getOptions(), deserialized.getOptions());
    }

    @Test
    @DisplayName("Test Message and FriendRequest models")
    void testMessageAndFriendRequest() throws Exception {
        Message message = new Message();
        message.setId(1);
        message.setSenderId(10);
        message.setReceiverId(20);
        message.setContent("Hello from student");

        assertEquals(1, message.getId());
        assertEquals(10, message.getSenderId());
        assertEquals(20, message.getReceiverId());
        assertEquals("Hello from student", message.getContent());

        String jsonMsg = objectMapper.writeValueAsString(message);
        Message deserializedMsg = objectMapper.readValue(jsonMsg, Message.class);
        assertEquals("Hello from student", deserializedMsg.getContent());

        FriendRequest req = new FriendRequest(1, 10, 20, "PENDING");
        assertEquals(1, req.getId());
        assertEquals(10, req.getSenderId());
        assertEquals(20, req.getReceiverId());
        assertEquals("PENDING", req.getStatus());
    }

    @Test
    @DisplayName("Test Result model")
    void testResultModel() {
        Result result = new Result(1, 10, 2, 95, "2026-10-17 12:00:00");
        assertEquals(1, result.getId());
        assertEquals(10, result.getUserId());
        assertEquals(2, result.getExamId());
        assertEquals(95, result.getScore());
        assertEquals("2026-10-17 12:00:00", result.getDateTaken());
    }
}
