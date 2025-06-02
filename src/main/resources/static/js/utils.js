// 通用工具函数
const Utils = {
    // API配置
    API_BASE: '/api',

    // 显示/隐藏元素
    show: function(elementId) {
        document.getElementById(elementId).classList.remove('hidden');
    },

    hide: function(elementId) {
        document.getElementById(elementId).classList.add('hidden');
    },

    // 显示警告信息
    showAlert: function(alertId, message, type) {
        const alert = document.getElementById(alertId);
        alert.textContent = message;
        alert.className = `alert alert-${type}`;
        alert.classList.remove('hidden');
    },

    // 隐藏警告信息
    hideAlert: function(alertId) {
        document.getElementById(alertId).classList.add('hidden');
    },

    // 本地存储操作
    storage: {
        set: function(key, value) {
            localStorage.setItem(key, value);
        },

        get: function(key) {
            return localStorage.getItem(key);
        },

        remove: function(key) {
            localStorage.removeItem(key);
        }
    },

    // HTTP请求封装
    request: async function(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json'
            }
        };

        const finalOptions = { ...defaultOptions, ...options };

        try {
            const response = await fetch(url, finalOptions);
            const data = await response.json();
            return { response, data };
        } catch (error) {
            throw new Error(`请求失败: ${error.message}`);
        }
    }
};