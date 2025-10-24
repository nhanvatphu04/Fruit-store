package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Controller xử lý các trang lỗi
@WebServlet(name = "ErrorController", urlPatterns = {"/error404", "/error403", "/error500", "/error"}, loadOnStartup = 1)

public class ErrorController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        String errorPage;
        
        switch (path) {
            case "/error404":
                errorPage = "/jsp/error/404.jsp";
                break;
            case "/error403":
                errorPage = "/jsp/error/403.jsp";
                break;
            case "/error500":
                errorPage = "/jsp/error/500.jsp";
                break;
            default:
                errorPage = "/jsp/error/error.jsp";
        }
        
        request.getRequestDispatcher(errorPage).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}