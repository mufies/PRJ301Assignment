<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Chat</title>
    <style>
        body { margin: 0; font-family: Arial, sans-serif; }
        .container { display: flex; height: 100vh; }
        .sidebar {
            width: 240px;
            background: #f5f6fa;
            border-right: 1px solid #ddd;
            overflow-y: auto;
        }
        .sidebar h2 {
            padding: 16px;
            margin: 0;
            background: #007bff;
            color: #fff;
            font-size: 18px;
        }
        .user-list { list-style: none; margin: 0; padding: 0; }
        .user-list li {
            padding: 12px 16px;
            border-bottom: 1px solid #eee;
            cursor: pointer;
        }
        .user-list li.active, .user-list li:hover {
            background: #e9ecef;
            font-weight: bold;
        }
        .chat-area {
            flex: 1;
            display: flex;
            flex-direction: column;
            background: #fff;
        }
        .chat-header {
            padding: 16px;
            background: #f1f3f6;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }
        .chat-messages {
            flex: 1;
            padding: 16px;
            overflow-y: auto;
            background: #fafbfc;
        }
        .chat-input-area {
            display: flex;
            padding: 12px 16px;
            border-top: 1px solid #ddd;
            background: #f5f6fa;
        }
        .chat-input-area input {
            flex: 1;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .chat-input-area button {
            margin-left: 8px;
            padding: 8px 16px;
            background: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Sidebar: User list -->
    <div class="sidebar">
        <h2>Người dùng</h2>
        <ul class="user-list" id="user-list">
            <!-- User list will be updated in real time -->
        </ul>
    </div>
    <!-- Chat area -->
    <div class="chat-area">
        <div class="chat-header">
            Chat với: <span id="chat-header-username">[Chọn người dùng]</span>
        </div>
        <div class="chat-messages" id="chat-messages">
            <!-- Messages will appear here -->
        </div>
        <div class="chat-input-area">
            <input id="chat-input" type="text" placeholder="Nhập tin nhắn..." onkeydown="if(event.key==='Enter') sendMessage()" />
            <button onclick="sendMessage()">Gửi</button>
        </div>
    </div>
</div>
<script>
    // Replace with your actual token
    var token = "employee-jwt-token";
    var ws = new WebSocket("ws://localhost:8080/chat?token=" + token + "&role=employee");

    var userList = {};
    var currentChatUser = null;

    ws.onmessage = function(event) {
        var data = JSON.parse(event.data);

        if (data.type === "user_status") {
            if (data.status === "online") {
                userList[data.username] = true;
                addUserToSidebar(data.username);
            } else if (data.status === "offline") {
                delete userList[data.username];
                removeUserFromSidebar(data.username);
            }
        }

        if (data.type === "message") {
            if (data.from === currentChatUser) {
                addMessageToChat(data.from, data.content);
            } else {
                // Optionally show unread badge
            }
        }
    };

    function addUserToSidebar(username) {
        if (!document.getElementById('user-' + username)) {
            var li = document.createElement('li');
            li.id = 'user-' + username;
            li.textContent = username;
            li.onclick = function() { selectUser(username); };
            document.getElementById('user-list').appendChild(li);
        }
    }

    function removeUserFromSidebar(username) {
        var li = document.getElementById('user-' + username);
        if (li) li.remove();
    }

    function selectUser(username) {
        currentChatUser = username;
        document.getElementById('chat-header-username').innerText = username;
        document.getElementById('chat-messages').innerHTML = '';
        document.querySelectorAll('.user-list li').forEach(li => li.classList.remove('active'));
        var li = document.getElementById('user-' + username);
        if (li) li.classList.add('active');
        // Optionally load chat history here
    }

    function addMessageToChat(from, content) {
        var messages = document.getElementById('chat-messages');
        var div = document.createElement('div');
        div.textContent = from + ': ' + content;
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }

    function sendMessage() {
        var input = document.getElementById('chat-input');
        var msg = input.value.trim();
        if (msg !== '' && currentChatUser) {
            ws.send(JSON.stringify({ type: "message", to: currentChatUser, content: msg }));
            addMessageToChat('Bạn', msg);
            input.value = '';
        }
    }
</script>
</body>
</html>
