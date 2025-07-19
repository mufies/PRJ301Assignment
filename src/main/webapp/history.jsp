<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử mua hàng</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/history.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #fff3e6;
            margin: 0;
            padding: 20px;
            color: #333;
        }

        h2 {
            color: #b22222;
            font-size: 28px;
            text-align: center;
            margin-bottom: 30px;
        }

        table {
            width: 80%;
            margin: 0 auto;
            border-collapse: collapse;
            background-color: #ffffff;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        thead {
            background-color: #b22222;
            color: white;
        }

        th, td {
            padding: 12px 16px;
            text-align: center;
            border: 1px solid #ddd;
        }

        tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tbody tr:hover {
            background-color: #ffe6e6;
            transition: background-color 0.3s ease;
        }

        #no-orders {
            text-align: center;
            margin-top: 20px;
            font-size: 18px;
            color: red;
            font-weight: bold;
        }

        .order-row {
            cursor: pointer;
            background-color: #fff;
        }

        .order-row:hover {
            background-color: #fce8e8;
        }

        .order-details td {
            background-color: #fff8f8;
            text-align: left;
            padding: 20px;
        }

        @media (max-width: 768px) {
            table {
                width: 100%;
                font-size: 14px;
            }
        }
    </style>

</head>

<body>
<header>
    <div class="logo">
        <a href="home"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
</header>
<h2>Lịch sử mua hàng</h2>

<table border="1">
    <thead>
    <tr>
        <th>Ngày đặt</th>
        <th>Tổng tiền</th>
        <th>Trạng thái</th>
    </tr>
    </thead>
    <tbody id="order-history-body">
    </tbody>
</table>

<p id="no-orders" style="display:none;">Không có đơn hàng nào.</p>
<div id="loggedModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeLoggedModal()">&times;</span>
        <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
        <h2 class="modal-subtitle">TÀI KHOẢN CỦA BẠN</h2>
        <div class="user-options" style="justify-content: center; align-items: center;">
            <button class="option-btn" onclick="window.location.href='profile'" style="justify-content: center; align-items: center;">
                <i class="fa-solid fa-gear"></i>
                Cài đặt tài khoản
            </button>
            <button class="option-btn" onclick="window.location.href='history'" style="justify-content: center; align-items: center;">
                <i class="fa-solid fa-clock-rotate-left"></i>
                Lịch sử mua hàng
            </button>
            <button class="option-btn logout-btn" onclick="logout()" style="justify-content: center; align-items: center;">
                <i class="fa-solid fa-right-from-bracket"></i>
                Đăng xuất
            </button>
        </div>
    </div>
</div>
<script src="js/update.js"></script>

<script>
    const contextPath = "${pageContext.request.contextPath}";
    window.addEventListener('DOMContentLoaded', loadOrderHistory);
</script>

</body>
</html>
