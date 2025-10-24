package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Category;
import utils.DbConnect;

// DAO cho danh mục sản phẩm
public class CategoryDAO {

	// Lấy tất cả danh mục
	public List<Category> getAllCategories() {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT * FROM categories";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Category c = new Category();
				c.setCategoryId(rs.getInt("category_id"));
				c.setName(rs.getString("name"));
				c.setDescription(rs.getString("description"));
				c.setImageUrl(rs.getString("image_url"));
				c.setIcon(rs.getString("icon"));
				c.setSlug(rs.getString("slug"));
				c.setCreatedAt(rs.getTimestamp("created_at"));
				list.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy danh mục theo id
	public Category getCategoryById(int id) {
		String sql = "SELECT * FROM categories WHERE category_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Category c = new Category();
				c.setCategoryId(rs.getInt("category_id"));
				c.setName(rs.getString("name"));
				c.setDescription(rs.getString("description"));
				c.setImageUrl(rs.getString("image_url"));
				c.setIcon(rs.getString("icon"));
				c.setSlug(rs.getString("slug"));
				c.setCreatedAt(rs.getTimestamp("created_at"));
				return c;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Thêm danh mục
	public boolean addCategory(Category category) {
		String sql = "INSERT INTO categories (name, description, image_url, icon, slug) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, category.getName());
			ps.setString(2, category.getDescription());
			ps.setString(3, category.getImageUrl());
			ps.setString(4, category.getIcon());
			ps.setString(5, category.getSlug());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật danh mục
	public boolean updateCategory(Category category) {
		String sql = "UPDATE categories SET name = ?, description = ?, image_url = ?, icon = ?, slug = ? WHERE category_id = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, category.getName());
			ps.setString(2, category.getDescription());
			ps.setString(3, category.getImageUrl());
			ps.setString(4, category.getIcon());
			ps.setString(5, category.getSlug());
			ps.setInt(6, category.getCategoryId());
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xoá danh mục
	public boolean deleteCategory(int id) {
		String sql = "DELETE FROM categories WHERE category_id = ?";
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

	// Lấy danh mục theo slug
	public Category getCategoryBySlug(String slug) {
		String sql = "SELECT * FROM categories WHERE slug = ?";
		try (Connection conn = DbConnect.getInstance().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, slug);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Category cat = new Category();
				cat.setCategoryId(rs.getInt("category_id"));
				cat.setName(rs.getString("name"));
				cat.setDescription(rs.getString("description"));
				cat.setIcon(rs.getString("icon"));
				cat.setSlug(rs.getString("slug"));
				cat.setImageUrl(rs.getString("image_url"));
				cat.setCreatedAt(rs.getTimestamp("created_at"));
				return cat;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
