package Controller;

import DAO.UserDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import Model.*;
import DAO.UserDAO;
import utils.JwtUtils;

@WebServlet(name = "UserLogin", urlPatterns = {"/login"})
public class UserLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserLogin</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserLogin at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
        rd.forward(request, response);


        
 }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.loginUser(username, password);
        if (user != null) {
            String token = JwtUtils.generateToken(username);
            response.setContentType("application/json");
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("jwt", token);
            response.sendRedirect("welcome.jsp");
        } else {

            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }


    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
