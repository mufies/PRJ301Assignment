<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menu - Mam Mam</title>
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/carticon.css">
    <link rel="stylesheet" href="css/search.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <script>
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
    </script>
    <script>
        function filterMenu(type) {
            const items = document.querySelectorAll('.product-item');
            items.forEach(item => {
                item.style.display = (type === 'all' || item.dataset.type === type) ? 'block' : 'none';
            });

            document.querySelectorAll('.category-list li').forEach(li => li.classList.remove('active'));
            document.getElementById(type).classList.add('active');
        }

        window.onload = function() {
            document.getElementById("foodInput").addEventListener("keyup", function() {
                fetchResults(function(food) {
                    addToCart(food.id.toString(), food.name, food.img, food.price.toFixed(0).toString());
                });
            });
            filterMenu('starter');
            updateCartCount();

            // Thêm interval để cập nhật cart count định kỳ (tùy chọn)
            setInterval(updateCartCount, 30000); // Cập nhật mỗi 30 giây
        };

        // Hàm xử lý sau khi đăng nhập thành công - SYNC TẤT CẢ
        function handleLoginSuccess(data) {
            localStorage.setItem('jwt', data.token);

            if(data.isAdmin) {
                window.location.href = 'ayxkix';
                return;
            }
            else if (data.isEmployee) {
                window.location.href = 'updateOrderStatus';
                return;
            }

            // Đối với user thông thường - luôn check sessionStorage
            const sessionCart = JSON.parse(sessionStorage.getItem('cart') || '[]');
            if (sessionCart.length > 0) {
                updateUIAfterLogin();
                openSessionCartChoiceModal();
            } else {
                window.location.reload();
            }
        }
    </script>
</head>
<body>
<header>
    <div class="logo">
        <a href="home"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
    <nav>
        <a href="home">Home</a>
        <a href="menu">Menu</a>
        <button class="login-btn" onclick="openLoginModal()">Login</button>
    </nav>
</header>

<!-- Thanh trên cùng: Home > Menu | Search -->
<div class="menu-top-bar">
    <h2>Home > Menu</h2>
    <div id="inputContainer">
        <input type="text" id="foodInput" class="search-box" placeholder="Search...">
        <div id="suggestionsPopup"></div>
    </div>
</div>

<!-- Nội dung chính -->
<div class="menu-page">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h3>Menu</h3>
        <ul class="category-list">
            <li id="starter" class="active" onclick="filterMenu('starter')">Khai vị & Ăn kèm</li>
            <li id="meat" onclick="filterMenu('meat')">Thịt</li>
            <li id="seafood" onclick="filterMenu('seafood')">Hải sản</li>
            <li id="rice" onclick="filterMenu('rice')">Cơm & Canh</li>
            <li id="hotpot" onclick="filterMenu('hotpot')">Lẩu</li>
        </ul>
    </aside>

    <!-- Danh sách món -->
    <main class="menu-content">
        <div class="product-list">
            <c:forEach var="product" items="${products}">
                <div class="product-item" data-type="${product.type}">
                    <img src="${product.image}" alt="${product.productName}">
                    <p class="product-name">${product.productName}</p>
                    <p class="product-price">${product.price}</p>
                    <button class="add-to-cart-btn" onclick="addToCart('${product.productId}', '${product.productName}', '${product.image}', '${product.price}');">Add to cart</button>
                </div>
            </c:forEach>
        </div>
    </main>
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

<div id="forgotPasswordModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeForgotPassword()">&times;</span>
        <h2 class="modal-subtitle">KHÔI PHỤC MẬT KHẨU</h2>
        <form action="forgotpassword" method="post">
            <label>Nhập email của bạn:</label>
            <input type="email" id="email" name="email" required>
            <button type="submit">Gửi mã xác nhận</button>
        </form>
    </div>
</div>

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

<div class="cart-icon">
    <i class="fa-solid fa-cart-shopping"></i>
    <span class="cart-count" id="cart-count">0</span>
</div>

