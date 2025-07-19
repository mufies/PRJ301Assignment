package Controller.employeeServlet;


import DAO.OrderDAOImpl;
import Model.Order;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "getNewOrders", urlPatterns = {"/new_orders"})
public class GetNewOrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Order> orders = new ArrayList<>();
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        orders = orderDAO.getTodayOrderForEmployee();
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(orders);
        response.getWriter().write(json);

    }
}