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

    // Có kết quả
    resultDiv.innerHTML = orders.map(order => `
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
        <p><strong>Ngày đặt:</strong> ${order.orderDate}</p>
        <p><strong>Tổng tiền:</strong> ${order.totalPrice} VND</p>
        <p><strong>Trạng thái:</strong> ${order.status}</p>
        <p><strong>Ghi chú:</strong> ${order.description || '(không có)'}</p>
      </div>
      <div class="collapse" id="items-${order.orderId}">
        <div class="card-body border-top">
          <h6>Chi tiết sản phẩm:</h6>
          <ul class="list-group">
            ${order.items.map(i => `
              <li class="list-group-item d-flex justify-content-between">
                <span>${i.productName} x${i.quantity}</span>
                <span>${i.price} VND</span>
              </li>`).join('')}
          </ul>
        </div>
      </div>
    </div>
  `).join('');
}