<div class="cart-popup">
    <ul class="cart-items logged-in"></ul>
    <ul class="cart-items unlogged-in"></ul>
    <div class="cart-total">
        <span>Total: </span>
        <span class="cart-total-price"></span>
    </div>
    <button class="checkout-btn" type="button" id="checkout-btn">Checkout</button>
</div>

<!-- Modal hỏi dùng giỏ hàng session sau khi login -->
<div id="sessionCartChoiceModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeSessionCartChoiceModal()">&times;</span>
        <h2>Bạn có giỏ hàng chưa thanh toán</h2>
        <p>Bạn có muốn thanh toán các món đã bỏ vào giỏ hàng trước khi đăng nhập không?</p>
        <div style="margin-top: 18px;">
            <button onclick="proceedCheckoutWithSessionCart()">Có, thanh toán ngay</button>
            <button onclick="clearSessionCartAndStay()">Không, xóa giỏ hàng này</button>
        </div>
    </div>
</div>

<!-- Modal hỏi đăng nhập khi checkout -->
<div id="guestCheckoutModal" class="modal">
    <div class="modal-content" id="guestCheckoutModalContent">
        <span class="close" onclick="closeGuestCheckoutModal()">&times;</span>
        <h2>Bạn chưa đăng nhập</h2>
        <p>Bạn có muốn đăng nhập để lưu đơn hàng vào tài khoản?</p>
        <div style="margin-top: 18px;">
            <button onclick="showLoginFormInModal()">Đăng nhập</button>
            <button onclick="proceedCheckoutAsGuest()">Tiếp tục thanh toán không cần đăng nhập</button>
        </div>
    </div>
</div>

