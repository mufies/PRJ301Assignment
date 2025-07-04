<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Customer Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</head>
<body>
<div class="bg-white border-bottom">
    <div class="container-fluid px-0">
        <ul class="nav nav-pills py-2 px-3">
            <li class="nav-item"><a class="nav-link" href="/ayxkix"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/product"><i class="bi bi-box-seam me-1"></i>Product Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
            <li class="nav-item"><a class="nav-link active" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
        </ul>
    </div>

    <div class="container mt-4">
        <h3>Quản lý khách hàng</h3>

        <!-- Ô tìm kiếm -->
        <div class="mb-3">
            <input type="text" id="searchBox" class="form-control" placeholder="Tìm theo số điện thoại...">
        </div>

        <table class="table table-hover" id="customerTable">
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

                <tr class="customer-row">
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
                    <td class="customer-name">${c.fullName}</td>
                    <td>${c.email}</td>
                    <td class="customer-phone">${c.phoneNumber}</td>
                    <td><fmt:formatNumber value="${c.totalSpent}" /> VND</td>
                    <td>
                        <c:if test="${c.fullName != 'account deleted'}">
                        <button type="button"
                                class="btn btn-sm btn-info"
                                data-bs-toggle="modal"
                                data-bs-target="#editCustomerModal"
                                data-id="${c.id}"
                                data-fullname="${c.fullName}"
                                data-phone="${c.phoneNumber}"
                                data-address="${c.address}"
                                data-email="${c.email}"
                                data-password="${c.password}"
                                onclick="fillEditCustomerModalFromData(this)">
                            Sửa
                        </button>

                        <button type="button" class="btn btn-sm btn-danger" onclick="deleteEmployee(${c.id})">Xóa</button>
                        </c:if>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Modal chỉnh sửa khách hàng -->
<div class="modal fade" id="editCustomerModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chỉnh sửa thông tin khách hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <form id="editCustomerForm" method="post" action="customer">
                    <input type="hidden" id="editCustomerId" name="customerId">

                    <div class="mb-3">
                        <label for="editFullName" class="form-label">Họ tên</label>
                        <input type="text" class="form-control" id="editFullName" name="fullName" required>
                    </div>

                    <div class="mb-3">
                        <label for="editPhone" class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" id="editPhone" name="phoneNumber" required>
                    </div>

                    <div class="mb-3">
                        <label for="editAddress" class="form-label">Địa chỉ</label>
                        <input type="text" class="form-control" id="editAddress" name="address" required>
                    </div>

                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email</label>
                        <input type="email" class="form-control" id="editEmail" name="email" required>
                    </div>

                    <div class="mb-3">
                        <label for="editPassword" class="form-label">Mật khẩu</label>
                        <input type="password" class="form-control" id="editPassword" name="password" required>
                    </div>

                    <button type="submit" class="btn btn-primary">Cập nhật khách hàng</button>
                </form>
            </div>
        </div>
    </div>
</div>



<script>
    document.getElementById('searchBox').addEventListener('input', function () {
        const keyword = this.value.trim().toLowerCase();
        const rows = document.querySelectorAll('#customerTable .customer-row');

        rows.forEach(row => {
            const phoneCell = row.querySelector('.customer-phone');
            const phone = phoneCell ? phoneCell.innerText.toLowerCase() : '';

            const isMatch = phone.includes(keyword);
            row.style.display = isMatch ? '' : 'none';
        });
    });
    function fillEditCustomerModalFromData(button) {
        document.getElementById('editCustomerId').value = button.dataset.id;
        document.getElementById('editFullName').value = button.dataset.fullname;
        document.getElementById('editPhone').value = button.dataset.phone;
        document.getElementById('editAddress').value = button.dataset.address;
        document.getElementById('editEmail').value = button.dataset.email;
        document.getElementById('editPassword').value = button.dataset.password;
    }
    function employeeUtils(customerId) {
        if (confirm("Are you sure you want to delete this customer?")) {
            fetch( '/ayxkix/customer', {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ customerId: customerId })
            })
                .then(response => {
                    if( response.ok) {
                        window.location.reload();
                    }
                    else {
                        throw new Error(response.statusText);
                    }

                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("An error occurred while deleting the employee.");
                });
        }
    }

</script>
</body>
</html>
