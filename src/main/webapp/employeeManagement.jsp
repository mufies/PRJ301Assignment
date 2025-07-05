<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <style>
        #addNewEmployee{
            position: fixed;            /* phủ toàn màn hình */
            inset: 0;                   /* top:0; right:0; bottom:0; left:0 */
            background: rgba(0,0,0,.45);/* lớp mờ phía sau */
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1050;
        }

        /* Khi cần hiện => thêm class .show */
        #addNewEmployee.show{
            display: flex !important;   /* dùng flex để căn giữa */
        }

        /* Giới hạn bề rộng khung nội dung (tuỳ ý) */
        #addNewEmployee .modal-content{
            width: 100%;
            max-width: 900px;           /* hoặc 600/700 tuỳ bạn */
        }
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
            <li class="nav-item"><a class="nav-link " href="/ayxkix"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a></li>
            <li class="nav-item"><a class="nav-link " href="/ayxkix/product"><i class="bi bi-box-seam me-1"></i>Product Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
            <li class="nav-item"><a class="nav-link active" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
            <li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
        </ul>
    </div>
</div>
<div class="container mt-4">
    <h3>Quản lý nhân viên</h3>

    <!-- Tìm kiếm -->
    <div class="mb-3">
        <input type="text" id="searchEmployeeBox" class="form-control" placeholder="Tìm kiếm theo tên, số điện thoại hoặc email...">
    </div>

    <!-- Nút thêm -->
    <button id="toggleEmployeeForm" class="btn btn-primary mb-3" onclick="openAddNewEmployee()">
        + Thêm nhân viên
    </button>

    <!-- Bảng danh sách nhân viên -->
    <table class="table table-bordered align-middle">
        <thead class="table-light">
        <tr>
            <th>ID</th><th>Họ tên</th><th>Điện thoại</th><th>Email</th>
            <th>Chức vụ</th><th>Lương</th><th>Trạng thái</th><th>Thao tác</th>
        </tr>
        </thead>
        <tbody id="employeeTableBody">
        <c:forEach var="e" items="${empList}">
            <tr class="employee-row">
                <td>${e.employeeId}</td>
                <td class="employee-name">${e.fullName}</td>
                <td class="employee-phone">${e.phone}</td>
                <td class="employee-email">${e.email}</td>
                <td>${e.position}</td>
                <td><fmt:formatNumber value="${e.salary}"/> VND</td>
                <td>${e.status}</td>
                <td>
                    <button class="btn btn-warning btn-sm"
                            onclick='openEdit({
                                    employeeId: ${e.employeeId},
                                    username: "${e.username}",
                                    password: "${e.password}",
                                    fullName: "${e.fullName}",
                                    phone: "${e.phone}",
                                    email: "${e.email}",
                                    address: "${e.address}",
                                    position: "${e.position}",
                                    salary: ${e.salary}
                                    })'>Sửa</button>

                    <button class="btn btn-danger btn-sm" onclick="employeeUtils(${e.employeeId})">Xóa</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


<!-- Modal cập nhật nhân viên -->
<div class="modal fade" id="editEmployeeModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg"> <!-- modal-lg cho rộng hơn -->
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chỉnh sửa thông tin nhân viên</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <form id="updateEmployeeForm" onsubmit="return false;">
                    <input type="hidden" id="editEmployeeId">

                    <!-- Username -->
                    <div class="mb-3">
                        <label for="editUsername" class="form-label">Username</label>
                        <input type="text" class="form-control" id="editUsername" name="username" required>
                    </div>

                    <!-- Password -->
                    <div class="mb-3">
                        <label for="editPassword" class="form-label">Mật khẩu</label>
                        <input type="password" class="form-control" id="editPassword" name="password" required>
                    </div>

                    <!-- Họ tên -->
                    <div class="mb-3">
                        <label for="editFullName" class="form-label">Họ tên</label>
                        <input type="text" class="form-control" id="editFullName" name="fullName" required>
                    </div>

                    <!-- Điện thoại -->
                    <div class="mb-3">
                        <label for="editPhone" class="form-label">Số điện thoại</label>
                        <input type="tel" class="form-control" id="editPhone" name="phone" pattern="0\d{9}" required>
                    </div>

                    <!-- Email -->
                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email</label>
                        <input type="email" class="form-control" id="editEmail" name="email" required>
                    </div>

                    <!-- Địa chỉ -->
                    <div class="mb-3">
                        <label for="editAddress" class="form-label">Địa chỉ</label>
                        <input type="text" class="form-control" id="editAddress" name="address">
                    </div>

                    <!-- Chức vụ -->
                    <div class="mb-3">
                        <label for="editPosition" class="form-label">Chức vụ</label>
                        <input type="text" class="form-control" id="editPosition" name="position">
                    </div>

                    <!-- Lương -->
                    <div class="mb-3">
                        <label for="editSalary" class="form-label">Lương (VNĐ)</label>
                        <input type="number" class="form-control" id="editSalary" name="salary" min="0" step="1000">
                    </div>

                    <button type="button" class="btn btn-primary" onclick="updateEmployee()">Cập nhật nhân viên</button>
                </form>
            </div>
        </div>
    </div>
