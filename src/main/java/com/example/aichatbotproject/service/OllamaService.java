package com.example.aichatbotproject.service;

import com.example.aichatbotproject.dto.OllamaRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class OllamaService {

    @Value("${ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ollama.model:deepseek-r1:1.5b}")
    private String defaultModel;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 与AI聊天的方法
     */
    public String chat(String message) {
        try {
            // 参数验证
            if (message == null || message.trim().isEmpty()) {
                return "消息内容不能为空";
            }

            System.out.println("🤖 正在向AI发送消息: " + message);

            // 创建请求对象
            OllamaRequest request = new OllamaRequest(defaultModel, message, false);
            String requestJson = objectMapper.writeValueAsString(request);

            // 创建HTTP请求
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaBaseUrl + "/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .timeout(Duration.ofSeconds(60))
                    .build();

            // 发送请求
            HttpResponse<String> response = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // 解析响应 - 添加空值检查
                JsonNode jsonNode = objectMapper.readTree(response.body());

                if (jsonNode != null && jsonNode.has("response")) {
                    String aiResponse = jsonNode.get("response").asText();

                    if (aiResponse != null && !aiResponse.trim().isEmpty()) {
                        System.out.println("✅ AI回复成功: " +
                                aiResponse.substring(0, Math.min(50, aiResponse.length())) + "...");
                        return aiResponse.trim();
                    } else {
                        System.err.println("❌ AI回复内容为空");
                        return "抱歉，AI没有返回有效回复。";
                    }
                } else {
                    System.err.println("❌ 响应格式错误，缺少response字段");
                    return "抱歉，AI服务响应格式错误。";
                }
            } else {
                System.err.println("❌ HTTP错误: " + response.statusCode() + " - " + response.body());
                return getErrorMessage(response.statusCode());
            }

        } catch (IOException e) {
            System.err.println("❌ 网络IO异常: " + e.getMessage());
            return "抱歉，网络连接失败，请检查Ollama服务是否运行。";
        } catch (InterruptedException e) {
            System.err.println("❌ 请求被中断: " + e.getMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
            return "抱歉，请求被中断，请稍后再试。";
        } catch (Exception e) {
            System.err.println("❌ 调用Ollama API失败: " + e.getMessage());
            e.printStackTrace(); // 开发环境打印完整堆栈
            return generateFallbackResponse(message);
        }
    }

    /**
     * 测试Ollama连接的方法
     */
    public boolean testConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaBaseUrl + "/api/tags"))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            boolean isConnected = response.statusCode() == 200;
            System.out.println(isConnected ? "✅ Ollama连接正常" : "❌ Ollama连接失败");
            return isConnected;

        } catch (Exception e) {
            System.err.println("❌ 测试Ollama连接失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取服务状态
     */
    public String getServiceStatus() {
        if (testConnection()) {
            return "Ollama服务运行正常 (" + ollamaBaseUrl + " - " + defaultModel + ")";
        } else {
            return "Ollama服务不可用 (" + ollamaBaseUrl + ")";
        }
    }

    /**
     * 获取当前模型
     */
    public String getCurrentModel() {
        return defaultModel;
    }

    /**
     * 根据HTTP状态码返回错误信息
     */
    private String getErrorMessage(int statusCode) {
        switch (statusCode) {
            case 404:
                return "抱歉，请求的AI模型不存在，请检查模型配置。";
            case 500:
                return "抱歉，AI服务内部错误，请稍后再试。";
            case 503:
                return "抱歉，AI服务暂时不可用，请稍后再试。";
            default:
                return "抱歉，AI服务响应错误 (状态码: " + statusCode + ")。";
        }
    }

    /**
     * 当AI服务不可用时的备用回复
     */
    private String generateFallbackResponse(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("你好") || lowerMessage.contains("hello")) {
            return "你好！AI服务暂时不可用，这是一个自动回复。";
        } else if (lowerMessage.contains("谢谢")) {
            return "不客气！AI服务恢复后将为您提供更好的服务。";
        } else {
            return "收到您的消息：" + userMessage + "\n\n" +
                    "抱歉，AI服务暂时不可用，请稍后再试或联系管理员。";
        }
    }
}