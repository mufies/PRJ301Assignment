<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Mam Mam</title>
    <link rel="stylesheet" href="css/login.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="login-container">
        <img src="images/logo.png" alt="Mam Mam Logo" class="logo">
        <h1 class="title">MĂM MĂM</h1>
        <h2 class="subtitle">VUI LÒNG ĐĂNG NHẬP</h2>
        
        <form action="login" method="post">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" placeholder="Số điện thoại / Gmail" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <div class="forgot">
                <a href="#">Quên mật khẩu?</a>
            </div>

            <button type="submit">Submit</button>
        </form>

        <p class="register">
            Bạn chưa có tài khoản? <a href="register.jsp"><strong>Đăng ký ngay</strong></a>
        </p>
    </div>
</body>
</html>
