<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng nhập - FruitStore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
        <style>
            .login-container {
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #f8f9fa;
            }
            .login-form {
                background: white;
                padding: 2rem;
                border-radius: 10px;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                width: 100%;
                max-width: 400px;
            }
            .login-form .btn-primary {
                background-color: #28a745;
                border-color: #28a745;
            }
            .login-form .btn-primary:hover {
                background-color: #218838;
                border-color: #1e7e34;
            }
            .login-form .register-link {
                color: #fd7e14;
            }
            .login-form .register-link:hover {
                color: #d63384;
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="login-form">
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-success mb-3">
                    <i class="fas fa-home me-2"></i>Về trang chủ
                </a>
                <h2 class="text-center mb-4">Đăng nhập</h2>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/login" method="POST">
                    <div class="mb-3">
                        <label for="username" class="form-label">Tên đăng nhập</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">Mật khẩu</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100 mb-3">
                        <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
                    </button>
                    
                    <div class="text-center">
                        <span>Chưa có tài khoản? </span>
                        <a href="${pageContext.request.contextPath}/register" class="register-link">
                            Đăng ký ngay
                        </a>
                    </div>
                </form>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </body>
</html>