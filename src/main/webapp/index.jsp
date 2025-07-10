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
            <a href="#" class="order-btn">Order Now</a>
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
                <a href="#" class="order-btn">View All Menu</a>
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
    <div id="loginModal" class="modal" style="display: none;">
        <div class="modal-content">
            <span class="close" onclick="closeLoginModal()">&times;</span>
            <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
            <h1 class="modal-title">MĂM MĂM</h1>
            <h2 class="modal-subtitle">VUI LÒNG ĐĂNG NHẬP</h2>

            <form action="login" method="post">
                <label>Username</label>
                <input type="text" name="username" placeholder="Số điện thoại/ Gmail" required>

                <label>Password</label>
                <input type="password" name="password" required>

<%--                <div class="forgot">--%>
<%--                    <button type="button" class="forgot-btn" onclick="closeLoginModal();openForgotPassword()">Quên mật khẩu?</button>--%>
<%--                </div>--%>

                <button type="submit">Submit</button>
            </form>

            <p class="register">
                Bạn chưa có tài khoản? <a href="register.jsp"><strong>Đăng ký ngay</strong></a>
            </p>
        </div>
    </div>
        <div id="forgotPasswordModal" style="display: none;">
            <form action="forgotpassword" method="post">
                <label for="email">Nhập email của bạn:</label><br>
                <input type="email" id="email" name="email" required /><br><br>
                <button type="submit">Gửi mã xác nhận</button>
            </form>
            <button onclick="closeForgotPassword()">Đóng</button>
        </div>
        <div id="enterForgotPasswordModal" style="display: none;">
            <form action="enterforgotpassword" method="post">
                <label for="code">Nhập mã xác nhận:</label><br>
                <input type="text" id="code" name="code" required /><br><br>
                <label for="newPassword">Nhập mật khẩu mới:</label><br>
                <input type="password" id="newPassword" name="newPassword" required /><br><br>
                <button type="submit">Xác nhận</button>
            </form>
            <button onclick="closeForgotPassword()">Đóng</button>
        </div>

    <!-- JavaScript mở/đóng modal -->
    <script>
        function openLoginModal() {
            document.getElementById('loginModal').style.display = 'block';
        }

        function closeLoginModal() {
            document.getElementById('loginModal').style.display = 'none';
        }
        function openForgotPassword() {
            document.getElementById('forgotPasswordModal').style.display = 'block';
        }
        function closeForgotPassword() {
            document.getElementById('forgotPasswordModal').style.display = 'none';
        }



        window.onclick = function(event) {
            const modal = document.getElementById('loginModal');
            if (event.target === modal) {
                closeLoginModal();
            }
        }
        document.querySelector('#loginModal form').onsubmit = async function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            const response = await fetch('login', {
                method: 'POST',
                body: formData
            });
            const data = await response.json();
            if (data.success) {
                localStorage.setItem('jwt', data.token);
                if( data.isAdmin) {
                    window.location.href = 'ayxkix';
                }
                else if (data.isEmployee) {
                    window.location.href = 'employeeMsg';
                }
                else {
                    window.location.href = 'menu';
                }
            } else {
                alert('Login failed: ' + (data.errorMessage || 'Unknown error'));
            }
        };

        function isJwtValid(token) {
            if (!token) return false;
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                return !payload.exp || (Date.now() / 1000 < payload.exp);
            } catch (e) {
                return false;
            }
        }

        window.onload = function() {
            const jwt = localStorage.getItem('jwt');
            if (isJwtValid(jwt)) {
                const loginBtn = document.querySelector('.login-btn');
                if (loginBtn) {
                    loginBtn.innerHTML = '<i class="fa-solid fa-user" style="font-size: 15px"></i>';
                    }
            }

        };
    </script>


    </body>
</html>