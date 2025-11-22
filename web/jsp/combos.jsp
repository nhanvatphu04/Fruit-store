<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Combo Khuyến Mãi | Fruit Store</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <style>
        .combo-card {
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s;
            height: 100%;
            display: flex;
            flex-direction: column;
            background: white;
        }
        
        .combo-card:hover {
            transform: translateY(-5px);
        }
        
        .combo-image {
            width: 100%;
            height: 250px;
            object-fit: cover;
        }
        
        .combo-badge {
            position: absolute;
            top: 10px;
            right: 10px;
            background-color: var(--secondary-color);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: bold;
            z-index: 1;
        }
        
        .combo-timer {
            position: absolute;
            bottom: 10px;
            left: 10px;
            right: 10px;
            background: rgba(0,0,0,0.7);
            color: white;
            padding: 0.5rem;
            border-radius: 5px;
            text-align: center;
            font-size: 0.9rem;
        }
        
        .combo-content {
            padding: 1.5rem;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }
        
        .combo-title {
            font-size: 1.3rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: var(--primary-color);
        }
        
        .combo-description {
            color: #666;
            margin-bottom: 1rem;
            flex-grow: 1;
        }
        
        .combo-prices {
            margin-bottom: 1rem;
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
        
        .status-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            padding: 0.4rem 0.8rem;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: 600;
            z-index: 1;
        }
        
        .status-active {
            background-color: #28a745;
            color: white;
        }
        
        .status-upcoming {
            background-color: #ffc107;
            color: black;
        }
        
        .status-expired {
            background-color: #dc3545;
            color: white;
        }

        .no-combos {
            text-align: center;
            padding: 3rem;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body class="bg-light">
    <%@ include file="common/header.jsp" %>

    <main class="py-5">
        <div class="container">
            <h1 class="mb-4">Combo Khuyến Mãi</h1>

            <c:if test="${empty combos}">
                <div class="no-combos">
                    <i class="fas fa-box-open fa-3x mb-3 text-muted"></i>
                    <h3 class="text-muted">Hiện chưa có combo khuyến mãi nào</h3>
                </div>
            </c:if>

            <div class="row g-4">
                <c:forEach items="${combos}" var="combo">
                    <div class="col-md-6 col-lg-4">
                        <div class="combo-card" id="combo-${combo.comboId}">
                            <!-- Trạng thái combo -->
                            <c:set var="now" value="<%=new java.util.Date()%>" />
                            <c:choose>
                                <c:when test="${combo.startDate.after(now)}">
                                    <div class="status-badge status-upcoming">Sắp diễn ra</div>
                                </c:when>
                                <c:when test="${combo.endDate.before(now)}">
                                    <div class="status-badge status-expired">Đã kết thúc</div>
                                </c:when>
                                <c:when test="${combo.active}">
                                    <div class="status-badge status-active">Đang diễn ra</div>
                                </c:when>
                            </c:choose>

                            <!-- Phần trăm giảm giá -->
                            <div class="combo-badge">-${combo.discountPercentage}%</div>
                            
                            <!-- Ảnh combo -->
                            <div class="position-relative">
                                <img src="${combo.imageUrl}" alt="${combo.name}" class="combo-image">
                                <c:if test="${combo.active && !combo.endDate.before(now)}">
                                    <div class="combo-timer" data-end="${combo.endDate.time}">
                                        <i class="fas fa-clock me-2"></i>
                                        <span class="countdown">Đang tải...</span>
                                    </div>
                                </c:if>
                            </div>
                            
                            <!-- Nội dung combo -->
                            <div class="combo-content">
                                <h3 class="combo-title">${combo.name}</h3>
                                <p class="combo-description">${combo.description}</p>
                                <div class="combo-prices">
                                    <div class="original-price mb-1">
                                        <fmt:formatNumber value="${combo.originalPrice}" type="currency" currencyCode="VND"/>
                                    </div>
                                    <div class="sale-price">
                                        <fmt:formatNumber value="${combo.salePrice}" type="currency" currencyCode="VND"/>
                                    </div>
                                </div>
                                
                                    <c:choose>
                                        <c:when test="${combo.startDate.after(now)}">
                                            <button class="btn btn-outline-warning w-100" disabled>
                                                <i class="fas fa-clock me-2"></i>Sắp mở bán
                                            </button>
                                        </c:when>
                                        <c:when test="${combo.endDate.before(now)}">
                                            <button class="btn btn-outline-danger w-100" disabled>
                                                <i class="fas fa-times-circle me-2"></i>Đã kết thúc
                                            </button>
                                        </c:when>
                                        <c:when test="${combo.active}">
                                            <button class="btn btn-success w-100 add-to-cart" data-combo-id="${combo.comboId}">
                                                <i class="fas fa-shopping-cart me-2"></i>Thêm vào giỏ
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-secondary w-100" disabled>
                                                <i class="fas fa-ban me-2"></i>Không khả dụng
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="${pageContext.request.contextPath}/combo-detail?comboId=${combo.comboId}"
                                       class="btn btn-outline-primary w-100" style="margin-top: 0.5rem;">
                                        <i class="fas fa-info-circle me-2"></i>Xem chi tiết
                                    </a>
                                </div>
                            </div>
                        </div>
                </c:forEach>
            </div>
        </div>
    </main>

    <%@ include file="common/footer.jsp" %>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script>
        $(document).ready(function() {
            // Cập nhật đếm ngược cho mỗi combo
            function updateCountdowns() {
                $('.combo-timer').each(function() {
                    const endTime = parseInt($(this).data('end'));
                    const now = new Date().getTime();
                    const distance = endTime - now;
                    
                    if (distance < 0) {
                        $(this).find('.countdown').text('Đã kết thúc');
                        return;
                    }
                    
                    const days = Math.floor(distance / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((distance % (1000 * 60)) / 1000);
                    
                    let countdownText = '';
                    if (days > 0) countdownText += days + ' ngày ';
                    countdownText += hours.toString().padStart(2, '0') + ':' +
                                   minutes.toString().padStart(2, '0') + ':' +
                                   seconds.toString().padStart(2, '0');
                    
                    $(this).find('.countdown').text(countdownText);
                });
            }
            
            // Cập nhật đếm ngược mỗi giây
            updateCountdowns();
            setInterval(updateCountdowns, 1000);
            
            // Xử lý thêm combo vào giỏ hàng
            $('.add-to-cart').click(function() {
                const comboId = $(this).data('combo-id');
                
                // Gọi API thêm vào giỏ hàng
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
                            
                            // Cập nhật số lượng trong giỏ hàng (nếu có)
                            if (typeof updateCartCount === 'function') {
                                updateCartCount();
                            }
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
