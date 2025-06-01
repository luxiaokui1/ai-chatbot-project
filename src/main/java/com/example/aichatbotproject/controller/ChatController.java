package com.example.aichatbotproject.controller;

import com.example.aichatbotproject.dto.ChatRequest;
import com.example.aichatbotproject.dto.ChatResponse;
import com.example.aichatbotproject.entity.ChatMessage;
import com.example.aichatbotproject.entity.ChatSession;
import com.example.aichatbotproject.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            // 只验证消息内容
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ChatResponse("消息内容不能为空"));
            }

            // 自动生成用户名 - 前端不需要提供
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                request.setUsername("访客"); // 固定用户名，所有对话归为一个用户
            }

            ChatResponse response = chatService.processChat(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new ChatResponse("系统错误：" + e.getMessage()));
        }
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<ChatSession>> getChatHistory(@PathVariable String username) {
        try {
            List<ChatSession> history = chatService.getUserChatHistory(username);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 新增：获取当前用户的聊天历史（前端可用）
    @GetMapping("/history")
    public ResponseEntity<List<ChatSession>> getCurrentUserHistory() {
        try {
            // 使用固定的访客用户名
            List<ChatSession> history = chatService.getUserChatHistory("访客");
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/session/{sessionId}/messages")
    public ResponseEntity<List<ChatMessage>> getSessionMessages(@PathVariable Long sessionId) {
        try {
            if (sessionId == null || sessionId <= 0) {
                return ResponseEntity.badRequest().build();
            }

            List<ChatMessage> messages = chatService.getSessionMessages(sessionId);
            return ResponseEntity.ok(messages);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        try {
            if (sessionId == null || sessionId <= 0) {
                return ResponseEntity.badRequest().build();
            }

            // 使用固定的访客用户名
            chatService.deleteSession(sessionId, "访客");
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("🤖 AI聊天机器人服务正常运行中...");
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        try {
            Map<String, String> status = new HashMap<>();
            status.put("service", "运行中");
            status.put("ollama", chatService.getOllamaStatus());
            status.put("model", chatService.getCurrentModel());
            status.put("timestamp", java.time.LocalDateTime.now().toString());

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            Map<String, String> errorStatus = new HashMap<>();
            errorStatus.put("service", "错误");
            errorStatus.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorStatus);
        }
    }
}