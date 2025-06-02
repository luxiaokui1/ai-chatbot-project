// 主应用逻辑
const App = {
    // 初始化
    init: function() {
        console.log('应用初始化');
        this.bindEvents();
        Auth.checkAuthStatus();
    },

    // 绑定事件
    bindEvents: function() {
        // 登录表单
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('loginUsername').value;
            const password = document.getElementById('loginPassword').value;

            this.setLoginLoading(true);
            Utils.hideAlert('loginAlert');

            const result = await Auth.login(username, password);

            if (!result.success) {
                Utils.showAlert('loginAlert', result.message, 'error');
            }

            this.setLoginLoading(false);
        });

        // 注册表单
        document.getElementById('registerForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('registerUsername').value;
            const email = document.getElementById('registerEmail').value;
            const password = document.getElementById('registerPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                Utils.showAlert('registerAlert', '两次输入的密码不一致', 'error');
                return;
            }

            this.setRegisterLoading(true);
            Utils.hideAlert('registerAlert');

            const result = await Auth.register(username, email, password);

            if (!result.success) {
                Utils.showAlert('registerAlert', result.message, 'error');
            }

            this.setRegisterLoading(false);
        });
    },

    // 页面切换
    showLogin: function() {
        console.log('显示登录页面');
        Utils.show('loginPage');
        Utils.hide('registerPage');
        Utils.hide('chatPage');
        document.body.style.display = 'flex';
    },

    showRegister: function() {
        console.log('显示注册页面');
        Utils.hide('loginPage');
        Utils.show('registerPage');
        Utils.hide('chatPage');
        document.body.style.display = 'flex';
    },

    showChatPage: function() {
        console.log('显示聊天页面，当前用户:', Auth.currentUser);
        Utils.hide('loginPage');
        Utils.hide('registerPage');
        Utils.show('chatPage');
        document.body.style.display = 'block';
        document.getElementById('currentUser').textContent = Auth.currentUser;
    },

    // 按钮状态管理
    setLoginLoading: function(loading) {
        const btn = document.getElementById('loginBtn');
        const text = document.getElementById('loginBtnText');
        const loadingSpinner = document.getElementById('loginLoading');

        btn.disabled = loading;
        text.classList.toggle('hidden', loading);
        loadingSpinner.classList.toggle('hidden', !loading);
    },

    setRegisterLoading: function(loading) {
        const btn = document.getElementById('registerBtn');
        const text = document.getElementById('registerBtnText');
        const loadingSpinner = document.getElementById('registerLoading');

        btn.disabled = loading;
        text.classList.toggle('hidden', loading);
        loadingSpinner.classList.toggle('hidden', !loading);
    }
};

// 全局函数（保持向后兼容）
function showLogin() { App.showLogin(); }
function showRegister() { App.showRegister(); }
function logout() { Auth.logout(); }
function sendMessage() {
    const input = document.getElementById('chatInput');
    const message = input.value.trim();
    if (message) {
        Chat.sendMessage(message);
        input.value = '';
    }
}
function handleEnter(event) {
    if (event.key === 'Enter') {
        sendMessage();
    }
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    App.init();
});