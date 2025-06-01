package com.example.aichatbotproject.dto;

public class OllamaResponse {
    private String model;
    private String response;
    private boolean done;

    public OllamaResponse() {
    }

    public OllamaResponse(String model, String response, boolean done) {
        this.model = model;
        this.response = response;
        this.done = done;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toString() {
        return "OllamaResponse{model='" + model + "', response='" + response + "', done=" + done + "}";
    }
}