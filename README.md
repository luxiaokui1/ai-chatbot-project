🤖 AI聊天机器人 v2.0 / AI ChatBot v2.0
English | 中文

中文
一个基于Spring Boot + JWT认证 + Ollama本地AI的现代化聊天机器人系统。
✨ 功能特性
🔐 用户认证系统

✅ 用户注册/登录
✅ JWT无状态认证
✅ BCrypt密码加密
✅ 角色权限管理
✅ 自动token管理

🤖 AI对话功能

✅ 本地Ollama AI集成
✅ DeepSeek-R1:1.5b模型
✅ 上下文记忆对话
✅ 多会话管理
✅ 聊天历史记录

🎨 现代化界面

✅ 响应式设计
✅ 自定义头像系统
✅ 现代化UI/UX
✅ 模块化前端架构

🏗️ 技术栈
后端技术

Spring Boot 3.2.0 - 主框架
Spring Security 6.x - 安全认证
Spring Data JPA - 数据持久层
MySQL 8.0 - 数据库
JWT - 无状态认证
BCrypt - 密码加密
Ollama - 本地AI服务

前端技术

HTML5 + CSS3 - 现代Web标准
ES6+ JavaScript - 模块化前端
Fetch API - 异步HTTP请求
响应式设计 - 移动端适配

开发工具

Maven - 构建管理
IntelliJ IDEA - 开发环境
Git - 版本控制

🚀 快速开始
环境要求

Java 17+
MySQL 8.0+
Ollama服务
Maven 3.6+

安装步骤

克隆项目
bashgit clone https://github.com/your-username/ai-chatbot-project.git
cd ai-chatbot-project

安装Ollama并下载模型
bash# 启动Ollama服务
ollama serve

# 下载AI模型
ollama pull deepseek-r1:1.5b

配置数据库
sqlCREATE DATABASE ai_chatbot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

修改配置文件
yaml# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_chatbot
    username: root
    password: your_password

运行项目
bashmvn spring-boot:run

访问应用
http://localhost:8080


📁 项目结构
src/
├── main/
│   ├── java/com/example/aichatbotproject/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # 控制器层
│   │   ├── dto/            # 数据传输对象
│   │   ├── entity/         # 实体类
│   │   ├── repository/     # 数据访问层
│   │   ├── security/       # 安全相关
│   │   └── service/        # 业务逻辑层
│   └── resources/
│       ├── static/         # 静态资源
│       │   ├── css/       # 样式文件
│       │   ├── js/        # JavaScript文件
│       │   └── images/    # 图片资源
│       └── application.yml # 配置文件
🔧 API接口
认证接口

POST /api/auth/register - 用户注册
POST /api/auth/login - 用户登录
POST /api/auth/validate - Token验证

聊天接口

POST /api/chat - 发送消息
GET /api/history - 获取聊天历史
GET /api/session/{id}/messages - 获取会话消息
DELETE /api/session/{id} - 删除会话

系统接口

GET /api/health - 健康检查
GET /api/status - 服务状态

🔐 安全特性

JWT认证 - 无状态token认证
密码加密 - BCrypt强加密算法
CORS保护 - 跨域请求控制
输入验证 - 防止注入攻击
权限控制 - 基于角色的访问控制

🎯 使用说明

注册账户 - 填写用户名、邮箱、密码
登录系统 - 使用注册的账户登录
开始对话 - 在聊天界面与AI助手对话
管理会话 - 查看历史对话，删除不需要的会话

🚧 未来计划

 Agent功能 - 智能工具调用
 RAG功能 - 文档知识库问答
 多模态支持 - 图片、文件上传
 WebSocket - 实时通信
 Docker化 - 容器化部署
 微服务架构 - 服务拆分

🤝 贡献指南

Fork 项目
创建功能分支 (git checkout -b feature/AmazingFeature)
提交更改 (git commit -m 'Add some AmazingFeature')
推送到分支 (git push origin feature/AmazingFeature)
打开 Pull Request

📄 许可证
本项目采用 MIT 许可证 - 查看 LICENSE 文件了解详情。
🙏 致谢

