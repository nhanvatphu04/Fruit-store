package controllers;

import dao.ComboDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Combo;

import java.io.IOException;

@WebServlet(name = "ComboDetailController", urlPatterns = {"/combo-detail"})
public class ComboDetailController extends HttpServlet {
    private final ComboDAO comboDAO = new ComboDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String comboIdParam = request.getParameter("comboId");

        if (comboIdParam == null || comboIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/combos");
            return;
        }

        Combo combo;
        try {
            int comboId = Integer.parseInt(comboIdParam);
            combo = comboDAO.getComboById(comboId);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/combos");
            return;
        }

        if (combo == null) {
            response.sendRedirect(request.getContextPath() + "/combos");
            return;
        }

        request.setAttribute("combo", combo);
        request.getRequestDispatcher("/jsp/combo-detail.jsp").forward(request, response);
    }
}

