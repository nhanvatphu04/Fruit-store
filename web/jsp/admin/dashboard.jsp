<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard - FruitStore</title>
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
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link active">
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
            <h2 class="mb-4">Dashboard Overview</h2>
            
            <!-- Stats Cards -->
            <div class="row">
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-users text-primary"></i>
                        <h3 class="text-primary">${totalUsers}</h3>
                        <p class="text-muted mb-0">Total Users</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-shopping-cart text-success"></i>
                        <h3 class="text-success">${totalOrders}</h3>
                        <p class="text-muted mb-0">Total Orders</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-money-bill-wave text-warning"></i>
                        <h3 class="text-warning"><fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</h3>
                        <p class="text-muted mb-0">Total Revenue</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-exclamation-triangle text-danger"></i>
                        <h3 class="text-danger">${cancellationRate}%</h3>
                        <p class="text-muted mb-0">Cancellation Rate</p>
                    </div>
                </div>
            </div>

            <!-- Secondary Stats Cards -->
            <div class="row mt-3">
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-calendar-day text-info"></i>
                        <h3 class="text-info"><fmt:formatNumber value="${revenueTodayAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</h3>
                        <p class="text-muted mb-0">Revenue Today</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-calendar-alt text-info"></i>
                        <h3 class="text-info"><fmt:formatNumber value="${revenueThisMonthAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</h3>
                        <p class="text-muted mb-0">Revenue This Month</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-box text-warning"></i>
                        <h3 class="text-warning">${lowStockCount}</h3>
                        <p class="text-muted mb-0">Low Stock Products</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <i class="fas fa-check-circle text-success"></i>
                        <h3 class="text-success">${completedOrdersCount}</h3>
                        <p class="text-muted mb-0">Completed Orders</p>
                    </div>
                </div>
            </div>

            <!-- Revenue Growth Chart -->
            <div class="row mt-4">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">Revenue Growth</h5>
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-sm btn-outline-primary active" id="btnByDay">By Day</button>
                                <button type="button" class="btn btn-sm btn-outline-primary" id="btnByMonth">By Month</button>
                            </div>
                        </div>
                        <div class="card-body">
                            <canvas id="revenueChart" style="height: 300px;"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Order Status Chart -->
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Order Status Distribution</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="orderStatusChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Low Stock Products Table -->
            <div class="row mt-4">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Low Stock Products</h5>
                        </div>
                        <div class="card-body">
                            <table id="lowStockTable" class="table table-striped table-sm">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Stock</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${lowStockProducts}" var="product">
                                        <tr>
                                            <td>${product.name}</td>
                                            <td>
                                                <span class="badge bg-${product.stockQuantity <= 5 ? 'danger' : 'warning'}">
                                                    ${product.stockQuantity}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Recent Orders Table -->
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Recent Orders</h5>
                        </div>
                        <div class="card-body">
                            <table id="recentOrdersTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Customer</th>
                                        <th>Amount</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentOrders}" var="order">
                                        <tr>
                                            <td>#${order.orderId}</td>
                                            <td>${order.userName}</td>
                                            <td><fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</td>
                                            <td>
                                                <span class="badge bg-${order.status == 'completed' ? 'success' : 
                                                                      order.status == 'pending' ? 'warning' : 'secondary'}">
                                                    ${order.status}
                                                </span>
                                            </td>
                                            <td>${order.orderDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Top Selling Products Chart -->
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Top Selling Products</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="topProductsChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Top Customers Table -->
            <div class="row mt-4">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Top Customers</h5>
                        </div>
                        <div class="card-body">
                            <table id="topCustomersTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Customer Name</th>
                                        <th>Username</th>
                                        <th>Total Orders</th>
                                        <th>Total Spend</th>
                                        <th>Last Order</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${topCustomers}" var="customer" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td>
                                            <td>${customer.fullName != null ? customer.fullName : '-'}</td>
                                            <td>${customer.username}</td>
                                            <td>${customer.totalOrders}</td>
                                            <td><fmt:formatNumber value="${customer.totalSpend}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</td>
                                            <td>${customer.lastOrderDate}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
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
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        
        <script>
            // Initialize DataTables
            $(document).ready(function() {
                $('#recentOrdersTable').DataTable({
                    order: [[4, 'desc']], // Sort by date column descending
                    pageLength: 5,
                    lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]]
                });

                $('#topCustomersTable').DataTable({
                    order: [[4, 'desc']], // Sort by total spend descending
                    pageLength: 10,
                    lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]]
                });

                // Back to Top button functionality
                $(window).scroll(function() {
                    if ($(this).scrollTop() > 100) {
                        $('#backToTop').fadeIn();
                    } else {
                        $('#backToTop').fadeOut();
                    }
                });

                $('#backToTop').click(function() {
                    $('html, body').animate({scrollTop: 0}, 'slow');
                    return false;
                });
            });
            
            // Top Products Chart
            const ctx = document.getElementById('topProductsChart').getContext('2d');
            const labels = JSON.parse('${topProductsLabels}');
            const data = JSON.parse('${topProductsData}');
            
            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        data: data,
                        backgroundColor: [
                            '#198754', // green
                            '#0d6efd', // blue
                            '#ffc107', // yellow
                            '#dc3545', // red
                            '#6c757d'  // gray
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        },
                        title: {
                            display: false
                        }
                    },
                    cutout: '70%'
                }
            });

            // Order Status Chart
            const orderStatusCtx = document.getElementById('orderStatusChart').getContext('2d');
            const orderStats = JSON.parse('${orderStats}');
            
            new Chart(orderStatusCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Completed', 'Pending', 'Cancelled'],
                    datasets: [{
                        data: [orderStats.completed || 0, orderStats.pending || 0, orderStats.cancelled || 0],
                        backgroundColor: [
                            '#198754', // green - completed
                            '#ffc107', // yellow - pending
                            '#dc3545'  // red - cancelled
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        },
                        title: {
                            display: false
                        }
                    },
                    cutout: '70%'
                }
            });

            // Revenue Chart with Day/Month Toggle
            const revenueCtx = document.getElementById('revenueChart').getContext('2d');
            
            // Parse data from server
            const revenueDayLabels = JSON.parse('${revenueDayLabels}');
            const revenueDayData = JSON.parse('${revenueDayData}');
            const revenueMonthLabels = JSON.parse('${revenueMonthLabels}');
            const revenueMonthData = JSON.parse('${revenueMonthData}');
            
            let currentMode = 'day';
            
            const revenueChart = new Chart(revenueCtx, {
                type: 'line',
                data: {
                    labels: revenueDayLabels,
                    datasets: [{
                        label: 'Revenue (đ)',
                        data: revenueDayData,
                        borderColor: '#198754',
                        backgroundColor: 'rgba(25, 135, 84, 0.1)',
                        tension: 0.4,
                        fill: true
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        },
                        title: {
                            display: false
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    if (context.parsed.y !== null) {
                                        label += context.parsed.y.toLocaleString('vi-VN') + '₫';
                                    }
                                    return label;
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return value.toLocaleString('vi-VN') + 'đ';
                                }
                            }
                        }
                    }
                }
            });

            // Toggle between day and month view
            document.getElementById('btnByDay').addEventListener('click', function() {
                if (currentMode !== 'day') {
                    currentMode = 'day';
                    revenueChart.data.labels = revenueDayLabels;
                    revenueChart.data.datasets[0].data = revenueDayData;
                    revenueChart.update();
                    
                    document.getElementById('btnByDay').classList.add('active');
                    document.getElementById('btnByMonth').classList.remove('active');
                }
            });

            document.getElementById('btnByMonth').addEventListener('click', function() {
                if (currentMode !== 'month') {
                    currentMode = 'month';
                    revenueChart.data.labels = revenueMonthLabels;
                    revenueChart.data.datasets[0].data = revenueMonthData;
                    revenueChart.update();
                    
                    document.getElementById('btnByMonth').classList.add('active');
                    document.getElementById('btnByDay').classList.remove('active');
                }
            });
        </script>
    </body>
</html>