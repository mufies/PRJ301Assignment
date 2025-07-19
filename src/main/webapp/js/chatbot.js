// ===== CHATBOT BACKEND INTEGRATION =====
let conversationHistory = [];
let allProducts = [];
let isInitialized = false;

// ✅ Khởi tạo dữ liệu menu
async function initializeGeminiBackend() {
    try {
        const res = await fetch('/Chatbot');
        const data = await res.json();
        allProducts = data.products || [];

        if (allProducts.length > 0) {
            isInitialized = true;
            // updateApiStatus('connected', '✅ Đã kết nối chatbot backend');
            console.log('🎉 Chatbot backend initialized successfully!');
        } else {
            throw new Error('No products found');
        }

    } catch (error) {
        console.error('❌ Lỗi khởi tạo chatbot backend:', error);
        updateApiStatus('error', '❌ Lỗi kết nối chatbot backend');
        addMessage('bot', '❌ Không thể khởi tạo dữ liệu chatbot từ hệ thống.', true);
    }
}

// ✅ Gửi tin nhắn đến backend
async function sendMessage() {
    const input = document.getElementById('chatInput');
    const message = input.value.trim();
    if (!message) return;

    if (!isInitialized) {
        addMessage('bot', 'Chatbot chưa được khởi tạo. Vui lòng thử lại sau.', true);
        return;
    }

    addMessage('user', message);
    input.value = '';
    input.disabled = true;
    document.getElementById('sendButton').disabled = true;
    showTyping();

    try {
        // ✅ Gửi đúng format JSON
        const res = await fetch('/Chatbot', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                message: message // ✅ Đúng key name
            })
        });

        if (!res.ok) {
            throw new Error(`HTTP ${res.status}: ${res.statusText}`);
        }

        const data = await res.json();

        if (data.error) {
            throw new Error(data.error);
        }

        hideTyping();
        addMessage('bot', data.reply);

    } catch (error) {
        console.error('❌ Lỗi gửi tin nhắn:', error);
        hideTyping();

        let errorMessage = 'Xin lỗi, có lỗi xảy ra khi xử lý tin nhắn.';
        if (error.message.includes('API')) {
            errorMessage = 'Lỗi API chatbot. Vui lòng thử lại sau.';
        } else if (error.message.includes('HTTP 500')) {
            errorMessage = 'Lỗi server. Vui lòng thử lại sau.';
        }

        addMessage('bot', errorMessage, true);
    }

    input.disabled = false;
    document.getElementById('sendButton').disabled = false;
    input.focus();
}

// ✅ Helper functions
function updateApiStatus(status, message) {
    const statusElement = document.getElementById('statusText');
    const apiStatus = document.getElementById('apiStatus');

    if (statusElement && apiStatus) {
        statusElement.textContent = message;
        apiStatus.className = 'api-status ' + (status === 'connected' ? 'status-connected' : 'status-error');
    }
}

function addMessage(sender, text, isError = false) {
    const messagesContainer = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;

    const avatar = sender === 'user' ? '👤' : '🤖';
    const avatarClass = sender === 'user' ? 'user-avatar' : 'bot-avatar';

    // Format text với markdown đơn giản
    let formattedText = text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/\n/g, '<br>');

    let messageContent = `
        <div class="avatar ${avatarClass}">${avatar}</div>
        <div class="message-bubble ${isError ? 'error-message' : ''}">${formattedText}</div>
    `;

    messageDiv.innerHTML = messageContent;
    messagesContainer.appendChild(messageDiv);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function showTyping() {
    const typingIndicator = document.getElementById('typingIndicator');
    if (typingIndicator) {
        typingIndicator.style.display = 'flex';
        const messagesContainer = document.getElementById('chatMessages');
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
}

function hideTyping() {
    const typingIndicator = document.getElementById('typingIndicator');
    if (typingIndicator) {
        typingIndicator.style.display = 'none';
    }
}

function handleKeyPress(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

// ✅ Khởi tạo khi load trang
window.addEventListener('load', async () => {
    const chatInput = document.getElementById('chatInput');
    if (chatInput) {
        chatInput.focus();
        chatInput.addEventListener('input', function () {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 100) + 'px';
        });
    }

    await initializeGeminiBackend();
});

// ✅ Global functions cho HTML
window.sendMessage = sendMessage;
window.handleKeyPress = handleKeyPress;
