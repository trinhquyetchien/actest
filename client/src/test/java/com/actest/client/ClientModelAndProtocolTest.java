package com.actest.client;

import com.actest.client.model.Exam;
import com.actest.client.model.Question;
import com.actest.client.model.Result;
import com.actest.client.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClientModelAndProtocolTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Test Client Models creation and attributes")
    void testClientModels() {
        User user = new User(1, "student@actest.edu", "pass", "Student A", "CLIENT");
        assertEquals("Student A", user.getName());
        assertEquals("student@actest.edu", user.getEmail());
        assertEquals("CLIENT", user.getRole());

        Result result = new Result(1, "Student A", 1, 8.5, 10);
        assertEquals(1, result.getId());
        assertEquals(10, result.getExamId());
        assertEquals(8.5, result.getScore());
        assertEquals(1, result.getFocusLostCount());
        assertEquals("Student A", result.getStudentName());

        Question q = new Question();
        q.setId(5);
        q.setContent("What does JDK stand for?");
        q.setOptions(List.of("Java Development Kit", "Java Design Kit", "Joint Data Kit", "None"));
        q.setCorrectOptionIndex(0);

        assertEquals(5, q.getId());
        assertEquals(4, q.getOptions().size());
        assertEquals("Java Development Kit", q.getOptions().get(0));
    }

    @Test
    @DisplayName("Test Exam serialization and Base64 round-trip (START_EXAM protocol)")
    void testExamBase64Protocol() throws Exception {
        Exam exam = new Exam(1, "Java Exam 1", 45, 100);
        Question q1 = new Question();
        q1.setId(1);
        q1.setContent("1 + 1 = ?");
        q1.setOptions(List.of("1", "2", "3", "4"));
        q1.setCorrectOptionIndex(1);
        exam.setQuestions(List.of(q1));

        String json = mapper.writeValueAsString(exam);
        String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

        String protocolMessage = "START_EXAM:" + base64;
        assertTrue(protocolMessage.startsWith("START_EXAM:"));

        String payload = protocolMessage.substring("START_EXAM:".length());
        String decodedJson = new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);

        Exam receivedExam = mapper.readValue(decodedJson, Exam.class);
        assertEquals("Java Exam 1", receivedExam.getName());
        assertEquals(45, receivedExam.getDuration());
        assertEquals(1, receivedExam.getQuestions().size());
        assertEquals("1 + 1 = ?", receivedExam.getQuestions().get(0).getContent());
    }

    @Test
    @DisplayName("Test SUBMIT_EXAM answers payload serialization and deserialization")
    void testSubmitExamPayload() throws Exception {
        Map<Integer, String> studentAnswers = new HashMap<>();
        studentAnswers.put(1, "Option B");
        studentAnswers.put(2, "Option D");
        studentAnswers.put(3, "Option A");

        String answersJson = mapper.writeValueAsString(studentAnswers);
        String base64Answers = Base64.getEncoder().encodeToString(answersJson.getBytes(StandardCharsets.UTF_8));

        String message = "SUBMIT_EXAM:10:" + base64Answers;
        String[] parts = message.split(":", 3);

        assertEquals("SUBMIT_EXAM", parts[0]);
        assertEquals(10, Integer.parseInt(parts[1]));

        String decodedAnswersJson = new String(Base64.getDecoder().decode(parts[2]), StandardCharsets.UTF_8);
        Map<Integer, String> restored = mapper.readValue(decodedAnswersJson, new TypeReference<Map<Integer, String>>() {});

        assertEquals(3, restored.size());
        assertEquals("Option B", restored.get(1));
        assertEquals("Option D", restored.get(2));
        assertEquals("Option A", restored.get(3));
    }

    @Test
    @DisplayName("Test Time broadcast parsing")
    void testTimeBroadcastParsing() {
        String timeMessage = "TIME:10:3540";
        String[] parts = timeMessage.split(":");
        assertEquals(3, parts.length);
        assertEquals(10, Integer.parseInt(parts[1]));
        int totalSeconds = Integer.parseInt(parts[2]);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        assertEquals(59, minutes);
        assertEquals(0, seconds);

        String finishMessage = "TIME:10:FINISHED";
        String[] finishParts = finishMessage.split(":");
        assertEquals("FINISHED", finishParts[2]);
    }
}
