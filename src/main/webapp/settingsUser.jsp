<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thông tin tài khoản</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<header>
    <div class="logo">
        <a href="home"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
</header>
<h2>Cập nhật thông tin</h2>


<label>Password:</label>
<input type="password" id="password" placeholder="*****" required><br>

<label>Họ và tên:</label>
<input type="text" id="full_name" required><br>

<label>Email:</label>
<input type="email" id="email" required><br>

<label>Số điện thoại:</label>
<input type="text" id="phone" required><br>

<label>Địa chỉ:</label>
<input type="text" id="address"><br>

<button onclick="updateUserInfo()">Lưu thay đổi</button>
<p id="status-message" style="font-weight: bold;"></p>

<script>
    window.onload = function () {
        const jwtToken = localStorage.getItem("jwt");
        if (!jwtToken) {
            alert("Bạn chưa đăng nhập hoặc token hết hạn!");
            return;
        }

        fetch("profile", {
            method: "POST",
            headers: {
                "Authorization": "getData " + jwtToken
            }
        })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error("Không thể lấy thông tin người dùng, mã lỗi: " + response.status);
            })
            .then(data => {
                document.getElementById("password").value = data.password || "";
                document.getElementById("full_name").value = data.full_name || "";
                document.getElementById("email").value = data.email || "";
                document.getElementById("phone").value = data.phone || "";
                document.getElementById("address").value = data.address || "";
            })
            .catch(error => {
                console.error("Lỗi khi lấy thông tin:", error);
                alert("Có lỗi khi tải thông tin tài khoản.");
            });
    };


    function updateUserInfo() {
        const jwtToken = localStorage.getItem("jwt");
        const password = document.getElementById("password").value.trim();
        const full_name = document.getElementById("full_name").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const address = document.getElementById("address").value.trim();



        fetch("profile", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "updateData " + jwtToken
            },
            body: JSON.stringify({
                password: password,
                full_name: full_name,
                email: email,
                phone: phone,
                address: address
            })
        })
            .then(response => {
                if (response.ok) {
                    alert("Cập nhật thành công!");
                    window.location.reload();
                } else {
                    throw new Error("Cập nhật thất bại, mã lỗi: " + response.status);
                }
            })
            .catch(error => {
                console.error("Lỗi khi cập nhật:", error);
                alert("Có lỗi xảy ra, vui lòng thử lại sau.");
            });
    }

</script>
</body>
</html>
