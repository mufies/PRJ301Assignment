package Controller.employeeServlet;

import DAO.OrderDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "employeeDashboard", urlPatterns = {"/updateOrderStatus"})
public class employeeDashboard extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("employeeOrderCheck.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JSONObject json = new JSONObject(sb.toString());
        int orderId = json.getInt("orderId");
        String newStatus = json.getString("newStatus");

        OrderDAOImpl dao = new OrderDAOImpl();
        boolean success = dao.updateOrderStatus(orderId, newStatus);

        response.setContentType("application/json");
        JSONObject res = new JSONObject();
        res.put("success", success);
        response.getWriter().write(res.toString());
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
