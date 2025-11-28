<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm kiếm - ${searchQuery} | Fruit Store</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
    <%@ include file="common/header.jsp" %>

    <main class="py-5">
        <div class="container">
            <h1 class="mb-4">Kết quả tìm kiếm cho "${searchQuery}"</h1>
            
            <c:choose>
                <c:when test="${empty products}">
                    <div class="alert alert-info">
                        Không tìm thấy sản phẩm nào phù hợp với từ khóa "${searchQuery}"
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:forEach items="${products}" var="product">
                            <div class="col-md-3 mb-4">
                                <c:set var="product" value="${product}" scope="request" />
                                <jsp:include page="common/product-card.jsp" />
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <%@ include file="common/footer.jsp" %>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- Import modal thêm vào giỏ hàng -->
    <%@ include file="common/add-to-cart-modal.jsp" %>
</body>
</html>