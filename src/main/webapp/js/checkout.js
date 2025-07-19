document.addEventListener('DOMContentLoaded', function () {
    const jwt = localStorage.getItem('jwt');
    const params = new URLSearchParams(window.location.search);

    if (params.get('useSessionCart') === 'true') {

        checkoutWithCartFromSessionStorage('user');

    } else if (params.get('guest') === 'true') {
        checkoutWithCartFromSessionStorage('guest');
    } else if (isJwtValid(jwt) && !params.get('useSessionCart') && !params.get('guest')) {
        checkoutWithCartFromDB(jwt);
    } else {
        checkoutWithCartFromSessionStorage('guest');
    }

    document.getElementById('placeOrderBtn').addEventListener('click', placeOrder);
});

function isJwtValid(token) {
    if (!token) return false;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return !payload.exp || (Date.now() / 1000 < payload.exp);
    } catch (e) {
        return false;
    }
}

async function checkoutWithCartFromDB(jwt) {
    const response = await fetch('menu', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({action: 'getUserCart', jwt})
    });
    if (response.ok) {
        const cartItems = await response.json();
        console.log(cartItems);
        renderCartItems(cartItems);
    }
    else {
        const errorData = await response.json();
        alert('Failed to retrieve cart: ' + errorData.errorMessage);
    }
}

async function checkoutWithCartFromSessionStorage(status) {
    let cartItems = JSON.parse(sessionStorage.getItem('cart')) || [];
    if (cartItems.length === 0) {
        alert('Your cart is empty. Please add items to your cart before checking out.');
        return;
    }

    const mergedItems = {};
    cartItems.forEach(item => {
        const key = item.productId;
        if (!mergedItems[key]) {
            mergedItems[key] = { ...item, quantity: 1 };
        } else {
            mergedItems[key].quantity += 1;
        }
    });

    const itemsToCheckout = Object.values(mergedItems).map(item => ({
        productId: item.productId,
        productName: item.productName,
        image: item.image,
        price: item.price,
        quantity: item.quantity
    }));

    if(status == 'user') {
        renderCartItems(itemsToCheckout);
    }
    else if(status == 'guest') {
        renderCartItems(itemsToCheckout);
        document.querySelectorAll('.checkout-form').forEach(form => form.style.display = 'block');
    }
}

function renderCartItems(items) {
    const listDiv = document.getElementById('cartItemsList');
    if (!listDiv) {
        console.error('Không tìm thấy phần tử cartItemsList trong DOM.');
        return;
    }

    listDiv.innerHTML = '';

    if (!Array.isArray(items) || items.length === 0) {
        document.getElementById('cartItems').style.display = 'none';
        document.getElementById('emptyCart').style.display = 'block';
        document.getElementById('orderDetails').style.display = 'none';
        return;
    }

    let totalItems = 0;
    let subtotal = 0;

    const table = document.createElement('table');
    table.className = 'cart-table';
    table.innerHTML = `
        <thead>
            <tr>
                <th>#</th>
                <th>Ảnh</th>
                <th>Tên món</th>
                <th>Đơn giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
            </tr>
        </thead>
        <tbody></tbody>
    `;
    const tbody = table.querySelector('tbody');

    items.forEach((item, idx) => {
        const price = Number(item.price);
        const qty = Number(item.quantity) || 1;
        const itemTotal = price * qty;
        subtotal += itemTotal;
        totalItems += qty;

        const tr = document.createElement('tr');
        tr.className = 'cart-item-row';
        tr.innerHTML = `
            <td class="item-no">${idx + 1}</td>
            <td class="item-img-td">
                <img src="${item.image || ''}" alt="${item.productName || ''}" class="cart-item-img" />
            </td>
            <td class="item-name">${item.productName || ''}</td>
            <td class="item-price">${price.toLocaleString()} đ</td>
            <td class="item-qty">
                <button class="qty-btn" onclick="updateItemQuantity('${item.productId}', -1)">-</button>
                <span class="quantity">${qty}</span>
                <button class="qty-btn" onclick="updateItemQuantity('${item.productId}', 1)">+</button>
            </td>
            <td class="item-total">${itemTotal.toLocaleString()} đ</td>
        `;
        tbody.appendChild(tr);
    });

    listDiv.appendChild(table);

    const shippingFee = 15000;
    const total = subtotal + shippingFee;
    document.getElementById('totalItems').textContent = totalItems;
    document.getElementById('subtotal').textContent = subtotal.toLocaleString();
    document.getElementById('shippingFee').textContent = shippingFee.toLocaleString();
    document.getElementById('totalAmount').textContent = total.toLocaleString();

    document.getElementById('cartItems').style.display = 'none';
    document.getElementById('emptyCart').style.display = 'none';
    document.getElementById('orderDetails').style.display = 'flex';
}

