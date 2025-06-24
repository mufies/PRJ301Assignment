<!DOCTYPE html>
<html>
<head>
    <title>Đổi mật khẩu</title>
    <meta charset="UTF-8">
</head>
<body>
<h2>Đổi mật khẩu</h2>
<form action="ChangePassword" method="post">
    <label for="code">Code</label><br>
    <input type="text" id="code" name="code" required /><br><br>
    <label for="new_password">Mật khẩu mới:</label><br>
    <input type="password" id="new_password" name="new_password" required /><br><br>

    <label for="confirm_password">Nhập lại mật khẩu mới:</label><br>
    <input type="password" id="confirm_password" name="confirm_password" required /><br><br>

    <button type="submit">Đổi mật khẩu</button>
</form>
</body>
</html>
