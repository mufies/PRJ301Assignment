<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Đơn hàng mới</title>
    <link rel="stylesheet" href="css/employeeOrders.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        /* Modal CSS - giữ nguyên */
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
<script>
    // ✅ Sửa function logout
    function logout() {
        localStorage.removeItem('jwt');
        window.location.href = '<%=request.getContextPath()%>/home'; // ✅ Thêm context path
    }

    // ✅ Định nghĩa functions trước
    async function isJwtValid(token) {
        if (!token) return false;
        try {
            const requestData = { isJwtValid: token };
            const response = await fetch('JwtServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.isJwtValid === true;
        } catch (e) {
            console.error("❌ Lỗi khi kiểm tra JWT:", e);
            return false;
        }
    }

    async function getRoleFromJwt(token) {
        if (!token) return null;

        try {
            const requestData = { getRole: token };
            const response = await fetch('JwtServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            if (data.error) {
                console.error('Error getting role from JWT:', data.error);
                return null;
            }

            return data.getRole;

        } catch (error) {
            console.error('Error calling JwtServlet:', error);
            return null;
        }
    }

    // ✅ Wrap authentication check trong async IIFE
    (async function() {
        const token = localStorage.getItem('jwt');

        if (!(await isJwtValid(token))) {
            alert('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
            window.location.replace('<%=request.getContextPath()%>/menu');
            return;
        }

        try {
            const role = await getRoleFromJwt(token);
            if (role !== 'Employee') {
                alert('Bạn không có quyền truy cập trang này.');
                window.location.replace('<%=request.getContextPath()%>/menu');
                return;
            }
        } catch (e) {
            console.error('Error checking role:', e);
            window.location.replace('<%=request.getContextPath()%>/menu');
        }
    })();

</script>

<header>
    <div class="logo">
        <a href="updateOrderStatus"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
    <nav>
        <a href="updateOrderStatus">Dashboard</a>
        <a href="employeeCheckingOrder">Create Order</a>
        <a href="updateEmployee">Update Info</a>
        <a href="#" onclick="logout()">Logout</a>
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
