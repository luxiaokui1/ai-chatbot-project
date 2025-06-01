package com.example.aichatbotproject.repository;

import com.example.aichatbotproject.entity.ChatSession;
import com.example.aichatbotproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    // 根据用户查找所有会话，按更新时间倒序
    List<ChatSession> findByUserOrderByUpdatedAtDesc(User user);

    // 根据用户ID查找会话
    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.id = ?1 ORDER BY cs.updatedAt DESC")
    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(Long userId);

    // 查找用户最近的会话
    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.id = ?1 ORDER BY cs.updatedAt DESC LIMIT 1")
    ChatSession findLatestSessionByUserId(Long userId);

    // 统计用户的会话总数
    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.user.id = ?1")
    long countByUserId(Long userId);
}
