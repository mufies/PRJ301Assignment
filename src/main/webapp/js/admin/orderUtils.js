/* --------- cấu hình --------- */
const ctx = document.body.dataset.ctx || '';               // gắn data-ctx trong <body> nếu thích
const ENDPOINT = `${ctx}/ayxkix/order`;                    // servlet trả JSON

/* --------- DOM & modal --------- */
const searchBox  = document.getElementById('searchBox');
const resultDiv  = document.getElementById('orderResults');
const modalElem  = document.getElementById('orderSearchModal');
const resultModal = new bootstrap.Modal(modalElem, { backdrop: 'static' });

/* --------- debounce giúp giảm gọi API --------- */
function debounce(fn, wait = 400) {
    let t;
    return (...args) => {
        clearTimeout(t);
        t = setTimeout(() => fn.apply(this, args), wait);
    };
}

/* --------- lắng nghe ô tìm kiếm --------- */
searchBox.addEventListener('input', debounce(handleSearch, 300));

function handleSearch() {
    const keyword = searchBox.value.trim();

    // Clear & đóng modal khi ô tìm kiếm rỗng
    if (!keyword) {
        resultDiv.innerHTML = '';
        if (modalElem.classList.contains('show')) {
            resultModal.hide();
        }
        return;
    }

    // Gửi yêu cầu tìm kiếm
    fetch(ENDPOINT, {
        method : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body   : JSON.stringify({ customerName: keyword })
    })
        .then(r => r.ok ? r.json() : Promise.reject(r.status))
        .then(renderOrders)
        .then(() => resultModal.show())
        .catch(err => console.error('Search error', err));
}

/* --------- vẽ kết quả --------- */
function renderOrders(orders) {
    // Không tìm thấy
    if (!orders || !orders.length) {
        resultDiv.innerHTML =
            '<div class="alert alert-warning">Không tìm thấy đơn hàng nào phù hợp.</div>';
        return;
    }

    // ✅ Sort theo ngày từ mới nhất đến cũ nhất
    const sortedOrders = orders.sort((a, b) => {
        // Parse date string to Date object
        const dateA = new Date(a.orderDate);
        const dateB = new Date(b.orderDate);

        // Sort descending (newest first)
        return dateB - dateA;
    });

    // Có kết quả
    resultDiv.innerHTML = sortedOrders.map(order => `
    <div class="card mb-2">
      <div class="card-header d-flex justify-content-between">
        <strong>Đơn hàng #${order.orderId}</strong>
        <button class="btn btn-sm btn-outline-primary"
                data-bs-toggle="collapse"
                data-bs-target="#items-${order.orderId}">
          Xem chi tiết
        </button>
      </div>
      <div class="card-body">
        <p><strong>Khách:</strong> ${order.customerName}</p>
        <p><strong>Ngày đặt:</strong> ${formatDate(order.orderDate)}</p>
        <p><strong>Tổng tiền:</strong> ${Number(order.totalPrice).toLocaleString('vi-VN')} VND</p>
        <p><strong>Trạng thái:</strong> ${order.status}</p>
        <p><strong>Ghi chú:</strong> ${order.description || '(không có)'}</p>
      </div>
      <div class="collapse" id="items-${order.orderId}">
        <div class="card-body border-top">
          <h6>Chi tiết sản phẩm:</h6>
          <ul class="list-group">
            ${(order.items || []).map(i => `
              <li class="list-group-item d-flex justify-content-between">
                <span>${i.productName || 'Unknown Product'} x${i.quantity || 0}</span>
                <span>${Number(i.price || 0).toLocaleString('vi-VN')} VND</span>
              </li>`).join('')}
          </ul>
        </div>
      </div>
    </div>
  `).join('');
}

// ✅ Helper function để format date cho hiển thị
function formatDate(dateString) {
    try {
        const date = new Date(dateString);
        return date.toLocaleString('vi-VN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    } catch (error) {
        return dateString; // Return original if parsing fails
    }
}

