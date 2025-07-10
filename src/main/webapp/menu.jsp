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
    <script>    const CONTEXT_PATH = "${pageContext.request.contextPath}";
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

        function openUserModal() {
            const jwt = localStorage.getItem('jwt');
            if (isJwtValid(jwt)) {
                document.getElementById('loggedModal').style.display = 'block';
            } else {
                openLoginModal();
            }
        }

        function closeLoggedModal() {
            document.getElementById('loggedModal').style.display = 'none';
        }

        function updateCartCount() {
            const jwt = localStorage.getItem('jwt');
            if (isJwtValid(jwt)) {
                fetch('menu', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ action: 'getUserCart', jwt })
                })
                    .then(res => res.json())
                    .then(cartItems => {
                        const totalQuantity = cartItems.reduce((sum, item) => sum + item.quantity, 0);
                        document.getElementById('cart-count').textContent = totalQuantity; });
            } else {
                const cart = JSON.parse(sessionStorage.getItem('cart')) || [];
                document.getElementById('cart-count').textContent = cart.length;
            }
        }
        window.onload = function() {
            const jwt = localStorage.getItem('jwt');
            const loginBtn = document.querySelector('.login-btn');

            if (jwt && isJwtValid(jwt)) {
                if (loginBtn) {
                    loginBtn.innerHTML = '<i class="fa-solid fa-user" style="font-size: 15px"></i>';
                    loginBtn.onclick = openUserModal;
                }
                document.getElementById('loggedModal').style.display = 'none';
            } else {
                if (loginBtn) {
                    loginBtn.innerHTML = 'Login';
                    loginBtn.onclick = openLoginModal;
                }
            }
            document.getElementById("foodInput").addEventListener("keyup", function() {
                fetchResults(function(food) {
                    addToCart(food.id.toString(), food.name, food.img, food.price.toFixed(0).toString());
                });
            });
            filterMenu('starter');
            updateCartCount();
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
            const loggedModal = document.getElementById('loggedModal');
            if (event.target === loggedModal) {
                closeLoggedModal();
            }
        }
        function logout() {
            localStorage.removeItem('jwt');
            document.querySelector('.login-btn').innerHTML = 'Login';
            document.getElementById('loggedModal').style.display = 'none';
            updateCartCount();
            window.location.href = '/menu';
        }






    </script>
</head>
<body>
<header>
    <div class="logo">
        <img src="images/logo.png" alt="Mam Mam Logo">
    </div>
    <nav>
        <a href="index.jsp">Home</a>
        <a href="menu">Menu</a>
        <a href="#">Contact</a>
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
            <button type="submit">Submit</button>
        </form>
        <p class="register">
            Bạn chưa có tài khoản? <a href="register.jsp"><strong>Đăng ký ngay</strong></a>
        </p>
    </div>
</div>

<div id="loggedModal" class="modal" style="display: none;">    <div class="modal-content">
    <span class="close" onclick="closeLoggedModal()">&times;</span>
    <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
    <h2 class="modal-subtitle">TÀI KHOẢN CỦA BẠN</h2>
    <div class="user-options" style="justify-content: center; align-items: center;">
        <button class="option-btn" onclick="window.location.href='settings.jsp'" style="justify-content: center; align-items: center;">
            <i class="fa-solid fa-gear"></i>
            Cài đặt tài khoản
        </button>
        <button class="option-btn" onclick="window.location.href='history.jsp'" style="justify-content: center; align-items: center;">
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
<div id="sessionCartChoiceModal" class="modal" style="display:none;">
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
<div id="guestCheckoutModal" class="modal" style="display:none;">
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


<%--<!-- Chat Icon -->--%>
<%--<div id="chat-icon" onclick="toggleChatBox()">--%>
<%--    <i class="fa-solid fa-comments"></i>--%>
<%--</div>--%>

