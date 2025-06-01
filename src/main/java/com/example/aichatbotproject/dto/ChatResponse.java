package com.example.aichatbotproject.dto;

public class ChatResponse {
    private String response;
    private boolean success;
    private String error;
    private Long sessionId;

    public ChatResponse() {}

    public ChatResponse(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    public ChatResponse(String response, boolean success, Long sessionId) {
        this.response = response;
        this.success = success;
        this.sessionId = sessionId;
    }

    public ChatResponse(String error) {
        this.error = error;
        this.success = false;
    }

    // Getter和Setter
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
}