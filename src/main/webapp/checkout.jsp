<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh Toán - Mam Mam</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/checkout.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

<!-- Header -->
<header>
    <div class="logo">
        <a href="menu"> <img src="images/logo.png" alt="Mam Mam Logo"> </a>
    </div>

</header>

<div class="checkout-container">
    <!-- Checkout content will be dynamically loaded here -->
    <div id="cartItems">
        <h2 class="cart-title">THANH TOÁN ĐƠN HÀNG</h2>
        <div class="loading-spinner">
            <i class="fas fa-spinner fa-spin"></i>
            <p>Đang tải giỏ hàng...</p>
        </div>
    </div>

    <div id="emptyCart" class="empty-cart" style="display: none">
        <div class="empty-cart-content">
            <div class="empty-text">
                <h1 class="empty-message">GIỎ HÀNG CỦA BẠN<br>ĐANG TRỐNG.<br>HÃY ĐẶT MÓN NGAY!</h1>
                <a class="order-btn" href="menu">ĐẶT HÀNG NGAY</a>
            </div>
            <div class="empty-image">
                <!-- Có thể chèn hình minh họa ở đây -->
            </div>
        </div>
    </div>

    <div id="orderDetails" class="order-details" style="display: none">
        <div class="cart-details">
            <h3>Các món đã chọn</h3>
            <div id="cartItemsList" class="cart-items-list">
                <!-- Cart items will be inserted here -->
            </div>
<%--            <div class="cart-total">--%>
<%--                <strong>Tổng cộng: <span id="cartTotal">0</span> đ</strong>--%>
<%--            </div>--%>
<%--            <button id="placeOrderBtn">Đặt hàng</button>--%>
        </div>

        <div class="checkout-form" style="display: none">
            <h3>Thông tin đặt hàng</h3>
            <form id="checkoutForm">
                <div class="form-group">
                    <label for="customerName">Họ và tên</label>
                    <input type="text" id="customerName" name="customerName" required>
                </div>
                <div class="form-group">
                    <label for="customerPhone">Số điện thoại</label>
                    <input type="tel"
                           id="customerPhone"
                           name="customerPhone"
                           placeholder="Nhập số điện thoại (VD: 0901234567)"
                           pattern="^(0[0-9]{9}|\+84[0-9]{9})$"
                           title="Số điện thoại phải bắt đầu bằng 0 hoặc +84 và có đúng 10 chữ số"
                           maxlength="12"
                           minlength="10"
                           oninput="this.value = this.value.replace(/[^0-9+]/g, '')"
                           required>
                    <span class="error-message" id="phoneError"></span>
                </div>

                <div class="form-group">
                    <label for="customerAddress">Địa chỉ giao hàng</label>
                    <textarea id="customerAddress" name="customerAddress" required></textarea>
                </div>
                <div class="form-group">
                    <label for="paymentMethod">Phương thức thanh toán</label>
                    <select id="paymentMethod" name="paymentMethod" required>
                        <option value="cod">Thanh toán khi nhận hàng (COD)</option>
                        <option value="bank">Chuyển khoản ngân hàng</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="orderNote">Ghi chú đơn hàng</label>
                    <textarea id="orderNote" name="orderNote"></textarea>
                </div>
            </form>
        </div>

        <div class="order-summary">
            <h3>Tổng đơn hàng</h3>
            <div class="summary-content">
                <p>Tổng số món: <span id="totalItems">0</span></p>
                <p>Giá trị đơn hàng: <span id="subtotal">0</span> VND</p>
                <p>Phí vận chuyển: <span id="shippingFee">15,000</span> VND</p>
                <p class="order-total">Tổng cộng: <span id="totalAmount">0</span> VND</p>
            </div>
            <button id="placeOrderBtn" class="place-order-btn">XÁC NHẬN ĐẶT HÀNG</button>
        </div>
    </div>

    <!-- Confirmation modal -->
<%--    <div id="confirmationModal" class="modal">--%>
<%--        <div class="modal-content">--%>
<%--            <span class="close" id="closeConfirmationModal">&times;</span>--%>
<%--            <h2>Xác nhận đơn hàng</h2>--%>
<%--            <p>Bạn có chắc chắn muốn đặt đơn hàng này?</p>--%>
<%--            <div class="modal-buttons">--%>
<%--                <button id="confirmOrderBtn">Xác nhận</button>--%>
<%--                <button id="cancelOrderBtn">Hủy bỏ</button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

    <!-- Success modal -->
<%--    <div id="successModal" class="modal">--%>
<%--        <div class="modal-content">--%>
<%--            <h2>Đặt hàng thành công!</h2>--%>
<%--            <p>Cảm ơn bạn đã đặt hàng tại Mam Mam. Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất.</p>--%>
<%--            <div class="modal-buttons">--%>
<%--                <button id="backToMenuBtn">Quay lại Menu</button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<!-- Footer -->
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

<!-- Modal login -->
<div id="loginModal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="close" onclick="closeLoginModal()">&times;</span>
        <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
        <h1 class="modal-title">MĂM MĂM</h1>
        <h2 class="modal-subtitle">VUI LÒNG ĐĂNG NHẬP</h2>
        <form id="loginForm">
            <label>Username</label>
            <input type="text" name="username" placeholder="Số điện thoại/ Gmail" required>
            <label>Password</label>
            <input type="password" name="password" required>
            <div class="forgot">
                <a href="#">Quên mật khẩu?</a>
            </div>
            <button type="submit">Submit</button>
        </form>
        <p class="register">
            Bạn chưa có tài khoản? <a href="register.jsp"><strong>Đăng ký ngay</strong></a>
        </p>
    </div>
</div>

<div id="successModal" class="modal" >
    <div class="modal-content">
        <span class="close" onclick="closeSuccessModal()">&times;</span>
        <h2>Đặt hàng thành công!</h2>
        <p>Mã vận đơn của bạn: <strong id="shippingCode"></strong></p>
        <h3>Danh sách món đã đặt:</h3>
        <ul id="orderedItemsList" style="text-align:left; margin: 0 auto 15px auto; max-width: 350px;"></ul>
        <button onclick="backToMenu()" class="order-btn" style="margin-top:10px;">Quay lại Menu</button>
    </div>
</div>

    <script>

</script>
<script src="js/checkout.js"></script>
<%--<script src="js/loginUtils.js"></script>--%>
</body>
</html>