<!-- Chat Box -->
<%--<div id="chat-box">--%>
<%--    <div id="chat-header">--%>
<%--        Hỗ trợ trực tuyến--%>
<%--        <span id="close-chat" onclick="toggleChatBox()">&times;</span>--%>
<%--    </div>--%>
<%--    <div id="chat-messages"></div>--%>
<%--    <div style="display: flex; align-items: center;">--%>
<%--        <input id="chat-input" type="text" placeholder="Nhập tin nhắn..." onkeydown="if(event.key==='Enter') sendMessage()" />--%>
<%--        <button onclick="sendMessage()">Gửi</button>--%>
<%--    </div>--%>
<%--</div>--%>


<script>
    // function toggleChatBox() {
    //     const chatBox = document.getElementById('chat-box');
    //     const chatIcon = document.getElementById('chat-icon');
    //     if (chatBox.style.display === 'flex') {
    //         chatBox.style.display = 'none';
    //         chatIcon.style.display = 'flex';
    //     } else {
    //         chatBox.style.display = 'flex';
    //         chatIcon.style.display = 'none';
    //     }
    // }
    //
    // function sendMessage() {
    //     const input = document.getElementById('chat-input');
    //     const messages = document.getElementById('chat-messages');
    //     if (input.value.trim() !== '') {
    //         const msgDiv = document.createElement('div');
    //         msgDiv.textContent = 'Bạn: ' + input.value;
    //         messages.appendChild(msgDiv);
    //         messages.scrollTop = messages.scrollHeight;
    //         // TODO: Gửi message qua WebSocket tới backend nếu muốn realtime
    //         input.value = '';
    //     }
    // }

    function openSessionCartChoiceModal() {
        document.getElementById('sessionCartChoiceModal').style.display = 'block';
    }
    function closeSessionCartChoiceModal() {
        document.getElementById('sessionCartChoiceModal').style.display = 'none';
    }
    function proceedCheckoutWithSessionCart() {
        window.location.href = 'checkout?useSessionCart=true';
    }
    function clearSessionCartAndStay() {
        sessionStorage.removeItem('cart');
        closeSessionCartChoiceModal();
        if (typeof updateCartCount === 'function') updateCartCount();
    }

    function openGuestCheckoutModal() {
        document.getElementById('guestCheckoutModal').style.display = 'block';
    }
    function closeGuestCheckoutModal() {
        document.getElementById('guestCheckoutModal').style.display = 'none';
        resetGuestCheckoutModal();
    }
    function showLoginFormInModal() {
        const modalContent = document.getElementById('guestCheckoutModalContent');
        modalContent.innerHTML = `
        <span class="close" onclick="closeGuestCheckoutModal()">&times;</span>
        <img src="images/logo.png" alt="Mam Mam Logo" class="modal-logo">
        <h2>Đăng nhập để tiếp tục</h2>
        <form id="modalLoginForm" style="margin-top:15px;">
            <label>Username</label>
            <input type="text" name="username" placeholder="Số điện thoại/ Gmail" required>
            <label>Password</label>
            <input type="password" name="password" required>
            <button type="submit">Đăng nhập</button>
        </form>
        <p class="register">
            Bạn chưa có tài khoản? <a href="register.jsp"><strong>Đăng ký ngay</strong></a>
        </p>
    `;
        // Gắn sự kiện submit cho form đăng nhập trong modal
        document.getElementById('modalLoginForm').onsubmit = async function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            const response = await fetch('login', {
                method: 'POST',
                body: formData
            });
            const data = await response.json();
            if (data.success) {
                localStorage.setItem('jwt', data.token);
                closeGuestCheckoutModal();
                const sessionCart = JSON.parse(sessionStorage.getItem('cart') || '[]');
                if (sessionCart.length > 0) {
                    sessionStorage.setItem('showSessionCartChoice', '1');
                }
                window.location.href = 'menu';
            } else {
                alert('Login failed: ' + (data.errorMessage || 'Unknown error'));
            }
        };
    }
    // Reset lại modal về nội dung gốc khi đóng
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
    // Thanh toán với tư cách khách (không đăng nhập)
    function proceedCheckoutAsGuest() {
        window.location.href = 'checkout?guest=true';
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
                        showCartChoiceModal();
                    } else {
                        window.location.href = 'checkout';
                    }
                }
            }
        }
    });







</script>
<script src="js/cart.js"></script>
<script src="js/search.js"></script>

</body>
</html>