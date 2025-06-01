package com.example.aichatbotproject.service;

import com.example.aichatbotproject.dto.ChatRequest;
import com.example.aichatbotproject.dto.ChatResponse;
import com.example.aichatbotproject.entity.ChatMessage;
import com.example.aichatbotproject.entity.ChatSession;
import com.example.aichatbotproject.entity.User;
import com.example.aichatbotproject.repository.ChatMessageRepository;
import com.example.aichatbotproject.repository.ChatSessionRepository;
import com.example.aichatbotproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private OllamaService ollamaService;

    @Transactional
    public ChatResponse processChat(ChatRequest request) {
        try {
            // 参数验证
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return new ChatResponse("消息内容不能为空");
            }

            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return new ChatResponse("用户名不能为空");
            }

            // 1. 获取或创建用户
            User user = getOrCreateUser(request.getUsername());

            // 2. 获取或创建会话
            ChatSession session = getOrCreateSession(user, request.getSessionId());

            // 3. 保存用户消息
            ChatMessage userMessage = new ChatMessage(
                    request.getMessage(),
                    ChatMessage.MessageType.USER,
                    session
            );
            chatMessageRepository.save(userMessage);

            // 4. 构建上下文（包含历史对话）
            String contextualPrompt = buildContextualPrompt(session.getId(), request.getMessage());

            // 5. 调用AI获取回复
            String aiResponse = ollamaService.chat(contextualPrompt);

            // 6. 保存AI回复
            ChatMessage aiMessage = new ChatMessage(
                    aiResponse,
                    ChatMessage.MessageType.AI,
                    session
            );
            chatMessageRepository.save(aiMessage);

            // 7. 更新会话标题（如果是第一条消息）
            updateSessionTitle(session, request.getMessage());

            return new ChatResponse(aiResponse, true, session.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return new ChatResponse("处理消息时发生错误: " + e.getMessage());
        }
    }

    private User getOrCreateUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User(username, username + "@example.com");
                    return userRepository.save(newUser);
                });
    }

    private ChatSession getOrCreateSession(User user, Long sessionId) {
        if (sessionId != null) {
            Optional<ChatSession> sessionOpt = chatSessionRepository.findById(sessionId);
            if (sessionOpt.isPresent() &&
                    sessionOpt.get().getUser().getId().equals(user.getId())) {
                return sessionOpt.get();
            }
        }

        // 创建新会话
        ChatSession newSession = new ChatSession("新对话", user);
        return chatSessionRepository.save(newSession);
    }

    private String buildContextualPrompt(Long sessionId, String currentMessage) {
        try {
            // 获取最近5条消息作为上下文
            List<ChatMessage> recentMessages = chatMessageRepository
                    .findRecentMessagesBySessionId(sessionId, 5);

            StringBuilder context = new StringBuilder();

            // 反转顺序，使对话按时间顺序排列
            for (int i = recentMessages.size() - 1; i >= 0; i--) {
                ChatMessage msg = recentMessages.get(i);
                String role = msg.getMessageType() == ChatMessage.MessageType.USER ? "用户" : "AI";
                context.append(role).append(": ").append(msg.getContent()).append("\n");
            }

            context.append("用户: ").append(currentMessage);
            return context.toString();
        } catch (Exception e) {
            // 如果获取上下文失败，直接返回当前消息
            return "用户: " + currentMessage;
        }
    }

    private void updateSessionTitle(ChatSession session, String firstMessage) {
        if (session.getTitle().equals("新对话")) {
            // 使用消息的前15个字符作为标题
            String title = firstMessage.length() > 15
                    ? firstMessage.substring(0, 15) + "..."
                    : firstMessage;
            session.setTitle(title);
            chatSessionRepository.save(session);
        }
    }

    // 获取用户的聊天历史
    public List<ChatSession> getUserChatHistory(String username) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                return chatSessionRepository.findByUserOrderByUpdatedAtDesc(userOpt.get());
            }
            return List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // 获取具体会话的消息
    public List<ChatMessage> getSessionMessages(Long sessionId) {
        try {
            return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // 删除会话
    @Transactional
    public void deleteSession(Long sessionId, String username) {
        try {
            Optional<ChatSession> sessionOpt = chatSessionRepository.findById(sessionId);
            if (sessionOpt.isEmpty()) {
                throw new RuntimeException("会话不存在");
            }

            ChatSession session = sessionOpt.get();
            if (!session.getUser().getUsername().equals(username)) {
                throw new RuntimeException("无权限删除此会话");
            }

            chatSessionRepository.delete(session);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除会话失败: " + e.getMessage());
        }
    }

    // 获取Ollama服务状态
    public String getOllamaStatus() {
        return ollamaService.getServiceStatus();
    }

    // 获取当前使用的AI模型
    public String getCurrentModel() {
        return ollamaService.getCurrentModel();
    }
}