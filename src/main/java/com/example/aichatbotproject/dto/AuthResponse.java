package com.example.aichatbotproject.dto;

/**
 * 认证响应DTO
 */
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;
    private Long expiresIn;

    // 无参构造函数
    public AuthResponse() {}

    // 有参构造函数
    public AuthResponse(String token, String username, String email, String role, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    // 简化构造函数（只包含token和用户名）
    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
        this.expiresIn = 86400000L; // 默认24小时
    }

    // Getter和Setter方法
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + (token != null ? "***" : null) + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}