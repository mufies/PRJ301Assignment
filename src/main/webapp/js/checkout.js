document.addEventListener('DOMContentLoaded', async function () {
    const jwt = localStorage.getItem('jwt');
    const params = new URLSearchParams(window.location.search);

    try {
        if (params.get('useSessionCart') === 'true') {
            await checkoutWithCartFromSessionStorage('user');
        } else if (params.get('guest') === 'true') {
            await checkoutWithCartFromSessionStorage('guest');
        } else if (await isJwtValid(jwt) && !params.get('useSessionCart') && !params.get('guest')) {
            await checkoutWithCartFromDB(jwt);
        } else {
            await checkoutWithCartFromSessionStorage('guest');
        }
    } catch (error) {
        console.error('Error initializing checkout:', error);
    }

    const placeOrderBtn = document.getElementById('placeOrderBtn');
    if (placeOrderBtn) {
        placeOrderBtn.addEventListener('click', placeOrder);
    }
});

// ✅ FIX: Thay đổi thành async function để validate JWT với server
async function isJwtValid(token) {
    if (!token) return false;
    try {
        const requestData = { isJwtValid: token };
        const response = await fetch('JwtServlet', {  // ✅ Thêm 'await'
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();  // ✅ Thêm 'await'
        return data.isJwtValid;
    } catch (e) {
        return false;
    }
}

async function checkoutWithCartFromDB(jwt) {
    try {
        const response = await fetch('menu', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({action: 'getUserCart', jwt})
        });

        if (response.ok) {
            const cartItems = await response.json();
            console.log('Cart items from DB:', cartItems);
            renderCartItems(cartItems);
        } else {
            const errorData = await response.json();
            console.error('Failed to retrieve cart:', errorData);
            showError('Failed to retrieve cart: ' + (errorData.errorMessage || 'Unknown error'));
        }
    } catch (error) {
        console.error('Error fetching cart from DB:', error);
        showError('Failed to connect to server');
    }
}

async function checkoutWithCartFromSessionStorage(status) {
    try {
        let cartItems = JSON.parse(sessionStorage.getItem('cart')) || [];

        if (cartItems.length === 0) {
            showEmptyCart();
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
            price: parseFloat(item.price || 0),
            quantity: parseInt(item.quantity || 1)
        }));

        renderCartItems(itemsToCheckout);

        if (status === 'guest') {
            document.querySelectorAll('.checkout-form').forEach(form => {
                form.style.display = 'block';
            });
        }
    } catch (error) {
        console.error('Error processing session cart:', error);
        showError('Failed to process cart data');
    }
}

function renderCartItems(items) {
    const listDiv = document.getElementById('cartItemsList');
    if (!listDiv) {
        console.error('cartItemsList element not found in DOM');
        return;
    }

    listDiv.innerHTML = '';

    if (!Array.isArray(items) || items.length === 0) {
        showEmptyCart();
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
        const price = Number(item.price) || 0;
        const qty = Number(item.quantity) || 1;
        const itemTotal = price * qty;
        subtotal += itemTotal;
        totalItems += qty;

        const tr = document.createElement('tr');
        tr.className = 'cart-item-row';
        tr.innerHTML = `
            <td class="item-no">${idx + 1}</td>
            <td class="item-img-td">
                <img src="${item.image || 'default-product.jpg'}" 
                     alt="${item.productName || 'Product'}" 
                     class="cart-item-img" 
                     onerror="this.src='default-product.jpg'" />
            </td>
            <td class="item-name">${item.productName || 'Unknown Product'}</td>
            <td class="item-price">${price.toLocaleString('vi-VN')} đ</td>
            <td class="item-qty">
                <button class="qty-btn" onclick="updateItemQuantity('${item.productId}', -1)" type="button">-</button>
                <span class="quantity">${qty}</span>
                <button class="qty-btn" onclick="updateItemQuantity('${item.productId}', 1)" type="button">+</button>
            </td>
            <td class="item-total">${itemTotal.toLocaleString('vi-VN')} đ</td>
        `;
        tbody.appendChild(tr);
    });

    listDiv.appendChild(table);

    const shippingFee = 15000;
    const total = subtotal + shippingFee;

    updateOrderSummary(totalItems, subtotal, shippingFee, total);
    showOrderDetails();
}

