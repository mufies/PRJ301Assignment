document.addEventListener('DOMContentLoaded', function() {
    var cartIcon = document.querySelector('.cart-icon');
    var cartPopup = document.querySelector('.cart-popup');

    cartIcon.addEventListener('click', async function (e) {
        const jwt = localStorage.getItem('jwt');
        if (isJwtValid(jwt)) {
            try {
                const response = await fetch('menu', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ action: 'getUserCart', jwt })
                });
                if (response.ok) {
                    const cartItems = await response.json();
                    const loggedInList = cartPopup.querySelector('.cart-items.logged-in');
                    loggedInList.innerHTML = '';
                    let total = 0;
                    cartItems.forEach(item => {
                        const productName = item.productName || item.product_name || '';
                        const price = parseFloat(item.price || item.price || 0);
                        const productId = item.productId || item.product_id || '';
                        const quantity = item.quantity || 1;
                        total += price * quantity;
                        const li = document.createElement('li');
                        li.innerHTML = `
                            <span class="cart-item-name">${productName}</span>
                            <span class="cart-item-price">${price}</span>
                            <span class="cart-item-qty">x${quantity}</span>
                            <button class="remove-btn" onclick="removeFromCartServer('${productId}')">X</button>
                        `;
                        loggedInList.appendChild(li);
                    });
                    cartPopup.querySelector('.cart-total-price').textContent = total;
                    cartPopup.querySelector('.cart-items.unlogged-in').style.display = 'none';
                    loggedInList.style.display = '';
                } else {
                    cartPopup.querySelector('.cart-items.logged-in').innerHTML = '<li>Lỗi lấy giỏ hàng!</li>';
                }
            } catch (err) {
                cartPopup.querySelector('.cart-items.logged-in').innerHTML = '<li>Lỗi kết nối server!</li>';
            }
            cartPopup.classList.add('active');
        } else {
            const cartItems = JSON.parse(sessionStorage.getItem('cart')) || [];
            const unloggedInList = cartPopup.querySelector('.cart-items.unlogged-in');
            unloggedInList.innerHTML = '';

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

            // Optional: Debug log
            // Object.values(mergedItems).forEach(item => {
            //     console.log(item.productName, item.quantity);
            // });

            let total = 0;
            Object.values(mergedItems).forEach(item => {
                const price = parseFloat(item.price || 0);
                const name = item.productName || "Unknown Product";
                const itemTotal = price * item.quantity;
                total += itemTotal;
                console.log(item.productName);
                const li = document.createElement('li');
                li.innerHTML = `
            <span class="cart-item-name">${name}</span>
            <span class="cart-item-quantity">x${item.quantity}</span>
            <span class="cart-item-price">${itemTotal.toFixed(0)}</span>
            <button class="remove-btn" onclick="removeFromCart('${item.productId}')">X</button>
        `;
                unloggedInList.appendChild(li);
            });

            cartPopup.querySelector('.cart-total-price').textContent = total.toFixed(0);
            cartPopup.querySelector('.cart-items.logged-in').style.display = 'none';
            unloggedInList.style.display = '';
            cartPopup.classList.add('active');
        }
        e.stopPropagation();
    });

    document.addEventListener('click', function(e) {
        if (cartPopup.classList.contains('active') && !cartPopup.contains(e.target)) {
            cartPopup.classList.remove('active');
        }
    });
});


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
        window.location.href = 'menu';
    } else {
        alert('Login failed: ' + (data.errorMessage || 'Unknown error'));
    }
};

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
                } else {
                    alert('Failed to add item to cart: ' + data.errorMessage);
                }
            });
    } else {
        let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
        cart.push({ productId, productName, image, price });
        sessionStorage.setItem('cart', JSON.stringify(cart));
        updateCartCount();
    }
}

function removeFromCart(productId) {
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
    updateCartCount();
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
                } else {
                    alert('Failed to remove item from cart: ' + data.errorMessage);
                }
            });
    } else {
        let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
        cart = cart.filter(item => item.productId !== productId);
        sessionStorage.setItem('cart', JSON.stringify(cart));
        updateCartCount();
    }
}
