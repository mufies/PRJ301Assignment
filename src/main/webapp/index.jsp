<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Mam Mam Korean BBQ</title>
        <link rel ="stylesheet" href ="css/style.css">
        <link rel ="stylesheet" href ="css/login.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <header>
            <div class = "logo">
                <img src = "images/logo.png" alt="Mam Mam Logo">
            </div>
            <nav>
                <a href ="menu">Menu</a>
                <button class="login-btn" onclick="openLoginModal()">Login</button>
            </nav>
        </header>

        <section class ="highlight">
            <h1>Enjoy Delicious <br><span>The <span class="highlight_text">Korean Foods</span></span></h1>
            <p>We serve authentic Korean dishes, prepared with fresh ingredients by skilled chefs for a truly traditional taste.</p>
            <a href="menu" class="order-btn">Order Now</a>
        </section>

        <section class = "banner">
            <img src ="images/mammam.png" alt="Food Banner">
        </section>



        <section class = "about">
            <h2>About Us / 저희소개</h2>
            <div class = "about-content">
                <div class = "text">
                    <p>
                        Mam Mam – The No.1 Korean BBQ Restaurant invites you on a flavorful journey to Seoul’s most famous barbecue spots that have made Korean cuisine world-renowned.
                        Once you’ve tasted the grilled delights at Mam Mam, you’ll never forget the mouthwatering flavors of U.S. short ribs, chuck roll, and fresh beef ribs—perfectly marinated with signature Korean spices.   Alongside the BBQ, don’t miss out on must-try side dishes like bibimbap, cold noodles, kimchi stew, and various types of hot pot that will deepen your appreciation for authentic Korean cuisine.
                    </p>
                </div>
                <div class = "images">
                    <img src="images/about us.jpg" alt="About Us">
                </div>
            </div>
        </section>

        <section class = "best-seller">
            <h2>Best Seller</h2>
            <div class="products">
                <div class="product">
                    <img src="images/bachibomatong.jpg" alt="Ba chỉ bò Mỹ ướp mật ong">
                    <p class="product-name">Ba chỉ bò Mỹ ướp xốt mật ong 200g</p>
                    <p class="price">159.000</p>
                </div>
                <div class="product">
                    <img src="images/canhkimchi.jpg" alt="Canh kim chi">
                    <p class="product-name">Canh kim chi</p>
                    <p class="price">99.000</p>
                </div>
                <div class="product">
                    <img src="images/maheo.jpg" alt="Má heo Mỹ">
                    <p class="product-name">Má heo Mỹ tươi</p>
                    <p class="price">149.000</p>
                </div>
                <div class="product">
                    <img src="images/lauquandoi.jpg" alt="Lẩu quân đội">
                    <p class="product-name">Lẩu quân đội</p>
                    <p class="price">309.000</p>
                </div>
                <div class="product">
                    <img src="images/mienxao.jpg" alt="Miến xào thập cẩm">
                    <p class="product-name">Miến xào thập cẩm</p>
                    <p class="price">69.000</p>
                </div>
            </div>
            <div class="view-all">
                <a href="menu" class="order-btn">View All Menu</a>
            </div>

        </section>
        <section class="feedback">
            <h2>Feedback / 고객 피드백</h2>
            <div class="feedbacks">
                <div class="fb-card">
                    <p class="fb-text">Fantastic, wonderful, significant, magnificent, outstanding... class of flavors, day is a word class thưa quý vị !</p>
                    <p class="fb-name">Name: Vee Hung<br><span class="rating">Rating: 5 stars</span></p>
                </div>
                <div class="fb-card">
                    <p class="fb-text">Rất ngon rất mlemmmm</p>
                    <p class="fb-name">Name: Nguyễn Khắc An<br><span class="rating">Rating: 5 stars</span></p>
                </div>
                <div class="fb-card">
                    <p class="fb-text">Thịt nướng ở đây 100 điểm, mình sẽ quay lại đây nhiều lần nữa!</p>
                    <p class="fb-name">Name: KhungHắcLong<br><span class="rating">Rating: 5 stars</span></p>
                </div>
            </div>
        </section>

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
        <!-- Login Modal (ẩn ban đầu) -->
        <!-- LOGIN MODAL -->
        <div id="loginModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeLoginModal()">&times;</span>
                <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
                <h1 class="modal-title">MĂM MĂM</h1>
                <h2 class="modal-subtitle">VUI LÒNG ĐĂNG NHẬP</h2>

                <form action="login" method="post">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="Username" required>

                    <label>Password</label>
                    <input type="password" name="password" required>



                    <button type="submit">Đăng nhập</button>
                    <div class="forgot">
                        <button type="button" class="forgot-btn" onclick="closeLoginModal(); openForgotPassword()">Quên mật khẩu?</button>
                    </div>
                </form>

                <p class="register">
                    Bạn chưa có tài khoản? <a href="register"><strong>Đăng ký ngay</strong></a>
                </p>
            </div>
        </div>

        <!-- FORGOT PASSWORD MODAL -->
        <div id="forgotPasswordModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeForgotPassword()">&times;</span>
                <h2 class="modal-subtitle">KHÔI PHỤC MẬT KHẨU</h2>
                <form action="forgotpassword" method="post">
                    <label for="email">Nhập email của bạn:</label>
                    <input type="email" id="email" name="email" required>
                    <button type="submit">Gửi mã xác nhận</button>
                </form>
            </div>
        </div>

        <!-- ENTER CODE MODAL -->
        <div id="enterForgotPasswordModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeForgotPassword()">&times;</span>
                <h2 class="modal-subtitle">XÁC NHẬN MÃ & ĐẶT LẠI MẬT KHẨU</h2>
                <form action="ChangePassword" method="post">
                    <label for="code">Nhập mã xác nhận:</label>
                    <input type="text" id="code" name="code" required>

                    <label for="newPassword">Nhập mật khẩu mới:</label>
                    <input type="password" id="newPassword" name="new_password" required>

                    <label for="confirmPassword">Nhập lại mật khẩu mới:</label>
                    <input type="password" id="confirmPassword" name="confirm_password" required>

                    <button type="submit">Xác nhận</button>
                </form>
            </div>
        </div>

        <div id="loggedModal" class="modal">
            <div class="modal-content">
            <span class="close" onclick="closeLoggedModal()">&times;</span>
            <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
            <h2 class="modal-subtitle">TÀI KHOẢN CỦA BẠN</h2>
            <div class="user-options" style="justify-content: center; align-items: center;">
                <button class="option-btn" onclick="window.location.href='profile'" style="justify-content: center; align-items: center;">
                    <i class="fa-solid fa-gear"></i>
                    Cài đặt tài khoản
                </button>
                <button class="option-btn" onclick="window.location.href='history'" style="justify-content: center; align-items: center;">
                    <i class="fa-solid fa-clock-rotate-left"></i>
                    Lịch sử mua hàng
                </button>
                <button class="option-btn logout-btn" onclick="logout()" style="justify-content: center; align-items: center;">
                    <i class="fa-solid fa-right-from-bracket"></i>
                    Đăng xuất
                </button>
            </div>
        </div>
        </div>


<script src="js/loginUtils.js"></script>

    </body>
</html>