<script>
    function openSessionCartChoiceModal() {
        document.getElementById('sessionCartChoiceModal').classList.add('active');
    }
    function closeSessionCartChoiceModal() {
        document.getElementById('sessionCartChoiceModal').classList.remove('active');
    }
    function proceedCheckoutWithSessionCart() {
        closeSessionCartChoiceModal();
        window.location.href = 'checkout?useSessionCart=true';
    }
    function clearSessionCartAndStay() {
        sessionStorage.removeItem('cart');
        closeSessionCartChoiceModal();

        // Cập nhật UI sau khi xóa giỏ hàng
        updateUIAfterLogin();
        if (typeof updateCartCount === 'function') {
            updateCartCount();
        }
    }

    function openGuestCheckoutModal() {
        document.getElementById('guestCheckoutModal').classList.add('active');
    }
    function closeGuestCheckoutModal() {
        document.getElementById('guestCheckoutModal').classList.remove('active');
        resetGuestCheckoutModal();
    }
    function showLoginFormInModal() {
        const modalContent = document.getElementById('guestCheckoutModalContent');
        modalContent.innerHTML = `
        <span class="close" onclick="closeGuestCheckoutModal()">&times;</span>
        <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
        <h1 class="modal-title">MĂM MĂM</h1>
        <h2 class="modal-subtitle">VUI LÒNG ĐĂNG NHẬP</h2>
        <form id="modalLoginForm" style="margin-top:15px;">
            <label>Username</label>
            <input type="text" name="username" placeholder="Username" required>
            <label>Password</label>
            <input type="password" name="password" required>

            <button type="submit">Đăng nhập</button>
            <div class="forgot">
                <button type="button" class="forgot-btn" onclick="closeGuestCheckoutModal(); openForgotPassword()">Quên mật khẩu?</button>
            </div>
        </form>
        <p class="register">
            Bạn chưa có tài khoản? <a href="register"><strong>Đăng ký ngay</strong></a>
        </p>
    `;

        // Gắn sự kiện submit cho form đăng nhập trong modal
        document.getElementById('modalLoginForm').onsubmit = async function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            try {
                const response = await fetch('login', {
                    method: 'POST',
                    body: formData
                });
                const data = await response.json();
                if (data.success) {
                    closeGuestCheckoutModal();
                    handleLoginSuccess(data);
                } else {
                    alert('Login failed: ' + (data.errorMessage || 'Unknown error'));
                }
            } catch (error) {
                console.error('Login error:', error);
                alert('Đã xảy ra lỗi khi đăng nhập. Vui lòng thử lại.');
            }
        };
    }

    function resetGuestCheckoutModal() {
        const modalContent = document.getElementById('guestCheckoutModalContent');
        modalContent.innerHTML = `
            <span class="close" onclick="closeGuestCheckoutModal()">&times;</span>
            <h2>Bạn chưa đăng nhập</h2>
            <p>Bạn có muốn đăng nhập để lưu đơn hàng vào tài khoản?</p>
            <div style="margin-top: 18px;">
              <button onclick="showLoginFormInModal()">Đăng nhập</button>
              <button onclick="proceedCheckoutAsGuest()">Tiếp tục thanh toán không cần đăng nhập</button>
            </div>
        `;
    }
    function proceedCheckoutAsGuest() {
        window.location.href = 'checkout?guest=true';
    }

    function updateUIAfterLogin() {
        const loginBtn = document.querySelector('.login-btn');
        if (loginBtn) {
            loginBtn.innerHTML = '<i class="fa-solid fa-user"></i>';
            loginBtn.onclick = function() { openLoggedModal(); };

            loginBtn.classList.remove('login-btn');
        }

        if (typeof updateCartCount === 'function') {
            updateCartCount();
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        if (sessionStorage.getItem('showSessionCartChoice') === '1') {
            sessionStorage.removeItem('showSessionCartChoice');
            openSessionCartChoiceModal();
        }

        const checkoutBtn = document.getElementById('checkout-btn');
        if (checkoutBtn) {
            checkoutBtn.onclick = function(e) {
                e.preventDefault();
                const jwt = localStorage.getItem('jwt');
                if (!isJwtValid(jwt)) {
                    openGuestCheckoutModal();
                } else {
                    const sessionCart = JSON.parse(sessionStorage.getItem('cart') || '[]');
                    if (sessionCart.length > 0) {
                        // Sử dụng hàm đã được định nghĩa
                        showCartChoiceModal();
                    } else {
                        window.location.href = 'checkout';
                    }
                }
            }
        }
    });

    // Cập nhật form login chính - SỬ DỤNG CHUNG handleLoginSuccess
    document.querySelector('#loginModal form').onsubmit = async function(e) {
        e.preventDefault();
        const formData = new FormData(this);

        try {
            const response = await fetch('login', {
                method: 'POST',
                body: formData
            });
            const data = await response.json();

            if (data.success) {
                closeLoginModal();
                handleLoginSuccess(data);
            } else {
                alert('Login failed: ' + (data.errorMessage || 'Unknown error'));
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('Đã xảy ra lỗi khi đăng nhập. Vui lòng thử lại.');
        }
    };
    function showCartChoiceModal() {
        const modal = document.createElement('div');
        modal.id = 'cartChoiceModal';
        modal.className = 'modal';
        modal.innerHTML = `
        <div class="modal-content">
            <span class="close" onclick="closeCartChoiceModal()">&times;</span>
            <h2>Bạn có giỏ hàng chưa thanh toán</h2>
            <p>Bạn có muốn thanh toán các món đã bỏ vào giỏ hàng trước khi đăng nhập không?</p>
            <div style="margin-top: 18px;">
                <button onclick="proceedCheckoutWithSessionCart()">Có, thanh toán ngay</button>
                <button onclick="clearSessionCartAndStay()">Không, xóa giỏ hàng này</button>
            </div>
        </div>
    `;
        document.body.appendChild(modal);
        modal.classList.add('active');
    }

    function closeCartChoiceModal() {
        const modal = document.getElementById('cartChoiceModal');
        if (modal) {
            modal.classList.remove('active');
            setTimeout(() => {
                document.body.removeChild(modal);
            }, 300);
        }
    }
</script>
<script src="js/cart.js"></script>
<script src="js/search.js"></script>
<script src="js/loginUtils.js"></script>

</body>
</html>
