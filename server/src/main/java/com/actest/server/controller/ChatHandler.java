package com.actest.server.controller;

import com.actest.server.model.Message;
import com.actest.server.model.User;
import com.actest.server.repository.MessageRepository;
import com.actest.server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ChatHandler {

    private final com.actest.server.repository.MessageRepository messageRepository = new com.actest.server.repository.MessageRepository();
    private final com.actest.server.repository.UserRepository userRepository = new com.actest.server.repository.UserRepository();
    private final com.actest.server.repository.FriendRepository friendRepository = new com.actest.server.repository.FriendRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HttpHandler getChatHandler() {
        return exchange -> {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("POST".equals(method)) {
                handleSendMessage(exchange);
            } else if ("GET".equals(method)) {
                handleGetMessages(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        };
    }

    public HttpHandler getUsersHandler() {
        return exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleGetUsers(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        };
    }

    private void handleSendMessage(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            Message message = objectMapper.readValue(is, Message.class);

            if (!friendRepository.areFriends(message.getSenderId(), message.getReceiverId())) {
                sendResponse(exchange, 403, "You are not friends with this user.", "text/plain; charset=UTF-8");
                return;
            }

            messageRepository.save(message);
            sendResponse(exchange, 200, "Message sent", "text/plain; charset=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error sending message", "text/plain; charset=UTF-8");
        }
    }

    private void handleGetMessages(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = queryToMap(query);

            int userId1 = Integer.parseInt(params.get("userId1"));
            int userId2 = Integer.parseInt(params.get("userId2"));

            List<Message> messages = messageRepository.getMessagesBetween(userId1, userId2);

            String response = objectMapper.writeValueAsString(messages);
            sendResponse(exchange, 200, response, "application/json; charset=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error fetching messages", "text/plain; charset=UTF-8");
        }
    }

    private void handleGetUsers(HttpExchange exchange) throws IOException {
        try {
            List<User> users = userRepository.getAllUsers();
            String response = objectMapper.writeValueAsString(users);
            sendResponse(exchange, 200, response, "application/json; charset=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error fetching users", "text/plain; charset=UTF-8");
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

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new java.util.HashMap<>();
        if (query == null)
            return result;
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
