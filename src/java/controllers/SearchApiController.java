package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import models.Product;
import services.ProductService;

@WebServlet("/api/search-suggest")
public class SearchApiController extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy query từ request
        String query = request.getParameter("q");
        
        if (query == null || query.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // Tìm các sản phẩm phù hợp
        List<Product> products = productService.searchProducts(query.trim(), 5); // Giới hạn 5 kết quả
        
        // Trả về kết quả dạng JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.print(gson.toJson(products));
        }
    }
}