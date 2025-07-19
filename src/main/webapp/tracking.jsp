<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Tra cứu vận đơn - Mam Mam</title>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/menu1.css">
  <link rel="stylesheet" href="css/checkout.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <style>
    html, body {
      height: 100%;
      margin: 0;
      padding: 0;
    }
    body {
      height: 100%;
      display: flex;
      flex-direction: column;
    }
    .main-wrapper {
      flex: 1 0 auto;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      min-height: 60vh;
    }
    .tracking-container {
      max-width: 480px;
      background: #fffbe7;
      border-radius: 16px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.07);
      padding: 40px 36px 40px 36px;
      min-height: 220px;
      text-align: center;
    }
    .tracking-input {
      width: 75%;
      padding: 12px 14px;
      border-radius: 8px;
      border: 1px solid #e2b9a1;
      font-size: 18px;
      margin-bottom: 18px;
      background: #fff;
    }
    .tracking-btn {
      background: #9B3F3F;
      color: #fff;
      border: none;
      padding: 12px 28px;
      font-size: 17px;
      border-radius: 8px;
      font-weight: bold;
      margin-left: 8px;
      cursor: pointer;
      transition: background 0.2s;
    }
    .tracking-btn:hover {
      background: #a33;
    }
    .tracking-status {
      color: #a33;
      margin-top: 16px;
      font-size: 16px;
    }
    footer {
      flex-shrink: 0;
    }
  </style>
</head>
<body>
<header>
  <div class="logo">
    <a href="home"> <img src="images/logo.png" alt="Mam Mam Logo"> </a>
  </div>
  <nav>
    <a href="index.jsp">Home</a>
    <a href="menu">Menu</a>
  </nav>
</header>

<div class="main-wrapper">
  <div class="tracking-container">
    <h2 style="color:#9B3F3F; font-weight:bold; margin-bottom:20px;">TRA CỨU VẬN ĐƠN</h2>
    <input type="text" id="trackingCodeInput" class="tracking-input" placeholder="Nhập mã vận đơn...">
    <button class="tracking-btn" onclick="trackOrder()">Tra cứu</button>
    <div id="trackingStatus" class="tracking-status"></div>
  </div>
</div>

<!-- Modal hiển thị thông tin đơn hàng -->
<div id="orderInfoModal" class="modal" >
  <div class="modal-content">
    <span class="close" onclick="closeOrderInfoModal()">&times;</span>
    <h2>Thông tin đơn hàng</h2>
    <p>Khách hàng: <span id="modalCustomer"></span></p>
    <p>Trạng thái: <span id="modalStatus"></span></p>
    <h3>Danh sách món:</h3>
    <ul id="modalItemsList" style="text-align:left; margin: 0 auto 15px auto; max-width: 350px;"></ul>
    <button onclick="closeOrderInfoModal()" class="order-btn" style="margin-top:10px;">Đóng</button>
  </div>
</div>


<footer>
  <div class="footer-content">
    <div class="contact-info">
      <h3>CONTACT INFORMATION</h3>
      <p>Mam Mam Korean Food<br>
        Address: FPT University, Hoa Hai Ward, Ngu Hanh Son District, Da Nang City<br>
        HOTLINE: 0123.456.789<br>
        Email: mammam.food@gmail.com
      </p>
    </div>
    <div class="logo-footer">
      <img src="images/logo.png" alt="Mam Mam Logo">
    </div>
  </div>
</footer>

<script src="js/tracking.js"></script>

</body>
</html>
