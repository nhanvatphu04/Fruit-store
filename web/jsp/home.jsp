<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fruit Store - Cửa hàng trái cây tươi</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    <!-- jQuery UI CSS -->
    <link href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/home.css" rel="stylesheet">
    
    <!-- FlipDown CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flipdown@0.3.2/dist/flipdown.min.css">
    <style>
        #flipdown {
            margin: 20px auto;
        }
        /* Custom FlipDown theme */
        .flipdown.flipdown__theme-dark {
            font-family: sans-serif;
            font-weight: bold;
        }
        .flipdown.flipdown__theme-dark .rotor-group-heading {
            color: #6c757d;
        }
        .flipdown.flipdown__theme-dark .rotor,
        .flipdown.flipdown__theme-dark .rotor-top,
        .flipdown.flipdown__theme-dark .rotor-bottom {
            background-color: #198754;
        }
        .flipdown.flipdown__theme-dark .rotor-leaf-front {
            color: #ffffff;
        }

        .category-badge,
        .category-badge-item,
        .category-name,
        .category-icon {
            color: #198754 !important;
        }

        .category-badge-item {
            border: 1px solid #e5e7eb;
            border-radius: 14px;
            padding: 18px 10px;
            background: #ffffff;
            text-align: center;
            transition: all 0.3s ease;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        }

        .category-badge-item:hover {
            border-color: #28a745;
            box-shadow: 0 4px 14px rgba(40,167,69,0.25);
            transform: translateY(-5px);
        }
    </style>
    
    <!-- Thêm contextPath cho JavaScript -->
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <!-- Swiper CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@10/swiper-bundle.min.css" />
</head>
<body>
    <!-- Header -->
    <%@ include file="common/header.jsp" %>

    <!-- Main Content -->
    <main>
        <!-- Enhanced Hero Banner -->
        <section class="hero-section">
            <div class="container-fluid px-0">
                <div id="heroCarousel" class="carousel slide hero-carousel" data-bs-ride="carousel" data-bs-interval="5000">
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="0" class="active"></button>
                        <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="1"></button>
                        <button type="button" data-bs-target="#heroCarousel" data-bs-slide-to="2"></button>
                    </div>
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <div class="hero-banner" style="background: linear-gradient(135deg, #28a745 0%, #20c997 100%); min-height: 500px; display: flex; align-items: center; position: relative; overflow: hidden;">
                                <div class="hero-content container d-flex align-items-center" style="position: relative; z-index: 2;">
                                    <div class="row w-100">
                                        <div class="col-md-6 text-white hero-text">
                                            <h1 class="display-4 fw-bold mb-3">Trái Cây Tươi Mỗi Ngày</h1>
                                            <p class="lead mb-4">Chúng tôi mang đến những loại trái cây tươi ngon nhất, chất lượng cao từ các nhà vườn tin cậy.</p>
                                            <a href="${pageContext.request.contextPath}/products" class="btn btn-light btn-lg me-3">
                                                <i class="fas fa-shopping-bag me-2"></i>Mua Ngay
                                            </a>
                                        </div>
                                        <div class="col-md-6 text-center d-none d-md-block">
                                            <div class="hero-image" style="font-size: 150px;">
                                                <i class="fas fa-apple-alt" style="color: rgba(255,255,255,0.2); filter: drop-shadow(0 10px 30px rgba(0,0,0,0.2));"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="hero-decoration" style="position: absolute; top: -50%; right: -10%; width: 500px; height: 500px; background: rgba(255,255,255,0.1); border-radius: 50%; z-index: 1;"></div>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <div class="hero-banner" style="background: linear-gradient(135deg, #ffc107 0%, #ff8c00 100%); min-height: 500px; display: flex; align-items: center; position: relative; overflow: hidden;">
                                <div class="hero-content container d-flex align-items-center" style="position: relative; z-index: 2;">
                                    <div class="row w-100">
                                        <div class="col-md-6 text-white hero-text">
                                            <h1 class="display-4 fw-bold mb-3">Ưu Đãi Hấp Dẫn</h1>
                                            <p class="lead mb-4">Giảm giá tới 50% cho các sản phẩm được chọn. Không bỏ lỡ cơ hội này!</p>
                                            <a href="${pageContext.request.contextPath}/products?category=combo-tiet-kiem" class="btn btn-light btn-lg me-3">
                                                <i class="fas fa-tags me-2"></i>Xem Ưu Đãi
                                            </a>
                                        </div>
                                        <div class="col-md-6 text-center d-none d-md-block">
                                            <div class="hero-image" style="font-size: 150px;">
                                                <i class="fas fa-star" style="color: rgba(255,255,255,0.2); filter: drop-shadow(0 10px 30px rgba(0,0,0,0.2));"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="hero-decoration" style="position: absolute; top: -50%; right: -10%; width: 500px; height: 500px; background: rgba(255,255,255,0.1); border-radius: 50%; z-index: 1;"></div>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <div class="hero-banner" style="background: linear-gradient(135deg, #dc3545 0%, #c82333 100%); min-height: 500px; display: flex; align-items: center; position: relative; overflow: hidden;">
                                <div class="hero-content container d-flex align-items: center" style="position: relative; z-index: 2;">
                                    <div class="row w-100">
                                        <div class="col-md-6 text-white hero-text">
                                            <h1 class="display-4 fw-bold mb-3">Giao Hàng Nhanh Chóng</h1>
                                            <p class="lead mb-4">Miễn phí giao hàng cho đơn hàng từ 100.000đ. Giao hàng trong vòng 24h.</p>
                                            <a href="${pageContext.request.contextPath}/products" class="btn btn-light btn-lg me-3">
                                                <i class="fas fa-truck me-2"></i>Đặt Hàng
                                            </a>
                                        </div>
                                        <div class="col-md-6 text-center d-none d-md-block">
                                            <div class="hero-image" style="font-size: 150px;">
                                                <i class="fas fa-gift" style="color: rgba(255,255,255,0.2); filter: drop-shadow(0 10px 30px rgba(0,0,0,0.2));"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="hero-decoration" style="position: absolute; top: -50%; right: -10%; width: 500px; height: 500px; background: rgba(255,255,255,0.1); border-radius: 50%; z-index: 1;"></div>
                            </div>
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#heroCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon"></span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#heroCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon"></span>
                    </button>
                </div>
            </div>
        </section>

        <!-- Trust Badges Section -->
        <section class="trust-badges-section py-4 bg-light border-bottom">
            <div class="container">
                <div class="row g-4 text-center">
                    <div class="col-md-3 col-sm-6">
                        <div class="trust-badge d-flex flex-column align-items-center">
                            <i class="fas fa-truck text-success" style="font-size: 2.5rem; margin-bottom: 10px;"></i>
                            <h6 class="fw-bold mb-1">Giao Hàng Nhanh</h6>
                            <small class="text-muted">Miễn phí từ 100.000đ</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-6">
                        <div class="trust-badge d-flex flex-column align-items-center">
                            <i class="fas fa-shield-alt text-success" style="font-size: 2.5rem; margin-bottom: 10px;"></i>
                            <h6 class="fw-bold mb-1">Thanh Toán An Toàn</h6>
                            <small class="text-muted">100% bảo mật</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-6">
                        <div class="trust-badge d-flex flex-column align-items-center">
                            <i class="fas fa-undo text-success" style="font-size: 2.5rem; margin-bottom: 10px;"></i>
                            <h6 class="fw-bold mb-1">Hoàn Trả Dễ Dàng</h6>
                            <small class="text-muted">7 ngày hoàn tiền</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-sm-6">
                        <div class="trust-badge d-flex flex-column align-items-center">
                            <i class="fas fa-headset text-success" style="font-size: 2.5rem; margin-bottom: 10px;"></i>
                            <h6 class="fw-bold mb-1">Hỗ Trợ 24/7</h6>
                            <small class="text-muted">Luôn sẵn sàng giúp</small>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Category Badges Section -->
        <section class="category-badges-section py-5 bg-white">
            <div class="container">
                <h2 class="section-title">Danh Mục Sản Phẩm</h2>
                <div class="row g-4 mb-5 justify-content-center">
                    <c:forEach items="${categories}" var="category">
                        <div class="col-lg-2 col-md-3 col-sm-4 col-6">
                            <a href="${pageContext.request.contextPath}/products?categoryId=${category.categoryId}" class="category-badge text-decoration-none">
                                <div class="category-badge-item">
                                    <i class="category-icon ${category.icon}"></i>
                                    <h6 class="category-name">${category.name}</h6>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Best Selling Products -->
        <section class="best-selling-section py-5">
            <div class="container">
                <h2 class="section-title">Sản phẩm bán chạy <i class="fas fa-fire text-danger"></i></h2>
                <div class="swiper best-selling-swiper">
                    <div class="swiper-wrapper">
                        <c:forEach items="${bestSellingProducts}" var="product">
                            <div class="swiper-slide">
                                <c:set var="product" value="${product}" scope="request" />
                                <jsp:include page="common/product-card.jsp" />
                            </div>
                        </c:forEach>
                    </div>
                    <!-- Swiper navigation -->
                    <div class="swiper-button-prev best-selling-prev"></div>
                    <div class="swiper-button-next best-selling-next"></div>
                </div>
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/products?filter=best-selling" class="btn btn-outline-success btn-lg">
                        Xem tất cả sản phẩm bán chạy <i class="fas fa-arrow-right"></i>
                    </a>
                </div>
            </div>
        </section>

        <!-- New Products -->
        <section class="new-products-section py-5 bg-light">
            <div class="container">
                <h2 class="section-title">Sản phẩm mới <i class="fas fa-sparkles text-success"></i></h2>
                <div class="swiper new-products-swiper">
                    <div class="swiper-wrapper">
                        <c:forEach items="${newProducts}" var="product">
                            <div class="swiper-slide">
                                <c:set var="product" value="${product}" scope="request" />
                                <jsp:include page="common/product-card.jsp" />
                            </div>
                        </c:forEach>
                    </div>
                    <!-- Swiper navigation -->
                    <div class="swiper-button-prev new-products-prev"></div>
                    <div class="swiper-button-next new-products-next"></div>
                </div>
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/products?filter=new" class="btn btn-outline-success btn-lg">
                        Xem tất cả sản phẩm mới <i class="fas fa-arrow-right"></i>
                    </a>
                </div>
            </div>
        </section>

        <!-- Combo Khuyến Mãi -->
        <section class="flash-sale-section py-5">
            <div class="container">
                <h2 class="section-title">Combo Trái Cây Tươi Ngon</h2>
                <div class="mb-4 text-center">
                    <p class="text-success"><i class="fas fa-leaf"></i> Trái cây tươi mới mỗi ngày</p>
                    <c:if test="${not empty activeCombo}">
                        <div class="timer-container">
                            <h3>Ưu đãi kết thúc sau:</h3>
                            <div id="flipdown" class="flipdown"></div>
                            <div class="timer d-none" data-end="${activeCombo[0].endDate.time}">00:00:00</div>
                        </div>
                    </c:if>
                    <c:if test="${empty activeCombo}">
                        <div class="text-center py-3">
                            <p class="text-muted"><i class="fas fa-info-circle"></i> Không có combo khuyến mãi nào đang diễn ra</p>
                        </div>
                    </c:if>
                </div>
                <div class="flash-sale-container">
                    <c:if test="${not empty activeCombo}">
                        <div class="row">
                            <c:forEach items="${activeCombo}" var="combo">
                                <div class="col-md-4 mb-4">
                                    <div class="combo-card shadow-sm rounded p-3">
                                        <div class="ribbon-wrapper">
                                            <span class="flash-sale-badge bg-success">-${combo.discountPercentage}%</span>
                                        </div>
                                        <img src="${combo.imageUrl}" alt="${combo.name}" class="img-fluid mb-3 rounded">
                                        <h4 class="text-success">${combo.name}</h4>
                                        <div class="prices mb-3">
                                            <span class="original-price text-muted text-decoration-line-through">
                                                <fmt:formatNumber value="${combo.originalPrice}" type="currency" currencyCode="VND"/>
                                            </span>
                                            <span class="sale-price text-danger ms-2 fw-bold">
                                                <fmt:formatNumber value="${combo.salePrice}" type="currency" currencyCode="VND"/>
                                            </span>
                                        </div>
                                        <p class="combo-description text-muted">${combo.description}</p>
                                        <c:set var="now" value="<%= new java.util.Date().getTime() %>" />
                                        <div class="d-grid gap-2">
                                            <c:choose>
                                                <c:when test="${combo.startDate.time > now}">
                                                    <button class="btn btn-secondary btn-lg w-100" disabled>
                                                        <i class="fas fa-clock"></i> Sắp mở bán
                                                    </button>
                                                    <small class="text-muted d-block text-center mt-2">
                                                        Bắt đầu vào: <fmt:formatDate value="${combo.startDate}" pattern="HH:mm dd/MM/yyyy" />
                                                    </small>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn btn-success btn-lg w-100" onclick="addComboToCart('${combo.comboId}')">
                                                        <i class="fas fa-shopping-cart"></i> Thêm vào giỏ
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                            <a href="${pageContext.request.contextPath}/combo-detail?comboId=${combo.comboId}"
                                               class="btn btn-outline-success w-100">
                                                <i class="fas fa-info-circle"></i> Xem chi tiết
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                    <c:if test="${empty activeCombo}">
                        <div class="text-center py-5">
                            <h3 class="text-muted"><i class="fas fa-clock"></i> Hiện không có combo khuyến mãi nào đang diễn ra</h3>
                            <p>Vui lòng quay lại sau để không bỏ lỡ những ưu đãi hấp dẫn!</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </section>
    </main>

    <!-- Back to Top Button -->
    <button id="backToTop" class="btn btn-success rounded-circle shadow" style="position: fixed; bottom: 20px; right: 20px; display: none; z-index: 1000;">
        <i class="fas fa-arrow-up"></i>
    </button>

    <!-- Footer -->
    <footer class="bg-dark text-light py-5">
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h5>Về Fruit Store</h5>
                    <p>Cung cấp các loại trái cây tươi ngon, chất lượng cao với giá cả hợp lý.</p>
                </div>
                <div class="col-md-4">
                    <h5>Liên hệ</h5>
                    <ul class="list-unstyled">
                        <li><i class="fas fa-phone me-2"></i> 0123 456 789</li>
                        <li><i class="fas fa-envelope me-2"></i> contact@fruitstore.com</li>
                        <li><i class="fas fa-map-marker-alt me-2"></i> 123 Đường ABC, Quận XYZ, TP.HCM</li>
                    </ul>
                </div>
                <div class="col-md-4">
                    <h5>Theo dõi chúng tôi</h5>
                    <div class="social-links">
                        <a href="#" class="text-light me-3"><i class="fab fa-facebook-f"></i></a>
                        <a href="#" class="text-light me-3"><i class="fab fa-instagram"></i></a>
                        <a href="#" class="text-light"><i class="fab fa-youtube"></i></a>
                    </div>
                </div>
            </div>
            <hr class="my-4">
            <div class="text-center">
                <p class="mb-0">&copy; 2025 Fruit Store. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <!-- Scripts -->
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- jQuery UI -->
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- Import modal thêm vào giỏ hàng -->
    <%@ include file="common/add-to-cart-modal.jsp" %>
    <!-- FlipDown JS -->
    <script src="https://cdn.jsdelivr.net/npm/flipdown@0.3.2/dist/flipdown.min.js"></script>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
    <script>
        // Back to Top Button
        $(window).scroll(function() {
            if ($(this).scrollTop() > 300) {
                $('#backToTop').fadeIn();
            } else {
                $('#backToTop').fadeOut();
            }
        });

        $('#backToTop').click(function() {
            $('html, body').animate({scrollTop : 0}, 800);
            return false;
        });
    </script>
    <!-- Swiper JS -->
    <script src="https://cdn.jsdelivr.net/npm/swiper@10/swiper-bundle.min.js"></script>
    <script>
      document.addEventListener('DOMContentLoaded', function() {
        // Swiper initialization
        new Swiper('.best-selling-swiper', {
          slidesPerView: 2,
          spaceBetween: 24,
          navigation: {
            nextEl: '.best-selling-next',
            prevEl: '.best-selling-prev',
          },
          breakpoints: {
            0: { slidesPerView: 1 },
            768: { slidesPerView: 2 }
          },
          grabCursor: true,
        });
        new Swiper('.new-products-swiper', {
          slidesPerView: 2,
          spaceBetween: 24,
          navigation: {
            nextEl: '.new-products-next',
            prevEl: '.new-products-prev',
          },
          breakpoints: {
            0: { slidesPerView: 1 },
            768: { slidesPerView: 2 }
          },
          grabCursor: true,
        });

        // FlipDown Timer
        const timerElement = document.querySelector('.timer[data-end]');
        if (timerElement) {
            const endTime = parseInt(timerElement.getAttribute('data-end'));
            
            // Validate end time
            if (isNaN(endTime) || endTime <= 0) {
                document.getElementById('flipdown').innerHTML = '<div class="text-center text-warning"><h4>Thời gian không hợp lệ</h4></div>';
                return;
            }
            
            // Create a proper Date object from the server timestamp
            const endDate = new Date(endTime);
            
            // Validate the date
            if (isNaN(endDate.getTime())) {
                document.getElementById('flipdown').innerHTML = '<div class="text-center text-warning"><h4>Thời gian không hợp lệ</h4></div>';
                return;
            }
            
            // Check if the end time is in the future
            const now = new Date();
            if (endDate <= now) {
                document.getElementById('flipdown').innerHTML = '<div class="text-center text-danger"><h4>Ưu đãi đã kết thúc</h4></div>';
                return;
            }
            
            // Convert to Unix timestamp (seconds) for FlipDown
            const unixEndTime = Math.floor(endDate.getTime() / 1000);
            
            // Khởi tạo FlipDown
            try {
                const flipdown = new FlipDown(unixEndTime, {
                    theme: 'dark',
                    headings: ['Ngày', 'Giờ', 'Phút', 'Giây']
                })
                .start()
                .ifEnded(() => {
                    // Tự động reload khi hết thời gian
                    location.reload();
                });
            } catch (error) {
                document.getElementById('flipdown').innerHTML = '<div class="text-center text-danger"><h4>Lỗi khởi tạo timer</h4></div>';
            }
        }
      });
    </script>
</body>
</html>
