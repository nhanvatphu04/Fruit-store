<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Combo | Fruit Store</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">

    <style>
        .combo-detail-card {
            margin-top: 2rem;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .combo-detail-image {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .combo-detail-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--primary-color);
        }

        .combo-detail-prices .original-price {
            font-size: 1rem;
        }

        .combo-detail-prices .sale-price {
            font-size: 1.6rem;
        }

        .original-price {
            text-decoration: line-through;
            color: #999;
            font-size: 0.9rem;
        }

        .sale-price {
            font-size: 1.4rem;
            font-weight: bold;
            color: var(--secondary-color);
        }

        .combo-products-title {
            margin-top: 2rem;
            margin-bottom: 1rem;
        }

        .combo-product-card {
            background: #ffffff;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            padding: 0.75rem;
            height: 100%;
            display: flex;
            gap: 0.75rem;
        }

        .combo-product-image {
            width: 70px;
            height: 70px;
            border-radius: 8px;
            object-fit: cover;
            flex-shrink: 0;
        }

        .combo-product-name {
            font-weight: 600;
            margin-bottom: 0.25rem;
        }

        .combo-product-quantity {
            font-size: 0.9rem;
            color: #555;
        }

        .combo-product-price {
            font-size: 0.9rem;
            color: var(--secondary-color);
            font-weight: 600;
        }
    </style>
</head>
<body class="bg-light">
    <%@ include file="common/header.jsp" %>

    <main class="py-5">
        <div class="container">
            <h1 class="mb-4">Chi tiết Combo</h1>

            <c:if test="${empty combo}">
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    Combo không tồn tại hoặc đã bị xoá.
                </div>
                <a href="${pageContext.request.contextPath}/combos" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách combo
                </a>
            </c:if>

            <c:if test="${not empty combo}">
                <c:set var="now" value="<%=new java.util.Date()%>" />
                <div class="combo-detail-card bg-white">
                    <div class="row g-0">
                        <div class="col-md-5">
                            <img src="${combo.imageUrl}" alt="${combo.name}" class="combo-detail-image">
                        </div>
                        <div class="col-md-7">
                            <div class="p-4 h-100 d-flex flex-column justify-content-between">
                                <div>
                                    <h2 class="combo-detail-title mb-3">${combo.name}</h2>
                                    <p class="mb-3 text-muted">${combo.description}</p>
                                    <div class="combo-detail-prices combo-prices mb-3">
                                        <div class="original-price mb-1">
                                            <fmt:formatNumber value="${combo.originalPrice}" type="currency" currencyCode="VND"/>
                                        </div>
                                        <div class="sale-price">
                                            <fmt:formatNumber value="${combo.salePrice}" type="currency" currencyCode="VND"/>
                                        </div>
                                    </div>
                                    <p class="mb-1">
                                        <strong>Thời gian áp dụng:</strong>
                                        <fmt:formatDate value="${combo.startDate}" pattern="HH:mm dd/MM/yyyy" />
                                        -
                                        <fmt:formatDate value="${combo.endDate}" pattern="HH:mm dd/MM/yyyy" />
                                    </p>
                                    <p class="mb-3">
                                        <strong>Trạng thái:</strong>
                                        <c:choose>
                                            <c:when test="${combo.startDate.after(now)}">
                                                <span class="badge bg-warning text-dark">Sắp diễn ra</span>
                                            </c:when>
                                            <c:when test="${combo.endDate.before(now)}">
                                                <span class="badge bg-danger">Đã kết thúc</span>
                                            </c:when>
                                            <c:when test="${combo.active}">
                                                <span class="badge bg-success">Đang diễn ra</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Không khả dụng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                                <div class="mt-3 d-flex flex-column flex-md-row gap-2">
                                    <a href="${pageContext.request.contextPath}/combos"
                                       class="btn btn-outline-secondary flex-fill">
                                        <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
                                    </a>
                                    <c:choose>
                                        <c:when test="${combo.startDate.after(now)}">
                                            <button class="btn btn-outline-warning flex-fill" disabled>
                                                <i class="fas fa-clock me-2"></i>Sắp mở bán
                                            </button>
                                        </c:when>
                                        <c:when test="${combo.endDate.before(now)}">
                                            <button class="btn btn-outline-danger flex-fill" disabled>
                                                <i class="fas fa-times-circle me-2"></i>Đã kết thúc
                                            </button>
                                        </c:when>
                                        <c:when test="${combo.active}">
                                            <button class="btn btn-success flex-fill add-to-cart" data-combo-id="${combo.comboId}">
                                                <i class="fas fa-shopping-cart me-2"></i>Thêm vào giỏ
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-secondary flex-fill" disabled>
                                                <i class="fas fa-ban me-2"></i>Không khả dụng
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Products included in combo -->
                <c:if test="${not empty comboProducts}">
                    <h3 class="combo-products-title">Sản phẩm trong combo</h3>
                    <div class="row g-3 mt-1">
                        <c:forEach var="item" items="${comboProducts}">
                            <c:set var="product" value="${item.product}" />
                            <div class="col-md-6 col-lg-4">
                                <div class="combo-product-card">
                                    <img src="${product.imageUrl}" alt="${product.name}" class="combo-product-image">
                                    <div class="flex-grow-1">
                                        <div class="combo-product-name">${product.name}</div>
                                        <div class="combo-product-quantity">
                                            Số lượng: x${item.quantity}
                                        </div>
                                        <div class="combo-product-price">
                                            <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </c:if>
        </div>
    </main>

    <%@ include file="common/footer.jsp" %>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
        $(document).ready(function() {
            $('.add-to-cart').click(function() {
                const comboId = $(this).data('combo-id');

                $.ajax({
                    url: '${pageContext.request.contextPath}/cart/add-combo',
                    method: 'POST',
                    data: { comboId: comboId },
                    success: function(response) {
                        if (response.success) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: 'Đã thêm combo vào giỏ hàng',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi!',
                                text: response.message || 'Không thể thêm combo vào giỏ hàng'
                            });
                        }
                    },
                    error: function() {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Đã xảy ra lỗi khi thêm vào giỏ hàng'
                        });
                    }
                });
            });
        });
    </script>
</body>
</html>
