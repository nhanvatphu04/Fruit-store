package services;

import dao.OrderDAO;
import dao.ProductDAO;
import dao.UserDAO;
import dao.ProductStatsDAO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Order;
import models.Product;
import models.ProductStats;

public class DashboardService {
    private final UserDAO userDAO;
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final ProductStatsDAO productStatsDAO;

    public DashboardService() {
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
        this.productStatsDAO = new ProductStatsDAO();
    }

    // Lấy số lượng người dùng
    public int getTotalUsers() throws SQLException {
        return userDAO.getTotalUsers();
    }

    // Lấy số lượng đơn hàng
    public int getTotalOrders() throws SQLException {
        return orderDAO.getTotalOrders();
    }

    // Lấy tổng doanh thu
    public double getTotalRevenue() throws SQLException {
        return orderDAO.getTotalRevenue();
    }

    // Lấy danh sách đơn hàng gần đây
    public List<Order> getRecentOrders(int limit) throws SQLException {
        return orderDAO.getRecentOrders(limit);
    }

    // Lấy danh sách sản phẩm bán chạy nhất
    public Map<String, Integer> getTopSellingProducts(int limit) throws SQLException {
        List<ProductStats> stats = productStatsDAO.getTopSellingProducts(limit);
        Map<String, Integer> result = new HashMap<>();

        for (ProductStats stat : stats) {
            Product product = productDAO.getProductById(stat.getProductId());
            if (product != null) {
                result.put(product.getName(), stat.getTotalSold());
            }
        }

        return result;
    }

    // Lấy doanh thu theo ngày
    public Map<String, Double> getRevenueByDay(int days) throws SQLException {
        return orderDAO.getRevenueByDay(days);
    }

    // Lấy doanh thu theo tháng
    public Map<String, Double> getRevenueByMonth(int months) throws SQLException {
        return orderDAO.getRevenueByMonth(months);
    }

    // Lấy top khách hàng
    public List<models.CustomerStats> getTopCustomerStats(int limit) throws SQLException {
        return orderDAO.getTopCustomers(limit);
    }

    // Lấy lịch sử mua hàng của khách hàng
    public List<Order> getCustomerOrderHistory(int userId, int limit) throws SQLException {
        return orderDAO.getCustomerPurchaseHistory(userId, limit);
    }

    // Lấy số lượng đơn hàng theo trạng thái
    public int getOrderCountByStatus(String status) throws SQLException {
        return orderDAO.countOrdersByStatus(status);
    }

    // Lấy doanh thu hôm nay
    public double getRevenueTodayAmount() throws SQLException {
        return orderDAO.getRevenueTodayAmount();
    }

    // Lấy doanh thu tháng này
    public double getRevenueThisMonthAmount() throws SQLException {
        return orderDAO.getRevenueThisMonthAmount();
    }

    // Lấy sản phẩm tồn kho thấp
    public List<Product> getLowStockProducts(int limit) throws SQLException {
        return productDAO.getLowStockProducts(limit);
    }
}