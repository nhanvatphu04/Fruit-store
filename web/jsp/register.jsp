<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký - FruitStore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
        <style>
            .register-container {
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #f8f9fa;
            }
            .register-form {
                background: white;
                padding: 2rem;
                border-radius: 10px;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                width: 100%;
                max-width: 400px;
            }
            .register-form .btn-primary {
                background-color: #fd7e14;
                border-color: #fd7e14;
            }
            .register-form .btn-primary:hover {
                background-color: #e66a06;
                border-color: #d96305;
            }
            .register-form .login-link {
                color: #28a745;
            }
            .register-form .login-link:hover {
                color: #218838;
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <div class="register-container">
            <div class="register-form">
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-success mb-3">
                    <i class="fas fa-home me-2"></i>Về trang chủ
                </a>
                <h2 class="text-center mb-4">Đăng ký tài khoản</h2>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">
                    <div class="mb-3">
                        <label for="username" class="form-label">Tên đăng nhập</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                            <input type="text" class="form-control" id="username" name="username" 
                                   required pattern="[a-zA-Z0-9]{6,20}"
                                   title="Tên đăng nhập phải từ 6-20 ký tự, chỉ gồm chữ và số">
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">Mật khẩu</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" 
                                   required pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                                   title="Mật khẩu phải có ít nhất 8 ký tự, bao gồm cả chữ và số">
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary w-100 mb-3">
                        <i class="fas fa-user-plus me-2"></i>Đăng ký
                    </button>
                    
                    <div class="text-center">
                        <span>Đã có tài khoản? </span>
                        <a href="${pageContext.request.contextPath}/login" class="login-link">
                            Đăng nhập
                        </a>
                    </div>
                </form>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        
        <script>
            $(document).ready(function() {
                $('#registerForm').submit(function(e) {
                    var password = $('#password').val();
                    var confirmPassword = $('#confirmPassword').val();
                    
                    if (password !== confirmPassword) {
                        e.preventDefault();
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Mật khẩu xác nhận không khớp!'
                        });
                    }
                });
            });
        </script>
    </body>
</html>