package Controller.AdminPageServlet;

import DAO.UserDAOImpl;
import Model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CustomerManagementServlet", urlPatterns = {"/ayxkix/customer"})
public class CustomerManagementServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDAOImpl userDAO = new UserDAOImpl();
        List<User> getAllUser = userDAO.getAllUsers();
        request.setAttribute("getAllUsers", getAllUser);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/customerManagement.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
