document.addEventListener('DOMContentLoaded', function () {
    const jwt = localStorage.getItem('jwt');
    const params = new URLSearchParams(window.location.search);

    if (params.get('useSessionCart') === 'true') {
        checkoutWithCartFromSessionStorage('user');
    } else if (params.get('guest') === 'true') {
        checkoutWithCartFromSessionStorage('guest');
    } else if (isJwtValid(jwt)) {
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

    // Clear previous items
    listDiv.innerHTML = '';

    // Handle empty cart
    if (!Array.isArray(items) || items.length === 0) {
        document.getElementById('cartItems').style.display = 'none';
        document.getElementById('emptyCart').style.display = 'block';
        document.getElementById('orderDetails').style.display = 'none';
        return;
    }

    let totalItems = 0;
    let subtotal = 0;

    // Table layout for cart
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
                <th>Xóa</th>
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
            <td class="item-remove">
                <button class="remove-btn" onclick="removeItem('${item.productId}')">
                    <i class="fa fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    listDiv.appendChild(table);

    // Update order summary
    const shippingFee = 15000;
    const total = subtotal + shippingFee;
    document.getElementById('totalItems').textContent = totalItems;
    document.getElementById('subtotal').textContent = subtotal.toLocaleString();
    document.getElementById('shippingFee').textContent = shippingFee.toLocaleString();
    document.getElementById('totalAmount').textContent = total.toLocaleString();

    // Show order details section
    document.getElementById('cartItems').style.display = 'none';
    document.getElementById('emptyCart').style.display = 'none';
    document.getElementById('orderDetails').style.display = 'flex';
}

// Function to place order
async function placeOrder() {
    const jwt = localStorage.getItem('jwt');
    const isGuest = !isJwtValid(jwt);

    // Get form data
    const customerName = document.getElementById('customerName').value;
    const customerPhone = document.getElementById('customerPhone').value;
    const customerAddress = document.getElementById('customerAddress').value;
    const paymentMethod = document.getElementById('paymentMethod')?.value || 'cod';
    const orderNote = document.getElementById('orderNote')?.value || '';

    try {
        let orderData;
        let cartItems = [];

        // Get cart items based on user type
        if (isGuest || new URLSearchParams(window.location.search).get('useSessionCart') === 'true') {
            // Get session cart
            const sessionCart = JSON.parse(sessionStorage.getItem('cart')) || [];

            // Merge items by productId
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
            // For logged-in users with DB cart
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

        // Check if cart is empty
        if (cartItems.length === 0) {
            alert('Your cart is empty');
            return;
        }

        // Prepare order data
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

        // Send order to server
        const orderResponse = await fetch('checkout', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(orderData)
        });

        const result = await orderResponse.json();

        if (result.success) {
            // Clear cart
            if (isGuest || new URLSearchParams(window.location.search).get('useSessionCart') === 'true') {
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

function postUserOrder(jwt, items) {
    return fetch('checkout', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({action: 'postUserOrder', jwt, items})
    });
}

function postGuestOrder(items) {
    return fetch('checkout', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({action: 'postGuestOrder', items})
    });
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
