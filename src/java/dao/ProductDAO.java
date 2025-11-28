package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Product;
import models.Category;
import utils.DbConnect;

// DAO cho sản phẩm
public class ProductDAO {

	// Lấy sản phẩm theo id
	public Product getProductById(int id) {
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p LEFT JOIN categories c ON p.category_id = c.category_id LEFT JOIN product_stats ps ON p.product_id = ps.product_id WHERE p.product_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapResultSetToProductWithCategory(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Lấy tất cả sản phẩm
	public List<Product> getAllProducts() {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p "
			+ "LEFT JOIN categories c ON p.category_id = c.category_id "
			+ "LEFT JOIN product_stats ps ON p.product_id = ps.product_id";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToProductWithCategory(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy sản phẩm bán chạy nhất
	public List<Product> getBestSellers(int limit) {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p " +
					"INNER JOIN order_items oi ON p.product_id = oi.product_id " +
					"LEFT JOIN categories c ON p.category_id = c.category_id " +
					"LEFT JOIN product_stats ps ON p.product_id = ps.product_id " +
					"GROUP BY p.product_id " +
					"ORDER BY SUM(oi.quantity) DESC " +
					"LIMIT ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToProductWithCategory(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy sản phẩm mới nhất
	public List<Product> getNewProducts(int limit) {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p LEFT JOIN categories c ON p.category_id = c.category_id LEFT JOIN product_stats ps ON p.product_id = ps.product_id ORDER BY p.created_at DESC LIMIT ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToProductWithCategory(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy sản phẩm đang flash sale
	public List<Product> getFlashSaleProducts(int limit) {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p LEFT JOIN categories c ON p.category_id = c.category_id LEFT JOIN product_stats ps ON p.product_id = ps.product_id WHERE p.discount_percent > 0 ORDER BY p.discount_percent DESC LIMIT ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToProductWithCategory(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy sản phẩm theo danh mục
	public List<Product> getProductsByCategory(int categoryId) {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p LEFT JOIN categories c ON p.category_id = c.category_id LEFT JOIN product_stats ps ON p.product_id = ps.product_id WHERE p.category_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, categoryId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToProductWithCategory(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Tìm kiếm sản phẩm theo tên hoặc mô tả
	public List<Product> searchProducts(String keyword) {
		return searchProducts(keyword, Integer.MAX_VALUE);
	}
	
	// Tìm kiếm sản phẩm theo tên hoặc mô tả với giới hạn số lượng
	public List<Product> searchProducts(String keyword, int limit) {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT p.*, c.name as category_name, COALESCE(ps.total_sold, 0) as total_sold FROM products p LEFT JOIN categories c ON p.category_id = c.category_id LEFT JOIN product_stats ps ON p.product_id = ps.product_id WHERE p.name LIKE ? OR p.description LIKE ? LIMIT ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			String kw = "%" + keyword + "%";
			ps.setString(1, kw);
			ps.setString(2, kw);
			ps.setInt(3, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToProductWithCategory(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm sản phẩm
	public boolean addProduct(Product product) {
		String sql = "INSERT INTO products (name, description, price, stock_quantity, image_url, category_id, discount_percent, is_new, is_best_seller) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setBigDecimal(3, product.getPrice());
			ps.setInt(4, product.getStockQuantity());
			ps.setString(5, product.getImageUrl());
			ps.setInt(6, product.getCategoryId());
			ps.setInt(7, product.getDiscountPercent());
			ps.setBoolean(8, product.isNew());
			ps.setBoolean(9, product.isBestSeller());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật sản phẩm
	public boolean updateProduct(Product product) {
		String sql = "UPDATE products SET name=?, description=?, price=?, stock_quantity=?, image_url=?, category_id=?, discount_percent=?, is_new=?, is_best_seller=? WHERE product_id=?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setBigDecimal(3, product.getPrice());
			ps.setInt(4, product.getStockQuantity());
			ps.setString(5, product.getImageUrl());
			ps.setInt(6, product.getCategoryId());
			ps.setInt(7, product.getDiscountPercent());
			ps.setBoolean(8, product.isNew());
			ps.setBoolean(9, product.isBestSeller());
			ps.setInt(10, product.getProductId());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xoá sản phẩm
	public boolean deleteProduct(int id) {
		String sql = "DELETE FROM products WHERE product_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Hàm tiện ích chuyển ResultSet sang Product (không có category)
	private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
		Product p = new Product();
		p.setProductId(rs.getInt("product_id"));
		p.setName(rs.getString("name"));
		p.setDescription(rs.getString("description"));
		p.setPrice(rs.getBigDecimal("price"));
		p.setStockQuantity(rs.getInt("stock_quantity"));
		p.setImageUrl(rs.getString("image_url"));
		p.setCategoryId(rs.getInt("category_id"));
		p.setDiscountPercent(rs.getInt("discount_percent"));
		p.setNew(rs.getBoolean("is_new"));
		p.setBestSeller(rs.getBoolean("is_best_seller"));
		p.setCreatedAt(rs.getTimestamp("created_at"));
		
		// Đặt total_sold nếu có trong ResultSet
		try {
			p.setTotalSold(rs.getInt("total_sold"));
		} catch (SQLException e) {
			p.setTotalSold(0);
		}
		
		return p;
	}

	// Hàm tiện ích chuyển ResultSet sang Product (có category)
	private Product mapResultSetToProductWithCategory(ResultSet rs) throws SQLException {
		Product p = mapResultSetToProduct(rs);
		
		// Set category data
		Category category = new Category();
		category.setCategoryId(rs.getInt("category_id"));
		category.setName(rs.getString("category_name")); // From joined categories table
		p.setCategory(category);
		
		return p;
	}
}
