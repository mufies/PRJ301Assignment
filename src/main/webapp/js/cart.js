document.addEventListener('DOMContentLoaded', function() {
    var cartIcon = document.querySelector('.cart-icon');
    var cartPopup = document.querySelector('.cart-popup');

    cartIcon.addEventListener('click', async function (e) {
        await updateCartPopup();
        e.stopPropagation();
    });

    document.addEventListener('click', function(e) {
        if (cartPopup.classList.contains('active') && !cartPopup.contains(e.target)) {
            cartPopup.classList.remove('active');
        }
    });
});



async function updateCartPopup() {
    const cartPopup = document.querySelector('.cart-popup');
    const jwt = localStorage.getItem('jwt');

    // Hiển thị popup ngay lập tức
    if (!cartPopup.classList.contains('active')) {
        cartPopup.classList.add('active');
    }

    fadeOutCartItems();

    setTimeout(async () => {
        if (isJwtValid(jwt)) {
            await updateLoggedInCart();
        } else {
            updateUnloggedInCart();
        }
    }, 200);
}

function fadeOutCartItems() {
    const allItems = document.querySelectorAll('.cart-items li');
    allItems.forEach(item => {
        item.style.opacity = '0';
        item.style.transform = 'translateX(-20px)';
    });
}

function fadeInCartItems(container) {
    const items = container.querySelectorAll('li');
    items.forEach((item, index) => {
        item.style.opacity = '0';
        item.style.transform = 'translateX(-20px)';
        setTimeout(() => {
            item.style.opacity = '1';
            item.style.transform = 'translateX(0)';
        }, index * 80);
    });
}

async function updateLoggedInCart() {
    const cartPopup = document.querySelector('.cart-popup');
    const jwt = localStorage.getItem('jwt');

    try {
        const response = await fetch('menu', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'getUserCart', jwt })
        });

        if (response.ok) {
            const cartItems = await response.json();
            const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
            const unloggedInList = cartPopup.querySelector('.cart-items.unlogged-in');

            loggedInList.innerHTML = '';
            let total = 0;

            cartItems.forEach(item => {
                const productName = item.productName || item.product_name || '';
                const price = parseFloat(item.price || 0);
                const productId = item.productId || item.product_id || '';
                const quantity = item.quantity || 1;
                total += price * quantity;

                const li = document.createElement('li');
                li.innerHTML = `
                    <div class="cart-item-info">
                        <span class="cart-item-name">${productName}</span>
                        <div class="cart-item-details">
                            <span class="cart-item-qty">x${quantity}</span>
                            <span class="cart-item-price">${price.toFixed(0)}đ</span>
                        </div>
                    </div>
                    <button class="remove-btn" onclick="removeFromCartServer('${productId}')">
                        <i class="fas fa-times"></i>
                    </button>
                `;
                loggedInList.appendChild(li);
            });

            cartPopup.querySelector('.cart-total-price').textContent = total.toFixed(0) + 'đ';
            unloggedInList.style.display = 'none';
            loggedInList.style.display = 'block';

            fadeInCartItems(loggedInList);

        } else {
            const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
            loggedInList.innerHTML = '<li class="error-message">Lỗi lấy giỏ hàng!</li>';
            loggedInList.style.display = 'block';
        }
    } catch (err) {
        const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
        loggedInList.innerHTML = '<li class="error-message">Lỗi kết nối server!</li>';
        loggedInList.style.display = 'block';
    }
}

