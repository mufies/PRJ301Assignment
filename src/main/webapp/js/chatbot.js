// ===== THAY ĐỔI API KEY TẠI ĐÂY =====
const API_KEY = "AIzaSyAv8wJRdkz2koc4XrU2vMWJokZgOtpAivo"; // Thay bằng Generative Language API key của bạn
// ====================================

let isInitialized = false;
let conversationHistory = [];
let allProducts = []; // Dữ liệu món ăn từ servlet

// ✅ Khởi tạo API khi load trang
async function initializeAPI() {
    try {
        const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-001:generateContent?key=${API_KEY}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                contents: [{ parts: [{ text: "Hi" }] }],
                generationConfig: {
                    temperature: 0.7,
                    topK: 40,
                    topP: 0.8,
                    maxOutputTokens: 1024,
                }
            })
        });

        if (response.ok) {
            isInitialized = true;
            updateApiStatus('connected', '✅ Đã kết nối Gemini API');
            console.log('🎉 Generative Language API connected successfully!');
        } else {
            const errorData = await response.json();
            throw new Error(`API Error: ${errorData.error?.message || 'Unknown error'}`);
        }
    } catch (error) {
        console.error('❌ API Error:', error);
        updateApiStatus('error', '❌ Lỗi kết nối API - Kiểm tra API key');
        addMessage('bot', `Có lỗi xảy ra khi kết nối API: ${error.message}`, true);
    }
}

// ✅ Tải danh sách món ăn từ servlet
async function fetchInitialData() {
    try {
        const response = await fetch('/Chatbot');
        const data = await response.json();
        allProducts = data.products || [];

        const productList = allProducts.map(p => `- ${p.name}: ${p.price}₫`).join('\n');

        const introPrompt = `
Bạn là nhân viên tư vấn món ăn cho nhà hàng.
Dưới đây là danh sách món hiện có:\n\n${productList}

Hãy gợi ý món theo yêu cầu người dùng, trả lời bằng tiếng Việt tự nhiên.`;

        conversationHistory.push({
            role: "user",
            parts: [{ text: introPrompt }]
        });

    } catch (err) {
        console.error("⚠️ Lỗi lấy dữ liệu menu:", err);
        addMessage('bot', '⚠️ Không thể tải danh sách món ăn từ hệ thống.', true);
    }
}


function updateApiStatus(status, message) {
    const statusElement = document.getElementById('statusText');
    const apiStatus = document.getElementById('apiStatus');
    statusElement.textContent = message;
    apiStatus.className = 'api-status ' + (status === 'connected' ? 'status-connected' : 'status-error');
}

function addMessage(sender, text, isError = false) {
    const messagesContainer = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;

    const avatar = sender === 'user' ? '👤' : '🤖';
    const avatarClass = sender === 'user' ? 'user-avatar' : 'bot-avatar';

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
    document.getElementById('typingIndicator').style.display = 'flex';
    document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
}

function hideTyping() {
    document.getElementById('typingIndicator').style.display = 'none';
}

// Gửi prompt đến Gemini
async function sendMessageToGemini(message) {
    conversationHistory.push({ role: "user", parts: [{ text: message }] });

    const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=${API_KEY}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            contents: conversationHistory,
            generationConfig: {
                temperature: 0.7,
                topK: 40,
                topP: 0.8,
                maxOutputTokens: 1024,
            },
            safetySettings: [
                { category: "HARM_CATEGORY_HARASSMENT", threshold: "BLOCK_MEDIUM_AND_ABOVE" },
                { category: "HARM_CATEGORY_HATE_SPEECH", threshold: "BLOCK_MEDIUM_AND_ABOVE" },
                { category: "HARM_CATEGORY_SEXUALLY_EXPLICIT", threshold: "BLOCK_MEDIUM_AND_ABOVE" },
                { category: "HARM_CATEGORY_DANGEROUS_CONTENT", threshold: "BLOCK_MEDIUM_AND_ABOVE" }
            ]
        })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`HTTP ${response.status}: ${errorData.error?.message || 'Unknown error'}`);
    }

    const data = await response.json();
    const responseText = data.candidates[0].content.parts[0].text;

    conversationHistory.push({ role: "model", parts: [{ text: responseText }] });

    // Giới hạn lịch sử
    if (conversationHistory.length > 20) {
        conversationHistory = conversationHistory.slice(-20);
    }

    return responseText;
}

// Gửi tin nhắn người dùng
async function sendMessage() {
    const input = document.getElementById('chatInput');
    const message = input.value.trim();
    if (!message) return;

    if (!isInitialized) {
        addMessage('bot', 'API chưa được khởi tạo. Vui lòng kiểm tra API key và reload trang.', true);
        return;
    }

    addMessage('user', message);
    input.value = '';
    input.disabled = true;
    document.getElementById('sendButton').disabled = true;

    showTyping();

    try {
        const response = await sendMessageToGemini(message);
        hideTyping();
        addMessage('bot', response);
    } catch (error) {
        console.error('💬 Chat Error:', error);
        hideTyping();
        let errorMessage = 'Xin lỗi, có lỗi xảy ra khi xử lý tin nhắn.';

        if (error.message.includes('API_KEY_INVALID')) {
            errorMessage = 'API key không hợp lệ. Vui lòng kiểm tra Generative Language API key.';
        } else if (error.message.includes('QUOTA_EXCEEDED')) {
            errorMessage = 'Đã vượt quá giới hạn API. Vui lòng thử lại sau.';
        } else if (error.message.includes('SAFETY')) {
            errorMessage = 'Tin nhắn bị từ chối do vi phạm chính sách an toàn.';
        }

        addMessage('bot', errorMessage, true);
    }

    input.disabled = false;
    document.getElementById('sendButton').disabled = false;
    input.focus();
}

// Xử lý Enter
function handleKeyPress(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

// Khởi chạy khi trang tải
window.addEventListener('load', async () => {
    document.getElementById('chatInput').focus();
    await initializeAPI();
    if (isInitialized) {
        await fetchInitialData();
    }
});

// Tự co input
document.getElementById('chatInput').addEventListener('input', function () {
    this.style.height = 'auto';
    this.style.height = Math.min(this.scrollHeight, 100) + 'px';
});
