// 认证相关功能
const Auth = {
    currentUser: null,
    authToken: null,

    // 检查认证状态
    checkAuthStatus: async function() {
        const token = Utils.storage.get('authToken');
        console.log('检查本地存储的token:', token ? '存在' : '不存在');

        if (token) {
            try {
                const { response, data } = await Utils.request(`${Utils.API_BASE}/auth/validate`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                console.log('Token验证响应状态:', response.status);

                if (response.ok && data.valid) {
                    this.authToken = token;
                    this.currentUser = data.username;
                    App.showChatPage();
                    return;
                }
            } catch (error) {
                console.error('验证token失败:', error);
            }

            Utils.storage.remove('authToken');
        }

        App.showLogin();
    },

    // 登录
    login: async function(username, password) {
        console.log('登录用户:', username);

        try {
            const { response, data } = await Utils.request(`${Utils.API_BASE}/auth/login`, {
                method: 'POST',
                body: JSON.stringify({ username, password })
            });

            console.log('登录响应状态:', response.status);

            if (response.ok) {
                this.authToken = data.token;
                this.currentUser = data.username;
                Utils.storage.set('authToken', data.token);
                console.log('登录成功');
                App.showChatPage();
                return { success: true };
            } else {
                return { success: false, message: data.message || '登录失败' };
            }
        } catch (error) {
            console.error('登录请求失败:', error);
            return { success: false, message: '网络错误，请稍后重试' };
        }
    },

    // 注册
    register: async function(username, email, password) {
        console.log('注册用户:', username);

        try {
            const { response, data } = await Utils.request(`${Utils.API_BASE}/auth/register`, {
                method: 'POST',
                body: JSON.stringify({ username, email, password })
            });

            console.log('注册响应状态:', response.status);

            if (response.ok) {
                this.authToken = data.token;
                this.currentUser = data.username;
                Utils.storage.set('authToken', data.token);
                console.log('注册成功');
                App.showChatPage();
                return { success: true };
            } else {
                return { success: false, message: data.message || '注册失败' };
            }
        } catch (error) {
            console.error('注册请求失败:', error);
            return { success: false, message: '网络错误，请稍后重试' };
        }
    },

    // 退出登录
    logout: function() {
        console.log('用户退出登录');
        Utils.storage.remove('authToken');
        this.authToken = null;
        this.currentUser = null;
        Chat.clearMessages();
        App.showLogin();
    }
};