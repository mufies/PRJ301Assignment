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

    <!-- FORM THÊM -->
    <!-- Form thông tin nhân viên | Bootstrap 5 -->



    <!-- TABLE LIST -->
    <button id="toggleEmployeeForm" class="btn btn-primary mb-3" onclick="openAddNewEmployee()">
        + Thêm nhân viên
    </button>    <table class="table table-bordered align-middle">
        <thead class="table-light">
        <tr>
            <th>ID</th><th>Họ tên</th><th>Điện thoại</th><th>Email</th>
            <th>Chức vụ</th><th>Lương</th><th>Trạng thái</th><th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${empList}">
            <tr>
                <td>${e.employeeId}</td>
                <td>${e.fullName}</td>
                <td>${e.phone}</td>
                <td>${e.email}</td>
                <td>${e.position}</td>
                <td><fmt:formatNumber value="${e.salary}"/> VND</td>
                <td>${e.status}</td>
                <td>
                    <button class="btn btn-warning btn-sm"
<%--                            onclick="openEdit(${e.employeeId},'${e.fullName}','${e.phone}',--%>
<%--                                    '${e.email}','${e.dob}','${e.role}',--%>
<%--                                ${e.salary},'${e.status}','${e.address}','${e.description}')">--%>
                        >Sửa
                    </button>
                    <button class="btn btn-danger btn-sm"
                            onclick="deleteEmployee(${e.employeeId})"
                    >Xóa</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div id="addNewEmployee"  style="display: none;">
    <div class="modal-content p-4 justify-content-center bg-white border rounded shadow-sm">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="mb-0">Thêm / Cập nhật nhân viên</h4>
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
                Thêm / Cập nhật
            </button>
        </div>
    </form>
    </div>
</div>

<%--<div id="editEmployee">--%>
<%--    <!-- Mật khẩu -->--%>
<%--    <div class="col-md-3">--%>
<%--        <label for="password" class="form-label fw-semibold small mb-0">Mật khẩu</label>--%>
<%--        <input type="password" id="password" name="password"--%>
<%--               class="form-control" placeholder="•••••••"--%>
<%--               minlength="3" required>--%>
<%--    </div>--%>

<%--    <!-- Họ tên -->--%>
<%--    <div class="col-md-4">--%>
<%--        <label for="fullName" class="form-label fw-semibold small mb-0">Họ tên</label>--%>
<%--        <input type="text" id="fullName" name="fullName"--%>
<%--               class="form-control" placeholder="full name" required>--%>
<%--    </div>--%>

<%--    <!-- Điện thoại -->--%>
<%--    <div class="col-md-2">--%>
<%--        <label for="phone" class="form-label fw-semibold small mb-0">Điện thoại</label>--%>
<%--        <input type="tel" id="phone" name="phone"--%>
<%--               class="form-control" placeholder="0909000001"--%>
<%--               pattern="0\d{9}" required>--%>
<%--    </div>--%>

<%--    <!-- Email -->--%>
<%--    <div class="col-md-4">--%>
<%--        <label for="email" class="form-label fw-semibold small mb-0">Email</label>--%>
<%--        <input type="email" id="email" name="email"--%>
<%--               class="form-control" placeholder="...@example.com" required>--%>
<%--    </div>--%>

<%--    <!-- Địa chỉ -->--%>
<%--    <div class="col-md-6">--%>
<%--        <label for="address" class="form-label fw-semibold small mb-0">Địa chỉ</label>--%>
<%--        <input type="text" id="address" name="address"--%>
<%--               class="form-control" placeholder="Số nhà, đường, phường, quận, thành phố">--%>
<%--    </div>--%>

<%--    <!-- Ngày vào làm -->--%>
<%--    --%>
<%--    <!-- Lương -->--%>
<%--    <div class="col-md-3">--%>
<%--        <label for="salary" class="form-label fw-semibold small mb-0">Lương (VNĐ)</label>--%>
<%--        <input type="number" id="salary" name="salary"--%>
<%--               class="form-control" min="0" step="1000"--%>
<%--               placeholder="5000000">--%>
<%--    </div>--%>

<%--    <!-- Nút gửi -->--%>
<%--    <div class="col-md-2 d-grid">--%>
<%--        <button type="submit" class="btn btn-success">--%>
<%--            <i class="bi bi-person-plus-fill me-1"></i>--%>
<%--            Thêm / Cập nhật--%>
<%--        </button>--%>
<%--    </div>--%>
<%--    </form>--%>
<%--</div>--%>
<script>
    function openAddNewEmployee() {
        document.getElementById('addNewEmployee').classList.add('show');
    }
    function closeAddNewEmployee() {
        document.getElementById('addNewEmployee').classList.remove('show');
    }


</script>
<script src="${pageContext.request.contextPath}/js/admin/deleteEmployee.js"></script>
</body>
</html>
