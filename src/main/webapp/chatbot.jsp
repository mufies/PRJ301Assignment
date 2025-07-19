<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Fix font loading -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">

    <title>AI Chatbot - Gemini API</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {

            font-family: 'Inter', system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            -webkit-font-smoothing: antialiased;
            text-rendering: optimizeLegibility;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .chatbot-container {
            width: 400px;
            height: 600px;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }

        .chat-header {
            background: linear-gradient(135deg, #4CAF50, #45a049);
            color: white;
            padding: 20px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .chat-header h1 {
            font-size: 24px;
            margin-bottom: 5px;
        }

        .chat-header p {
            font-size: 14px;
            opacity: 0.9;
        }

        .chat-messages {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
            background: #f8f9fa;
        }

        .message {
            margin-bottom: 15px;
            display: flex;
            align-items: flex-start;
            animation: fadeIn 0.3s ease;
        }

        .message.user {
            justify-content: flex-end;
        }

        .message.bot {
            justify-content: flex-start;
        }

        .message-bubble {
            max-width: 80%;
            padding: 12px 16px;
            border-radius: 18px;
            word-wrap: break-word;
            line-height: 1.4;
        }

        .message.user .message-bubble {
            background: linear-gradient(135deg, #4CAF50, #45a049);
            color: white;
            border-bottom-right-radius: 5px;
        }

        .message.bot .message-bubble {
            background: white;
            color: #333;
            border: 1px solid #e0e0e0;
            border-bottom-left-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .avatar {
            width: 35px;
            height: 35px;
            border-radius: 50%;
            margin: 0 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            color: white;
        }

        .user-avatar {
            background: linear-gradient(135deg, #667eea, #764ba2);
        }

        .bot-avatar {
            background: linear-gradient(135deg, #4CAF50, #45a049);
        }

        .chat-input-container {
            padding: 20px;
            background: white;
            border-top: 1px solid #e0e0e0;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .chat-input {
            flex: 1;
            padding: 12px 16px;
            border: 2px solid #e0e0e0;
            border-radius: 25px;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s ease;
        }

        .chat-input:focus {
            border-color: #4CAF50;
        }

        .send-button {
            width: 45px;
            height: 45px;
            border: none;
            border-radius: 50%;
            background: linear-gradient(135deg, #4CAF50, #45a049);
            color: white;
            font-size: 18px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: transform 0.2s ease;
        }

        .send-button:hover {
            transform: scale(1.1);
        }

        .send-button:disabled {
            background: #ccc;
            cursor: not-allowed;
            transform: scale(1);
        }

        .typing-indicator {
            display: none;
            padding: 10px 16px;
            color: #666;
            font-style: italic;
            font-size: 13px;
            align-items: center;
        }

        .typing-dots {
            display: inline-block;
        }

        .typing-dots::after {
            content: '';
            animation: typing 1.4s infinite;
        }

        @keyframes typing {
            0%, 60% { content: ''; }
            30% { content: '.'; }
            60% { content: '..'; }
            90% { content: '...'; }
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .api-status {
            text-align: center;
            padding: 10px;
            font-size: 12px;
            color: #4CAF50;
            border-bottom: 1px solid #e0e0e0;
        }

        .status-connected {
            color: #4CAF50;
        }

        .status-error {
            color: #f44336;
        }

        .error-message {
            background: #ffebee !important;
            border: 1px solid #ffcdd2 !important;
            color: #d32f2f !important;
        }
        .message-bubble {
            font-family: inherit;
            word-break: break-word;
            white-space: pre-wrap;
        }

        /* Responsive */
        @media (max-width: 480px) {
            .chatbot-container {
                width: 95%;
                height: 90vh;
            }
        }
    </style>
</head>
<body>
<div class="chatbot-container">
    <div class="chat-header">
        <h1>🤖 AI Chatbot</h1>
        <p>Powered by Google Gemini API</p>
    </div>

    <div class="api-status" id="apiStatus">
        <span id="statusText">🔄 Đang kết nối Gemini API...</span>
    </div>

    <div class="chat-messages" id="chatMessages">
        <div class="message bot">
            <div class="avatar bot-avatar">🤖</div>
            <div class="message-bubble">
                Xin chào! Tôi là AI chatbot được kết nối với Gemini API. Hãy hỏi tôi bất kỳ điều gì bạn muốn! ✨
            </div>
        </div>
    </div>

    <div class="typing-indicator" id="typingIndicator">
        <div class="avatar bot-avatar">🤖</div>
        <div class="typing-dots">AI đang gõ</div>
    </div>

    <div class="chat-input-container">
        <input type="text" class="chat-input" id="chatInput"
               placeholder="Nhập tin nhắn..."
               onkeypress="handleKeyPress(event)">
        <button class="send-button" id="sendButton" onclick="sendMessage()">
            📤
        </button>
    </div>
</div>

<script src="js/chatbot.js"></script>
</body>
</html>
