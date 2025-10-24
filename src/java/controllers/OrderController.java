package controllers;

import dao.OrderDAO;
import models.Order;
import models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderController", urlPatterns = {"/orders"})
public class OrderController extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy danh sách đơn hàng của user
            List<Order> orders = orderDAO.getOrdersByUserId(user.getUserId());
            request.setAttribute("orders", orders);
            
            // Chuyển hướng đến orders.jsp
            request.getRequestDispatcher("/jsp/orders.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý bất kỳ yêu cầu POST nào nếu cần thiết (ví dụ: hủy đơn hàng)
        doGet(request, response);
    }
}