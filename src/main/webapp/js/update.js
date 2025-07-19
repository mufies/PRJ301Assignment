function loadOrderHistory() {
    const jwt = localStorage.getItem('jwt');
    if (!isJwtValid(jwt)) {
        alert("Bạn cần đăng nhập để xem lịch sử mua hàng.");
        window.location.href = contextPath + '/menu';
        return;
    }

    fetch('history', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ jwt: jwt })
    })
        .then(res => {
            if (!res.ok) throw new Error("Lỗi khi lấy đơn hàng");
            return res.json();
        })
        .then(data => {
            let orders = data;
            const tbody = document.getElementById("order-history-body");
            const noOrders = document.getElementById("no-orders");

            tbody.innerHTML = "";

            if (!orders || orders.length === 0) {
                noOrders.style.display = "block";
                return;
            }

            // Sắp xếp đơn hàng theo ngày mới nhất lên đầu
// Sắp xếp đơn hàng theo ngày mới nhất lên đầu
            orders = orders.sort((a, b) => {
                // Chuyển đổi định dạng dd/mm/yyyy hh:mm sang Date
                const parseDate = (dateStr) => {
                    const [datePart, timePart] = dateStr.split(' ');
                    const [day, month, year] = datePart.split('/');
                    return new Date(`${year}-${month}-${day}T${timePart}:00`);
                };

                const dateA = parseDate(a.orderDate);
                const dateB = parseDate(b.orderDate);
                return dateB - dateA; // Sắp xếp giảm dần (mới nhất trước)
            });


            noOrders.style.display = "none";

            orders.forEach(order => {
                const row = document.createElement("tr");
                row.classList.add("order-row");

                row.innerHTML = `
                        <td>${(order.orderDate)}</td>
                        <td>${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice)}</td>
                        <td>${order.status}</td>
                    `;

                const detailRow = document.createElement("tr");
                detailRow.classList.add("order-details");
                detailRow.style.display = "none";
                detailRow.innerHTML = `
                        <td colspan="4">
                            <strong>Mô tả:</strong> ${order.description || 'Không có'}<br>
                            <strong>Sản phẩm:</strong>
                            <ul>
                                ${order.items.map(item => `
                                    <li>${item.productName} - SL: ${item.quantity} - Giá: ${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(item.price)}</li>
                                `).join("")}
                            </ul>
                        </td>
                    `;

                row.addEventListener("click", () => {
                    detailRow.style.display = (detailRow.style.display === "none") ? "table-row" : "none";
                });

                tbody.appendChild(row);
                tbody.appendChild(detailRow);
            });
        })
        .catch(err => {
            console.error("❌ Lỗi khi load lịch sử:", err);
            alert("Không thể tải lịch sử mua hàng.");
        });
}
