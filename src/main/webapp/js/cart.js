document.addEventListener('DOMContentLoaded', function() {
    var cartIcon = document.querySelector('.cart-icon');
    var cartPopup = document.querySelector('.cart-popup');

    if (cartIcon) {  // ✅ Kiểm tra element tồn tại
        cartIcon.addEventListener('click', async function (e) {
            e.stopPropagation();
            await updateCartPopup();
        });
    }

    if (cartPopup) {  // ✅ Kiểm tra element tồn tại
        document.addEventListener('click', function(e) {
            if (cartPopup.classList.contains('active') && !cartPopup.contains(e.target) && !cartIcon.contains(e.target)) {
                cartPopup.classList.remove('active');
            }
        });
    }
});

async function updateCartPopup() {
    const cartPopup = document.querySelector('.cart-popup');
    if (!cartPopup) return; // ✅ Guard clause

    const jwt = localStorage.getItem('jwt');

    // Hiển thị popup ngay lập tức
    if (!cartPopup.classList.contains('active')) {
        cartPopup.classList.add('active');
    }

    fadeOutCartItems();

    // ✅ Giảm timeout và sử dụng try-catch
    try {
        await new Promise(resolve => setTimeout(resolve, 200));

        if (await isJwtValid(jwt)) {
            await updateLoggedInCart();
        } else {
            updateUnloggedInCart();
        }
    } catch (error) {
        console.error('Error updating cart popup:', error);
    }
}

function fadeOutCartItems() {
    const allItems = document.querySelectorAll('.cart-items li');
    allItems.forEach(item => {
        item.style.transition = 'opacity 0.2s ease, transform 0.2s ease'; // ✅ Thêm transition
        item.style.opacity = '0';
        item.style.transform = 'translateX(-20px)';
    });
}

