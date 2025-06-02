package com.example.aichatbotproject.controller;

import com.example.aichatbotproject.dto.AuthResponse;
import com.example.aichatbotproject.dto.LoginRequest;
import com.example.aichatbotproject.dto.RegisterRequest;
import com.example.aichatbotproject.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("REGISTRATION_FAILED", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("INTERNAL_ERROR", "注册过程中发生未知错误"));
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("LOGIN_FAILED", "用户名或密码错误"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("INTERNAL_ERROR", "登录过程中发生未知错误"));
        }
    }

    /**
     * 验证Token接口
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // 移除 "Bearer " 前缀
            String jwtToken = token.substring(7);
            boolean isValid = authService.validateToken(jwtToken);

            if (isValid) {
                String username = authService.getUsernameFromToken(jwtToken);
                return ResponseEntity.ok(new ValidationResponse(true, username));
            } else {
                return ResponseEntity.ok(new ValidationResponse(false, null));
            }
        } catch (StringIndexOutOfBoundsException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("INVALID_TOKEN_FORMAT", "Token格式错误"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ValidationResponse(false, null));
        }
    }

    /**
     * 获取当前用户信息接口
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            if (authService.validateToken(jwtToken)) {
                String username = authService.getUsernameFromToken(jwtToken);
                return ResponseEntity.ok(new UserInfoResponse(username));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("INVALID_TOKEN", "Token无效或已过期"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("TOKEN_ERROR", "Token处理错误"));
        }
    }

    /**
     * 刷新Token接口（可选）
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            AuthResponse response = authService.refreshToken(jwtToken);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("REFRESH_FAILED", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("INTERNAL_ERROR", "刷新Token时发生错误"));
        }
    }

    // =================== 内部响应类 ===================

    /**
     * 错误响应类
     */
    public static class ErrorResponse {
        private String code;
        private String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        // Getter方法
        public String getCode() { return code; }
        public String getMessage() { return message; }

        // Setter方法
        public void setCode(String code) { this.code = code; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * Token验证响应类
     */
    public static class ValidationResponse {
        private boolean valid;
        private String username;

        public ValidationResponse(boolean valid, String username) {
            this.valid = valid;
            this.username = username;
        }

        // Getter方法
        public boolean isValid() { return valid; }
        public String getUsername() { return username; }

        // Setter方法
        public void setValid(boolean valid) { this.valid = valid; }
        public void setUsername(String username) { this.username = username; }
    }

    /**
     * 用户信息响应类
     */
    public static class UserInfoResponse {
        private String username;

        public UserInfoResponse(String username) {
            this.username = username;
        }

        // Getter和Setter方法
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}