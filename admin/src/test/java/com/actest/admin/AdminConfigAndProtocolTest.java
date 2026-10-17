package com.actest.admin;

import com.actest.admin.config.Config;
import com.actest.admin.model.Exam;
import com.actest.admin.model.Question;
import com.actest.admin.model.Result;
import com.actest.admin.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminConfigAndProtocolTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Test Config helper defaults and parsing")
    void testConfigDefaults() {
        // Fallback checks
        String serverHost = Config.get("NON_EXISTENT_KEY", "127.0.0.1");
        assertEquals("127.0.0.1", serverHost);

        int port = Config.getInt("NON_EXISTENT_PORT", 8080);
        assertEquals(8080, port);

        int fallback = Config.getInt("NON_EXISTENT_BAD_PORT", 9000);
        assertEquals(9000, fallback);
    }

    @Test
    @DisplayName("Test Admin Models and serialization")
    void testAdminModels() throws Exception {
        User adminUser = new User(1, "admin@actest.com", "secret", "Admin Chien", "ADMIN");
        assertEquals(1, adminUser.getId());
        assertEquals("admin@actest.com", adminUser.getEmail());
        assertEquals("Admin Chien", adminUser.getName());
        assertEquals("ADMIN", adminUser.getRole());

        Exam exam = new Exam(1, "Midterm Exam", 90, 1);
        exam.setStatus("NOT_STARTED");
        Question q = new Question();
        q.setId(10);
        q.setContent("Select primitive types in Java");
        q.setOptions(List.of("int", "String", "Integer", "Object"));
        q.setCorrectOptionIndex(0);
        exam.setQuestions(List.of(q));

        String json = mapper.writeValueAsString(exam);
        assertNotNull(json);

        Exam decoded = mapper.readValue(json, Exam.class);
        assertEquals(exam.getName(), decoded.getName());
        assertEquals(1, decoded.getQuestions().size());
        assertEquals("int", decoded.getQuestions().get(0).getOptions().get(0));

        Result result = new Result(1, "Student Test", 0, 9.5, 1);
        assertEquals("Student Test", result.getStudentName());
        assertEquals(9.5, result.getScore());
        assertEquals(1, result.getExamId());
    }

    @Test
    @DisplayName("Test Violation Protocol format and parsing")
    void testViolationProtocol() {
        String violationMsg = "VIOLATION:FOCUS_LOST:123:iVBORw0KGgoAAAANSUhEUg==";
        String[] parts = violationMsg.split(":", 4);

        assertEquals(4, parts.length);
        assertEquals("VIOLATION", parts[0]);
        assertEquals("FOCUS_LOST", parts[1]);
        assertEquals("123", parts[2]);
        assertEquals("iVBORw0KGgoAAAANSUhEUg==", parts[3]);

        byte[] decodedImage = Base64.getDecoder().decode(parts[3]);
        assertNotNull(decodedImage);
        assertTrue(decodedImage.length > 0);
    }

    @Test
    @DisplayName("Test Client Message parsing: ANSWER, RECONNECT, REQUEST_EXAM")
    void testClientMessageParsing() {
        String answerMsg = "ANSWER:5:102:B";
        String[] answerParts = answerMsg.split(":", 4);
        assertEquals(4, answerParts.length);
        assertEquals(5, Integer.parseInt(answerParts[1]));
        assertEquals(102, Integer.parseInt(answerParts[2]));
        assertEquals("B", answerParts[3]);

        String reconnectMsg = "RECONNECT:Nguyen Van A";
        String[] reconnectParts = reconnectMsg.split(":", 2);
        assertEquals(2, reconnectParts.length);
        assertEquals("Nguyen Van A", reconnectParts[1]);

        String joinMsg = "JOIN:Nguyen Van B";
        assertTrue(joinMsg.startsWith("JOIN:"));
        assertEquals("Nguyen Van B", joinMsg.substring(5));
    }
}
