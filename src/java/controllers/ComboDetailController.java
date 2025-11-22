package controllers;

import dao.ComboDAO;
import dao.ComboItemDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Combo;
import models.ComboItem;
import models.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ComboDetailController", urlPatterns = {"/combo-detail"})
public class ComboDetailController extends HttpServlet {
    private final ComboDAO comboDAO = new ComboDAO();
    private final ComboItemDAO comboItemDAO = new ComboItemDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String comboIdParam = request.getParameter("comboId");

        if (comboIdParam == null || comboIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/combos");
            return;
        }

        Combo combo;
        int comboId;
        try {
            comboId = Integer.parseInt(comboIdParam);
            combo = comboDAO.getComboById(comboId);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/combos");
            return;
        }

        if (combo == null) {
            response.sendRedirect(request.getContextPath() + "/combos");
            return;
        }

        // Load products included in this combo
        List<ComboItem> items = comboItemDAO.getComboItemsByComboId(comboId);
        List<Map<String, Object>> comboProducts = new ArrayList<Map<String, Object>>();
        for (ComboItem item : items) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product != null) {
                Map<String, Object> detail = new HashMap<String, Object>();
                detail.put("product", product);
                detail.put("quantity", item.getQuantity());
                comboProducts.add(detail);
            }
        }

        request.setAttribute("combo", combo);
        request.setAttribute("comboProducts", comboProducts);
        request.getRequestDispatcher("/jsp/combo-detail.jsp").forward(request, response);
    }
}

