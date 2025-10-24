package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Product;
import services.ProductService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.math.BigDecimal;

@WebServlet(name = "SearchController", urlPatterns = {"/api/search", "/search"})
public class SearchController extends HttpServlet {
    private final ProductService productService;
    private final Gson gson;

    public SearchController() {
        productService = new ProductService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("q");
        String path = request.getServletPath();

        // Nếu là API call, trả về JSON
        if ("/api/search".equals(path)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (query != null && !query.trim().isEmpty()) {
                List<Product> products = productService.searchProducts(query);
                // Chỉ trả về thông tin cần thiết cho autocomplete
                String json = gson.toJson(products.stream().map(p -> {
                    String finalPrice = p.getDiscountPercent() > 0 
                        ? p.getPrice().multiply(BigDecimal.valueOf(1 - p.getDiscountPercent()/100.0)).toString()
                        : p.getPrice().toString();
                    return new SearchResult(p.getProductId(), p.getName(), 
                           finalPrice, p.getImageUrl(), p.getDiscountPercent());
                }).limit(5).toList());
                
                try (PrintWriter out = response.getWriter()) {
                    out.print(json);
                }
            }
        } 
        // Nếu là trang tìm kiếm, hiển thị kết quả
        else {
            if (query != null && !query.trim().isEmpty()) {
                List<Product> products = productService.searchProducts(query);
                request.setAttribute("products", products);
                request.setAttribute("searchQuery", query);
            }
            request.getRequestDispatcher("/jsp/search.jsp").forward(request, response);
        }
    }

    // Class nội bộ để serialize kết quả tìm kiếm
    private static class SearchResult {
        private final int id;
        private final String name;
        private final String price;
        private final String image;
        private final int discountPercent;

        public SearchResult(int id, String name, String price, String image, int discountPercent) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.image = image;
            this.discountPercent = discountPercent;
        }
    }
}