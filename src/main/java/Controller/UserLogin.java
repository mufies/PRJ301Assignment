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
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    System.out.println("Attempting to log in with username: " + username);
    User user = userDAO.loginUser(username, password);
    Admin admin = userDAO.loginAdmin(username, password);

    if (user != null) {
        String token = JwtUtils.generateToken(username, "User");
        System.out.println("Token generated: " + token);
        out.print("{\"success\":true, \"token\":\"" + token + "\"}");
    }
    else if (admin != null) {
        String token = JwtUtils.generateToken(username, "Admin");
        System.out.println("Token generated for admin: " + token);
        out.print("{\"success\":true, \"token\":\"" + token + "\", \"isAdmin\":true}");
    }
    else {
        out.print("{\"success\":false, \"message\":\"Invalid credentials\"}");
    }

    out.flush();
}


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
