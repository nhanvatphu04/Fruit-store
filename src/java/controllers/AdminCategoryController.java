package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Category;
import models.User;
import services.CategoryService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "AdminCategoryController", urlPatterns = {
    "/admin/categories",
    "/admin/categories/*"
})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class AdminCategoryController extends HttpServlet {
    private CategoryService categoryService;
    private static final String UPLOAD_DIR = "assets/images/categories";
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/error/403.jsp");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Hiển thị trang quản lý danh mục
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/jsp/admin/categories.jsp").forward(request, response);
            
        } else if (pathInfo.startsWith("/get/")) {
            // API lấy thông tin danh mục
            response.setContentType("application/json");
            String idStr = pathInfo.substring(5);
            try {
                int categoryId = Integer.parseInt(idStr);
                Category category = categoryService.getCategoryById(categoryId);
                if (category != null) {
                    response.getWriter().write(gson.toJson(category));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Category not found");
                    response.getWriter().write(gson.toJson(error));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Invalid category ID");
                response.getWriter().write(gson.toJson(error));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đặt loại phản hồi sớm để xử lý lỗi phù hợp
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> jsonResponse = new HashMap<>();
        
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Access denied. Admin privileges required.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/add")) {
            // Thêm danh mục mới
            try {
                Category category = getCategoryFromRequest(request);
                if (categoryService.addCategory(category)) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to add category");
                }
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", e.getMessage());
            }
            
        } else if (pathInfo.startsWith("/update/")) {
            // Cập nhật danh mục
            try {
                int categoryId = Integer.parseInt(pathInfo.substring(8));
                
                // Get existing category to preserve image if not updated
                Category existingCategory = categoryService.getCategoryById(categoryId);
                
                Category category = getCategoryFromRequest(request);
                category.setCategoryId(categoryId);
                
                // If no new image provided, keep the existing image URL
                if (category.getImageUrl() == null || category.getImageUrl().isEmpty()) {
                    if (existingCategory != null) {
                        category.setImageUrl(existingCategory.getImageUrl());
                    }
                }
                
                // If no slug provided, keep the existing slug
                if (category.getSlug() == null || category.getSlug().isEmpty()) {
                    if (existingCategory != null) {
                        category.setSlug(existingCategory.getSlug());
                    }
                }
                
                if (categoryService.updateCategory(category)) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to update category");
                }
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", e.getMessage());
            }
            
        } else if (pathInfo.startsWith("/delete/")) {
            // Xoá danh mục
            try {
                int categoryId = Integer.parseInt(pathInfo.substring(8));
                if (categoryService.deleteCategory(categoryId)) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to delete category");
                }
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", e.getMessage());
            }
        }
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }
    
    private Category getCategoryFromRequest(HttpServletRequest request) throws Exception {
        Category category = new Category();
        
        category.setName(request.getParameter("name"));
        category.setDescription(request.getParameter("description"));
        category.setIcon(request.getParameter("icon"));
        
        // Xử lý upload ảnh
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = UUID.randomUUID().toString() + getFileExtension(filePart);
            
            // Get the project web directory (for development)
            String projectWebDir = System.getProperty("user.dir");
            // Remove "/build/web" if it exists (when running from IDE)
            if (projectWebDir.endsWith("/build/web") || projectWebDir.endsWith("\\build\\web")) {
                projectWebDir = projectWebDir.substring(0, projectWebDir.length() - 10);
            }
            
            Path uploadDir = Paths.get(projectWebDir, "web", UPLOAD_DIR);
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Lưu tệp vào đĩa
            String filePath = uploadDir.resolve(fileName).toString();
            filePart.write(filePath);
            
            // Lưu đường dẫn tương đối, không bao gồm đường dẫn context path
            String imageUrl = "/" + UPLOAD_DIR + "/" + fileName;
            category.setImageUrl(imageUrl);
            
            // Also save to build directory for immediate access
            String webRoot = request.getServletContext().getRealPath("/");
            Path buildUploadDir = Paths.get(webRoot, UPLOAD_DIR);
            
            if (!Files.exists(buildUploadDir)) {
                Files.createDirectories(buildUploadDir);
            }
            
            // Sao chép tệp vào thư mục build dự án
            Path sourcePath = Paths.get(filePath);
            Path targetPath = buildUploadDir.resolve(fileName);
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Generate slug from name
        String slug = request.getParameter("slug");
        if (slug == null || slug.isEmpty()) {
            slug = categoryService.generateSlug(category.getName());
        }
        category.setSlug(slug);
        
        return category;
    }
    
    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                return fileName.substring(fileName.lastIndexOf("."));
            }
        }
        return "";
    }
}
