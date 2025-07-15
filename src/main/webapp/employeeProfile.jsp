<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin nhân viên</title>
    <link rel="stylesheet" href="css/edit-employee.css">
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

<script src="js/employeeProfile.js"></script>
</body>
</html>