</div>

<div id="addNewEmployee"  style="display: none;">
    <div class="modal-content p-4 justify-content-center bg-white border rounded shadow-sm">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="mb-0">Thêm</h4>
            <button type="button" class="btn-close" onclick="closeAddNewEmployee()"></button>
        </div>
        <form id="employeeForm"
              action="employee"
              method="post"
              class="row gy-2   gx-3 align-items-center mb-4">

            <!-- Username -->
            <div class="col-md-3">
                <label for="username" class="form-label fw-semibold small mb-0">Username</label>
                <input type="text" id="username" name="username"
                       class="form-control" placeholder="username" required>
            </div>

            <!-- Mật khẩu -->
            <div class="col-md-3">
                <label for="password" class="form-label fw-semibold small mb-0">Mật khẩu</label>
                <input type="password" id="password" name="password"
                       class="form-control" placeholder="•••••••"
                       minlength="3" required>
            </div>

            <!-- Họ tên -->
            <div class="col-md-4">
                <label for="fullName" class="form-label fw-semibold small mb-0">Họ tên</label>
                <input type="text" id="fullName" name="fullName"
                       class="form-control" placeholder="full name" required>
            </div>

            <!-- Điện thoại -->
            <div class="col-md-2">
                <label for="phone" class="form-label fw-semibold small mb-0">Điện thoại</label>
                <input type="tel" id="phone" name="phone"
                       class="form-control" placeholder="0909000001"
                       pattern="0\d{9}" required>
            </div>

            <!-- Email -->
            <div class="col-md-4">
                <label for="email" class="form-label fw-semibold small mb-0">Email</label>
                <input type="email" id="email" name="email"
                       class="form-control" placeholder="...@example.com" required>
            </div>

            <!-- Địa chỉ -->
            <div class="col-md-6">
                <label for="address" class="form-label fw-semibold small mb-0">Địa chỉ</label>
                <input type="text" id="address" name="address"
                       class="form-control" placeholder="Số nhà, đường, phường, quận, thành phố">
            </div>

            <!-- Ngày vào làm -->
            <div class="col-md-3">
                <label for="hireDate" class="form-label fw-semibold small mb-0">Ngày vào làm</label>
                <input type="date" id="hireDate" name="hireDate"
                       class="form-control">
            </div>

            <!-- Chức vụ -->
            <div class="col-md-3">
                <label for="position" class="form-label fw-semibold small mb-0">Chức vụ</label>
                <input type="text" id="position" name="position"
                       class="form-control" placeholder="Nhân viên">
            </div>

            <!-- Lương -->
            <div class="col-md-3">
                <label for="salary" class="form-label fw-semibold small mb-0">Lương (VNĐ)</label>
                <input type="number" id="salary" name="salary"
                       class="form-control" min="0" step="1000"
                       placeholder="5000000">
            </div>

            <!-- Nút gửi -->
            <div class="col-md-2 d-grid">
                <button type="submit" class="btn btn-success">
                    <i class="bi bi-person-plus-fill me-1"></i>
                    Thêm
                </button>
            </div>
        </form>
    </div>