function updateOrderSummary(totalItems, subtotal, shippingFee, total) {
    const elements = {
        totalItems: document.getElementById('totalItems'),
        subtotal: document.getElementById('subtotal'),
        shippingFee: document.getElementById('shippingFee'),
        totalAmount: document.getElementById('totalAmount')
    };

    if (elements.totalItems) elements.totalItems.textContent = totalItems;
    if (elements.subtotal) elements.subtotal.textContent = subtotal.toLocaleString('vi-VN');
    if (elements.shippingFee) elements.shippingFee.textContent = shippingFee.toLocaleString('vi-VN');
    if (elements.totalAmount) elements.totalAmount.textContent = total.toLocaleString('vi-VN');
}

function showEmptyCart() {
    const elements = {
        cartItems: document.getElementById('cartItems'),
        emptyCart: document.getElementById('emptyCart'),
        orderDetails: document.getElementById('orderDetails')
    };

    if (elements.cartItems) elements.cartItems.style.display = 'none';
    if (elements.emptyCart) elements.emptyCart.style.display = 'block';
    if (elements.orderDetails) elements.orderDetails.style.display = 'none';
}

function showOrderDetails() {
    const elements = {
        cartItems: document.getElementById('cartItems'),
        emptyCart: document.getElementById('emptyCart'),
        orderDetails: document.getElementById('orderDetails')
    };

    if (elements.cartItems) elements.cartItems.style.display = 'none';
    if (elements.emptyCart) elements.emptyCart.style.display = 'none';
    if (elements.orderDetails) elements.orderDetails.style.display = 'flex';
}

function showError(message) {
    alert(message); // You can replace this with a better error display
}

async function placeOrder() {
    const jwt = localStorage.getItem('jwt');
    const isValidJwt = await isJwtValid(jwt);
    const params = new URLSearchParams(window.location.search);

    // ✅ FIX: Validate required fields for guest checkout
    if (!isValidJwt || params.get('guest') === 'true') {
        const requiredFields = {
            customerName: document.getElementById('customerName')?.value?.trim(),
            customerPhone: document.getElementById('customerPhone')?.value?.trim(),
            customerAddress: document.getElementById('customerAddress')?.value?.trim()
        };

        const missingFields = Object.entries(requiredFields)
            .filter(([key, value]) => !value)
            .map(([key]) => key);

        if (missingFields.length > 0) {
            showError('Please fill in all required fields: ' + missingFields.join(', '));
            return;
        }
    }

    const orderDetails = {
        customerName: document.getElementById('customerName')?.value?.trim() || '',
        customerPhone: document.getElementById('customerPhone')?.value?.trim() || '',
        customerAddress: document.getElementById('customerAddress')?.value?.trim() || '',
        paymentMethod: document.getElementById('paymentMethod')?.value || 'cod',
        orderNote: document.getElementById('orderNote')?.value?.trim() || ''
    };

    const placeOrderBtn = document.getElementById('placeOrderBtn');
    if (placeOrderBtn) {
        placeOrderBtn.disabled = true;
        placeOrderBtn.textContent = 'Đang xử lý...';
    }

    try {
        let cartItems = [];

        if (params.get('useSessionCart') === 'true' && isValidJwt) {
            // Add session items to database first
            await addSessionItemsToDatabase(jwt);

            // Then fetch cart from DB
            const response = await fetch('menu', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({action: 'getUserCart', jwt})
            });

            if (response.ok) {
                cartItems = await response.json();
            } else {
                throw new Error('Failed to retrieve cart from database');
            }

        } else if (!isValidJwt || params.get('guest') === 'true') {
            // Guest checkout or invalid JWT
            cartItems = processSessionCart();

        } else {
            // Regular user checkout
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
            throw new Error('Your cart is empty');
        }

        const orderData = isValidJwt && params.get('guest') !== 'true' ? {
            action: 'postUserOrder',
            jwt: jwt,
            description: `${orderDetails.orderNote} | Payment: ${orderDetails.paymentMethod}`,
            items: cartItems
        } : {
            action: 'postGuestOrder',
            name: orderDetails.customerName,
            phone: orderDetails.customerPhone,
            address: orderDetails.customerAddress,
            description: `${orderDetails.orderNote} | Payment: ${orderDetails.paymentMethod}`,
            items: cartItems
        };

        const orderResponse = await fetch('checkout', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(orderData)
        });

        const result = await orderResponse.json();

        if (result.success) {
            // Clear session cart if used
            if (params.get('useSessionCart') === 'true' || !isValidJwt || params.get('guest') === 'true') {
                sessionStorage.removeItem('cart');
            }

            showSuccessModal(result.shippingCode, cartItems);
        } else {
            throw new Error(result.errorMessage || 'Failed to place order');
        }

    } catch (error) {
        console.error('Error placing order:', error);
        showError('Failed to place order: ' + error.message);
    } finally {
        if (placeOrderBtn) {
            placeOrderBtn.disabled = false;
            placeOrderBtn.textContent = 'Đặt hàng';
        }
    }
}