// FIXED: Sửa logic place order để không xóa DB cart
async function placeOrder() {
    const jwt = localStorage.getItem('jwt');
    const isGuest = !isJwtValid(jwt);
    const params = new URLSearchParams(window.location.search);

    const customerName = document.getElementById('customerName')?.value || '';
    const customerPhone = document.getElementById('customerPhone')?.value || '';
    const customerAddress = document.getElementById('customerAddress')?.value || '';
    const paymentMethod = document.getElementById('paymentMethod')?.value || 'cod';
    const orderNote = document.getElementById('orderNote')?.value || '';

    try {
        let orderData;
        let cartItems = [];

        if (params.get('useSessionCart') === 'true' && isJwtValid(jwt)) {
            // FIXED: Thêm session items vào DB trước khi lấy cart
            await addSessionItemsToDatabase(jwt);

            // Sau đó lấy cart từ DB (đã bao gồm cả session items)
            const response = await fetch('menu', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({action: 'getUserCart', jwt})
            });

            if (response.ok) {
                cartItems = await response.json();
            }

        } else if (isGuest || params.get('guest') === 'true') {
            const sessionCart = JSON.parse(sessionStorage.getItem('cart')) || [];
            const mergedItems = {};
            sessionCart.forEach(item => {
                const key = item.productId;
                if (!mergedItems[key]) {
                    mergedItems[key] = {
                        productId: item.productId,
                        productName: item.productName,
                        image: item.image,
                        price: parseFloat(item.price),
                        quantity: 1
                    };
                } else {
                    mergedItems[key].quantity += 1;
                }
            });
            cartItems = Object.values(mergedItems);
        } else {
            const response = await fetch('menu', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({action: 'getUserCart', jwt})
            });

            if (!response.ok) {
                throw new Error('Failed to retrieve cart data');
            }

            cartItems = await response.json();
        }

        if (cartItems.length === 0) {
            alert('Your cart is empty');
            return;
        }

        if (isGuest) {
            orderData = {
                action: 'postGuestOrder',
                name: customerName,
                phone: customerPhone,
                address: customerAddress,
                description: orderNote + ' | Payment: ' + paymentMethod,
                items: cartItems
            };
        } else {
            orderData = {
                action: 'postUserOrder',
                jwt: jwt,
                description: orderNote + ' | Payment: ' + paymentMethod,
                items: cartItems
            };
        }

        const orderResponse = await fetch('checkout', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(orderData)
        });

        const result = await orderResponse.json();

        if (result.success) {
            // Chỉ clear session cart
            if (params.get('useSessionCart') === 'true' || isGuest) {
                sessionStorage.removeItem('cart');
            }

            showSuccessModal(result.shippingCode, cartItems);
        } else {
            throw new Error(result.errorMessage || 'Failed to place order');
        }

    } catch (error) {
        console.error('Error placing order:', error);
        alert('Failed to place order: ' + error.message);
    }
}

// NEW: Hàm helper để thêm session items vào DB
async function addSessionItemsToDatabase(jwt) {
    const sessionCart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const sessionMerged = {};

    // Merge session items
    sessionCart.forEach(item => {
        const key = item.productId;
        if (!sessionMerged[key]) {
            sessionMerged[key] = { ...item, quantity: 1 };
        } else {
            sessionMerged[key].quantity += 1;
        }
    });

    // Thêm từng item vào DB bằng API có sẵn
    for (const item of Object.values(sessionMerged)) {
        for (let i = 0; i < item.quantity; i++) {
            await fetch('menu', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    action: 'addToCart',
                    productId: item.productId,
                    productName: item.productName,
                    image: item.image,
                    price: item.price.toString(),
                    jwt: jwt
                })
            });
        }
    }
}

function showSuccessModal(shippingCode, items) {
    document.getElementById('successModal').style.display = 'block';
    document.getElementById('shippingCode').textContent = shippingCode || 'Không xác định';

    const ul = document.getElementById('orderedItemsList');
    ul.innerHTML = '';
    if (Array.isArray(items)) {
        items.forEach(item => {
            const li = document.createElement('li');
            li.innerHTML = `<b>${item.productName || item.product_name || ''}</b> x${item.quantity || 1}`;
            ul.appendChild(li);
        });
    } else {
        ul.innerHTML = '<li>Không có dữ liệu món ăn.</li>';
    }
}

function closeSuccessModal() {
    document.getElementById('successModal').style.display = 'none';
    window.location.href = 'menu';
}

function backToMenu() {
    closeSuccessModal();
}

async function updateItemQuantity(productId, delta) {
    const jwt = localStorage.getItem('jwt');
    const params = new URLSearchParams(window.location.search);
    const isUsingSessionCart = params.get('useSessionCart') === 'true' || params.get('guest') === 'true';

    if (isUsingSessionCart || !isJwtValid(jwt)) {
        updateSessionCartQuantity(productId, delta);
    }
    else {
        await updateDatabaseCartQuantity(productId, delta, jwt);
    }

    // Refresh the cart display
    if (isUsingSessionCart || params.get('guest') === 'true') {
        checkoutWithCartFromSessionStorage(params.get('guest') === 'true' ? 'guest' : 'user');
    } else if (isJwtValid(jwt)) {
        checkoutWithCartFromDB(jwt);
    }
}

function updateSessionCartQuantity(productId, delta) {
    let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const mergedItems = {};

    cart.forEach(item => {
        const key = item.productId;
        if (!mergedItems[key]) {
            mergedItems[key] = { ...item, quantity: 1 };
        } else {
            mergedItems[key].quantity += 1;
        }
    });

    if (mergedItems[productId]) {
        mergedItems[productId].quantity += delta;

        if (mergedItems[productId].quantity <= 0) {
            delete mergedItems[productId];
        }

        const newCart = [];
        Object.values(mergedItems).forEach(item => {
            for (let i = 0; i < item.quantity; i++) {
                newCart.push({
                    productId: item.productId,
                    productName: item.productName,
                    image: item.image,
                    price: item.price
                });
            }
        });

        sessionStorage.setItem('cart', JSON.stringify(newCart));
    }
}

async function updateDatabaseCartQuantity(productId, delta, jwt) {
    try {
        const endpoint = 'menu';
        const method = delta > 0 ? 'PUT' : 'DELETE';

        const response = await fetch(endpoint, {
            method: method,
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                jwt: jwt,
                productId: productId.toString()
            })
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Failed to update cart:', errorData.message);
        }
    } catch (error) {
        console.error('Error updating cart:', error);
    }
}
