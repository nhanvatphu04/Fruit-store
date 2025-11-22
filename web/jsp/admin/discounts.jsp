<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Discount Management - FruitStore</title>
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
                <a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link">
                    <i class="fas fa-apple-alt me-2"></i> Products
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link">
                    <i class="fas fa-shopping-cart me-2"></i> Orders
                </a>
                <a href="${pageContext.request.contextPath}/admin/discounts" class="sidebar-link active">
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
                <h2>Discount Management</h2>
                <button type="button" class="btn btn-success" onclick="openDiscountModal()">
                    <i class="fas fa-plus me-2"></i>Add New Discount
                </button>
            </div>

            <!-- Discount Table -->
            <div class="card">
                <div class="card-body">
                    <table id="discountTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Type</th>
                                <th>Value</th>
                                <th>Min Amount</th>
                                <th>Limit</th>
                                <th>Used</th>
                                <th>Valid Period</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${discounts}" var="discount">
                                <tr>
                                    <td>${discount.code}</td>
                                    <td>${discount.discountType}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${discount.discountType == 'percentage'}">
                                                ${discount.discountValue}%
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatNumber value="${discount.discountValue}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatNumber value="${discount.minOrderAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</td>
                                    <td>${discount.usageLimit}</td>
                                    <td>${discount.usedCount}</td>
                                    <td>
                                        <fmt:formatDate value="${discount.startDate}" pattern="dd/MM/yyyy"/> - 
                                        <fmt:formatDate value="${discount.endDate}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <span class="badge bg-${discount.active ? 'success' : 'danger'}">
                                            ${discount.active ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-primary"
                                                onclick="editDiscount('${discount.discountId}')">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-danger"
                                                onclick="deleteDiscount('${discount.discountId}')">
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

        <!-- Discount Modal -->
        <div class="modal fade" id="discountModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">Add New Discount</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="discountForm">
                            <input type="hidden" id="discountId" name="discountId">
                            <input type="hidden" id="usedCount" name="usedCount" value="0">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="code" class="form-label">Code</label>
                                    <input type="text" class="form-control" id="code" name="code" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="discountType" class="form-label">Type</label>
                                    <select class="form-select" id="discountType" name="discountType" required>
                                        <option value="percentage">Percentage</option>
                                        <option value="fixed_amount">Fixed Amount</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="discountValue" class="form-label">Value</label>
                                    <input type="number" class="form-control" id="discountValue" name="discountValue" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="minOrderAmount" class="form-label">Minimum Order Amount</label>
                                    <input type="number" class="form-control" id="minOrderAmount" name="minOrderAmount" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="maxDiscountAmount" class="form-label">Maximum Discount Amount</label>
                                    <input type="number" class="form-control" id="maxDiscountAmount" name="maxDiscountAmount" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="usageLimit" class="form-label">Usage Limit</label>
                                    <input type="number" class="form-control" id="usageLimit" name="usageLimit" required>
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
                                    <input class="form-check-input" type="checkbox" id="isActive" name="isActive" checked>
                                    <label class="form-check-label" for="isActive">
                                        Active
                                    </label>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-success" onclick="saveDiscount()">Save</button>
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
        <script src="${pageContext.request.contextPath}/assets/js/discount-management.js"></script>
    </body>
</html>