function processSessionCart() {
    const sessionCart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const mergedItems = {};

    sessionCart.forEach(item => {
        const key = item.productId;
        if (!mergedItems[key]) {
            mergedItems[key] = {
                productId: item.productId,
                productName: item.productName,
                image: item.image,
                price: parseFloat(item.price || 0),
                quantity: 1
            };
        } else {
            mergedItems[key].quantity += 1;
        }
    });

    return Object.values(mergedItems);
}

async function addSessionItemsToDatabase(jwt) {
    const sessionCart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const mergedItems = processSessionCart();

    // Add each item to database
    for (const item of mergedItems) {
        for (let i = 0; i < item.quantity; i++) {
            try {
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
            } catch (error) {
                console.error('Error adding item to database:', error);
            }
        }
    }
}

function showSuccessModal(shippingCode, items) {
    const modal = document.getElementById('successModal');
    const codeElement = document.getElementById('shippingCode');
    const itemsList = document.getElementById('orderedItemsList');

    if (modal) modal.style.display = 'block';
    if (codeElement) codeElement.textContent = shippingCode || 'N/A';

    if (itemsList) {
        itemsList.innerHTML = '';
        if (Array.isArray(items) && items.length > 0) {
            items.forEach(item => {
                const li = document.createElement('li');
                li.innerHTML = `<b>${item.productName || item.product_name || 'Unknown Product'}</b> x${item.quantity || 1}`;
                itemsList.appendChild(li);
            });
        } else {
            itemsList.innerHTML = '<li>No items found.</li>';
        }
    }
}

function closeSuccessModal() {
    const modal = document.getElementById('successModal');
    if (modal) modal.style.display = 'none';
    window.location.href = 'menu';
}

function backToMenu() {
    closeSuccessModal();
}

async function updateItemQuantity(productId, delta) {
    const jwt = localStorage.getItem('jwt');
    const params = new URLSearchParams(window.location.search);
    const isUsingSessionCart = params.get('useSessionCart') === 'true' || params.get('guest') === 'true';
    const isValidJwt = await isJwtValid(jwt);

    try {
        if (isUsingSessionCart || !isValidJwt) {
            updateSessionCartQuantity(productId, delta);
        } else {
            await updateDatabaseCartQuantity(productId, delta, jwt);
        }

        // Refresh the cart display
        if (isUsingSessionCart || params.get('guest') === 'true') {
            await checkoutWithCartFromSessionStorage(params.get('guest') === 'true' ? 'guest' : 'user');
        } else if (isValidJwt) {
            await checkoutWithCartFromDB(jwt);
        }
    } catch (error) {
        console.error('Error updating quantity:', error);
        showError('Failed to update quantity');
    }
}

function updateSessionCartQuantity(productId, delta) {
    let cart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const mergedItems = {};

    // Merge existing items
    cart.forEach(item => {
        const key = item.productId;
        if (!mergedItems[key]) {
            mergedItems[key] = { ...item, quantity: 1 };
        } else {
            mergedItems[key].quantity += 1;
        }
    });

    // Update quantity
    if (mergedItems[productId]) {
        mergedItems[productId].quantity += delta;

        if (mergedItems[productId].quantity <= 0) {
            delete mergedItems[productId];
        }

        // Rebuild cart array
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
        const action = delta > 0 ? 'addToCart' : 'removeFromCart';
        const method = delta > 0 ? 'PUT' : 'DELETE';

        let requestBody = {
            jwt: jwt,
            productId: productId.toString()
        };

        if (delta > 0) {
            requestBody.action = action;
        } else {
            requestBody.action = action;
        }

        const response = await fetch('menu', {
            method: method,
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(requestBody)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to update cart');
        }
    } catch (error) {
        console.error('Error updating database cart:', error);
        throw error;
    }
}
