package Controller.AdminPageServlet;

import DAO.UserDAOImpl;
import Model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.JSONTokener;

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

        String fullname = request.getParameter("fullName");
        String password = request.getParameter("password");
        String phone = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        int id = Integer.parseInt(request.getParameter("customerId"));

        UserDAOImpl userDAO = new UserDAOImpl();

        // Đúng thứ tự: id, fullName, email, phone, address, password
        if (userDAO.updateUser(id, fullname, email, phone, address, password)) {
            response.sendRedirect("/ayxkix/customer");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Cập nhật thất bại');");
            out.println("location='/ayxkix/customer';");
            out.println("</script>");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject(new JSONTokener(request.getReader()));
        int customerId = json.getInt("customerId");
        UserDAOImpl userDAO = new UserDAOImpl();

        if (userDAO.deleteUserById(customerId)) {
            response.sendRedirect("/ayxkix/customer");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Xóa người dùng thất bại');");
            out.println("location='/ayxkix/customer';");
            out.println("</script>");
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
