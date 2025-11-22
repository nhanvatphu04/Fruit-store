<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - FruitStore</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <style>
        .checkout-container {
            background: #f8f9fa;
            padding: 30px 0;
            min-height: 100vh;
        }
        .order-summary {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .order-item {
            border-bottom: 1px solid #eee;
            padding: 15px 0;
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .item-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }
        .combo-image {
            width: 80px;
            height: 80px;
            background: #e9ecef;
            border-radius: 4px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #6c757d;
        }
        .total-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
        }
        .total-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            font-size: 16px;
        }
        .total-row.final {
            border-top: 2px solid #dee2e6;
            padding-top: 10px;
            font-size: 20px;
            font-weight: bold;
            color: #28a745;
        }
        .discount-badge {
            background: #28a745;
            color: white;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
            margin-left: 10px;
        }
        .confirm-btn {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            font-weight: bold;
            margin-top: 20px;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Header -->
    <%@ include file="common/header.jsp" %>

    <div class="checkout-container">
        <div class="container">
            <div class="row">
                <div class="col-lg-8">
                    <div class="order-summary">
                        <h3 class="mb-4">
                            <i class="fas fa-receipt me-2"></i>Xác nhận đơn hàng
                        </h3>

                        <!-- Thông tin người dùng -->
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <i class="fas fa-user me-2"></i>Thông tin giao hàng
                            </div>
                            <div class="card-body">
                                <p class="mb-2"><strong>Tên:</strong> ${user.fullName}</p>
                                <p class="mb-2"><strong>Email:</strong> ${user.email}</p>
                                <p class="mb-2"><strong>Số điện thoại:</strong> ${user.phone}</p>

                                <div class="mt-3">
                                    <label class="form-label">Địa chỉ giao hàng</label>
                                    <textarea class="form-control" id="shippingAddress"
                                              name="shippingAddress" rows="2"
                                              placeholder="Nhập địa chỉ giao hàng">${shippingAddress}</textarea>
                                    <small class="text-muted">
                                        Địa chỉ mặc định lấy từ tài khoản của bạn. Bạn có thể chỉnh sửa cho đơn hàng này.
                                    </small>
                                </div>
                            </div>
                        </div>

                        <!-- Danh sách sản phẩm -->
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                <i class="fas fa-box me-2"></i>Sản phẩm
                            </div>
                            <div class="card-body">
                                <c:forEach var="item" items="${selectedItems}">
                                    <div class="order-item">
                                        <div class="row">
                                            <div class="col-md-2">
                                                <img src="${pageContext.request.contextPath}/${item.product.imageUrl}" 
                                                     alt="${item.product.name}" class="item-image">
                                            </div>
                                            <div class="col-md-6">
                                                <h6>${item.product.name}</h6>
                                                <small class="text-muted">Số lượng: ${item.quantity}</small>
                                            </div>
                                            <div class="col-md-4 text-end">
                                                <p class="mb-0">
                                                    <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₫"/>
                                                    <br>
                                                    <strong>
                                                        <fmt:formatNumber value="${item.product.price.multiply(item.quantity)}" type="currency" currencySymbol="₫"/>
                                                    </strong>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <!-- Danh sách combo -->
                                <c:forEach var="combo" items="${selectedCombos}">
                                    <div class="order-item">
                                        <div class="row">
                                            <div class="col-md-2">
                                                <div class="combo-image">
                                                    <i class="fas fa-gift fa-2x"></i>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <h6>${combo.combo.name}</h6>
                                                <small class="text-muted">Số lượng: ${combo.quantity}</small>
                                            </div>
                                            <div class="col-md-4 text-end">
                                                <p class="mb-0">
                                                    <fmt:formatNumber value="${combo.combo.salePrice}" type="currency" currencySymbol="₫"/>
                                                    <br>
                                                    <strong>
                                                        <fmt:formatNumber value="${combo.combo.salePrice.multiply(combo.quantity)}" type="currency" currencySymbol="₫"/>
                                                    </strong>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tóm tắt đơn hàng -->
                <div class="col-lg-4">
                    <div class="order-summary">
                        <h4 class="mb-4">
                            <i class="fas fa-calculator me-2"></i>Tóm tắt đơn hàng
                        </h4>

                        <div class="total-section">
                            <div class="total-row">
                                <span>Tạm tính:</span>
                                <span><fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="₫"/></span>
                            </div>

                            <c:if test="${discount > 0}">
                                <div class="total-row">
                                    <span>
                                        Giảm giá
                                        <c:if test="${not empty discountCode}">
                                            <span class="discount-badge">${discountCode}</span>
                                        </c:if>
                                    </span>
                                    <span class="text-success">
                                        -<fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫"/>
                                    </span>
                                </div>
                            </c:if>

                            <div class="total-row final">
                                <span>Tổng cộng:</span>
                                <span><fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/></span>
                            </div>
                        </div>

                        <!-- Nút xác nhận -->
                        <form method="POST" action="${pageContext.request.contextPath}/checkout">
                            <button type="submit" class="btn btn-success confirm-btn">
                                <i class="fas fa-check me-2"></i>Xác nhận đơn hàng
                            </button>
                        </form>

                        <!-- Nút quay lại -->
                        <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary w-100 mt-2">
                            <i class="fas fa-arrow-left me-2"></i>Quay lại giỏ hàng
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <%@ include file="common/footer.jsp" %>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</body>
</html>

