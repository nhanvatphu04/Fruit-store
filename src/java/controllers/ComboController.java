package controllers;

import dao.ComboDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Combo;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ComboController", urlPatterns = {"/combos"})
public class ComboController extends HttpServlet {
    private final ComboDAO comboDAO = new ComboDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy danh sách tất cả combo
        List<Combo> combos = comboDAO.getAllCombos();
        
        // Set attribute để hiển thị trong JSP
        request.setAttribute("combos", combos);
        
        // Forward đến trang JSP
        request.getRequestDispatcher("/jsp/combos.jsp").forward(request, response);
    }
}