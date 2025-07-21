function updateStatus(orderId) {
    const select = document.getElementById(`status-` + orderId);
    const newStatus = select.value;

    if (newStatus === 'Hủy đơn') {
        // Gọi hàm xóa thay vì update
        deleteOrder({ stopPropagation: () => {} }, orderId);
        return;
    }

    $.ajax({
        url: 'updateOrderStatus',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ orderId, newStatus }),
        success: function (response) {
            if (response.success) {
                alert('Cập nhật trạng thái thành công');
                loadOrders();
            } else {
                alert('Cập nhật thất bại, vui lòng thử lại');
            }
        },
        error: function () {
            alert('Lỗi khi gửi yêu cầu cập nhật');
        }
    });
}

function showOrderDetails(orderId) {
    const order = window.ordersData.find(o => o.orderId === orderId);
    if (!order) return;

    let html = `<strong>Mã vận đơn:</strong> ${order.shippingCode}<br>
        <strong>Khách hàng:</strong> ${order.customerName}<br>
        <strong>Ngày đặt:</strong> ${new Date(order.orderDate).toLocaleString()}<br>
        <strong>Tổng tiền:</strong> ${order.totalAmount.toLocaleString()} VND<br>
        <strong>Trạng thái:</strong> ${order.status}<br>
        <hr>
        <h4>Chi tiết sản phẩm:</h4>
        <ul>`;
    (order.details || []).forEach(item => {
        html += `<li>
                ${item.imageUrl ? `<img src="${item.imageUrl}" width="40">` : ''}
                <strong>${item.productName}</strong>
                - SL: ${item.quantity}
                - Giá: ${item.price.toLocaleString()} VND
            </li>`;
    });
    html += '</ul>';

    $("#orderDetailsModal .modal-body").html(html);
    $("#orderDetailsModal").show();
}

function loadOrders() {
    $.ajax({
        url: 'new_orders',
        method: 'GET',
        success: function (data) {
            const tbody = $("#orderTable tbody");
            tbody.empty();

            // Sắp xếp đơn hàng theo ngày mới nhất lên đầu
            data = data.sort((a, b) => {
                // Xử lý định dạng ngày "7/18/2025, 3:37:46 PM"
                const parseDate = (dateStr) => {
                    // Chuyển đổi string thành Date object
                    return new Date(dateStr);
                };

                const dateA = parseDate(a.orderDate);
                const dateB = parseDate(b.orderDate);

                // Sắp xếp giảm dần (mới nhất trước)
                return dateB.getTime() - dateA.getTime();
            });

            window.ordersData = data; // Lưu lại để dùng khi show chi tiết

            data.forEach(order => {
                let options = `
                        <option${order.status === 'Chờ xử lý' ? ' selected' : ''}>Chờ xử lý</option>
                        <option${order.status === 'Đã nhận' ? ' selected' : ''}>Đã nhận</option>
                        <option${order.status === 'Đang làm' ? ' selected' : ''}>Đang làm</option>
                        <option${order.status === 'Đã giao' ? ' selected' : ''}>Đã giao</option>
                        <option${order.status === 'Hủy đơn' ? ' selected' : ''}>Hủy đơn</option>
                    `;
                const row = `
                    <tr>
                      <td>${order.shippingCode}</td>
                      <td>${order.username || order.customerName}</td>
                      <td>${new Date(order.orderDate).toLocaleString()}</td>
                      <td>${order.totalAmount.toLocaleString()} VND</td>
                      <td>${order.status}</td>
                      <td>
                        <select id="status-${order.orderId}">${options}</select>
                        <button onclick="updateStatus(${order.orderId}); event.stopPropagation();">Cập nhật</button>
                      </td>
                      <td>
                        <button onclick="showOrderDetails(${order.orderId}); event.stopPropagation();">Xem chi tiết</button>
                      </td>
                    </tr>`;
                tbody.append(row);
            });
        }
    });
}

$(document).ready(function() {
    loadOrders();
    setInterval(loadOrders, 5000);

    // Đóng modal khi click ra ngoài modal-content
    $('#orderDetailsModal').on('click', function(e) {
        if ($(e.target).is('#orderDetailsModal')) {
            $(this).hide();
        }
    });
});

async function deleteOrder(event, orderId) {
    if (event && event.stopPropagation) event.stopPropagation();

    if (!confirm("Bạn có chắc chắn muốn xoá đơn hàng này không?")) return;

    try {
        const response = await fetch("deleteOrder", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "orderId=" + orderId
        });

        const data = await response.json();
        alert(data.message);
        if (data.success) {
            loadOrders();
        }
    } catch (err) {
        console.error("❌ Lỗi khi xoá đơn hàng:", err);
        alert("Không thể xoá đơn hàng: " + err.message);
    }
}

