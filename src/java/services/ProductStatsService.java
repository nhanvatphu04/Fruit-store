package services;

import dao.ProductStatsDAO;
import models.ProductStats;
import java.util.List;

// Service xử lý nghiệp vụ thống kê sản phẩm
public class ProductStatsService {
    private ProductStatsDAO productStatsDAO;

    public ProductStatsService() {
        productStatsDAO = new ProductStatsDAO();
    }

    // Lấy thống kê theo product_id
    public ProductStats getProductStatsById(int productId) {
        return productStatsDAO.getProductStatsById(productId);
    }

    // Lấy tất cả thống kê
    public List<ProductStats> getAllProductStats() {
        return productStatsDAO.getAllProductStats();
    }

    // Thêm thống kê mới
    public boolean addProductStats(ProductStats productStats) {
        return productStatsDAO.addProductStats(productStats);
    }

    // Cập nhật thống kê
    public boolean updateProductStats(ProductStats productStats) {
        return productStatsDAO.updateProductStats(productStats);
    }

    // Cập nhật số lượng đã bán (khi có đơn hàng mới)
    public boolean updateTotalSold(int productId, int additionalSold) {
        return productStatsDAO.updateTotalSold(productId, additionalSold);
    }

    // Xóa thống kê
    public boolean deleteProductStats(int productId) {
        return productStatsDAO.deleteProductStats(productId);
    }

    // Khởi tạo thống kê cho sản phẩm mới
    public boolean initializeProductStats(int productId) {
        ProductStats productStats = new ProductStats(productId, 0, null);
        return productStatsDAO.addProductStats(productStats);
    }

    // Lấy top sản phẩm bán chạy theo thống kê
    public List<ProductStats> getTopSellingProducts(int limit) {
        List<ProductStats> allStats = productStatsDAO.getAllProductStats();
        return allStats.stream()
                .sorted((a, b) -> Integer.compare(b.getTotalSold(), a.getTotalSold()))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }
}