</div>


<div id="editEmployee" style="display: none;">
    <form id="updateEmployeeForm" onsubmit="return false;" class="row gy-2 gx-3 align-items-center mb-4">
        <input type="hidden" id="editEmployeeId">

        <!-- Các input y như cũ -->
        <div class="col-md-3">
            <label for="editUsername" class="form-label">Username</label>
            <input type="text" id="editUsername" name="username" class="form-control" required>
        </div>
        <div class="col-md-3">
            <label for="editPassword" class="form-label">Mật khẩu</label>
            <input type="password" id="editPassword" name="password" class="form-control" required>
        </div>
        <div class="col-md-4">
            <label for="editFullName" class="form-label">Họ tên</label>
            <input type="text" id="editFullName" name="fullName" class="form-control" required>
        </div>
        <div class="col-md-3">
            <label for="editPhone" class="form-label">Điện thoại</label>
            <input type="tel" id="editPhone" name="phone" class="form-control" pattern="0\d{9}" required>
        </div>
        <div class="col-md-4">
            <label for="editEmail" class="form-label">Email</label>
            <input type="email" id="editEmail" name="email" class="form-control" required>
        </div>
        <div class="col-md-6">
            <label for="editAddress" class="form-label">Địa chỉ</label>
            <input type="text" id="editAddress" name="address" class="form-control">
        </div>
        <div class="col-md-3">
            <label for="editPosition" class="form-label">Chức vụ</label>
            <input type="text" id="editPosition" name="position" class="form-control">
        </div>
        <div class="col-md-3">
            <label for="editSalary" class="form-label">Lương (VNĐ)</label>
            <input type="number" id="editSalary" name="salary" class="form-control" min="0" step="1000">
        </div>

        <!-- Nút cập nhật -->
        <div class="col-md-2 d-grid">
            <button type="button" class="btn btn-success" onclick="updateEmployee()">
                <i class="bi bi-save2 me-1"></i> Cập nhật
            </button>
        </div>
    </form>
</div>

<script>
    function openAddNewEmployee() {
        document.getElementById('addNewEmployee').classList.add('show');
    }
    function closeAddNewEmployee() {
        document.getElementById('addNewEmployee').classList.remove('show');
    }

    function openEdit(employeeId, fullName, phone, email, dob, role, salary, status, address, description) {
        document.getElementById('fullName').value = fullName;
        document.getElementById('phone').value = phone;
        document.getElementById('email').value = email;
        document.getElementById('address').value = address;
        document.getElementById('salary').value = salary;

        // Show the edit form
        document.getElementById('editEmployee').style.display = 'block';
    }

    document.getElementById('searchEmployeeBox').addEventListener('input', function () {
        const keyword = this.value.trim().toLowerCase();
        const rows = document.querySelectorAll('.employee-row');

        rows.forEach(row => {
            const name = row.querySelector('.employee-name')?.innerText.toLowerCase() || '';
            const phone = row.querySelector('.employee-phone')?.innerText.toLowerCase() || '';
            const email = row.querySelector('.employee-email')?.innerText.toLowerCase() || '';
            const matched = name.includes(keyword) || phone.includes(keyword) || email.includes(keyword);
            row.style.display = matched ? '' : 'none';
        });
    });

    function openEdit(employee) {
        document.getElementById('editEmployeeId').value = employee.employeeId;
        document.getElementById('editUsername').value = employee.username;
        document.getElementById('editPassword').value = employee.password;
        document.getElementById('editFullName').value = employee.fullName;
        document.getElementById('editPhone').value = employee.phone;
        document.getElementById('editEmail').value = employee.email;
        document.getElementById('editAddress').value = employee.address;
        document.getElementById('editPosition').value = employee.position;
        document.getElementById('editSalary').value = employee.salary;

        // Show Bootstrap modal
        const modal = new bootstrap.Modal(document.getElementById('editEmployeeModal'));
        modal.show();
    }




</script>
<script src="${pageContext.request.contextPath}/js/admin/employeeUtils.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
