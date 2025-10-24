<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết sản phẩm | Fruit Store</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <style>
        .product-detail-img {
            width: 100%;
            max-width: 400px;
            border-radius: 12px;
            object-fit: cover;
            background: #fff;
            box-shadow: var(--shadow);
        }
        .product-detail-info {
            padding: 2rem 1rem;
        }
        .product-detail-title {
            font-size: 2rem;
            font-weight: 700;
            color: var(--primary-color);
        }
        .product-detail-price {
            font-size: 1.5rem;
            font-weight: 600;
            color: var(--secondary-color);
        }
        .product-detail-original {
            font-size: 1.1rem;
            color: #888;
            text-decoration: line-through;
            margin-left: 1rem;
        }
        .product-detail-discount {
            background: var(--secondary-color);
            color: #fff;
            border-radius: 1rem;
            padding: 0.3rem 0.8rem;
            font-weight: bold;
            margin-left: 1rem;
        }
        .product-detail-stock {
            font-size: 1rem;
            color: #28a745;
            margin-bottom: 1rem;
        }
        .product-detail-desc {
            font-size: 1.1rem;
            color: var(--text-color);
            margin: 1.5rem 0;
        }
    </style>
</head>
<body>
    <%@ include file="common/header.jsp" %>
    <main class="py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="row g-4 align-items-center">
                        <div class="col-md-5 text-center">
                            <img src="${product.imageUrl}" alt="${product.name}" class="product-detail-img">
                        </div>
                        <div class="col-md-7 product-detail-info">
                            <h1 class="product-detail-title mb-3">${product.name}</h1>
                            <div class="mb-3">
                                <c:if test="${product.discountPercent > 0}">
                                    <span class="product-detail-price text-danger">
                                        <fmt:formatNumber value="${product.price * (1 - product.discountPercent/100)}" type="currency" currencyCode="VND"/>
                                    </span>
                                    <span class="product-detail-original">
                                        <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
                                    </span>
                                    <span class="product-detail-discount">-${product.discountPercent}%</span>
                                </c:if>
                                <c:if test="${product.discountPercent == 0}">
                                    <span class="product-detail-price">
                                        <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
                                    </span>
                                </c:if>
                            </div>
                            <div class="product-detail-stock mb-2">
                                <i class="fas fa-box"></i> Tồn kho: ${product.stockQuantity}
                            </div>
                            <div class="product-detail-desc">
                                <i class="fas fa-info-circle"></i> ${product.description}
                            </div>
                            <c:choose>
                                <c:when test="${product.stockQuantity > 0}">
                                    <button class="btn btn-success btn-lg px-5" id="addToCartBtn">
                                        <i class="fas fa-shopping-cart"></i> Thêm vào giỏ hàng
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-secondary btn-lg px-5" disabled>
                                        <i class="fas fa-box-open"></i> Hết hàng
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <%@ include file="common/footer.jsp" %>
    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- Import modal thêm vào giỏ hàng -->
    <%@ include file="common/add-to-cart-modal.jsp" %>
    <script>
        $(function() {
            $('#addToCartBtn').click(function() {
                // Lấy thông tin sản phẩm và giá
                const productId = ${product.productId};
                const productName = $('.product-detail-title').text().trim();
                const stockQuantity = parseInt('${product.stockQuantity}');
                
                // Lấy giá sản phẩm (giá sau giảm giá nếu có)
                let productPrice, originalPrice, discountPercent;
                <c:choose>
                    <c:when test="${product.discountPercent > 0}">
                        productPrice = '<fmt:formatNumber value="${product.discountedPrice}" type="currency" currencyCode="VND"/>';
                        originalPrice = '<fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>';
                        discountPercent = ${product.discountPercent};
                    </c:when>
                    <c:otherwise>
                        productPrice = '<fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>';
                        originalPrice = null;
                        discountPercent = 0;
                    </c:otherwise>
                </c:choose>
                
                // Hiển thị modal thêm vào giỏ hàng
                showAddToCartModal(productId, productName, productPrice, stockQuantity, originalPrice, discountPercent);
            });
        });
    </script>
</body>
</html>
