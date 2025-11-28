<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sản phẩm | Fruit Store</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- jQuery UI CSS -->
    <link href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <!-- Thêm contextPath cho JavaScript -->
    <meta name="contextPath" content="${pageContext.request.contextPath}">
    <style>
        .category-sidebar {
            background: #fff;
            border-radius: 8px;
            padding: 1rem;
            box-shadow: var(--shadow);
        }
        
        .category-item {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            transition: all 0.2s;
            color: var(--text-color);
            text-decoration: none;
        }
        
        .category-item:hover, .category-item.active {
            background: var(--primary-color);
            color: #fff;
        }
        
        .category-item i {
            margin-right: 0.5rem;
        }
        
        .product-filters {
            background: #fff;
            padding: 1rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            margin-bottom: 1rem;
        }
        
        .filter-group {
            margin-bottom: 1rem;
        }
        
        .filter-label {
            font-weight: 500;
            margin-bottom: 0.5rem;
        }
        
        .price-slider {
            margin: 0 1rem;
        }
        
        .sort-select {
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 0.5rem;
        }
        
        .pagination .page-item.active .page-link {
            border-color: var(--primary-color);
        }
        
        .pagination .page-link {
            color: var(--primary-color);
        }
        
        .filter-group .btn.active {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
            color: white;
        }
        
        .filter-group .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .product-grid-header {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <%@ include file="common/header.jsp" %>

    <main class="py-5">
        <div class="container">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-lg-3">
                    <!-- Danh mục sản phẩm -->
                    <div class="category-sidebar mb-4">
                        <h4 class="mb-3">Danh mục</h4>
                        <div class="list-group">
                            <a href="${pageContext.request.contextPath}/products" 
                               class="category-item list-group-item ${empty param.category ? 'active' : ''}">
                                <i class="fas fa-apple-alt"></i> Tất cả sản phẩm
                            </a>
                            <c:forEach items="${categories}" var="cat">
                                <a href="${pageContext.request.contextPath}/products?category=${cat.slug}" 
                                   class="category-item list-group-item ${param.category eq cat.slug ? 'active' : ''}">
                                    <i class="${cat.icon}"></i> ${cat.name}
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <!-- Bộ lọc -->
                    <div class="product-filters">
                        <h4 class="mb-3">Lọc sản phẩm</h4>
                        
                        <!-- Quick Filters -->
                        <div class="filter-group">
                            <div class="filter-label">Bộ lọc nhanh:</div>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/products?filter=best-selling" 
                                   class="btn btn-outline-success ${param.filter eq 'best-selling' ? 'active' : ''}">
                                    <i class="fas fa-fire"></i> Bán chạy
                                </a>
                                <a href="${pageContext.request.contextPath}/products?filter=new" 
                                   class="btn btn-outline-success ${param.filter eq 'new' ? 'active' : ''}">
                                    <i class="fas fa-sparkles"></i> Mới nhất
                                </a>
                                <c:if test="${not empty param.filter}">
                                    <a href="${pageContext.request.contextPath}/products" 
                                       class="btn btn-outline-secondary btn-sm">
                                        <i class="fas fa-times"></i> Xóa bộ lọc
                                    </a>
                                </c:if>
                            </div>
                        </div>
                        
                        <!-- Lọc giá -->
                        <div class="filter-group"></div>
                            <div class="filter-label">Khoảng giá:</div>
                            <div class="price-range mb-3">
                                <input type="text" id="priceRange" readonly style="border:0; font-weight:bold;">
                                <div id="price-slider" class="price-slider"></div>
                            </div>
                            <div class="d-grid gap-2">
                                <button class="btn btn-primary" id="filterPriceBtn">
                                    <i class="fas fa-filter"></i> Lọc giá
                                </button>
                                <button class="btn btn-outline-secondary btn-sm" id="clearPriceFilter" style="display: none;">
                                    <i class="fas fa-times"></i> Xóa lọc giá
                                </button>
                            </div>
                        </div>
                        
                        <!-- Sắp xếp -->
                        <div class="filter-group">
                            <div class="filter-label">Sắp xếp theo:</div>
                            <select class="sort-select form-select" id="sortSelect">
                                <option value="default">Mặc định</option>
                                <option value="price_asc">Giá tăng dần</option>
                                <option value="price_desc">Giá giảm dần</option>
                                <option value="name_asc">Tên A-Z</option>
                                <option value="name_desc">Tên Z-A</option>
                                <option value="newest">Mới nhất</option>
                                <option value="oldest">Cũ nhất</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Product Grid -->
                <div class="col-lg-9">
                    <div class="product-grid-header d-flex justify-content-between align-items-center">
                        <h2 class="mb-0">
                            <c:choose>
                                <c:when test="${param.filter eq 'best-selling'}">
                                    <i class="fas fa-fire text-danger"></i> Sản phẩm bán chạy
                                </c:when>
                                <c:when test="${param.filter eq 'new'}">
                                    <i class="fas fa-sparkles text-success"></i> Sản phẩm mới
                                </c:when>
                                <c:when test="${not empty param.category}">
                                    <i class="fas fa-apple-alt text-primary"></i> ${categoryName}
                                </c:when>
                                <c:when test="${not empty filterName}">
                                    <i class="fas fa-filter text-primary"></i> ${filterName}
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-apple-alt text-primary"></i> Tất cả sản phẩm
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not empty param.q}">
                                - Kết quả cho "${param.q}"
                            </c:if>
                        </h2>
                        <div class="d-flex align-items-center gap-3">
                            <small class="text-muted">${products.size()} sản phẩm</small>
                        </div>
                    </div>

                    <div class="row">
                        <c:forEach items="${products}" var="product">
                            <div class="col-md-4 mb-4">
                                <c:set var="product" value="${product}" scope="request" />
                                <jsp:include page="common/product-card.jsp" />
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Phân trang -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Product navigation" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}${not empty param.category ? '&category='.concat(param.category) : ''}${not empty param.filter ? '&filter='.concat(param.filter) : ''}${not empty param.q ? '&q='.concat(param.q) : ''}${not empty param.sort ? '&sort='.concat(param.sort) : ''}${not empty param.min_price ? '&min_price='.concat(param.min_price) : ''}${not empty param.max_price ? '&max_price='.concat(param.max_price) : ''}">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>
                                
                                <c:forEach begin="1" end="${totalPages}" var="page">
                                    <li class="page-item ${currentPage == page ? 'active' : ''}">
                                        <a class="page-link" href="?page=${page}${not empty param.category ? '&category='.concat(param.category) : ''}${not empty param.filter ? '&filter='.concat(param.filter) : ''}${not empty param.q ? '&q='.concat(param.q) : ''}${not empty param.sort ? '&sort='.concat(param.sort) : ''}${not empty param.min_price ? '&min_price='.concat(param.min_price) : ''}${not empty param.max_price ? '&max_price='.concat(param.max_price) : ''}">
                                            ${page}
                                        </a>
                                    </li>
                                </c:forEach>
                                
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}${not empty param.category ? '&category='.concat(param.category) : ''}${not empty param.filter ? '&filter='.concat(param.filter) : ''}${not empty param.q ? '&q='.concat(param.q) : ''}${not empty param.sort ? '&sort='.concat(param.sort) : ''}${not empty param.min_price ? '&min_price='.concat(param.min_price) : ''}${not empty param.max_price ? '&max_price='.concat(param.max_price) : ''}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="common/footer.jsp" %>

    <!-- Scripts -->
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- jQuery UI -->
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- Import modal thêm vào giỏ hàng -->
    <%@ include file="common/add-to-cart-modal.jsp" %>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    <script>
        $(function() {
            const urlParams = new URLSearchParams(window.location.search);

            const minPrice = parseInt(urlParams.get('min_price')) || 0;
            const maxPrice = parseInt(urlParams.get('max_price')) || 1000000;
            const hasPriceFilter = urlParams.get('min_price') || urlParams.get('max_price');

            // ensure buttons won't submit forms
            $("#filterPriceBtn, #clearPriceFilter").attr("type", "button");

            // Price slider
            $("#price-slider").slider({
                range: true,
                min: 0,
                max: 1000000,
                values: [minPrice, maxPrice],
                slide: function(event, ui) {
                    $("#priceRange").val(formatPrice(ui.values[0]) + " - " + formatPrice(ui.values[1]));
                }
            });

            $("#priceRange").val(
                formatPrice($("#price-slider").slider("values", 0)) +
                " - " + formatPrice($("#price-slider").slider("values", 1))
            );

            if (hasPriceFilter) {
                $("#filterPriceBtn").html('<i class="fas fa-filter"></i> Đang lọc giá');
                $("#filterPriceBtn").addClass('btn-success').removeClass('btn-primary');
                $("#clearPriceFilter").show();
            }

            // Clear price filter
            $("#clearPriceFilter").on("click", function(e) {
                e.preventDefault();
                const searchParams = new URLSearchParams(window.location.search);
                searchParams.delete('min_price');
                searchParams.delete('max_price');
                const base = window.location.origin + window.location.pathname;
                const newUrl = base + (searchParams.toString() ? "?" + searchParams.toString() : "");
                window.location.href = newUrl;
            });

            // Apply price filter
            $("#filterPriceBtn").on("click", function(e) {
                e.preventDefault();
                const currentMinPrice = $("#price-slider").slider("values", 0);
                const currentMaxPrice = $("#price-slider").slider("values", 1);
                const searchParams = new URLSearchParams(window.location.search);
                searchParams.set('min_price', currentMinPrice);
                searchParams.set('max_price', currentMaxPrice);
                const base = window.location.origin + window.location.pathname;
                const newUrl = base + "?" + searchParams.toString();
                window.location.href = newUrl;
            });

            // Sort change
            $("#sortSelect").change(function() {
                const searchParams = new URLSearchParams(window.location.search);
                searchParams.set('sort', $(this).val());
                const base = window.location.origin + window.location.pathname;
                window.location.href = base + "?" + searchParams.toString();
            });

            // Only handle anchor buttons (avoid catching other .btn)
            $('.filter-group a.btn').on('click', function(e) {
                e.preventDefault();
                window.location.href = $(this).attr('href');
            });

            // set initial select
            const sortValue = urlParams.get('sort');
            if (sortValue) $("#sortSelect").val(sortValue);
        });

        // Price formatter
        function formatPrice(price) {
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(price);
        }
    </script>

</body>
</html>