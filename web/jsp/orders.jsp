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
                                    <c:when test="${order.status eq 'pending'}">Chờ xác nhận</c:when>
                                    <c:when test="${order.status eq 'completed'}">Hoàn thành</c:when>
                                    <c:when test="${order.status eq 'cancelled'}">Đã hủy</c:when>
                                    <c:otherwise>${order.status}</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="col text-end">
                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </div>
                    </div>
                </div>
                <div class="order-body">
                    <!-- Show first 2 items only -->
                    <c:forEach var="item" items="${order.orderItems}" begin="0" end="1">
                        <div class="order-item">
                            <div class="row align-items-center">
                                <div class="col-2">
                                    <img src="${item.product.imageUrl}"
                                         class="product-img" alt="${item.product.name}"
                                         onerror="this.src='${pageContext.request.contextPath}/assets/images/default-product.jpg'">
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
                    
                    <!-- Show more items indicator -->
                    <c:if test="${order.orderItems.size() > 2}">
                        <div class="text-center mt-2">
                            <small class="text-muted">Và ${order.orderItems.size() - 2} sản phẩm khác...</small>
                        </div>
                    </c:if>
                    
                    <div class="mt-3">
                        <div class="row align-items-center">
                            <div class="col">
                                <h5 class="mb-0">Tổng cộng: <span class="text-success"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></span></h5>
                            </div>
                            <div class="col text-end">
                                <button class="btn btn-sm btn-outline-primary view-order-details me-2" 
                                        data-order-id="${order.orderId}">
                                    <i class="fas fa-eye me-1"></i>Chi tiết
                                </button>
                                <c:if test="${order.status eq 'pending'}">
                                    <button class="btn btn-sm btn-outline-danger cancel-order" 
                                            data-order-id="${order.orderId}">
                                        <i class="fas fa-times me-1"></i>Hủy đơn
                                    </button>
                                </c:if>
                            </div>
                        </div>
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

    <%@ include file="common/order-details-modal.jsp" %>

    <!-- Include Footer -->
    <jsp:include page="common/footer.jsp"/>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        $(document).ready(function() {
            // View Order Details
            $('.view-order-details').click(function() {
                const orderId = $(this).data('order-id');
                
                // Clear previous modal data
                $('#modalOrderId').text('');
                $('#detailOrderId').text('');
                $('#detailOrderDate').text('');
                $('#detailOrderStatus').html('');
                $('#detailCustomerName').text('');
                $('#detailCustomerEmail').text('');
                $('#detailCustomerPhone').text('');
                $('#detailCustomerAddress').text('');
                $('#detailOrderDiscount').text('');
                $('#discountRow').hide();
                $('#detailOrderItems').html('');
                $('#detailOrderTotal').text('');
                
                // Fetch order details
                $.ajax({
                    url: contextPath + '/orders/details/' + orderId,
                    type: 'GET',
                    dataType: 'json',
                    success: function(response) {

                        if (!response || !response.orderId) {
                            Swal.fire('Lỗi', 'Không thể tải thông tin đơn hàng', 'error');
                            return;
                        }
                        
                        // Update modal with order details
                        $('#modalOrderId').text('#' + response.orderId);
                        $('#detailOrderId').text('#' + response.orderId);
                        $('#detailOrderDate').text(response.orderDate || 'N/A');
                        
                        // Status badge
                        let statusText = response.status;
                        let badgeClass = 'secondary';
                        if (response.status === 'pending') {
                            statusText = 'Chờ xác nhận';
                            badgeClass = 'warning';
                        } else if (response.status === 'completed') {
                            statusText = 'Hoàn thành';
                            badgeClass = 'success';
                        } else if (response.status === 'cancelled') {
                            statusText = 'Đã hủy';
                            badgeClass = 'danger';
                        }
                        $('#detailOrderStatus').html('<span class="badge bg-' + badgeClass + '">' + statusText + '</span>');
                        
                        // Customer info
                        const customer = response.customer || {};
                        $('#detailCustomerName').text(customer.fullName || customer.username || 'N/A');
                        $('#detailCustomerEmail').text(customer.email || 'N/A');
                        $('#detailCustomerPhone').text(customer.phone || 'N/A');
                        $('#detailCustomerAddress').text(response.shippingAddress || customer.address || 'N/A');
                        
                        // Discount info
                        if (response.discountCode && response.discountAmount > 0) {
                            $('#detailDiscountCode').text(response.discountCode);
                            $('#detailDiscountAmount').text(parseFloat(response.discountAmount).toLocaleString('vi-VN') + '₫');
                            $('#discountSection').show();
                        } else {
                            $('#discountSection').hide();
                        }
                        
                        // Order items & combos
                        let itemsHtml = '';
                        let total = 0;

                        if (response.items && Array.isArray(response.items) && response.items.length > 0) {
                            response.items.forEach((item) => {
                                const price = parseFloat(item.price) || 0;
                                const quantity = parseInt(item.quantity) || 0;
                                const subtotal = price * quantity;
                                total += subtotal;

                                const productName = (item.product && item.product.name) ? item.product.name : 'Sản phẩm không xác định';

                                itemsHtml += '<tr>' +
                                    '<td>' + productName + '</td>' +
                                    '<td>' + price.toLocaleString('vi-VN') + '₫</td>' +
                                    '<td>' + quantity + '</td>' +
                                    '<td>' + subtotal.toLocaleString('vi-VN') + '₫</td>' +
                                '</tr>';
                            });
                        }

                        if (response.combos && Array.isArray(response.combos) && response.combos.length > 0) {
                            response.combos.forEach((comboItem) => {
                                const price = parseFloat(comboItem.price) || 0;
                                const quantity = parseInt(comboItem.quantity) || 0;
                                const subtotal = price * quantity;
                                total += subtotal;

                                const comboName = (comboItem.combo && comboItem.combo.name)
                                    ? '[Combo] ' + comboItem.combo.name
                                    : 'Combo';

                                itemsHtml += '<tr>' +
                                    '<td>' + comboName + '</td>' +
                                    '<td>' + price.toLocaleString('vi-VN') + '₫</td>' +
                                    '<td>' + quantity + '</td>' +
                                    '<td>' + subtotal.toLocaleString('vi-VN') + '₫</td>' +
                                '</tr>';
                            });
                        }

                        if (!itemsHtml) {
                            itemsHtml = '<tr><td colspan="4" class="text-center">Không có sản phẩm</td></tr>';
                        }

                        $('#detailOrderItems').html(itemsHtml);
                        
                        // Display discount row if there's a discount
                        let discountAmount = response.discountAmount ? parseFloat(response.discountAmount) : 0;
                        if (!isNaN(discountAmount) && discountAmount > 0) {
                            $('#detailOrderDiscount').text('- ₫' + discountAmount.toLocaleString('vi-VN'));
                            $('#discountRow').show();
                            total -= discountAmount;
                        } else {
                            $('#discountRow').hide();
                        }
                        
                        $('#detailOrderTotal').text('₫' + total.toLocaleString('vi-VN'));
                        
                        // Show modal
                        $('#orderDetailsModal').modal('show');
                    },
                    error: function(xhr, status, error) {
                        console.error('Error loading order details:', xhr, status, error);
                        Swal.fire('Lỗi', 'Không thể tải thông tin đơn hàng', 'error');
                    }
                });
            });
            
            // Cancel Order
            $('.cancel-order').click(function() {
                const orderId = $(this).data('order-id');
                
                Swal.fire({
                    title: 'Xác nhận hủy đơn hàng',
                    text: 'Bạn có chắc chắn muốn hủy đơn hàng #' + orderId + '?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Đồng ý hủy',
                    cancelButtonText: 'Không'
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            url: contextPath + '/orders/cancel',
                            type: 'POST',
                            data: { orderId: orderId },
                            dataType: 'json',
                            success: function(response) {
                                if (response.success) {
                                    Swal.fire({
                                        icon: 'success',
                                        title: 'Đã hủy đơn hàng',
                                        text: 'Đơn hàng của bạn đã được hủy thành công.'
                                    }).then(() => {
                                        location.reload();
                                    });
                                } else {
                                    Swal.fire('Lỗi', response.message || 'Không thể hủy đơn hàng', 'error');
                                }
                            },
                            error: function(xhr) {
                                console.error('Error cancelling order:', xhr);
                                let errorMsg = 'Không thể hủy đơn hàng';
                                try {
                                    const errorResponse = JSON.parse(xhr.responseText);
                                    if (errorResponse.message) {
                                        errorMsg = errorResponse.message;
                                    }
                                } catch (e) {}
                                Swal.fire('Lỗi', errorMsg, 'error');
                            }
                        });
                    }
                });
            });
        });
    </script>
</body>
</html>
