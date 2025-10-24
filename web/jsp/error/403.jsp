<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Truy cập bị từ chối | Fruit Store</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- jQuery UI CSS -->
    <link href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    
    <style>
        .error-container {
            text-align: center;
            padding: 4rem 1rem;
            max-width: 600px;
            margin: 0 auto;
        }
        .error-code {
            font-size: 6rem;
            font-weight: bold;
            color: var(--primary-color);
            margin: 0;
            line-height: 1;
        }
        .error-message {
            font-size: 1.5rem;
            color: var(--text-color);
            margin: 1rem 0 2rem;
        }
        .error-image {
            max-width: 300px;
            margin: 2rem 0;
        }
        .home-link {
            display: inline-block;
            padding: 0.5rem 1.5rem;
            font-size: 1.1rem;
            color: white;
            background-color: var(--primary-color);
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }
        .home-link:hover {
            background-color: var(--primary-dark);
        }
    </style>
</head>
<body class="bg-light">
    <!-- Header -->
    <%@ include file="../common/header.jsp" %>

    <div class="error-container">
        <h1 class="error-code">403</h1>
        <p class="error-message">Truy cập bị từ chối</p>
        <img src="${pageContext.request.contextPath}/assets/images/error/403.jpg" alt="403 Error" class="error-image">
        <p>Bạn không có quyền truy cập trang này.</p>
        <a href="${pageContext.request.contextPath}/" class="home-link">
            <i class="fas fa-home"></i> Về trang chủ
        </a>
    </div>

    <!-- Scripts -->
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- jQuery UI -->
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</body>
</html>