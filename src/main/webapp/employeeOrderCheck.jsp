<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Đơn hàng mới</title>
    <link rel="stylesheet" href="css/employeeOrders.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        /* Modal CSS */
        #orderDetailsModal {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0; top: 0;
            width: 100vw; height: 100vh;
            background: rgba(0,0,0,0.5);
        }
        #orderDetailsModal .modal-content {
            background: #fff;
            margin: 5% auto;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
            position: relative;
        }
        #orderDetailsModal .close-modal {
            position: absolute; right: 10px; top: 10px;
            cursor: pointer; font-size: 18px;
        }
        #orderDetailsModal ul { padding-left: 18px; }
        #orderDetailsModal img { vertical-align: middle; margin-right: 8px; }
        #orderDetailsModal li { margin-bottom: 8px; }
    </style>
</head>
<body>
<header>
    <div class="logo">
        <a href="updateOrderStatus"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
    <nav>
        <a href="updateOrderStatus">Dashboard</a>
        <a href="employeeCheckingOrder">Create Order</a>
        <a href="updateEmployee">Update Info</a>
    </nav>
</header>
<h2>Đơn hàng đang chờ xử lý</h2>
<table id="orderTable">
    <thead>
    <tr>
        <th>Mã vận đơn</th>
        <th>Người đặt</th>
        <th>Ngày đặt</th>
        <th>Tổng tiền</th>
        <th>Trạng thái hiện tại</th>
        <th>Cập nhật trạng thái</th>
        <th>Chi tiết</th>
    </tr>
    </thead>
    <tbody></tbody>
</table>

<!-- Modal hiển thị chi tiết đơn hàng -->
<div id="orderDetailsModal">
    <div class="modal-content">
        <span class="close-modal" onclick="$('#orderDetailsModal').hide();">&times;</span>
        <div class="modal-body"></div>
    </div>
</div>

<script src="js/employeeOrderCheck.js"></script>
</body>
</html>
