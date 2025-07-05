package Controller.AdminPageServlet;

import DAO.OrderDAOImpl;
import Model.Order;
import Model.OrderItem;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "OrderManagementServlet", urlPatterns = {"/ayxkix/order"})
public class OrderManagementServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orderList;
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        orderList = orderDAO.getOrderForThisWeek();
        for (Order order : orderList) {
            if(order.isLoggedIn())
            {
                List<OrderItem> orderItems = orderDAO.getOrderItems(order.getOrderId());
                order.setDetails(orderItems);
            }
            else
            {
                List<OrderItem> orderItems = orderDAO.getGuestOrderItems(order.getOrderId());

                order.setDetails(orderItems);
            }
        }
        request.setAttribute("orderList", orderList);

        List<Order> allOrders = orderDAO.getAllOrders();
        for (Order order : allOrders) {
            if(order.isLoggedIn())
            {
                List<OrderItem> orderItems = orderDAO.getOrderItems(order.getOrderId());
                order.setDetails(orderItems);
            }
            else
            {
                List<OrderItem> orderItems = orderDAO.getGuestOrderItems(order.getOrderId());

                order.setDetails(orderItems);
            }
        }
        request.setAttribute("orders", allOrders);




        RequestDispatcher rd = request.getRequestDispatcher("/orderManagement.jsp");
        rd.forward(request, response);
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
