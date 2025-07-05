<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <style>
        .order-detail { display: none; }
    </style>

    <script>
        const token = localStorage.getItem('jwt');
        if (!isJwtValid(token)) {
            window.location.replace('<%=request.getContextPath()%>/menu');
        } else {
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                if (payload.role !== 'Admin') {
                    window.location.replace('<%=request.getContextPath()%>/menu');
                }
            } catch (e) {
                window.location.replace('<%=request.getContextPath()%>/menu');
            }
        }
        function isJwtValid(token) {
            if (!token) return false;
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                return !payload.exp || (Date.now() / 1000 < payload.exp);
            } catch (e) {
                return false;
            }
        }

    </script>
</head>
<body>
<div class="bg-white border-bottom">
    <div class="container-fluid px-0">
        <ul class="nav nav-pills py-2 px-3">
            <li class="nav-item"><a class="nav-link" href="/ayxkix"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/product"><i class="bi bi-box-seam me-1"></i>Product Management</a></li>
            <li class="nav-item"><a class="nav-link active" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
        </ul>
    </div>
</div>

<div class="container mt-4">
    <h3>Quản lý đơn hàng</h3>

    <!-- Search box -->
    <div class="mb-3">
        <input type="text" id="searchBox" class="form-control" placeholder="Tìm theo tên khách hàng...">
    </div>


    <table class="table table-hover" id="orderTable">
        <thead>
        <tr>
            <th>Mã đơn</th>
            <th>Khách hàng</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Chi tiết</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="o" items="${orderList}">
            <tr class="order-row">
                <td>${o.orderId}</td>
                <td class="customer-name">${o.customerName}</td>
                <td>${o.orderDate}</td>
                <td><fmt:formatNumber value="${o.totalAmount}" /> VND</td>
                <td>${o.status}</td>
                <td>
                    <button type="button" class="btn btn-sm btn-info" onclick="toggleDetail(${o.orderId})">Xem</button>
                </td>
            </tr>
            <tr class="order-detail" id="detail-${o.orderId}">
                <td colspan="6">
                    <div class="p-3 bg-light rounded shadow-sm">
                        <strong class="text-primary fs-5">Sản phẩm trong đơn:</strong>
                        <ul class="list-group list-group-flush mt-2">
                            <c:forEach var="d" items="${o.details}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>
                                        <strong>${d.productName}</strong>
                                        <span class="badge bg-secondary ms-2">SL: ${d.quantity}</span>
                                    </span>
                                    <span class="text-success fw-bold">
                                        <fmt:formatNumber value="${d.price}" /> VND
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<!-- Modal để hiển thị kết quả tìm kiếm -->
<!-- Modal hiển thị kết quả -->
<div class="modal fade" id="orderSearchModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Kết quả tìm kiếm đơn hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <!-- duy nhất 1 id=orderResults -->
            <div class="modal-body">
                <div id="orderResults"></div>
            </div>
        </div>
    </div>
</div>


<script>
    function toggleDetail(orderId) {
        const detailRow = document.getElementById('detail-' + orderId);
        if (detailRow.style.display === 'table-row') {
            detailRow.style.display = 'none';
        } else {
            detailRow.style.display = 'table-row';
        }
    }

    // // Simple client-side search
    // document.getElementById('searchBox').addEventListener('input', function () {
    //     const keyword = this.value.toLowerCase();
    //     const rows = document.querySelectorAll('#orderTable .order-row');
    //
    //     rows.forEach(row => {
    //         const name = row.querySelector('.customer-name').innerText.toLowerCase();
    //         row.style.display = name.includes(keyword) ? '' : 'none';
    //         const nextDetail = row.nextElementSibling;
    //         if (nextDetail && nextDetail.classList.contains('order-detail')) {
    //             nextDetail.style.display = 'none';
    //         }
    //     });
    // });


</script>
<script src="${pageContext.request.contextPath}/js/admin/orderUtils.js"></script>
</body>
</html>
