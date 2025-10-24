package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Category;
import models.Product;
import models.User;
import services.CategoryService;
import services.ProductService;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "AdminProductController", urlPatterns = {
    "/admin/products",
    "/admin/products/*"
})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class AdminProductController extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;
    private static final String UPLOAD_DIR = "assets/images/products";
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
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
            // Hiển thị trang quản lý sản phẩm
            List<Product> products = productService.getAllProducts();
            List<Category> categories = categoryService.getAllCategories();
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/jsp/admin/products.jsp").forward(request, response);
            
        } else if (pathInfo.startsWith("/get/")) {
            // API lấy thông tin sản phẩm
            response.setContentType("application/json");
            String idStr = pathInfo.substring(5);
            try {
                int productId = Integer.parseInt(idStr);
                Product product = productService.getProductById(productId);
                if (product != null) {
                    response.getWriter().write(gson.toJson(product));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Product not found");
                    response.getWriter().write(gson.toJson(error));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Invalid product ID");
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
            // Thêm sản phẩm mới
            try {
                Product product = getProductFromRequest(request);
                if (productService.addProduct(product)) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to add product");
                }
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", e.getMessage());
            }
            
        } else if (pathInfo.startsWith("/update/")) {
            // Cập nhật sản phẩm
            try {
                int productId = Integer.parseInt(pathInfo.substring(8));
                Product product = getProductFromRequest(request);
                product.setProductId(productId);
                
                if (productService.updateProduct(product)) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to update product");
                }
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", e.getMessage());
            }
            
        } else if (pathInfo.startsWith("/delete/")) {
            // Xoá sản phẩm
            try {
                int productId = Integer.parseInt(pathInfo.substring(8));
                if (productService.deleteProduct(productId)) {
                    jsonResponse.put("success", true);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to delete product");
                }
            } catch (Exception e) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", e.getMessage());
            }
        }
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }
    
    private Product getProductFromRequest(HttpServletRequest request) throws Exception {
        Product product = new Product();
        
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(new BigDecimal(request.getParameter("price")));
        product.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        product.setDiscountPercent(Integer.parseInt(request.getParameter("discountPercent")));
        product.setNew(Boolean.parseBoolean(request.getParameter("isNew")));
        product.setBestSeller(Boolean.parseBoolean(request.getParameter("isBestSeller")));
        
        // Xử lý upload ảnh
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = UUID.randomUUID().toString() + getFileExtension(filePart);
            
            // Lấy đường dẫn thực cho việc lưu trữ tệp
            String webRoot = request.getServletContext().getRealPath("/");
            Path uploadDir = Paths.get(webRoot, UPLOAD_DIR);
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Lưu tệp vào đĩa
            String filePath = uploadDir.resolve(fileName).toString();
            filePart.write(filePath);
            
            // Lưu đường dẫn tương đối, không bao gồm đường dẫn context path
            String imageUrl = "/" + UPLOAD_DIR + "/" + fileName;
            product.setImageUrl(imageUrl);
            
            // Tạo bản sao trong thư mục web thực nếu nó tồn tại và khác với webRoot
            String projectWebDir = System.getProperty("user.dir") + "/web/" + UPLOAD_DIR;
            Path projectUploadDir = Paths.get(projectWebDir);
            if (!Files.exists(projectUploadDir)) {
                Files.createDirectories(projectUploadDir);
            }
            
            // Sao chép tệp vào thư mục web dự án
            Path sourcePath = Paths.get(filePath);
            Path targetPath = projectUploadDir.resolve(fileName);
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return product;
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