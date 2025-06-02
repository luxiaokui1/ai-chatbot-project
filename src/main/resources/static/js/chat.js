// 聊天相关功能
const Chat = {
    // 发送消息
    sendMessage: async function(message) {
        if (!message.trim()) return;

        console.log('发送消息:', message);

        this.addMessage('user', message);
        this.setSendButtonState(true);

        const thinkingId = this.addMessage('ai', '正在思考...');

        try {
            const { response, data } = await Utils.request(`${Utils.API_BASE}/chat`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${Auth.authToken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message })
            });

            document.getElementById(thinkingId).remove();

            if (response.ok && data.success) {
                this.addMessage('ai', data.response);
            } else {
                this.addMessage('ai', `错误: ${data.error || '请求失败'}`);
            }
        } catch (error) {
            console.error('发送消息失败:', error);
            document.getElementById(thinkingId).remove();
            this.addMessage('ai', '网络错误，请稍后重试');
        } finally {
            this.setSendButtonState(false);
        }
    },

    // 添加消息到界面
    addMessage: function(type, content) {
        const messagesContainer = document.getElementById('chatMessages');
        const messageDiv = document.createElement('div');
        const messageId = 'msg_' + Date.now();

        messageDiv.id = messageId;
        messageDiv.className = `message ${type}`;

        if (type === 'user') {
            messageDiv.innerHTML = `<strong>您:</strong> ${content}`;
        } else {
            messageDiv.innerHTML = `<strong>AI助手:</strong> ${content}`;
        }

        messagesContainer.appendChild(messageDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        return messageId;
    },

    // 设置发送按钮状态
    setSendButtonState: function(disabled) {
        const sendBtn = document.getElementById('sendBtn');
        sendBtn.disabled = disabled;
        sendBtn.textContent = disabled ? '发送中...' : '发送';
    },

    // 清空消息
    clearMessages: function() {
        const messagesContainer = document.getElementById('chatMessages');
        messagesContainer.innerHTML = '<div class="message ai"><strong>AI助手:</strong> 您好，我是AI助手，有什么可以帮助您的吗？</div>';
    }
};