<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Category Management - Admin Panel</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <!-- DataTables -->
        <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <!-- SweetAlert2 -->
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/admin.css" rel="stylesheet">
        <style>
            .category-image {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 5px;
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
                <a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link">
                    <i class="fas fa-users me-2"></i> Users
                </a>
                <a href="${pageContext.request.contextPath}/admin/categories" class="sidebar-link active">
                    <i class="fas fa-list me-2"></i> Categories
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
            
            <!-- Home Button -->
            <a href="${pageContext.request.contextPath}/" class="sidebar-link mt-auto">
                <i class="fas fa-home me-2"></i> Back to Home
            </a>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Category Management</h2>
                <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
                    <i class="fas fa-plus me-2"></i>Add New Category
                </button>
            </div>

            <!-- Categories Table -->
            <div class="card">
                <div class="card-body">
                    <table id="categoriesTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Slug</th>
                                <th>Icon</th>
                                <th>Description</th>
                                <th>Created At</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${categories}" var="category">
                                <tr>
                                    <td>${category.categoryId}</td>
                                    <td>
                                        <c:set var="imgUrl" value="${category.imageUrl}" />
                                        <c:if test="${not fn:startsWith(imgUrl, '/')}">
                                            <c:set var="imgUrl" value="/${imgUrl}" />
                                        </c:if>
                                        <img src="${pageContext.request.contextPath}${empty imgUrl ? '/assets/images/categories/default.png' : imgUrl}" class="category-image" alt="${category.name}">
                                    </td>
                                    <td>${category.name}</td>
                                    <td><code>${category.slug}</code></td>
                                    <td>
                                        <c:if test="${not empty category.icon}">
                                            <i class="${category.icon}"></i> ${category.icon}
                                        </c:if>
                                    </td>
                                    <td>${empty category.description ? '-' : category.description}</td>
                                    <td><fmt:formatDate value="${category.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-primary edit-category" data-id="${category.categoryId}">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-danger delete-category" data-id="${category.categoryId}">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Add Category Modal -->
        <div class="modal fade" id="addCategoryModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Category</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addCategoryForm" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label class="form-label">Category Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="name" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Description</label>
                                <textarea class="form-control" name="description" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Icon Class <span class="text-muted">(Font Awesome)</span></label>
                                <input type="text" class="form-control" name="icon" placeholder="e.g., fas fa-apple-alt">
                                <small class="text-muted d-block mt-2">
                                    <i class="fas fa-info-circle"></i> 
                                    Use Font Awesome icon classes. Examples: fas fa-apple-alt, fas fa-carrot, fas fa-lemon
                                </small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Slug</label>
                                <input type="text" class="form-control" name="slug" placeholder="Leave empty to auto-generate">
                                <small class="text-muted">Leave empty to auto-generate from category name</small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Category Image</label>
                                <input type="file" class="form-control" name="image" accept="image/*" required>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" form="addCategoryForm" class="btn btn-success">Add Category</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Category Modal -->
        <div class="modal fade" id="editCategoryModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Category</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editCategoryForm" enctype="multipart/form-data">
                            <input type="hidden" name="categoryId">
                            <div class="mb-3">
                                <label class="form-label">Category Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="name" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Description</label>
                                <textarea class="form-control" name="description" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Icon Class <span class="text-muted">(Font Awesome)</span></label>
                                <input type="text" class="form-control" name="icon" placeholder="e.g., fas fa-apple-alt">
                                <small class="text-muted d-block mt-2">
                                    <i class="fas fa-info-circle"></i> 
                                    Use Font Awesome icon classes. Examples: fas fa-apple-alt, fas fa-carrot, fas fa-lemon
                                </small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Slug</label>
                                <input type="text" class="form-control" name="slug">
                                <small class="text-muted">Leave empty to keep current slug</small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Category Image</label>
                                <input type="file" class="form-control" name="image" accept="image/*">
                                <small class="text-muted">Leave empty to keep current image</small>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" form="editCategoryForm" class="btn btn-primary">Update Category</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Back to Top Button -->
        <button id="backToTop" class="btn btn-success rounded-circle" title="Back to Top">
            <i class="fas fa-arrow-up"></i>
        </button>

        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        
        <!-- Define contextPath for JavaScript -->
        <script>
            const contextPath = '${pageContext.request.contextPath}';
        </script>
        
        <script src="${pageContext.request.contextPath}/assets/js/category-management.js"></script>
    </body>
</html>