function fadeInCartItems(container) {
    if (!container) return; // ✅ Guard clause

    const items = container.querySelectorAll('li');
    items.forEach((item, index) => {
        item.style.transition = 'opacity 0.3s ease, transform 0.3s ease'; // ✅ Thêm transition
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
    if (!cartPopup) return; // ✅ Guard clause

    const jwt = localStorage.getItem('jwt');

    try {
        const response = await fetch('menu', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'getUserCart', jwt })
        });

        const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
        const unloggedInList = cartPopup.querySelector('.cart-items.unlogged-in');
        const totalPriceElement = cartPopup.querySelector('.cart-total-price');

        if (!loggedInList || !unloggedInList || !totalPriceElement) {
            console.error('Cart elements not found');
            return;
        }

        if (response.ok) {
            const cartItems = await response.json();
            loggedInList.innerHTML = '';
            let total = 0;

            if (cartItems.length === 0) {
                loggedInList.innerHTML = '<li class="empty-cart">Giỏ hàng trống</li>';
            } else {
                cartItems.forEach(item => {
                    const productName = item.productName || item.product_name || 'Unknown Product';
                    const price = parseFloat(item.price || 0);
                    const productId = item.productId || item.product_id || '';
                    const quantity = parseInt(item.quantity || 1);
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
                        <button class="remove-btn" onclick="removeFromCartServer('${productId}')" type="button">
                            <i class="fas fa-times"></i>
                        </button>
                    `;
                    loggedInList.appendChild(li);
                });
            }

            totalPriceElement.textContent = total.toFixed(0) + 'đ';
            unloggedInList.style.display = 'none';
            loggedInList.style.display = 'block';

            fadeInCartItems(loggedInList);

        } else {
            loggedInList.innerHTML = '<li class="error-message">Lỗi lấy giỏ hàng!</li>';
            loggedInList.style.display = 'block';
            unloggedInList.style.display = 'none';
        }
    } catch (err) {
        console.error('Error fetching cart:', err);
        const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
        if (loggedInList) {
            loggedInList.innerHTML = '<li class="error-message">Lỗi kết nối server!</li>';
            loggedInList.style.display = 'block';
        }
    }
}

function updateUnloggedInCart() {
    const cartPopup = document.querySelector('.cart-popup');
    if (!cartPopup) return; // ✅ Guard clause

    const cartItems = JSON.parse(sessionStorage.getItem('cart')) || [];
    const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
    const unloggedInList = cartPopup.querySelector('.cart-items.unlogged-in');
    const totalPriceElement = cartPopup.querySelector('.cart-total-price');

    if (!loggedInList || !unloggedInList || !totalPriceElement) {
        console.error('Cart elements not found');
        return;
    }

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

    const mergedItemsArray = Object.values(mergedItems);

    if (mergedItemsArray.length === 0) {
        unloggedInList.innerHTML = '<li class="empty-cart">Giỏ hàng trống</li>';
    } else {
        mergedItemsArray.forEach(item => {
            const price = parseFloat(item.price || 0);
            const name = item.productName || "Unknown Product";
            const quantity = parseInt(item.quantity || 1);
            const itemTotal = price * quantity;
            total += itemTotal;

            const li = document.createElement('li');
            li.innerHTML = `
                <div class="cart-item-info">
                    <span class="cart-item-name">${name}</span>
                    <div class="cart-item-details">
                        <span class="cart-item-qty">x${quantity}</span>
                        <span class="cart-item-price">${itemTotal.toFixed(0)}đ</span>
                    </div>
                </div>
                <button class="remove-btn" onclick="removeFromCartUnlogged('${item.productId}')" type="button">
                    <i class="fas fa-times"></i>
                </button>
            `;
            unloggedInList.appendChild(li);
        });
    }

    totalPriceElement.textContent = total.toFixed(0) + 'đ';
    loggedInList.style.display = 'none';
    unloggedInList.style.display = 'block';

    fadeInCartItems(unloggedInList);
}

function animateCartCount(element, newCount) {
    if (!element) return; // ✅ Guard clause

    element.style.transform = 'scale(1.3)';
    element.style.backgroundColor = '#9B3F3F';
    element.style.transition = 'all 0.2s ease';

    setTimeout(() => {
        element.textContent = newCount;
        element.style.transform = 'scale(1)';
        element.style.backgroundColor = '#9B3F3F';
    }, 150);
}

async function updateCartCount() {
    const jwt = localStorage.getItem('jwt');
    const cartCountElement = document.getElementById('cart-count');

    if (!cartCountElement) return; // ✅ Guard clause

    try {
        if (await isJwtValid(jwt)) {
            const response = await fetch('menu', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({action: 'getUserCart', jwt})
            });

            if (response.ok) {
                const cartItems = await response.json();
                const totalQuantity = cartItems.reduce((sum, item) => sum + (parseInt(item.quantity) || 1), 0);
                animateCartCount(cartCountElement, totalQuantity);
            } else {
                console.error('Failed to fetch cart items');
            }
        } else {
            const cart = JSON.parse(sessionStorage.getItem('cart')) || [];
            animateCartCount(cartCountElement, cart.length);
        }
    } catch (error) {
        console.error('Error updating cart count:', error);
    }
}

async function addToCart(productId, productName, image, price) {
    const jwt = localStorage.getItem('jwt');

    try {
        if (await isJwtValid(jwt)) {
            const response = await fetch('menu', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({action: 'addToCart', productId, productName, image, price, jwt})
            });

            const data = await response.json();

            if (data.success) {
                await updateCartCount();
                // Cập nhật popup nếu đang mở
                if (document.querySelector('.cart-popup')?.classList.contains('active')) {
                    await updateCartPopup();
                }
                if (typeof showNotification === 'function') {
                    showNotification('Đã thêm vào giỏ hàng', 'success');
                }
            } else {
                if (typeof showNotification === 'function') {
                    showNotification('Không thể thêm sản phẩm vào giỏ hàng', 'error');
                }
            }
        } else {
            let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
            cart.push({productId, productName, image, price});
            sessionStorage.setItem('cart', JSON.stringify(cart));
            await updateCartCount();
            if (document.querySelector('.cart-popup')?.classList.contains('active')) {
                updateCartPopup();
            }
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        if (typeof showNotification === 'function') {
            showNotification('Lỗi kết nối server', 'error');
        }
    }
}

async function removeFromCartUnlogged(productId) {
    let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
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
    await updateCartCount();
    await updateCartPopup(); // ✅ Thêm await
}

async function removeFromCartServer(productId) {
    const jwt = localStorage.getItem('jwt');

    try {
        if (await isJwtValid(jwt)) {
            const response = await fetch('menu', {
                method: 'DELETE',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({action: 'removeFromCart', productId, jwt})
            });

            const data = await response.json();

            if (data.success) {
                await updateCartCount();
                await updateCartPopup();
            } else {
                console.log("Không thể xóa sản phẩm khỏi giỏ hàng");
            }
        } else {
            await removeFromCartUnlogged(productId);
        }
    } catch (error) {
        console.error('Error removing item from cart:', error);
    }
}

// ✅ Remove unused function
// function removeFromCart(productId) {
//     removeFromCartUnlogged(productId);
// }
