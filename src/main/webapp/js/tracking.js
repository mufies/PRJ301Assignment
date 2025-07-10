    async function trackOrder() {
    const code = document.getElementById('trackingCodeInput').value.trim();
    const statusDiv = document.getElementById('trackingStatus');
    statusDiv.textContent = '';

    if (!code) {
    statusDiv.textContent = 'Vui lòng nhập mã vận đơn!';
    return;
}
    statusDiv.textContent = 'Đang tra cứu...';

    try {
    const response = await fetch('tracking', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({ action: 'getOrderByShippingCode', shippingCode: code })
});

    if (!response.ok) {
    statusDiv.textContent = 'Lỗi server, vui lòng thử lại!';
    return;
}

    const result = await response.json();

    if (result.success && result.order) {
    showOrderInfoModal(result.order);
    statusDiv.textContent = '';
} else {
    statusDiv.textContent = result.errorMessage || 'Không tìm thấy đơn hàng!';
}
} catch (e) {
    statusDiv.textContent = 'Có lỗi xảy ra, vui lòng thử lại!';
}
}

    function showOrderInfoModal(order) {
        document.getElementById('orderInfoModal').style.display = 'block';

        document.getElementById('modalCustomer').textContent = order.customerName || order.guestName || '';
        document.getElementById('modalStatus').textContent = order.status || 'Chờ xử lý';

        const ul = document.getElementById('modalItemsList');
        ul.innerHTML = '';
        if (Array.isArray(order.items) && order.items.length > 0) {
            order.items.forEach(item => {
                const li = document.createElement('li');
                li.innerHTML = `<b>${item.productName || item.product_name || ''}</b> x${item.quantity || 1}${item.price ? ' - ' + Number(item.price).toLocaleString() + ' đ' : ''}`;
                ul.appendChild(li);
            });
        } else {
            ul.innerHTML = '<li>Không có dữ liệu món ăn.</li>';
        }
    }


    function closeOrderInfoModal() {
    document.getElementById('orderInfoModal').style.display = 'none';
}