function updateUnloggedInCart() {
    const cartPopup = document.querySelector('.cart-popup');
    const cartItems = JSON.parse(sessionStorage.getItem('cart')) || [];
    const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
    const unloggedInList = cartPopup.querySelector('.cart-items.unlogged-in');

    // Merge items by productId
    const mergedItems = {};
    cartItems.forEach(item => {
        const key = item.productId;
        if (!mergedItems[key]) {
            mergedItems[key] = { ...item, quantity: 1 };
        } else {
            mergedItems[key].quantity += 1;
        }
    });

    unloggedInList.innerHTML = '';
    let total = 0;

    Object.values(mergedItems).forEach(item => {
        const price = parseFloat(item.price || 0);
        const name = item.productName || "Unknown Product";
        const itemTotal = price * item.quantity;
        total += itemTotal;

        const li = document.createElement('li');
        li.innerHTML = `
            <div class="cart-item-info">
                <span class="cart-item-name">${name}</span>
                <div class="cart-item-details">
                    <span class="cart-item-qty">x${item.quantity}</span>
                    <span class="cart-item-price">${itemTotal.toFixed(0)}đ</span>
                </div>
            </div>
            <button class="remove-btn" onclick="removeFromCartUnlogged('${item.productId}')">
                <i class="fas fa-times"></i>
            </button>
        `;
        unloggedInList.appendChild(li);
    });

    cartPopup.querySelector('.cart-total-price').textContent = total.toFixed(0) + 'đ';
    loggedInList.style.display = 'none';
    unloggedInList.style.display = 'block';

    // Fade in items
    fadeInCartItems(unloggedInList);
}



function animateCartCount(element, newCount) {
    element.style.transform = 'scale(1.3)';
    element.style.backgroundColor = '#9B3F3F';
    element.style.transition = 'all 0.2s ease';

    setTimeout(() => {
        element.textContent = newCount;
        element.style.transform = 'scale(1)';
        element.style.backgroundColor = '#9B3F3F';
    }, 150);
}

function updateCartCount() {
    const jwt = localStorage.getItem('jwt');
    const cartCountElement = document.getElementById('cart-count');

    if (isJwtValid(jwt)) {
        fetch('menu', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'getUserCart', jwt })
        })
            .then(res => res.json())
            .then(cartItems => {
                const totalQuantity = cartItems.reduce((sum, item) => sum + item.quantity, 0);
                animateCartCount(cartCountElement, totalQuantity);
            });
    } else {
        const cart = JSON.parse(sessionStorage.getItem('cart')) || [];
        animateCartCount(cartCountElement, cart.length);
    }
}

function addToCart(productId, productName, image, price) {
    const jwt = localStorage.getItem('jwt');

    if (isJwtValid(jwt)) {
        fetch('menu', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'addToCart', productId, productName, image, price, jwt })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    updateCartCount();
                    // Cập nhật popup nếu đang mở
                    if (document.querySelector('.cart-popup').classList.contains('active')) {
                        updateCartPopup();
                    }
                } else {
                    showNotification('Không thể thêm sản phẩm vào giỏ hàng', 'error');
                }
            })
            .catch(error => {
                showNotification('Lỗi kết nối server', 'error');
            });
    } else {
        let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
        cart.push({ productId, productName, image, price });
        sessionStorage.setItem('cart', JSON.stringify(cart));
        updateCartCount();
        if (document.querySelector('.cart-popup').classList.contains('active')) {
            updateCartPopup();
        }
    }
}

function removeFromCartUnlogged(productId) {
    let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const itemToRemove = cart.find(item => item.productId === productId);
    const itemCount = cart.filter(item => item.productId === productId).length;

    if (itemCount > 1) {
        const index = cart.findIndex(item => item.productId === productId);
        if (index !== -1) {
            cart.splice(index, 1);
        }
    } else {
        cart = cart.filter(item => item.productId !== productId);
    }

    sessionStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();

    updateCartPopup();
}

function removeFromCart(productId) {
    removeFromCartUnlogged(productId);
}

function removeFromCartServer(productId) {
    const jwt = localStorage.getItem('jwt');

    if (isJwtValid(jwt)) {
        fetch('menu', {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'removeFromCart', productId, jwt })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    updateCartCount();
                    updateCartPopup();
                } else {
                    console.log("Không thể xóa sản phẩm khỏi giỏ hàng");
                }
            })
            .catch(error => {
                console.error('Error removing item from cart:', error);
            });
    } else {
        removeFromCartUnlogged(productId);
    }
}

// function updateUIAfterLogin() {
//     const loginBtn = document.querySelector('.login-btn');
//     if (loginBtn) {
//         loginBtn.innerHTML = '<i class="fa-solid fa-user"></i>';
//         loginBtn.onclick = function() { openLoggedModal(); };
//
//         loginBtn.classList.remove('login-btn');
//     }
//
//     // Cập nhật cart count nếu có
//     if (typeof updateCartCount === 'function') {
//         updateCartCount();
//     }
// }
