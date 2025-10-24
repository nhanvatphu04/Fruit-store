<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
.product-badges .badge {
    font-size: 0.75rem;
    padding: 0.25rem 0.5rem;
    border-radius: 0.375rem;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.product-image-container {
    overflow: hidden;
    border-radius: 0.5rem;
}

.product-image-container img {
    transition: transform 0.3s ease;
}

.product-image-container:hover img {
    transform: scale(1.05);
}

.product-card {
    transition: transform 0.2s ease, box-shadow 0.2s ease;
    border-radius: 0.5rem;
    overflow: hidden;
}

.product-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.product-price-container {
    min-height: 60px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}
</style>

<%-- Nhận product từ request --%>
<c:set var="product" value="${requestScope.product}" />

<div class="product-card">
    <a href="${pageContext.request.contextPath}/product?id=${product.productId}" class="text-decoration-none">
        <div class="product-image-container position-relative">
            <img src="${product.imageUrl}" class="card-img-top" alt="${product.name}">
            <div class="product-badges position-absolute top-0 start-0 p-2">
                <c:if test="${product['new']}">
                    <span class="badge bg-success mb-1">Mới</span>
                </c:if>
                <c:if test="${product.bestSeller}">
                    <span class="badge bg-danger mb-1">Bán chạy</span>
                </c:if>
                <c:if test="${product.onSale}">
                    <span class="badge bg-warning text-dark">-${product.discountPercent}%</span>
                </c:if>
            </div>
        </div>
    </a>
    <div class="card-body">
        <h5 class="card-title">
            <a href="${pageContext.request.contextPath}/product?id=${product.productId}" 
               class="text-decoration-none text-dark product-link">
                ${product.name}
            </a>
        </h5>
        <div class="product-price-container">
            <c:if test="${product.onSale}">
                <p class="original-price text-muted text-decoration-line-through mb-1">
                    <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
                </p>
                <p class="discounted-price text-danger fw-bold mb-0">
                    <fmt:formatNumber value="${product.discountedPrice}" type="currency" currencyCode="VND"/>
                </p>
                <small class="text-success">
                    <i class="fas fa-coins"></i> Tiết kiệm: <fmt:formatNumber value="${product.savingsAmount}" type="currency" currencyCode="VND"/>
                </small>
            </c:if>
            <c:if test="${!product.onSale}">
                <p class="product-price fw-bold mb-0">
                    <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
                </p>
            </c:if>
        </div>
        <c:choose>
            <c:when test="${product.stockQuantity > 0}">
                <button class="btn btn-success" 
                        onclick="addToCart(${product.productId}, '${product.name}', '<fmt:formatNumber value="${product.onSale ? product.discountedPrice : product.price}" type="currency" currencyCode="VND"/>', ${product.stockQuantity}, '<fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>', ${product.discountPercent})"
                        data-product-id="${product.productId}">
                    <i class="fas fa-shopping-cart"></i> Thêm vào giỏ
                </button>
            </c:when>
            <c:otherwise>
                <button class="btn btn-secondary" disabled>
                    <i class="fas fa-box-open"></i> Hết hàng
                </button>
            </c:otherwise>
        </c:choose>
    </div>
</div>