<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin nhân viên</title>
    <link rel="stylesheet" href="css/edit-employee.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<style>
    /* Tổng thể toàn trang */
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #FFF8F3;
        margin: 0;
        padding: 0;
    }

    /* Khung form */
    .form-container {
        background-color: #ffffff;
        max-width: 600px;
        margin: 60px auto;
        padding: 40px 50px;
        border-radius: 16px;
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
        border-top: 8px solid #9D3B3B;
    }

    /* Tiêu đề */
    .form-container h2 {
        text-align: center;
        color: #9D3B3B;
        margin-bottom: 30px;
        font-size: 24px;
    }

    /* Nhãn */
    label {
        font-weight: 600;
        color: #333;
        display: block;
        margin-bottom: 6px;
    }

    /* Input */
    input[type="text"],
    input[type="password"],
    input[type="email"],
    input[type="number"] {
        width: 100%;
        padding: 12px;
        border: 1px solid #ddd;
        border-radius: 8px;
        margin-bottom: 20px;
        font-size: 15px;
        transition: border-color 0.3s ease;
    }

    input:focus {
        border-color: #9D3B3B;
        outline: none;
    }

    /* Nút submit */
    button {
        background-color: #9D3B3B;
        color: white;
        padding: 14px;
        width: 100%;
        font-size: 16px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    button:hover {
        background-color: #B44747;
    }

    /* Thông báo lỗi nếu cần */
    .error-message {
        color: #c0392b;
        text-align: center;
        margin-top: 20px;
        font-weight: bold;
    }
</style>

<body>
<header>
    <div class="logo">
        <a href="updateOrderStatus"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
    <nav>
        <a href="updateOrderStatus">Dashboard</a>
        <a href="employeeCheckingOrder">Create Order</a>
        <a href="updateEmployee">Update Info</a>
        <a href="" onclick="logout()">Logout</a>


    </nav>
</header>
<div class="form-container">
    <h2>Chỉnh sửa thông tin nhân viên</h2>

    <form action="#" method="post" onsubmit="event.preventDefault(); updateEmployeeInfo();">
        <input type="hidden" name="employee_id" value="">

        <label>Username:</label><br/>
        <input type="text" name="username" required readonly/><br/><br/>

        <label>Password:</label><br/>
        <input type="password" name="password" required/><br/><br/>

        <label>Họ tên:</label><br/>
        <input type="text" name="fullName" required/><br/><br/>

        <label>Email:</label><br/>
        <input type="email" name="email" required/><br/><br/>

        <label>Số điện thoại:</label><br/>
        <input type="text" name="phone" required/><br/><br/>

        <label>Địa chỉ:</label><br/>
        <input type="text" name="address" required/><br/><br/>



        <button type="submit">Cập nhật</button>
    </form>

</div>
<script>
    function logout() {
        localStorage.removeItem('jwt');
        window.location.href = 'home';
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
            window.location.replace('<%=request.getContextPath()%>/home');
            return;
        }

        try {
            const role = await getRoleFromJwt(token);
            if (role !== 'Employee') {
                window.location.replace('<%=request.getContextPath()%>/home');
                return;
            }
        } catch (e) {
            console.error('Error checking role:', e);
            window.location.replace('<%=request.getContextPath()%>/home');
        }
    })();
</script>

<script src="js/employeeProfile.js"></script>
</body>
</html>