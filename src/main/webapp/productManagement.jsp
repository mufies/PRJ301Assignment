<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Product Management</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="icon" type="image/x-icon" href="favicon.ico">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carticon.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
  <script>    const CONTEXT_PATH = "${pageContext.request.contextPath}";
  </script>
  <script src="${pageContext.request.contextPath}/js/search.js"></script>
    <script src="${pageContext.request.contextPath}/js/admin/deleteProduct.js"></script>

  <script>
  window.onload = function (){
    document.getElementById("foodInput").addEventListener("keyup", function() {
      fetchResults(function(food) {
        openEditProductSection(food.id.toString(), food.name, food.price, food.type, food.img);
      });
    });
  }
  </script>
</head>
<body>
<div class="bg-white border-bottom">
  <div class="container-fluid px-0">
    <ul class="nav nav-pills py-2 px-3">
      <li class="nav-item"><a class="nav-link" href="/ayxkix"><i class="bi bi-speedometer2 me-1"></i>Dashboard</a></li>
      <li class="nav-item"><a class="nav-link active" href="/ayxkix/product"><i class="bi bi-box-seam me-1"></i>Product Management</a></li>
      <li class="nav-item"><a class="nav-link" href="/ayxkix/order"><i class="bi bi-cart-check me-1"></i>Order Management</a></li>
      <li class="nav-item"><a class="nav-link" href="/ayxkix/customer"><i class="bi bi-people me-1"></i>Customer Management</a></li>
      <li class="nav-item"><a class="nav-link" href="/ayxkix/employee"><i class="bi bi-person-badge me-1"></i>Employee Management</a></li>
      <li class="nav-item"><a class="nav-link" href="/ayxkix/other"><i class="bi bi-gear me-1"></i>Other</a></li>
    </ul>
  </div>
</div>

<div class="container mt-4">
  <h3>Quản lý sản phẩm</h3>
  <div id="inputContainer" style="margin-bottom: 10px" class="w-100">
    <input type="text" id="foodInput"  class="form-control search-box  "  placeholder="Search...">
    <div id="suggestionsPopup"></div>
  </div>
  <!-- Form thêm sản phẩm -->
  <form class="row g-3 mb-4" method="post" action="product">
    <div class="col-md-3">
      <input type="text" class="form-control" name="productName" placeholder="Tên món ăn" required>
    </div>
    <div class="col-md-2">
      <input type="number" class="form-control" name="price" placeholder="Giá" required>
    </div>
    <div class="col-md-2">
      <select class="form-select" id="editType" name="type" required>
        <option value="starter">Starter</option>
        <option value="meat">Meat</option>
        <option value="rice">Rice</option>
        <option value="hotpot">Hotpot</option>
        <option value="seafood">Seafood</option>
      </select>
    </div>
    <div class="col-md-3">
      <input type="text" class="form-control" name="image" placeholder="Link ảnh">
    </div>
    <div class="col-md-2">
      <button class="btn btn-success w-100" type="submit">Thêm món</button>
    </div>
  </form>
  <table class="table table-bordered align-middle">
    <thead>
    <tr>
      <th onclick="sortTable(0)">ID</th>
      <th onclick="sortTable(1)">Tên món</th>
      <th onclick="sortTable(2)">Giá</th>
      <th onclick="sortTable(3)">Loại</th>
      <th onclick="sortTable(4)">Ảnh</th>
      <th>Thao tác</th>
    </tr>
    </thead>
    <tbody id="productTableBody">
    <c:forEach var="p" items="${productList}">
      <tr>
        <td>${p.productId}</td>
        <td>${p.productName}</td>
        <td>${p.price}</td>
        <td>${p.type}</td>
        <td><img src="${p.image}" style="width:40px;height:40px"></td>
        <td>
          <button onclick="openEditProductSection('${p.productId}', '${p.productName}', '${p.price}', '${p.type}', '${p.image}')"
                  class="btn btn-sm btn-warning">Sửa</button>
          <button class="btn btn-sm btn-danger" onclick="deleteProduct(${p.productId})">Xóa</button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- Modal chỉnh sửa sản phẩm -->
<div class="modal fade" id="editProductSection" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Chỉnh sửa sản phẩm</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form id="editProductForm" method="post" action="product">
          <input type="hidden" id="editProductId" name="productId">
          <div class="mb-3">
            <label for="editProductName" class="form-label">Tên món ăn</label>
            <input type="text" class="form-control" id="editProductName" name="productName" required>
          </div>
          <div class="mb-3">
            <label for="editPrice" class="form-label">Giá</label>
            <input type="number" class="form-control" id="editPrice" name="price" required>
          </div>
          <div class="mb-3">
            <label for="editType" class="form-label">Loại</label>
            <select class="form-select" id="editType" name="type" required>
              <option value="starter">Starter</option>
              <option value="meat">Meat</option>
              <option value="rice">Rice</option>
              <option value="hotpot">Hotpot</option>
              <option value="seafood">Seafood</option>
            </select>
          </div>
          <div class="mb-3">
            <label for="editImage" class="form-label">Link ảnh</label>
            <input type="text" class="form-control" id="editImage" name="image">
          </div>
          <button type="submit" class="btn btn-primary">Cập nhật sản phẩm</button>
        </form>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function openEditProductSection(productId, productName, price, type, image) {
    document.getElementById('editProductId').value = productId;
    document.getElementById('editProductName').value = productName;
    document.getElementById('editPrice').value = price;
    const selectType = document.getElementById('editType');
    if (selectType.querySelector(`option[value="${type}"]`)) {
      selectType.value = type;
    } else {
      selectType.value = "starter";
    }
    document.getElementById('editImage').value = image;
    var modal = new bootstrap.Modal(document.getElementById('editProductSection'));
    modal.show();
  }
  let sortDirection = 1;

  function sortTable(colIndex) {
    const tbody = document.getElementById('productTableBody');
    const rows = Array.from(tbody.querySelectorAll('tr'));

    rows.sort((a, b) => {
      let aVal = a.cells[colIndex].textContent;
      let bVal = b.cells[colIndex].textContent;

      if (colIndex === 2) { // Giá
        aVal = parseFloat(aVal.replace(/[^\d.-]/g, '')) || 0;
        bVal = parseFloat(bVal.replace(/[^\d.-]/g, '')) || 0;
      }

      if (aVal < bVal) return -1 * sortDirection;
      if (aVal > bVal) return 1 * sortDirection;
      return 0;
    });

    sortDirection = -sortDirection;
    tbody.innerHTML = '';
    rows.forEach(row => tbody.appendChild(row));
  }


</script>

</body>
</html>
