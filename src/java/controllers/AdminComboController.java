package controllers;

import com.google.gson.Gson;
import dao.ComboDAO;
import dao.ComboItemDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Combo;
import models.ComboItem;
import models.Product;
import models.User;

@WebServlet(name = "AdminComboController", urlPatterns = {
    "/admin/combos/*"
})
public class AdminComboController extends HttpServlet {

    private final ComboDAO comboDAO = new ComboDAO();
    private final ComboItemDAO comboItemDAO = new ComboItemDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final Gson gson = new Gson();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/error/403.jsp");
            return;
        }
            
        String pathInfo = request.getPathInfo(); // cho ánh xạ /admin/combos/*

        if (pathInfo == null || "/".equals(pathInfo)) {
            // Hiển thị trang quản lý combo
            List<Combo> combos = comboDAO.getAllCombos();
            List<Product> products = productDAO.getAllProducts();
            request.setAttribute("combos", combos);
            request.setAttribute("products", products);
            request.getRequestDispatcher("/jsp/admin/combos.jsp").forward(request, response);
            return;
        }

        // Chuẩn hóa pathInfo (ví dụ: "/list", "/{id}/items")
        if ("/list".equals(pathInfo)) {
            // API lấy danh sách combo
            List<Combo> combos = comboDAO.getAllCombos();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(combos));
            return;
        }

        // API lấy danh sách sản phẩm trong combo
        String[] pathParts = pathInfo.split("/"); // ["", "{id}", "items"]
        if (pathParts.length >= 3 && "items".equals(pathParts[2])) {
            try {
                int comboId = Integer.parseInt(pathParts[1]);
                List<ComboItem> items = comboItemDAO.getComboItemsByComboId(comboId);
                List<Map<String, Object>> itemsWithDetails = new ArrayList<Map<String, Object>>();

                for (ComboItem item : items) {
                    Product product = productDAO.getProductById(item.getProductId());
                    Map<String, Object> itemDetail = new HashMap<>();
                    itemDetail.put("comboItemId", item.getComboItemId());
                    itemDetail.put("productId", item.getProductId());
                    itemDetail.put("productName", product.getName());
                    itemDetail.put("quantity", item.getQuantity());
                    itemsWithDetails.add(itemDetail);
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(itemsWithDetails));
                return;
            } catch (Exception e) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Error loading combo items: " + e.getMessage());
                response.getWriter().write(gson.toJson(error));
                return;
            }
        }

        // Không tìm thấy đường dẫn khác trong servlet này
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem người dùng đã đăng nhập và có vai trò quản trị viên chưa
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Access denied. Admin privileges required.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(gson.toJson(error));
            return;
        }
            
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();
        Map<String, Object> result = new HashMap<>();

        try {
            if ("/add".equals(pathInfo)) {
                // Thêm combo mới
                Combo combo = new Combo();
                populateComboFromRequest(combo, request);
                if (comboDAO.addCombo(combo)) {
                    result.put("success", true);
                    result.put("message", "Combo added successfully");
                } else {
                    result.put("success", false);
                    result.put("message", "Failed to add combo");
                }
            } else if ("/update".equals(pathInfo)) {
                // Cập nhật combo
                int comboId = Integer.parseInt(request.getParameter("comboId"));
                Combo combo = comboDAO.getComboById(comboId);
                if (combo != null) {
                    populateComboFromRequest(combo, request);
                    if (comboDAO.updateCombo(combo)) {
                        result.put("success", true);
                        result.put("message", "Combo updated successfully");
                    } else {
                        result.put("success", false);
                        result.put("message", "Failed to update combo");
                    }
                } else {
                    result.put("success", false);
                    result.put("message", "Combo not found");
                }
            } else if ("/delete".equals(pathInfo)) {
                // Xóa combo
                int comboId = Integer.parseInt(request.getParameter("comboId"));
                if (comboDAO.deleteCombo(comboId)) {
                    result.put("success", true);
                    result.put("message", "Combo deleted successfully");
                } else {
                    result.put("success", false);
                    result.put("message", "Failed to delete combo");
                }
            } else if ("/items/add".equals(pathInfo)) {
                // Thêm sản phẩm vào combo
                ComboItem item = new ComboItem();
                item.setComboId(Integer.parseInt(request.getParameter("itemComboId")));
                item.setProductId(Integer.parseInt(request.getParameter("productId")));
                item.setQuantity(Integer.parseInt(request.getParameter("quantity")));

                if (comboItemDAO.addComboItem(item)) {
                    result.put("success", true);
                    result.put("message", "Product added to combo successfully");
                } else {
                    result.put("success", false);
                    result.put("message", "Failed to add product to combo");
                }
            } else if ("/items/delete".equals(pathInfo)) {
                // Xóa sản phẩm khỏi combo
                int comboItemId = Integer.parseInt(request.getParameter("comboItemId"));
                if (comboItemDAO.removeComboItem(comboItemId)) {
                    result.put("success", true);
                    result.put("message", "Product removed from combo successfully");
                } else {
                    result.put("success", false);
                    result.put("message", "Failed to remove product from combo");
                }
            } else {
                result.put("success", false);
                result.put("message", "Invalid endpoint");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(result));
    }

    private void populateComboFromRequest(Combo combo, HttpServletRequest request) throws Exception {
        combo.setName(request.getParameter("name"));
        combo.setDescription(request.getParameter("description"));
        combo.setImageUrl(request.getParameter("imageUrl"));
        combo.setOriginalPrice(new BigDecimal(request.getParameter("originalPrice")));
        combo.setComboPrice(new BigDecimal(request.getParameter("comboPrice")));
        combo.setStartDate(new Timestamp(dateFormat.parse(request.getParameter("startDate")).getTime()));
        combo.setEndDate(new Timestamp(dateFormat.parse(request.getParameter("endDate")).getTime()));
        combo.setActive(Boolean.parseBoolean(request.getParameter("isActive")));
    }
}