<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - FruitStore</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        .cart-item {
            border-radius: 10px;
            background-color: #fff;
            margin-bottom: 15px;
            padding: 15px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .product-img {
            max-width: 100px;
            height: auto;
            object-fit: cover;
        }
        .quantity-input {
            width: 70px;
            text-align: center;
        }
        .quantity-display {
            width: 70px;
            text-align: center;
            background-color: #f8f9fa !important;
            border: 1px solid #dee2e6 !important;
            color: #6c757d;
            font-weight: 500;
        }
        .btn-update {
            background-color: #28a745;
            color: white;
        }
        .btn-remove {
            background-color: #dc3545;
            color: white;
        }
        .discount-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }
        .total-section {
            font-size: 1.2em;
            font-weight: bold;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Header -->
    <%@ include file="common/header.jsp" %>

    <div class="container my-5">
        <h2 class="mb-4">Giỏ hàng của bạn</h2>
        
        <div class="row">
            <!-- Giỏ hàng -->
            <div class="col-lg-8">
                <c:if test="${empty cartItems and empty cartCombos}">
                    <div class="alert alert-info">
                        Giỏ hàng của bạn đang trống!
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary ms-3">
                            Tiếp tục mua sắm
                        </a>
                    </div>
                </c:if>

                <!-- Hiển thị sản phẩm thường -->
                <c:if test="${not empty cartItems}">
                    <h4 class="mb-3">Sản phẩm</h4>
                    <c:forEach items="${cartItems}" var="item">
                    <div class="cart-item">
                        <div class="row align-items-center">
                            <!-- Checkbox -->
                            <div class="col-1">
                                <input type="checkbox" class="form-check-input select-item" 
                                       value="${item.cartId}" ${item.selected ? 'checked' : ''}>
                            </div>
                            <!-- Ảnh sản phẩm -->
                            <div class="col-2">
                                <img src="${item.product.imageUrl}" 
                                     alt="${item.product.name}" class="product-img">
                            </div>
                            <!-- Thông tin sản phẩm -->
                            <div class="col-4">
                                <h5>${item.product.name}</h5>
                                <c:choose>
                                    <c:when test="${item.product.discountPercent > 0}">
                                        <p class="mb-1">
                                            <span class="text-muted text-decoration-line-through">
                                                <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₫"/>
                                            </span>
                                        </p>
                                        <p class="text-success mb-0">
                                            <fmt:formatNumber value="${item.discountedPrice}" type="currency" currencySymbol="₫"/>
                                            <span class="badge bg-danger ms-2">-${item.product.discountPercent}%</span>
                                        </p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-success mb-0">
                                            <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₫"/>
                                        </p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <!-- Số lượng -->
                            <div class="col-3">
                                <div class="input-group">
                                    <span class="form-control quantity-display text-center" 
                                          style="background-color: #f8f9fa; border: 1px solid #dee2e6;">
                                        ${item.quantity}
                                    </span>
                                </div>
                            </div>
                            <!-- Nút xoá -->
                            <div class="col-2">
                                <button class="btn btn-remove remove-item" data-cart-id="${item.cartId}">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                </c:if>

                <!-- Hiển thị combos -->
                <c:if test="${not empty cartCombos}">
                    <h4 class="mt-4 mb-3">Combo</h4>
                    <c:forEach items="${cartCombos}" var="combo">
                        <div class="cart-item">
                            <div class="row align-items-center">
                                <!-- Checkbox -->
                                <div class="col-1">
                                    <input type="checkbox" class="form-check-input select-combo" 
                                           value="${combo.cartComboId}" ${combo.selected ? 'checked' : ''}>
                                </div>
                                <!-- Ảnh combo -->
                                <div class="col-2">
                                    <img src="${combo.combo.imageUrl}" 
                                         alt="${combo.combo.name}" class="product-img">
                                </div>
                                <!-- Thông tin combo -->
                                <div class="col-4">
                                    <h5>${combo.combo.name}</h5>
                                    <p class="mb-1">
                                        <span class="text-muted text-decoration-line-through">
                                            <fmt:formatNumber value="${combo.combo.originalPrice}" type="currency" currencySymbol="₫"/>
                                        </span>
                                    </p>
                                    <p class="text-success mb-0">
                                        <fmt:formatNumber value="${combo.combo.salePrice}" type="currency" currencySymbol="₫"/>
                                        <span class="badge bg-danger ms-2">-${combo.combo.discountPercentage}%</span>
                                    </p>
                                </div>
                                <!-- Số lượng -->
                                <div class="col-3">
                                    <div class="input-group">
                                        <span class="form-control quantity-display text-center">
                                            ${combo.quantity}
                                        </span>
                                    </div>
                                </div>
                                <!-- Nút xoá -->
                                <div class="col-2">
                                    <button class="btn btn-remove remove-combo" data-cart-combo-id="${combo.cartComboId}">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
            </div>

            <!-- Tổng tiền và thanh toán -->
            <div class="col-lg-4">
                <!-- Mã giảm giá -->
                <div class="discount-section mb-4">
                    <h5>Mã giảm giá</h5>
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" id="discountCode" 
                               placeholder="Nhập mã giảm giá">
                        <button class="btn btn-success" id="applyDiscount">Áp dụng</button>
                    </div>
                    <div id="discountMessage"></div>
                </div>

                <!-- Tổng tiền -->
                <div class="card">
                    <div class="card-body">
                        <div class="total-section">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Tạm tính:</span>
                                <span id="subtotal">
                                    <fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="₫"/>
                                </span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Giảm giá:</span>
                                <span id="discount" class="text-success">
                                    -<fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫"/>
                                </span>
                            </div>
                            <div class="d-flex justify-content-between mb-3">
                                <span>Tổng cộng:</span>
                                <span id="total" class="text-primary">
                                    <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                                </span>
                            </div>
                            <button class="btn btn-primary w-100" id="checkoutBtn">
                                Thanh toán
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Include Footer -->
    <jsp:include page="common/footer.jsp" />

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        $(document).ready(function() {
            // Quantity update functionality has been disabled

            // Xoá combo
            $('.remove-combo').click(function() {
                let cartComboId = $(this).data('cart-combo-id');
                Swal.fire({
                    title: 'Xác nhận xoá?',
                    text: "Bạn có chắc muốn xoá combo này khỏi giỏ hàng?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Xoá',
                    cancelButtonText: 'Huỷ'
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            url: '${pageContext.request.contextPath}/cart/remove-combo',
                            method: 'POST',
                            data: { cartComboId: cartComboId },
                            success: function(response) {
                                if(response.success) {
                                    // Update cart count before reloading
                                    if (typeof updateCartCountBadge === 'function') {
                                        updateCartCountBadge();
                                    }
                                    location.reload();
                                } else {
                                    Swal.fire('Lỗi', 'Không thể xoá combo', 'error');
                                }
                            }
                        });
                    }
                });
            });

            // Chọn combo để thanh toán
            $('.select-combo').change(function() {
                let cartComboId = $(this).val();
                let selected = $(this).prop('checked');
                $.ajax({
                    url: '${pageContext.request.contextPath}/cart/select-combo',
                    method: 'POST',
                    data: {
                        cartComboId: cartComboId,
                        selected: selected
                    },
                    success: function(response) {
                        if(response.success) {
                            updateTotals();
                        }
                    }
                });
            });

            // Xoá sản phẩm
            $('.remove-item').click(function() {
                let cartId = $(this).data('cart-id');
                Swal.fire({
                    title: 'Xác nhận xoá?',
                    text: "Bạn có chắc muốn xoá sản phẩm này khỏi giỏ hàng?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Xoá',
                    cancelButtonText: 'Huỷ'
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            url: '${pageContext.request.contextPath}/cart/remove',
                            method: 'POST',
                            data: { cartId: cartId },
                            success: function(response) {
                                if(response.success) {
                                    // Update cart count before reloading
                                    if (typeof updateCartCountBadge === 'function') {
                                        updateCartCountBadge();
                                    }
                                    location.reload();
                                } else {
                                    Swal.fire('Lỗi', 'Không thể xoá sản phẩm', 'error');
                                }
                            }
                        });
                    }
                });
            });

            // Áp dụng mã giảm giá
            $('#applyDiscount').click(function() {
                let code = $('#discountCode').val();
                $.ajax({
                    url: '${pageContext.request.contextPath}/discount/apply',
                    method: 'POST',
                    data: { code: code },
                    success: function(response) {
                        if(response.success) {
                            $('#discountMessage').html(
                                '<div class="alert alert-success">' + (response.message || 'Đã áp dụng mã giảm giá!') + '</div>'
                            );
                            updateTotals();
                        } else {
                            $('#discountMessage').html(
                                '<div class="alert alert-danger">' + (response.message || 'Mã giảm giá không hợp lệ!') + '</div>'
                            );
                        }
                    }
                });
            });

            // Chọn sản phẩm để thanh toán
            $('.select-item').change(function() {
                let cartId = $(this).val();
                let selected = $(this).prop('checked');
                $.ajax({
                    url: '${pageContext.request.contextPath}/cart/select',
                    method: 'POST',
                    data: {
                        cartId: cartId,
                        selected: selected
                    },
                    success: function(response) {
                        if(response.success) {
                            updateTotals();
                        }
                    }
                });
            });

            // Cập nhật tổng tiền
            function updateTotals() {
                $.ajax({
                    url: '${pageContext.request.contextPath}/cart/totals',
                    method: 'GET',
                    success: function(response) {
                        $('#subtotal').text(formatCurrency(response.subtotal));
                        $('#discount').text('-' + formatCurrency(response.discount));
                        $('#total').text(formatCurrency(response.total));
                    }
                });
            }

            // Format tiền tệ
            function formatCurrency(amount) {
                return new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                }).format(amount);
            }
            
            // Function to update cart count badge
            function updateCartCountBadge() {
                $.ajax({
                    url: '${pageContext.request.contextPath}/cart/count',
                    method: 'GET',
                    success: function(response) {
                        if (response.success) {
                            $('.cart-count').text(response.count);
                        }
                    }
                });
            }

            // Thanh toán
            $('#checkoutBtn').click(function() {
                // Kiểm tra xem có sản phẩm nào được chọn không
                if($('.select-item:checked').length === 0) {
                    Swal.fire('Thông báo', 'Vui lòng chọn ít nhất một sản phẩm để thanh toán', 'info');
                    return;
                }
                
                window.location.href = '${pageContext.request.contextPath}/checkout';
            });
        });
    </script>
</body>
</html>