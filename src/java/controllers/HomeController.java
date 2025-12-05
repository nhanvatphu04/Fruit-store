package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Product;
import models.Combo;
import models.Category;
import services.ProductService;
import services.ComboService;
import services.CategoryService;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeController", urlPatterns = {"", "/home"})
public class HomeController extends HttpServlet {
    private final ProductService productService;
    private final ComboService comboService;
    private final CategoryService categoryService;

    public HomeController() {
        productService = new ProductService();
        comboService = new ComboService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy 4 sản phẩm bán chạy nhất (theo flag is_best_seller)
            List<Product> bestSellingProducts = productService.getBestSellerProductsByFlag(4);
            request.setAttribute("bestSellingProducts", bestSellingProducts);

            // Lấy 4 sản phẩm mới nhất (theo flag is_new)
            List<Product> newProducts = productService.getNewProductsByFlag(4);
            request.setAttribute("newProducts", newProducts);

            // Lấy combo/flash sale đang diễn ra
            List<Combo> activeCombo = comboService.getActiveCombo();
            request.setAttribute("activeCombo", activeCombo);

            // Lấy tất cả danh mục từ database
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);

            // Forward đến trang home.jsp
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}