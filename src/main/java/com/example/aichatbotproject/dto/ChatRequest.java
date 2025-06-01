package com.example.aichatbotproject.dto;

public class ChatRequest {

    private String message;
    private String username; // 前端可以不提供，后端会自动生成
    private Long sessionId;

    public ChatRequest() {}

    public ChatRequest(String message) {
        this.message = message;
    }

    public ChatRequest(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public ChatRequest(String message, String username, Long sessionId) {
        this.message = message;
        this.username = username;
        this.sessionId = sessionId;
    }

    // Getter和Setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", sessionId=" + sessionId +
                '}';
    }
}