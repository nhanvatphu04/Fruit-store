<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Management - Admin Panel</title>
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
                <a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link active">
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
                <h2>Order Management</h2>
                
                <!-- Filter Buttons -->
                <div class="btn-group">
                    <button type="button" class="btn btn-outline-primary filter-btn" data-status="all">
                        All Orders
                    </button>
                    <button type="button" class="btn btn-outline-warning filter-btn" data-status="pending">
                        Pending
                    </button>
                    <button type="button" class="btn btn-outline-success filter-btn" data-status="completed">
                        Completed
                    </button>
                    <button type="button" class="btn btn-outline-danger filter-btn" data-status="cancelled">
                        Cancelled
                    </button>
                </div>
            </div>

            <!-- Orders Table -->
            <div class="card">
                <div class="card-body">
                    <table id="ordersTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Total Amount</th>
                                <th>Status</th>
                                <th>Order Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orders}" var="order">
                                <tr>
                                    <td>#${order.orderId}</td>
                                    <td>${order.userName}</td>
                                    <td>$${order.totalAmount}</td>
                                    <td>
                                        <span class="badge bg-${order.status == 'completed' ? 'success' : 
                                                              order.status == 'pending' ? 'warning' : 'danger'}">
                                            ${order.status}
                                        </span>
                                    </td>
                                    <td>${order.orderDate}</td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-primary view-order" 
                                                data-id="${order.orderId}" title="View Details">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                        <button type="button" class="btn btn-sm btn-success update-status" 
                                                data-id="${order.orderId}" title="Update Status">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- View Order Modal -->
        <div class="modal fade" id="viewOrderModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Order Details</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Order Info -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h6>Order Information</h6>
                                <p class="mb-1">Order ID: <span id="modalOrderId"></span></p>
                                <p class="mb-1">Date: <span id="modalOrderDate"></span></p>
                                <p class="mb-1">Status: <span id="modalOrderStatus"></span></p>
                            </div>
                            <div class="col-md-6">
                                <h6>Customer Information</h6>
                                <p class="mb-1">Name: <span id="modalCustomerName"></span></p>
                                <p class="mb-1">Email: <span id="modalCustomerEmail"></span></p>
                                <p class="mb-1">Phone: <span id="modalCustomerPhone"></span></p>
                            </div>
                        </div>
                        
                        <!-- Order Items -->
                        <h6>Order Items</h6>
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody id="modalOrderItems">
                                    <!-- Items will be inserted here -->
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3" class="text-end"><strong>Total:</strong></td>
                                        <td id="modalOrderTotal"></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
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
        
        <!-- Define contextPath for JavaScript -->
        <script>
            const contextPath = '${pageContext.request.contextPath}';
        </script>
        
        <script src="${pageContext.request.contextPath}/assets/js/order-management.js"></script>
    </body>
</html>