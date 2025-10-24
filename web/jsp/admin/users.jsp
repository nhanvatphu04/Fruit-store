<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Management - FruitStore</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <!-- DataTables CSS -->
        <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <!-- SweetAlert2 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/admin.css" rel="stylesheet">
        <style>
            .avatar-sm {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                object-fit: cover;
            }
        </style>
    </head>
    <body>
        <!-- Sidebar -->
        <div class="sidebar">
            <h3 class="text-white text-center mb-4">Admin Panel</h3>
            <nav>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link">
                    <i class="fas fa-chart-line me-2"></i> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link active">
                    <i class="fas fa-users me-2"></i> Users
                </a>
                <a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link">
                    <i class="fas fa-apple-alt me-2"></i> Products
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link">
                    <i class="fas fa-shopping-cart me-2"></i> Orders
                </a>
                <a href="${pageContext.request.contextPath}/admin/discounts" class="sidebar-link">
                    <i class="fas fa-tag me-2"></i> Discounts
                </a>
                <a href="${pageContext.request.contextPath}/admin/combos" class="sidebar-link">
                    <i class="fas fa-boxes me-2"></i> Combos
                </a>
            </nav>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Quản lý người dùng</h2>
            </div>

            <!-- Filter -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <select class="form-select" id="roleFilter">
                                <option value="">Tất cả vai trò</option>
                                <option value="customer">customer</option>
                                <option value="admin">admin</option>
                            </select>
                        </div>
                        <div class="col-md-8">
                            <div class="input-group">
                                <input type="text" class="form-control" id="searchInput" 
                                       placeholder="Tìm kiếm theo tên, email, số điện thoại...">
                                <button class="btn btn-success" type="button">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Users Table -->
            <div class="card">
                <div class="card-body">
                    <table id="usersTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Avatar</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Họ tên</th>
                                <th>SĐT</th>
                                <th>Vai trò</th>
                                <th>Ngày tạo</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${users}" var="user">
                                <tr>
                                    <td>${user.userId}</td>
                                    <td>
                                        <img src="${pageContext.request.contextPath}${not empty user.avatarUrl ? user.avatarUrl : '/assets/images/avatars/default.png'}" 
                                             alt="Avatar" class="avatar-sm">
                                    </td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>${user.fullName}</td>
                                    <td>${user.phone}</td>
                                    <td>
                                        <span class="badge bg-${user.role == 'admin' ? 'danger' : 'success'}">
                                            ${user.role}
                                        </span>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-primary" type="button" onclick="openUserModal('${user.userId}')">
                                            <i class="fas fa-edit"></i> Đổi vai trò
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- User Modal -->
        <div class="modal fade" id="userModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">Cập nhật vai trò người dùng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="userForm">
                            <input type="hidden" id="userId" name="userId">
                            
                            <div class="mb-3">
                                <label class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" readonly>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Vai trò</label>
                                <select class="form-select" id="role" name="role" required>
                                    <option value="customer">customer</option>
                                    <option value="admin">admin</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="button" class="btn btn-success" onclick="saveUser()">Lưu</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
            let userTable;
            
            $(document).ready(function() {
                // Initialize DataTable
                userTable = $('#usersTable').DataTable({
                    order: [[0, 'desc']], // Sort by ID descending
                    pageLength: 10
                });

                // Apply filters
                $('#roleFilter').change(function() {
                    userTable.draw();
                });

                $('#searchInput').keyup(function() {
                    userTable.draw();
                });

                // Custom filtering function
                $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
                    let roleFilter = $('#roleFilter').val();
                    let searchText = $('#searchInput').val().toLowerCase();
                    
                    let role = data[6].toLowerCase(); // Role column
                    let matchesRole = !roleFilter || role.includes(roleFilter);
                    
                    let matchesSearch = !searchText || 
                                      data[2].toLowerCase().includes(searchText) || // Username
                                      data[3].toLowerCase().includes(searchText) || // Email
                                      data[4].toLowerCase().includes(searchText) || // Full Name
                                      data[5].toLowerCase().includes(searchText);   // Phone
                    
                    return matchesRole && matchesSearch;
                });
            });

            // Open user modal for role update
            function openUserModal(userId) {
                if (!userId) return;
                
                console.log('Fetching user data for ID:', userId);
                
                // Load user data
                $.ajax({
                    url: '${pageContext.request.contextPath}/admin/users/get/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function(response) {
                        console.log('Received user data:', response);
                        if (response.success === false) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: response.message || 'Không thể tải thông tin người dùng'
                            });
                            return;
                        }
                        
                        if (response && response.username) {
                            $('#userId').val(response.userId);
                            $('#username').val(response.username);
                            $('#role').val(response.role);
                            $('#userModal').modal('show');
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: 'Không nhận được thông tin người dùng hợp lệ'
                            });
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error('Error fetching user:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: 'Không thể tải thông tin người dùng'
                        });
                    }
                }).fail(function(error) {
                    console.error('Error fetching user:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: 'Không thể tải thông tin người dùng'
                    });
                });
            }

            // Save user role
            function saveUser() {
                const userId = $('#userId').val();
                const role = $('#role').val();
                
                console.log('Updating role for user:', userId, 'to:', role);
                
                $.ajax({
                    url: '${pageContext.request.contextPath}/admin/users/update-role',
                    type: 'POST',
                    data: {
                        userId: userId,
                        role: role
                    },
                    success: function(response) {
                        if (response.success) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công',
                                text: 'Cập nhật vai trò thành công!'
                            }).then(() => {
                                window.location.reload();
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi',
                                text: response.message || 'Có lỗi xảy ra khi cập nhật vai trò'
                            });
                        }
                    },
                    error: function() {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: 'Có lỗi xảy ra, vui lòng thử lại'
                        });
                    }
                });
            }
        </script>
    </body>
</html>