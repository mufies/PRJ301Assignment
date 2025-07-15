<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử mua hàng</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/history.css">
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
<script src="js/update.js"></script>

<body>
<h2>Lịch sử mua hàng</h2>

<table border="1">
    <thead>
    <tr>
        <th>Mã đơn</th>
        <th>Ngày đặt</th>
        <th>Tổng tiền</th>
        <th>Trạng thái</th>
    </tr>
    </thead>
    <tbody id="order-history-body">
    </tbody>
</table>

<p id="no-orders" style="display:none;">Không có đơn hàng nào.</p>

<script>
    const contextPath = "${pageContext.request.contextPath}";

    function isJwtValid(token) {
        if (!token) return false;
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return !payload.exp || (Date.now() / 1000 < payload.exp);
        } catch (e) {
            return false;
        }
    }




    window.addEventListener('DOMContentLoaded', loadOrderHistory);
</script>
</body>
</html>
