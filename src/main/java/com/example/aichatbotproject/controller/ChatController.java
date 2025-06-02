package com.example.aichatbotproject.controller;

import com.example.aichatbotproject.dto.ChatRequest;
import com.example.aichatbotproject.dto.ChatResponse;
import com.example.aichatbotproject.entity.ChatMessage;
import com.example.aichatbotproject.entity.ChatSession;
import com.example.aichatbotproject.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 发送聊天消息接口
     */
    @PostMapping("/chat")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            // 从认证上下文获取当前用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 设置请求中的用户名
            request.setUsername(username);

            // 验证消息内容
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ChatResponse("消息内容不能为空"));
            }

            // 处理聊天请求
            ChatResponse response = chatService.processChat(request);
            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(new ChatResponse("权限不足：" + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ChatResponse("请求参数错误：" + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new ChatResponse("系统错误：" + e.getMessage()));
        }
    }

    /**
     * 获取当前用户的聊天历史
     */
    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUserHistory() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            List<ChatSession> history = chatService.getUserChatHistory(username);
            return ResponseEntity.ok(history);

        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(createErrorResponse("PERMISSION_DENIED", "权限不足"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("INTERNAL_ERROR", "获取聊天历史失败"));
        }
    }

    /**
     * 根据用户名获取聊天历史（管理员功能）
     */
    @GetMapping("/history/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getChatHistory(@PathVariable String username) {
        try {
            List<ChatSession> history = chatService.getUserChatHistory(username);
            return ResponseEntity.ok(history);

        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(createErrorResponse("PERMISSION_DENIED", "只有管理员可以查看其他用户的聊天历史"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("INTERNAL_ERROR", "获取聊天历史失败"));
        }
    }

    /**
     * 获取指定会话的消息列表
     */
    @GetMapping("/session/{sessionId}/messages")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getSessionMessages(@PathVariable Long sessionId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 参数验证
            if (sessionId == null || sessionId <= 0) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("INVALID_PARAMETER", "会话ID无效"));
            }

            List<ChatMessage> messages = chatService.getSessionMessages(sessionId, username);
            return ResponseEntity.ok(messages);

        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(createErrorResponse("PERMISSION_DENIED", "无权限访问此会话"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("INVALID_REQUEST", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("INTERNAL_ERROR", "获取会话消息失败"));
        }
    }

    /**
     * 删除指定会话
     */
    @DeleteMapping("/session/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteSession(@PathVariable Long sessionId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 参数验证
            if (sessionId == null || sessionId <= 0) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("INVALID_PARAMETER", "会话ID无效"));
            }

            chatService.deleteSession(sessionId, username);
            return ResponseEntity.ok(createSuccessResponse("会话删除成功"));

        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(createErrorResponse("PERMISSION_DENIED", "无权限删除此会话"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("INVALID_REQUEST", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("INTERNAL_ERROR", "删除会话失败"));
        }
    }

    /**
     * 创建新的聊天会话
     */
    @PostMapping("/session/new")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewSession(@RequestBody(required = false) Map<String, String> request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // 获取会话标题，如果没有提供则使用默认标题
            String title = (request != null && request.containsKey("title"))
                    ? request.get("title")
                    : "新对话";

            ChatSession newSession = chatService.createNewSession(username, title);
            return ResponseEntity.ok(newSession);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("INTERNAL_ERROR", "创建会话失败"));
        }
    }

    // =================== 公开接口（无需认证） ===================

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("🤖 AI聊天机器人服务正常运行中...");
    }

    /**
     * 服务状态接口
     */
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
            errorStatus.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.internalServerError().body(errorStatus);
        }
    }

    /**
     * 获取系统信息接口
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        try {
            Map<String, Object> info = new HashMap<>();
            info.put("applicationName", "AI聊天机器人");
            info.put("version", "1.0.0");
            info.put("features", List.of("用户认证", "多会话管理", "AI对话", "消息历史"));
            info.put("supportedModels", List.of("deepseek-r1:1.5b"));

            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =================== 工具方法 ===================

    /**
     * 创建错误响应
     */
    private Map<String, String> createErrorResponse(String code, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    /**
     * 创建成功响应
     */
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}