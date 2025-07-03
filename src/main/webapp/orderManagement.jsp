<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="favicon.ico">
</head>
<body>
<div class="bg-white border-bottom">
    <div class="container-fluid px-0">
        <ul class="nav nav-pills py-2 px-3">
            <li class="nav-item"><a class="nav-link " href="/ayxkix"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link " href="/ayxkix/product"><i class="bi bi-box-seam me-1"></i>Product Management</a></li>
            <li class="nav-item"><a class="nav-link active" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
        </ul>
    </div>
</div><div class="container mt-4">
    <h3>Quản lý đơn hàng</h3>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>Order ID</th><th>Khách</th><th>Ngày đặt</th><th>Tổng tiền</th><th>Trạng thái</th><th>Chi tiết</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="o" items="${orderList}">
            <tr>
                <td>${o.orderId}</td>
                <td>${o.customerName}</td>
                <td>${o.orderDate}</td>
                <td><fmt:formatNumber value="${o.totalAmount}" />VND</td>
                <td>${o.status}</td>
                <td>${o.description}</td>
                <td>
                    <button type="button" class="btn btn-sm btn-info"

                            data-bs-target="#orderDetail${o.orderId}">
                        Xem
                    </button>
                </td>
            </tr>
            <tr>
                <td colspan="7" style="padding:0; border:none;">
                    <div id="orderDetail${o.orderId}" class="p-3 bg-light rounded shadow-sm">
                        <strong class="mb-2 d-block text-primary fs-5">Sản phẩm trong đơn:</strong>
                        <ul class="list-group list-group-flush">
                            <c:forEach var="d" items="${o.details}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                        <span>
                            <strong>${d.productName}</strong>
                            <span class="badge bg-secondary ms-2">SL: ${d.quantity}</span>
                        </span>
                                    <span class="text-success fw-bold">
                            <fmt:formatNumber value="${d.price}" />VND
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
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var orderDetails = document.querySelectorAll('[id^="orderDetail"]');
        orderDetails.forEach(function (detail) {
            detail.style.display = 'none';
        });

        var buttons = document.querySelectorAll('.btn-info');
        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                var targetId = this.getAttribute('data-bs-target');
                var targetDetail = document.querySelector(targetId);
                if (targetDetail.style.display === 'none') {
                    targetDetail.style.display = 'block';
                } else {
                    targetDetail.style.display = 'none';
                }
            });
        });
    });
</script>
</body>
</html>
