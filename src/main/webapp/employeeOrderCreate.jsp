<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Tạo đơn hàng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carticon.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
    <link rel="stylesheet" href="css/menu1.css">
    <link rel="stylesheet" href="css/login.css">
    <style>
        .cart-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .cart-table th, .cart-table td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }
        .cart-item-img {
            max-width: 60px;
            max-height: 60px;
        }
        .order-summary {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
        }
        .tab-content {
            padding: 20px 0;
        }
        #productList {
            max-height: 400px;
            overflow-y: auto;
        }
        .product-card {
            cursor: pointer;
            transition: all 0.3s;
            border-radius: 5px;
            padding: 10px;
            margin-bottom: 10px;
        }
        .product-card:hover {
            background-color: #f1f1f1;
            transform: translateY(-2px);
        }
        .customer-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            padding: 0;
            background: #FFF0DD;
            color: #333;
        }

        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 40px;
            background-color: #FFF0DD;
        }

        .logo img {
            height: 100px;
        }

        nav a {
            margin: 0 15px;
            text-decoration: none;
            color: #9B3F3F;
            font-weight: bold;
        }

        .login-btn {
            background-color: #9B3F3F;
            color: #FFF0DD;
            padding: 8px 16px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

    </style>
    <script>const CONTEXT_PATH = "${pageContext.request.contextPath}";</script>
    <script src="${pageContext.request.contextPath}/js/search.js"></script>
</head>
<script>
    function logout()
    {
        localStorage.removeItem('jwt');
        window.location.href = 'home';
    }
    const token = localStorage.getItem('jwt');
    if (!isJwtValid(token)) {
        window.location.replace('<%=request.getContextPath()%>/menu');
    } else {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            if (payload.role !== 'Employee') {
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
<body>
<header>
    <div class="logo">
        <a href="updateOrderStatus"> <img src="images/logo.png" alt="Mam Mam Logo"></a>
    </div>
    <nav>
        <a href="updateOrderStatus">Dashboard</a>
        <a href="employeeCheckingOrder">Create Order</a>
        <a href="updateEmployee">Update Info</a>
        <a href="#" onclick="logout()">Logout</a>


    </nav>
</header>
<div class="container mt-4">
    <h2>Tạo đơn hàng mới</h2>

    <!-- Customer information form -->
    <div class="customer-info">
        <h4>Thông tin khách hàng</h4>
        <div class="mb-3">
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="customerType" id="registeredCustomer" value="registered" checked>
                <label class="form-check-label" for="registeredCustomer">Khách hàng đã đăng ký</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="customerType" id="guestCustomer" value="guest">
                <label class="form-check-label" for="guestCustomer">Khách vãng lai</label>
            </div>
        </div>

        <!-- Registered customer section -->
        <div id="registeredCustomerSection">
            <div class="mb-3">
                <label for="username" class="form-label">Tên đăng nhập</label>
                <input type="text" class="form-control" id="username" placeholder="Nhập tên đăng nhập">
                <div class="form-text">Nhập tên đăng nhập của khách hàng đã đăng ký</div>
            </div>
        </div>

        <!-- Guest customer section -->
        <div id="guestCustomerSection" style="display: none;">
            <div class="mb-3">
                <label for="customerName" class="form-label">Họ và tên</label>
                <input type="text" class="form-control" id="customerName" placeholder="Nhập họ tên khách hàng">
            </div>
            <div class="mb-3">
                <label for="customerPhone" class="form-label">Số điện thoại</label>
                <input type="tel" class="form-control" id="customerPhone" placeholder="Nhập số điện thoại">
            </div>
            <div class="mb-3">
                <label for="customerAddress" class="form-label">Địa chỉ giao hàng</label>
                <textarea class="form-control" id="customerAddress" rows="2" placeholder="Nhập địa chỉ giao hàng"></textarea>
            </div>
        </div>

        <div class="mb-3">
            <label for="orderNote" class="form-label">Ghi chú đơn hàng</label>
            <textarea class="form-control" id="orderNote" rows="2" placeholder="Ghi chú cho đơn hàng"></textarea>
        </div>
    </div>

    <div class="row">
        <!-- Product selection section -->
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h4>Chọn món ăn</h4>
                    <input type="text" id="foodInput" class="form-control search-box" placeholder="Tìm kiếm món ăn...">
                    <div id="suggestionsPopup"></div>
                </div>
                <div class="card-body">
                    <ul class="nav nav-tabs" id="productTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="all-tab" data-bs-toggle="tab" data-bs-target="#all" type="button" role="tab">Tất cả</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="starter-tab" data-bs-toggle="tab" data-bs-target="#starter" type="button" role="tab">Starter</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="meat-tab" data-bs-toggle="tab" data-bs-target="#meat" type="button" role="tab">Meat</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="rice-tab" data-bs-toggle="tab" data-bs-target="#rice" type="button" role="tab">Rice</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="hotpot-tab" data-bs-toggle="tab" data-bs-target="#hotpot" type="button" role="tab">Hotpot</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="seafood-tab" data-bs-toggle="tab" data-bs-target="#seafood" type="button" role="tab">Seafood</button>
                        </li>
                    </ul>
                    <div class="tab-content" id="productTabsContent">
                        <div class="tab-pane fade show active" id="all" role="tabpanel">
                            <div id="productList" class="row row-cols-1 row-cols-md-2 g-3">
                                <c:forEach var="p" items="${productList}">
                                    <div class="col">
                                        <div class="product-card" onclick="addToCart('${p.productId}', '${p.productName}', '${p.price}', '${p.image}')">
                                            <div class="d-flex align-items-center">
                                                <img src="${p.image}" alt="${p.productName}" class="me-2" style="width:50px;height:50px;object-fit:cover;">
                                                <div>
                                                    <h6 class="mb-0">${p.productName}</h6>
                                                    <small>${p.price} VND</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <c:forEach var="type" items="starter,meat,rice,hotpot,seafood">
                            <div class="tab-pane fade" id="${type}" role="tabpanel">
                                <div id="${type}List" class="row row-cols-1 row-cols-md-2 g-3">
                                    <c:forEach var="p" items="${productList}">
                                        <c:if test="${p.type eq type}">
                                            <div class="col">
                                                <div class="product-card" onclick="addToCart('${p.productId}', '${p.productName}', '${p.price}', '${p.image}')">
                                                    <div class="d-flex align-items-center">
                                                        <img src="${p.image}" alt="${p.productName}" class="me-2" style="width:50px;height:50px;object-fit:cover;">
                                                        <div>
                                                            <h6 class="mb-0">${p.productName}</h6>
                                                            <small>${p.price} VND</small>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order summary section -->
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h4>Giỏ hàng</h4>
                </div>
                <div class="card-body">
                    <div id="cartItemsList">
                        <table class="cart-table">
                            <thead>
                                <tr>
                                    <th>Ảnh</th>
                                    <th>Tên món</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                    <th>Xóa</th>
                                </tr>
                            </thead>
                            <tbody id="cartTableBody">
                                <!-- Cart items will be added here -->
                            </tbody>
                        </table>
                    </div>

                    <div class="order-summary">
                        <h5>Tổng đơn hàng</h5>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Tổng số món:</span>
                            <span id="totalItems">0</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Giá trị đơn hàng:</span>
                            <span id="subtotal">0 VND</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Phí vận chuyển:</span>
                            <span id="shippingFee">15,000 VND</span>
                        </div>
                        <div class="d-flex justify-content-between fw-bold mt-2">
                            <span>Tổng cộng:</span>
                            <span id="totalAmount">15,000 VND</span>
                        </div>
                        <button id="submitOrderBtn" class="btn btn-primary w-100 mt-3">Xác nhận đơn hàng</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Order Success Modal -->
<div class="modal fade" id="successModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Đặt hàng thành công!</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>Mã vận đơn: <strong id="shippingCode"></strong></p>
                <h6>Danh sách món đã đặt:</h6>
                <ul id="orderedItemsList"></ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/employeeCreateCart.js">

</script>
</body>
</html>