Spring Boot - 强大的Java框架
Ollama - 本地AI服务
DeepSeek - 优秀的开源AI模型



English
A modern chatbot system based on Spring Boot + JWT Authentication + Ollama Local AI.
✨ Features
🔐 User Authentication System

✅ User registration and login
✅ JWT stateless authentication
✅ BCrypt password encryption
✅ Role-based access control
✅ Automatic token management

🤖 AI Conversation Features

✅ Local Ollama AI integration
✅ DeepSeek-R1:1.5b model
✅ Context-aware conversations
✅ Multi-session management
✅ Chat history persistence

🎨 Modern Interface

✅ Responsive design
✅ Custom avatar system
✅ Modern UI/UX
✅ Modular frontend architecture

🏗️ Tech Stack
Backend Technologies

Spring Boot 3.2.0 - Main framework
Spring Security 6.x - Security authentication
Spring Data JPA - Data persistence layer
MySQL 8.0 - Database
JWT - Stateless authentication
BCrypt - Password encryption
Ollama - Local AI service

Frontend Technologies

HTML5 + CSS3 - Modern web standards
ES6+ JavaScript - Modular frontend
Fetch API - Asynchronous HTTP requests
Responsive Design - Mobile adaptation

Development Tools

Maven - Build management
IntelliJ IDEA - Development environment
Git - Version control

🚀 Quick Start
Requirements

Java 17+
MySQL 8.0+
Ollama service
Maven 3.6+

Installation Steps

Clone the project
bashgit clone https://github.com/your-username/ai-chatbot-project.git
cd ai-chatbot-project

Install Ollama and download model
bash# Start Ollama service
ollama serve

# Download AI model
ollama pull deepseek-r1:1.5b

Configure database
sqlCREATE DATABASE ai_chatbot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Modify configuration file
yaml# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_chatbot
    username: root
    password: your_password

Run the project
bashmvn spring-boot:run

Access the application
http://localhost:8080


📁 Project Structure
src/
├── main/
│   ├── java/com/example/aichatbotproject/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # Controller layer
│   │   ├── dto/            # Data transfer objects
│   │   ├── entity/         # Entity classes
│   │   ├── repository/     # Data access layer
│   │   ├── security/       # Security related
│   │   └── service/        # Business logic layer
│   └── resources/
│       ├── static/         # Static resources
│       │   ├── css/       # Style files
│       │   ├── js/        # JavaScript files
│       │   └── images/    # Image resources
│       └── application.yml # Configuration file
🔧 API Endpoints
Authentication APIs

POST /api/auth/register - User registration
POST /api/auth/login - User login
POST /api/auth/validate - Token validation

Chat APIs

POST /api/chat - Send message
GET /api/history - Get chat history
GET /api/session/{id}/messages - Get session messages
DELETE /api/session/{id} - Delete session

System APIs

GET /api/health - Health check
GET /api/status - Service status

🔐 Security Features

JWT Authentication - Stateless token authentication
Password Encryption - BCrypt strong encryption algorithm
CORS Protection - Cross-origin request control
Input Validation - Prevent injection attacks
Access Control - Role-based access control

🎯 Usage Guide

Register Account - Fill in username, email, password
Login System - Use registered account to login
Start Conversation - Chat with AI assistant in the interface
Manage Sessions - View chat history, delete unnecessary sessions

🚧 Future Plans

 Agent Features - Intelligent tool calling
 RAG Features - Document knowledge base Q&A
 Multimodal Support - Image and file upload
 WebSocket - Real-time communication
 Dockerization - Container deployment
 Microservices - Service decomposition

🤝 Contributing

Fork the project
Create your feature branch (git checkout -b feature/AmazingFeature)
Commit your changes (git commit -m 'Add some AmazingFeature')
Push to the branch (git push origin feature/AmazingFeature)
Open a Pull Request

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
🙏 Acknowledgments

Spring Boot - Powerful Java framework
Ollama - Local AI service
DeepSeek - Excellent open-source AI model
