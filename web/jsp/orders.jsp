<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng - FruitStore</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <style>
        .order-card {
            border: 1px solid rgba(0,0,0,0.125);
            border-radius: 8px;
            margin-bottom: 20px;
            background-color: #fff;
            box-shadow: var(--shadow);
            transition: transform 0.2s;
        }
        .order-card:hover {
            transform: translateY(-2px);
        }
        .order-header {
            background-color: var(--light-bg);
            padding: 15px;
            border-bottom: 1px solid rgba(0,0,0,0.125);
            border-radius: 8px 8px 0 0;
        }
        .order-body {
            padding: 15px;
        }
        .order-item {
            border-bottom: 1px solid #eee;
            padding: 10px 0;
            transition: background-color 0.2s;
        }
        .order-item:hover {
            background-color: var(--light-bg);
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .status-badge {
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.9em;
            font-weight: 500;
        }
        .status-pending { background-color: var(--secondary-color); color: white; }
        .status-processing { background-color: #2d7a2d80; color: white; }
        .status-shipped { background-color: var(--primary-color); color: white; }
        .status-delivered { background-color: var(--primary-color); color: white; }
        .status-cancelled { background-color: #dc3545; color: white; }
        
        .order-price {
            color: var(--secondary-color);
            font-weight: 500;
        }
        
        .product-img {
            max-width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }
        
        .btn-view-products {
            background-color: var(--primary-color);
            color: white;
            transition: all 0.2s;
        }
        
        .btn-view-products:hover {
            background-color: var(--secondary-color);
            color: white;
            transform: translateY(-1px);
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="common/header.jsp"/>

    <div class="container my-5">
        <h2 class="mb-4">Đơn hàng của tôi</h2>

        <!-- Order List -->
        <c:forEach var="order" items="${orders}">
            <div class="order-card">
                <div class="order-header">
                    <div class="row align-items-center">
                        <div class="col">
                            <h5 class="mb-0">Đơn hàng #${order.orderId}</h5>
                        </div>
                        <div class="col text-center">
                            <span class="status-badge status-${order.status.toLowerCase()}">
                                <c:choose>
                                    <c:when test="${order.status eq 'PENDING'}">Chờ xác nhận</c:when>
                                    <c:when test="${order.status eq 'PROCESSING'}">Đang xử lý</c:when>
                                    <c:when test="${order.status eq 'SHIPPED'}">Đang giao hàng</c:when>
                                    <c:when test="${order.status eq 'DELIVERED'}">Đã giao hàng</c:when>
                                    <c:when test="${order.status eq 'CANCELLED'}">Đã hủy</c:when>
                                </c:choose>
                            </span>
                        </div>
                        <div class="col text-end">
                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </div>
                    </div>
                </div>
                <div class="order-body">
                    <c:forEach var="item" items="${order.orderItems}">
                        <div class="order-item">
                            <div class="row align-items-center">
                                <div class="col-2">
                                    <img src="${item.product.imageUrl}" class="product-img" alt="${item.product.name}">
                                </div>
                                <div class="col">
                                    <h6 class="mb-0">${item.product.name}</h6>
                                    <small class="text-muted">Số lượng: ${item.quantity}</small>
                                </div>
                                <div class="col-3 text-end">
                                    <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <div class="mt-3 text-end">
                        <h5>Tổng cộng: <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></h5>
                    </div>
                </div>
            </div>
        </c:forEach>

        <!-- No Orders Message -->
        <c:if test="${empty orders}">
            <div class="text-center py-5">
                <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
                <h4>Bạn chưa có đơn hàng nào</h4>
                <p class="text-muted">Hãy khám phá các sản phẩm của chúng tôi và đặt hàng ngay!</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-view-products">
                    <i class="fas fa-shopping-basket me-2"></i>Xem sản phẩm
                </a>
            </div>
        </c:if>
    </div>

    <!-- Include Footer -->
    <jsp:include page="common/footer.jsp"/>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</body>
</html>