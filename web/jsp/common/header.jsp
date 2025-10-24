<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="sticky-top">
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <!-- Logo -->
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Fruit Store" height="60">
            </a>

            <!-- Toggle button for mobile -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
                <span class="navbar-toggler-icon"></span>
            </button>

            <!-- Main navigation -->
            <div class="collapse navbar-collapse" id="navbarMain">
                <!-- Menu items -->
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                            <i class="fas fa-home"></i> Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/products">
                            <i class="fas fa-apple-alt"></i> Sản phẩm
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/combos">
                            <i class="fas fa-gift"></i> Combo
                        </a>
                    </li>
                </ul>

                <!-- Search bar -->
                <form class="d-flex mx-auto" style="min-width: 300px;" action="${pageContext.request.contextPath}/search" method="GET">
                    <div class="input-group">
                        <input type="search" 
                               name="q" 
                               class="form-control search-input" 
                               placeholder="Tìm kiếm sản phẩm..."
                               autocomplete="off">
                        <button class="btn btn-success" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </form>

                <!-- Right navigation -->
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                            <i class="fas fa-shopping-cart"></i>
                            <span class="badge bg-success rounded-pill cart-count">0</span>
                        </a>
                    </li>
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.user.username}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <c:if test="${sessionScope.user.role == 'admin'}">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="fas fa-chart-line me-2"></i>Dashboard
                                        </a></li>
                                        <li><hr class="dropdown-divider"></li>
                                    </c:if>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Tài khoản</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">Đơn hàng</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- CSS cho thanh tìm kiếm và navbar -->
    <style>
    /* Autocomplete styles */
    .ui-autocomplete {
        max-height: 400px;
        overflow-y: auto;
        overflow-x: hidden;
        z-index: 9999 !important;
    }
    .ui-menu-item {
        padding: 10px;
        border-bottom: 1px solid #eee;
    }
    .search-item {
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .search-item img {
        width: 50px;
        height: 50px;
        object-fit: cover;
    }
    .search-item-info {
        flex: 1;
    }
    .search-item-name {
        font-weight: bold;
        margin-bottom: 5px;
    }
    .search-item-price {
        color: #28a745;
    }

    /* Navbar styles */
    .navbar {
        padding: 0.5rem 0;
    }
    .nav-item {
        margin: 0 0.25rem;
    }
    .nav-link {
        color: var(--text-color) !important;
        padding: 0.5rem 1rem !important;
        border-radius: 4px;
        transition: all 0.2s ease;
    }
    .nav-link:hover {
        color: var(--primary-color) !important;
        background: rgba(45, 122, 45, 0.1);
    }
    .nav-link i {
        margin-right: 0.5rem;
    }
    .navbar-brand {
        padding: 0;
    }
    .dropdown-menu {
        border: none;
        box-shadow: var(--shadow);
    }
    .dropdown-item {
        padding: 0.5rem 1rem;
    }
    .dropdown-item:hover {
        background-color: rgba(45, 122, 45, 0.1);
        color: var(--primary-color);
    }
    </style>
    
    <!-- Scripts for cart count -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            // Function to update cart count
            function updateCartCount() {
                $.ajax({
                    url: '${pageContext.request.contextPath}/cart/count',
                    method: 'GET',
                    success: function(response) {
                        if (response.success) {
                            let count = response.count || 0;
                            $('.cart-count').text(count);
                            // Hiển thị hoặc ẩn badge dựa vào số lượng
                            if (count > 0) {
                                $('.cart-count').show();
                            } else {
                                $('.cart-count').hide();
                            }
                        } else {
                            $('.cart-count').text('0').hide();
                        }
                    },
                    error: function() {
                        $('.cart-count').text('0').hide();
                    }
                });
            }
            
            // Gọi hàm cập nhật khi:
            // 1. Trang web tải xong
            updateCartCount();
            
            // 2. Sau mỗi 30 giây (thay vì 5 giây để giảm tải server)
            setInterval(updateCartCount, 30000);
            
            // 3. Sau khi thêm sản phẩm/combo vào giỏ
            $(document).on('cartUpdated', function() {
                updateCartCount();
            });
            
            // Đặt hàm updateCartCount vào window để các trang khác có thể gọi
            window.updateCartCount = updateCartCount;
        });
    </script>
</header>