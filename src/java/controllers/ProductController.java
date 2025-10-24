package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import models.Product;
import services.ProductService;
import services.CategoryService;
import models.Category;
import java.util.ArrayList;

@WebServlet(name = "ProductController", urlPatterns = {"/products", "/product"})
public class ProductController extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
        categoryService = new CategoryService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/product".equals(servletPath)) {
            // Trang chi tiết sản phẩm
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            Product product = productService.getProductById(id);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("product", product);
            request.getRequestDispatcher("/jsp/product.jsp").forward(request, response);
            return;
        }
        
        // Lấy tham số từ URL
        String categorySlug = request.getParameter("category");
        String searchQuery = request.getParameter("q");
        String filter = request.getParameter("filter");
        String sortBy = request.getParameter("sort");
        int page = getPageNumber(request);
        int pageSize = 9; // Số sản phẩm mỗi trang
        
        // Lấy danh sách danh mục
        List<Category> categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);
        
        // Lấy danh sách sản phẩm theo điều kiện
        List<Product> products = new ArrayList<>();
        int totalProducts = 0;
        
        // Lấy tham số lọc giá
        String minPriceStr = request.getParameter("min_price");
        String maxPriceStr = request.getParameter("max_price");
        double minPrice = minPriceStr != null ? Double.parseDouble(minPriceStr) : 0;
        double maxPrice = maxPriceStr != null ? Double.parseDouble(maxPriceStr) : Double.MAX_VALUE;

        // Lấy danh sách sản phẩm theo điều kiện
        if (categorySlug != null && !categorySlug.isEmpty()) {
            Category category = categoryService.getCategoryBySlug(categorySlug);
            if (category != null) {
                products = productService.getProductsByCategory(category.getCategoryId());
                request.setAttribute("categoryName", category.getName());
            }
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            products = productService.searchProducts(searchQuery);
        } else if (filter != null && !filter.isEmpty()) {
            // Xử lý các bộ lọc đặc biệt
            System.out.println("Filter parameter: " + filter);
            switch (filter) {
                case "best-selling":
                    products = productService.getBestSellerProductsByFlag(1000); // Use a large but reasonable limit
                    request.setAttribute("filterName", "Sản phẩm bán chạy");
                    System.out.println("Found " + products.size() + " best-selling products");
                    break;
                case "new":
                    products = productService.getNewProductsByFlag(1000); // Use a large but reasonable limit
                    request.setAttribute("filterName", "Sản phẩm mới");
                    System.out.println("Found " + products.size() + " new products");
                    break;
                default:
                    products = productService.getAllProducts();
                    System.out.println("Unknown filter: " + filter + ", using all products");
                    break;
            }
        } else {
            products = productService.getAllProducts();
        }

        // Lọc sản phẩm theo giá (dựa trên giá đã giảm)
        if (minPriceStr != null || maxPriceStr != null) {
            products = products.stream()
                .filter(p -> {
                    double discountedPrice = p.getPrice().doubleValue() * (1 - p.getDiscountPercent() / 100.0);
                    return discountedPrice >= minPrice && discountedPrice <= maxPrice;
                })
                .toList();
        }
        
        // Sắp xếp sản phẩm nếu có
        if (sortBy != null) {
            sortProducts(products, sortBy);
        }
        
        totalProducts = products.size();
        
        // Phân trang
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalProducts);
        
        List<Product> pagedProducts = products.subList(startIndex, endIndex);
        
        // Đăt thuộc tính cho JSP
        request.setAttribute("products", pagedProducts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        
        // Chuyển hướng đến products.jsp
        request.getRequestDispatcher("/jsp/products.jsp").forward(request, response);
    }
    
    private int getPageNumber(HttpServletRequest request) {
        try {
            String pageStr = request.getParameter("page");
            return pageStr != null ? Integer.parseInt(pageStr) : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
    
    private void sortProducts(List<Product> products, String sortBy) {
        switch (sortBy) {
            case "price_asc":
                products.sort((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));
                break;
            case "price_desc":
                products.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
                break;
            case "name_asc":
                products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
                break;
            case "name_desc":
                products.sort((p1, p2) -> p2.getName().compareTo(p1.getName()));
                break;
            default:
                // Giữ nguyên thứ tự mặc định
                break;
        }
    }
}