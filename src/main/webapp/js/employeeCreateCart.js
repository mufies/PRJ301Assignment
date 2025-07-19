// Global cart object to store selected items
let cart = [];

// Function to show/hide customer sections based on selected type
document.querySelectorAll('input[name="customerType"]').forEach(radio => {
    radio.addEventListener('change', function() {
        if (this.value === 'registered') {
            document.getElementById('registeredCustomerSection').style.display = 'block';
            document.getElementById('guestCustomerSection').style.display = 'none';
        } else {
            document.getElementById('registeredCustomerSection').style.display = 'none';
            document.getElementById('guestCustomerSection').style.display = 'block';
        }
    });
});

// Function to add product to cart
function addToCart(productId, productName, price, image) {
    cart.push({
        productId: productId,
        productName: productName,
        price: parseInt(price),
        image: image,
        quantity: 1
    });
    updateCartDisplay();
}

// Function to update cart item quantity
function updateQuantity(index, delta) {
    cart[index].quantity += delta;

    if (cart[index].quantity <= 0) {
        removeFromCart(index);
    } else {
        updateCartDisplay();
    }
}

// Function to remove item from cart
function removeFromCart(index) {
    cart.splice(index, 1);
    updateCartDisplay();
}

// Function to update the cart display
function updateCartDisplay() {
    const tableBody = document.getElementById('cartTableBody');
    tableBody.innerHTML = '';

    let totalItems = 0;
    let subtotal = 0;

    cart.forEach((item, index) => {
        const itemTotal = item.price * item.quantity;
        subtotal += itemTotal;
        totalItems += item.quantity;

        const tr = document.createElement('tr');
        tr.innerHTML = `
                <td><img src="${item.image}" alt="${item.productName}" class="cart-item-img"></td>
                <td>${item.productName}</td>
                <td>${item.price.toLocaleString()} VND</td>
                <td>
                    <div class="btn-group btn-group-sm">
                        <button class="btn btn-outline-secondary" onclick="updateQuantity(${index}, -1)">-</button>
                        <span class="btn btn-outline-secondary disabled">${item.quantity}</span>
                        <button class="btn btn-outline-secondary" onclick="updateQuantity(${index}, 1)">+</button>
                    </div>
                </td>
                <td>${itemTotal.toLocaleString()} VND</td>
                <td><button class="btn btn-sm btn-danger" onclick="removeFromCart(${index})">✕</button></td>
            `;
        tableBody.appendChild(tr);
    });

    // Update order summary
    const shippingFee = 15000;
    const total = subtotal + shippingFee;

    document.getElementById('totalItems').textContent = totalItems;
    document.getElementById('subtotal').textContent = subtotal.toLocaleString() + ' VND';
    document.getElementById('totalAmount').textContent = total.toLocaleString() + ' VND';
}

// Function to search products
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('foodInput').addEventListener('keyup', function() {
        const searchTerm = this.value.toLowerCase();
        const allProducts = document.querySelectorAll('.product-card');

        allProducts.forEach(product => {
            const name = product.querySelector('h6').textContent.toLowerCase();
            if (name.includes(searchTerm)) {
                product.style.display = 'block';
            } else {
                product.style.display = 'none';
            }
        });
    });
});

// Submit order
document.getElementById('submitOrderBtn').addEventListener('click', function() {
    if (cart.length === 0) {
        alert('Vui lòng chọn ít nhất một món ăn!');
        return;
    }

    const customerType = document.querySelector('input[name="customerType"]:checked').value;
    let orderData = {
        customerType: customerType,
        items: cart,
        description: document.getElementById('orderNote').value
    };

    if (customerType === 'registered') {
        const username = document.getElementById('username').value.trim();
        if (!username) {
            alert('Vui lòng nhập tên đăng nhập của khách hàng!');
            return;
        }
        orderData.username = username;
    } else {
        const name = document.getElementById('customerName').value.trim();
        const phone = document.getElementById('customerPhone').value.trim();
        const address = document.getElementById('customerAddress').value.trim();

        if (!name || !phone || !address) {
            alert('Vui lòng điền đầy đủ thông tin khách hàng!');
            return;
        }

        orderData.name = name;
        orderData.phone = phone;
        orderData.address = address;
    }

    // Submit order to server
    fetch('employeeCheckingOrder', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(orderData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const successModal = new bootstrap.Modal(document.getElementById('successModal'));
                document.getElementById('shippingCode').textContent = data.shippingCode;

                const orderList = document.getElementById('orderedItemsList');
                orderList.innerHTML = '';
                cart.forEach(item => {
                    const li = document.createElement('li');
                    li.textContent = `${item.productName} x${item.quantity} - ${(item.price * item.quantity).toLocaleString()} VND`;
                    orderList.appendChild(li);
                });

                successModal.show();

                cart = [];
                updateCartDisplay();

                if (customerType === 'guest') {
                    document.getElementById('customerName').value = '';
                    document.getElementById('customerPhone').value = '';
                    document.getElementById('customerAddress').value = '';
                } else {
                    document.getElementById('username').value = '';
                }
                document.getElementById('orderNote').value = '';

            } else {
                alert('Lỗi: ' + (data.errorMessage || 'Không thể tạo đơn hàng'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.');
        });
});