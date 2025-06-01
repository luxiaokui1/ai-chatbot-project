package com.example.aichatbotproject.dto;

public class OllamaRequest {
    private String model;
    private String prompt;
    private boolean stream;

    // 无参构造函数
    public OllamaRequest() {}

    // 有参构造函数
    public OllamaRequest(String model, String prompt, boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    // Getter和Setter方法
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "OllamaRequest{" +
                "model='" + model + '\'' +
                ", prompt='" + prompt + '\'' +
                ", stream=" + stream +
                '}';
    }
}