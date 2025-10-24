package services;

import dao.CategoryDAO;
import models.Category;
import java.util.List;

// Service quản lý danh mục sản phẩm
public class CategoryService {
	private CategoryDAO categoryDAO;

	public CategoryService() {
		categoryDAO = new CategoryDAO();
	}

	// Lấy tất cả danh mục
	public List<Category> getAllCategories() {
		return categoryDAO.getAllCategories();
	}

	// Lấy danh mục theo id
	public Category getCategoryById(int id) {
		return categoryDAO.getCategoryById(id);
	}

	// Thêm danh mục
	public boolean addCategory(Category category) {
		return categoryDAO.addCategory(category);
	}

	// Cập nhật danh mục
	public boolean updateCategory(Category category) {
		return categoryDAO.updateCategory(category);
	}

	// Xoá danh mục
	public boolean deleteCategory(int id) {
		return categoryDAO.deleteCategory(id);
	}

	// Lấy danh mục theo slug
	public Category getCategoryBySlug(String slug) {
		return categoryDAO.getCategoryBySlug(slug);
	}

	// Lấy danh mục theo icon
	public List<Category> getCategoriesByIcon(String icon) {
		List<Category> allCategories = categoryDAO.getAllCategories();
		return allCategories.stream()
				.filter(category -> icon.equals(category.getIcon()))
				.collect(java.util.stream.Collectors.toList());
	}

	// Tìm kiếm danh mục theo tên hoặc slug
	public List<Category> searchCategories(String keyword) {
		List<Category> allCategories = categoryDAO.getAllCategories();
		return allCategories.stream()
				.filter(category -> 
					category.getName().toLowerCase().contains(keyword.toLowerCase()) ||
					(category.getSlug() != null && category.getSlug().toLowerCase().contains(keyword.toLowerCase()))
				)
				.collect(java.util.stream.Collectors.toList());
	}

	// Kiểm tra slug có tồn tại không
	public boolean isSlugExists(String slug) {
		return categoryDAO.getCategoryBySlug(slug) != null;
	}

	// Tạo slug từ tên danh mục
	public String generateSlug(String categoryName) {
		if (categoryName == null || categoryName.trim().isEmpty()) {
			return "";
		}
		
		String slug = categoryName.toLowerCase()
				.replaceAll("[^a-z0-9\\s]", "")
				.replaceAll("\\s+", "-")
				.trim();
		
		// Kiểm tra và thêm số nếu slug đã tồn tại
		String originalSlug = slug;
		int counter = 1;
		while (isSlugExists(slug)) {
			slug = originalSlug + "-" + counter;
			counter++;
		}
		
		return slug;
	}
}
