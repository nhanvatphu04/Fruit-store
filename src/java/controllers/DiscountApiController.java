package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Discount;
import models.User;
import services.CartService;
import services.DiscountService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/discount/*")
public class DiscountApiController extends HttpServlet {
    private final DiscountService discountService = new DiscountService();
    private final CartService cartService = new CartService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Kiểm tra đăng nhập
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                out.print(gson.toJson(Map.of(
                    "success", false,
                    "message", "Vui lòng đăng nhập để sử dụng mã giảm giá"
                )));
                return;
            }

            String pathInfo = request.getPathInfo();
            if ("/apply".equals(pathInfo)) {
                handleApplyDiscount(request, response, user.getUserId());
            }
        } catch (Exception e) {
            out.print(gson.toJson(Map.of(
                "success", false,
                "message", "Có lỗi xảy ra: " + e.getMessage()
            )));
        }
    }

    private void handleApplyDiscount(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {
        String code = request.getParameter("code");
        if (code == null || code.trim().isEmpty()) {
            response.getWriter().print(gson.toJson(Map.of(
                "success", false,
                "message", "Vui lòng nhập mã giảm giá"
            )));
            return;
        }

        HttpSession session = request.getSession();

        // Kiểm tra xem đã có mã giảm giá nào được áp dụng chưa
        String previousDiscount = (String) session.getAttribute("appliedDiscount");
        if (previousDiscount != null && !previousDiscount.isEmpty() && !previousDiscount.equals(code)) {
            // Nếu có mã giảm giá khác, xóa nó trước khi áp dụng mã mới
            session.removeAttribute("appliedDiscount");
            session.removeAttribute("cartDiscount");
        }

        // Lấy tổng tiền giỏ hàng hiện tại
        BigDecimal cartTotal = cartService.getCartSubtotal(userId);

        // Áp dụng mã giảm giá
        Map<String, Object> result = discountService.applyDiscount(code, cartTotal);

        if ((boolean) result.get("success")) {
            // Lưu mã giảm giá và số tiền giảm vào session
            session.setAttribute("appliedDiscount", code);

            // Lấy số tiền giảm từ kết quả
            BigDecimal discountAmount = (BigDecimal) result.get("discountAmount");
            session.setAttribute("cartDiscount", discountAmount);

            // Ghi lại việc sử dụng mã giảm giá
            Discount discount = (Discount) result.get("discountInfo");
            if (discount != null) {
                discountService.recordDiscountUsage(
                    discount.getDiscountId(),
                    userId,
                    code,
                    discountAmount
                );
            }
        }

        response.getWriter().print(gson.toJson(result));
    }
}