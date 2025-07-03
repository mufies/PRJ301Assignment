<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Customer Management</title>
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
            <li class="nav-item"><a class="nav-link" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
            <li class="nav-item"><a class="nav-link active" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
        </ul>
    </div>
    <div class="container mt-4">
        <h3>Quản lý khách hàng</h3>

        <table class="table table-hover">
            <thead>
            <tr>
                <th>ID</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Tổng chi tiêu</th>
                <th>Chi tiết</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="c" items="${getAllUsers}">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${c.id != null}">
                                ${c.id}
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Guest</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${c.fullName}</td>
                    <td>${c.email}</td>
                    <td>${c.phoneNumber}</td>
                    <td><fmt:formatNumber value="${c.totalSpent}" /> VND</td>
                    <td>
                        <button type="button" class="btn btn-sm btn-info"
                                data-bs-toggle="collapse" >
                            Sửa
                        </button>
                        <button type="button" class="btn btn-sm btn-info"
                                data-bs-toggle="collapse" >Xóa
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
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
