package com.example.aichatbotproject.repository;

import com.example.aichatbotproject.entity.ChatMessage;
import com.example.aichatbotproject.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 根据会话查找所有消息，按创建时间正序
    List<ChatMessage> findByChatSessionOrderByCreatedAtAsc(ChatSession chatSession);

    // 根据会话ID查找消息
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatSession.id = ?1 ORDER BY cm.createdAt ASC")
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

    // 查找会话最近的几条消息
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatSession.id = ?1 ORDER BY cm.createdAt DESC LIMIT ?2")
    List<ChatMessage> findRecentMessagesBySessionId(Long sessionId, int limit);

    // 统计会话的消息总数
    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatSession.id = ?1")
    long countBySessionId(Long sessionId);
}