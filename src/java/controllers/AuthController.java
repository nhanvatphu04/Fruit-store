package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import services.AuthService;
import java.io.IOException;

@WebServlet(name = "AuthController", urlPatterns = {"/login", "/register", "/logout"})
public class AuthController extends HttpServlet {
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        
        switch (servletPath) {
            case "/login":
                // Hiển thị form login
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                break;
                
            case "/register":
                // Hiển thị form register
                request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
                break;
                
            case "/logout":
                // Xử lý logout
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/home");
                break;
                
            default:
                response.sendRedirect(request.getContextPath() + "/home");
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        
        switch (servletPath) {
            case "/login":
                handleLogin(request, response);
                break;
                
            case "/register":
                handleRegister(request, response);
                break;
                
            default:
                response.sendRedirect(request.getContextPath() + "/home");
                break;
        }
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Xác thực đầu vào
        if (username == null || username.trim().isEmpty() 
            || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }
        
        // Thử đăng nhập
        User user = authService.login(username, password);
        
        if (user != null) {
            // Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // Nếu là admin thì chuyển đến trang dashboard
            if ("admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Xác thực đầu vào
        if (username == null || username.trim().isEmpty()
            || email == null || email.trim().isEmpty()
            || password == null || password.trim().isEmpty()
            || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Tạo user mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole("customer"); // Mặc định role là customer
        
        // Thử đăng ký
        if (authService.register(newUser)) {
            // Đăng ký thành công
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // Đăng ký thất bại
            request.setAttribute("error", "Tên đăng nhập hoặc email đã tồn tại!");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        }
    }
}