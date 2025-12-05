<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Combo Management - FruitStore</title>
<!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <!-- DataTables CSS -->
        <link href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/admin.css" rel="stylesheet">
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
                <a href="${pageContext.request.contextPath}/admin/categories" class="sidebar-link">
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
                <a href="${pageContext.request.contextPath}/admin/combos" class="sidebar-link active">
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
                <h2>Combo Management</h2>
                <button type="button" class="btn btn-success" onclick="openComboModal()">
                    <i class="fas fa-plus me-2"></i>Add New Combo
                </button>
            </div>

            <!-- Combo Table -->
            <div class="card">
                <div class="card-body">
                    <table id="comboTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Original Price</th>
                                <th>Combo Price</th>
                                <th>Valid Period</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${combos}" var="combo">
                                <tr>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/${combo.imageUrl}" alt="${combo.name}" class="img-thumbnail me-2" style="width: 50px; height: 50px;">
                                        ${combo.name}
                                    </td>
                                    <td><fmt:formatNumber value="${combo.originalPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</td>
                                    <td><fmt:formatNumber value="${combo.comboPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</td>
                                    <td>
                                        <fmt:formatDate value="${combo.startDate}" pattern="dd/MM/yyyy"/> -
                                        <fmt:formatDate value="${combo.endDate}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <span class="badge bg-${combo.active ? 'success' : 'danger'}">
                                            ${combo.active ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-primary me-1" onclick="editCombo('${combo.comboId}')">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-danger me-1" onclick="deleteCombo('${combo.comboId}')">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-info" onclick="manageComboItems('${combo.comboId}')">
                                            <i class="fas fa-list"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Combo Modal -->
        <div class="modal fade" id="comboModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">Add New Combo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="comboForm">
                            <input type="hidden" id="comboId" name="comboId">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="name" class="form-label">Combo Name</label>
                                    <input type="text" class="form-control" id="name" name="name" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="imageUrl" class="form-label">Image URL</label>
                                    <input type="url" class="form-control" id="imageUrl" name="imageUrl" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="originalPrice" class="form-label">Original Price</label>
                                    <input type="number" step="0.01" class="form-control" id="originalPrice" name="originalPrice" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="comboPrice" class="form-label">Combo Price</label>
                                    <input type="number" step="0.01" class="form-control" id="comboPrice" name="comboPrice" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="startDate" class="form-label">Start Date</label>
                                    <input type="datetime-local" class="form-control" id="startDate" name="startDate" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="endDate" class="form-label">End Date</label>
                                    <input type="datetime-local" class="form-control" id="endDate" name="endDate" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                            </div>
                            <div class="mb-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="isActive" name="isActive">
                                    <label class="form-check-label" for="isActive">Active</label>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-success" onclick="saveCombo()">Save</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Combo Items Modal -->
        <div class="modal fade" id="comboItemsModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Manage Combo Items</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="comboItemForm" class="mb-3">
                            <input type="hidden" id="itemComboId" name="itemComboId">
                            <div class="row">
                                <div class="col-md-6">
                                    <select class="form-select" id="productId" name="productId" required>
                                        <c:forEach items="${products}" var="product">
                                            <option value="${product.productId}">${product.name} - <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" id="quantity" name="quantity" placeholder="Quantity" required>
                                </div>
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-success w-100" onclick="addComboItem()">Add</button>
                                </div>
                            </div>
                        </form>
                        <table id="comboItemsTable" class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="comboItemsList">
                            </tbody>
                        </table>
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
        <script src="${pageContext.request.contextPath}/assets/js/combo-management.js"></script>
    </body>
</html>