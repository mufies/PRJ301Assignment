package Controller;

import DAO.OrderDAOImpl;
import DAO.UserDAOImpl;
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
import utils.JwtUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

@WebServlet("/history")
public class GetUserOrdersServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter out = response.getWriter();
        JSONArray orderJson = new JSONArray();

        try {
            // Đọc dữ liệu từ body
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // Kiểm tra dữ liệu JSON đầu vào
            String requestBody = sb.toString().trim();
            if (requestBody.isEmpty()) {
                throw new Exception("Request body is empty!");
            }

            JSONObject json = new JSONObject(requestBody);

            if (!json.has("jwt")) {
                throw new Exception("Missing 'jwt' field in request body!");
            }
            String jwt = json.getString("jwt");

            String username = JwtUtils.getUsernameFromToken(jwt);

            OrderDAOImpl orderDAO = new OrderDAOImpl();
            List<Order> allOrders = orderDAO.getAllOrders();

            for (Order order : allOrders) {
                String customerName = order.getUsername() != null ? order.getUsername().toLowerCase() : "";

                if (!username.isEmpty() && !customerName.contains(username.toLowerCase())) {
                    continue;
                }

                List<OrderItem> orderItems = null;
                try {
                    orderItems = order.isLoggedIn()
                            ? orderDAO.getOrderItems(order.getOrderId())
                            : orderDAO.getGuestOrderItems(order.getOrderId());
                } catch (Exception ex) {
                    orderItems = new ArrayList<>();
                }

                JSONObject obj = new JSONObject();
                obj.put("orderId", order.getOrderId());
                obj.put("customerName", order.getCustomerName() != null ? order.getCustomerName() : "");
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                String dateStr = order.getOrderDate(); // kiểu String
                String formattedDate = "";
                try {
                    if (dateStr != null && !dateStr.isEmpty()) {
                        Date date = inputFormat.parse(dateStr); // parse thành Date
                        formattedDate = outputFormat.format(date); // format lại
                    }
                } catch (Exception e) {
                    formattedDate = "";
                }
                obj.put("orderDate", formattedDate);
                obj.put("orderDate", order.getOrderDate() != null ? formattedDate : "");
                obj.put("totalPrice", order.getTotalAmount());
                obj.put("status", order.getStatus() != null ? order.getStatus() : "");
                obj.put("description", order.getDescription() != null ? order.getDescription() : "");
                obj.put("isLoggedIn", order.isLoggedIn());

                JSONArray itemsJson = new JSONArray();
                if (orderItems != null) {
                    for (OrderItem item : orderItems) {
                        JSONObject itemObj = new JSONObject();
                        itemObj.put("productName", item.getProductName() != null ? item.getProductName() : "");
                        itemObj.put("quantity", item.getQuantity());
                        itemObj.put("price", item.getPrice());
                        itemsJson.put(itemObj);
                    }
                }

                obj.put("items", itemsJson);
                orderJson.put(obj);
            }

            out.print(orderJson.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", e.getMessage());
            out.print(errorJson.toString());
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("history.jsp");
        rd.forward(request, response);

    }

}
