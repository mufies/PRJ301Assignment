async function loadOrderHistory() {
    const jwt = localStorage.getItem('jwt');
    console.log(jwt); // ✅ Thêm log để kiểm tra jwt\
    const jwtValid = await isJwtValid(jwt);
    console.log("JWT Valid:", jwtValid); // ✅ Thêm log để kiểm tra tính hợp lệ của jwt
    if (await isJwtValid(jwt)!== true) {
        alert("Bạn cần đăng nhập để xem lịch sử mua hàng.");
        window.location.href = contextPath + '/menu';
        return;
    }

    try { // ✅ Thêm try-catch
        const response = await fetch('history', { // ✅ Sử dụng async/await thay vì then
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ jwt: jwt })
        });

        if (!response.ok) {
            throw new Error("Lỗi khi lấy đơn hàng");
        }

        const data = await response.json();
        let orders = data;
        const tbody = document.getElementById("order-history-body");
        const noOrders = document.getElementById("no-orders");

        if (!tbody) { // ✅ Thêm null check
            console.error("Không tìm thấy element order-history-body");
            return;
        }

        tbody.innerHTML = "";

        if (!orders || orders.length === 0) {
            if (noOrders) noOrders.style.display = "block"; // ✅ Thêm null check
            return;
        }

        // Sort orders by date (newest first)
        orders = orders.sort((a, b) => {
            const parseDate = (dateStr) => {
                const [datePart, timePart] = dateStr.split(' ');
                const [day, month, year] = datePart.split('/');
                return new Date(`${year}-${month}-${day}T${timePart}:00`);
            };

            const dateA = parseDate(a.orderDate);
            const dateB = parseDate(b.orderDate);
            return dateB - dateA; // Sort descending (newest first)
        });

        if (noOrders) noOrders.style.display = "none"; // ✅ Thêm null check

        orders.forEach(order => {
            const row = document.createElement("tr");
            row.classList.add("order-row");

            row.innerHTML = `
                <td>${order.orderDate || 'N/A'}</td>
                <td>${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0)}</td>
                <td>${order.status || 'Unknown'}</td>
            `;

            const detailRow = document.createElement("tr");
            detailRow.classList.add("order-details");
            detailRow.style.display = "none";
            detailRow.innerHTML = `
                <td colspan="4">
                    <strong>Mô tả:</strong> ${order.description || 'Không có'}<br>
                    <strong>Sản phẩm:</strong>
                    <ul>
                        ${(order.items || []).map(item => `
                            <li>${item.productName || 'Unknown Product'} - SL: ${item.quantity || 0} - Giá: ${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(item.price || 0)}</li>
                        `).join("")}
                    </ul>
                </td>
            `;

            row.addEventListener("click", () => {
                const isVisible = detailRow.style.display !== "none";
                detailRow.style.display = isVisible ? "none" : "table-row";
            });

            tbody.appendChild(row);
            tbody.appendChild(detailRow);
        });
    } catch (err) {
        console.error("❌ Lỗi khi load lịch sử:", err);
        alert("Không thể tải lịch sử mua hàng: " + err.message);
    }
}

// ✅ Đảm bảo có function isJwtValid async
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
        console.log("❌ Lỗi khi kiểm tra JWT:", e);
        return false;
    }
}
