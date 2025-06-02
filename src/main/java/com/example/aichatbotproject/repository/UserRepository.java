package com.example.aichatbotproject.repository;

import com.example.aichatbotproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 查找活跃用户（最近24小时内有活动）
     */
    @Query("SELECT u FROM User u WHERE u.lastActive >= :since")
    List<User> findActiveUsers(@Param("since") LocalDateTime since);

    /**
     * 统计用户总数
     */
    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();

    /**
     * 查找启用的用户
     */
    List<User> findByEnabledTrue();

    /**
     * 根据角色查找用户
     */
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") User.Role role);

    /**
     * 根据用户名模糊查询
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    List<User> findByUsernameContaining(@Param("username") String username);

    /**
     * 查找最近注册的用户
     */
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers();
}