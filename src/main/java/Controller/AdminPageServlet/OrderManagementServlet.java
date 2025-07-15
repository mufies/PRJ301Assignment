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
import org.json.JSONArray;
import org.json.JSONObject;

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
        RequestDispatcher rd = request.getRequestDispatcher("/orderManagement.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        JSONObject inputJson = new JSONObject(sb.toString());
        String keyword = inputJson.optString("customerName", "").toLowerCase();

        OrderDAOImpl orderDAO = new OrderDAOImpl();
        List<Order> allOrders = orderDAO.getAllOrders();

        JSONArray orderJson = new JSONArray();

        for (Order order : allOrders) {
            String customerName = order.getCustomerName().toLowerCase();

            if (!keyword.isEmpty() && !customerName.contains(keyword)) {
                continue;
            }

            List<OrderItem> orderItems = order.isLoggedIn()
                    ? orderDAO.getOrderItems(order.getOrderId())
                    : orderDAO.getGuestOrderItems(order.getOrderId());

            JSONObject obj = new JSONObject();
            obj.put("orderId", order.getOrderId());
            obj.put("customerName", order.getCustomerName());
            obj.put("orderDate", order.getOrderDate());
            obj.put("totalPrice", order.getTotalAmount());
            obj.put("status", order.getStatus());
            obj.put("description", order.getDescription());
            obj.put("isLoggedIn", order.isLoggedIn());

            JSONArray itemsJson = new JSONArray();
            for (OrderItem item : orderItems) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("productName", item.getProductName());
                itemObj.put("quantity", item.getQuantity());
                itemObj.put("price", item.getPrice());
                itemsJson.put(itemObj);
            }

            obj.put("items", itemsJson);
            orderJson.put(obj);
        }

        out.print(orderJson.toString());
        out.flush();
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
