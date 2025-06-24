package Controller;

import DAO.UserDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import Model.*;
import utils.JwtUtils;

@WebServlet(name = "UserLogin", urlPatterns = {"/login"})
@MultipartConfig
public class UserLogin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDAOImpl userDAO = new UserDAOImpl();
        User user = userDAO.loginUser(username, password);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println("Attempting to log in with username: " + username);
        if (user != null) {
            String token = JwtUtils.generateToken(username);
            System.out.println("Token generated: " + token);
            out.print("{\"success\":true, \"token\":\"" + token + "\"}");

        } else {
            out.print("{\"success\":false, \"errorMessage\":\"Sai tài khoản hoặc mật khẩu\"}");
        }
        out.flush();
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
