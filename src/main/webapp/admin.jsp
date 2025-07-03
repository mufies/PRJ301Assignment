<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <link rel="stylesheet" href="css/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

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
<li class="nav-item"><a class="nav-link active" href="/ayxkix"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a></li>
<li class="nav-item"><a class="nav-link" href="/ayxkix/product"><i class="bi bi-box-seam me-1"></i>Product Management</a></li>
<li class="nav-item"><a class="nav-link" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
<li class="nav-item"><a class="nav-link" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
<li class="nav-item"><a class="nav-link" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
<li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
        </ul>
    </div>
</div>
<!-- Dashboard Cards -->
<div class="container-fluid my-4">
    <div class="row g-3">
        <div class="col-md-6">
            <div class="dashboard-card orders d-flex align-items-center justify-content-center">
                <div class="icon"><i class="bi bi-cart"></i></div>
                <div>
                    <div class="label">Total Orders</div>
                    <div class="value" id="totalOrders"><c:out value="${curWeekOrd}" default="0"/></div>
                    <div class="change ${curWeekOrd - preWeekOrd >= 0 ? 'text-success' : 'text-danger'}">
                        <c:choose>
                            <c:when test="${preWeekOrd == 0}">
                                N/A
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber type="number" maxFractionDigits="1" value="${((curWeekOrd - preWeekOrd) / preWeekOrd) * 100}"/>%
                            </c:otherwise>
                        </c:choose>
                        from last week
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="dashboard-card revenue d-flex align-items-center justify-content-center">
                <div class="icon"><i class="bi bi-cash-stack"></i></div>
                <div>
                    <div>
                        <div class="label">Total Revenue</div>
                        <div class="value" id="totalOrders"><c:out value="${curWeekRevenue}" default="0"/></div>
                        <div class="change ${curWeekRevenue - preWeekRevenue >= 0 ? 'text-success' : 'text-danger'}">
                            <c:choose>
                                <c:when test="${preWeekRevenue == 0}">
                                    N/A
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber type="number" maxFractionDigits="1" value="${((curWeekRevenue - preWeekRevenue) / preWeekRevenue) * 100}"/>%
                                </c:otherwise>
                            </c:choose>
                            from last week
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <!-- Charts -->
    <div class="row g-3 mt-2">
        <div class="col-lg-8">
            <div class="chart-container mb-3">
                <div class="fw-semibold mb-2">Sales Overview</div>
                <canvas id="saleChart" height="120"></canvas>

            </div>
        </div>
        <div class="col-lg-4">
            <div class="chart-container mb-3">
                <div class="fw-semibold mb-2">Trending Products</div>
                <canvas id="myDoughnutChart" height="200"></canvas>

            </div>
        </div>
    </div>
    <!-- Table Recent Orders (optional, bạn có thể bổ sung data sau) -->
    <div class="row mt-3">
        <div class="col-12">
            <div class="card shadow-sm">
                <div class="card-header fw-semibold">Most Products Ordered on this week</div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead>
                            <tr>
                                <th>Product ID</th>
                                <th>Product Name</th>
                                <th>Image</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${mostRecentProductOrdered}" var="order">
                                <tr>
                                    <td>${order.productId}</td>
                                    <td>${order.productName}</td>
                                    <td><img src="${order.image}" alt="Product Image" class="img-thumbnail" style="width: 50px; height: 50px;"></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/chart/chartData.js"></script>
<script src="js/chart/chartConfig.js"></script>
</body>
</html>
