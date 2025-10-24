package services;

import dao.ProductDAO;
import models.Product;
import java.util.List;

// Service xử lý nghiệp vụ sản phẩm
public class ProductService {
	private ProductDAO productDAO;

	public ProductService() {
		productDAO = new ProductDAO();
	}

	// Lấy tất cả sản phẩm
	public List<Product> getAllProducts() {
		return productDAO.getAllProducts();
	}

	// Lấy sản phẩm theo id
	public Product getProductById(int id) {
		return productDAO.getProductById(id);
	}

	// Lấy sản phẩm theo danh mục
	public List<Product> getProductsByCategory(int categoryId) {
		return productDAO.getProductsByCategory(categoryId);
	}

	// Tìm kiếm sản phẩm theo từ khoá
	public List<Product> searchProducts(String keyword) {
		return productDAO.searchProducts(keyword);
	}
	
	// Tìm kiếm sản phẩm theo từ khoá với giới hạn số lượng
	public List<Product> searchProducts(String keyword, int limit) {
		return productDAO.searchProducts(keyword, limit);
	}

	// Thêm sản phẩm mới
	public boolean addProduct(Product product) {
		return productDAO.addProduct(product);
	}

	// Cập nhật thông tin sản phẩm
	public boolean updateProduct(Product product) {
		return productDAO.updateProduct(product);
	}

	// Xoá sản phẩm
	public boolean deleteProduct(int id) {
		return productDAO.deleteProduct(id);
	}

	// Cập nhật tồn kho sản phẩm
	public boolean updateStock(int productId, int newStock) {
		Product p = productDAO.getProductById(productId);
		if (p != null) {
			p.setStockQuantity(newStock);
			return productDAO.updateProduct(p);
		}
		return false;
	}

	// Lấy danh sách sản phẩm bán chạy với giới hạn số lượng
	public List<Product> getBestSellers(int limit) {
		return productDAO.getBestSellers(limit);
	}

	// Lấy danh sách sản phẩm mới nhất với giới hạn số lượng
	public List<Product> getNewProducts(int limit) {
		return productDAO.getNewProducts(limit);
	}

	// Lấy danh sách sản phẩm flash sale với giới hạn số lượng
	public List<Product> getFlashSaleProducts(int limit) {
		return productDAO.getFlashSaleProducts(limit);
	}

	// Lấy danh sách sản phẩm mới (is_new = true)
	public List<Product> getNewProductsByFlag(int limit) {
		List<Product> allProducts = productDAO.getAllProducts();
		return allProducts.stream()
				.filter(Product::isNew)
				.limit(limit)
				.collect(java.util.stream.Collectors.toList());
	}

	// Lấy danh sách sản phẩm bán chạy (is_best_seller = true)
	public List<Product> getBestSellerProductsByFlag(int limit) {
		List<Product> allProducts = productDAO.getAllProducts();
		return allProducts.stream()
				.filter(Product::isBestSeller)
				.limit(limit)
				.collect(java.util.stream.Collectors.toList());
	}

	// Cập nhật trạng thái sản phẩm mới
	public boolean updateProductNewStatus(int productId, boolean isNew) {
		Product product = productDAO.getProductById(productId);
		if (product != null) {
			product.setNew(isNew);
			return productDAO.updateProduct(product);
		}
		return false;
	}

	// Cập nhật trạng thái sản phẩm bán chạy
	public boolean updateProductBestSellerStatus(int productId, boolean isBestSeller) {
		Product product = productDAO.getProductById(productId);
		if (product != null) {
			product.setBestSeller(isBestSeller);
			return productDAO.updateProduct(product);
		}
		return false;
	}

	// Tính giá sau giảm giá
	public java.math.BigDecimal getDiscountedPrice(Product product) {
		if (product.getDiscountPercent() > 0) {
			java.math.BigDecimal discountAmount = product.getPrice().multiply(
				java.math.BigDecimal.valueOf(product.getDiscountPercent())
					.divide(java.math.BigDecimal.valueOf(100))
			);
			return product.getPrice().subtract(discountAmount);
		}
		return product.getPrice();
	}

	// Kiểm tra sản phẩm có đang giảm giá không
	public boolean isOnSale(Product product) {
		return product.getDiscountPercent() > 0;
	}
}
