//package Controller;
//
//import DAO.MenuDAOImpl;
//import DAO.OrderDAOImpl;
//import DAO.UserDAOImpl;
//import Model.Product;
//import Model.User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
//@WebServlet(name = "Chatbot", urlPatterns = {"/Chatbot"})
//public class Chatbot extends HttpServlet {
//
//
//
//@Override
//protected void doGet(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//    UserDAOImpl userDAO = new UserDAOImpl();
//    List<User> users = userDAO.getAllUsers();
//    MenuDAOImpl menuDAO = new MenuDAOImpl();
//    List<Product> products = menuDAO.getMenuItems();
//
//    response.setContentType("application/json");
//    response.setCharacterEncoding("UTF-8");
//
//    PrintWriter out = response.getWriter();
//    StringBuilder jsonResponse = new StringBuilder();
//    jsonResponse.append("{\"users\":[");
//
//    for (int i = 0; i < users.size(); i++) {
//        User user = users.get(i);
//        jsonResponse.append("{")
//                   .append("\"id\":\"").append(user.getId()).append("\",")
//                   .append("\"username\":\"").append(user.getUsername()).append("\",")
//                   .append("\"email\":\"").append(user.getEmail()).append("\"")
//                   .append("}");
//        if (i < users.size() - 1) {
//            jsonResponse.append(",");
//        }
//    }
//    jsonResponse.append("],");
//
//    // Add products array
//    jsonResponse.append("\"products\":[");
//    for (int i = 0; i < products.size(); i++) {
//        Product product = products.get(i);
//        jsonResponse.append("{")
//                   .append("\"id\":\"").append(product.getProductId()).append("\",")
//                   .append("\"name\":\"").append(product.getProductName()).append("\",")
//                   .append("\"price\":").append(product.getPrice())
//                   .append("}");
//        if (i < products.size() - 1) {
//            jsonResponse.append(",");
//        }
//    }
//    jsonResponse.append("]}");
//
//    out.print(jsonResponse.toString());
//    out.flush();
//}
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter out = response.getWriter();
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = request.getReader().readLine()) != null) {
//            sb.append(line);
//        }
//
//        try {
//            JSONObject json = new JSONObject(sb.toString());
//            int userId = json.getInt("user_id");
//            int totalMoney = json.getInt("total_money");
//            String description = json.optString("description", "");
//            JSONArray orderDetails = json.getJSONArray("order_details");
//
//            OrderDAOImpl orderDAO = new OrderDAOImpl();
//
//            if (userId != -1) {
//                int orderId = orderDAO.addOrder(userId, totalMoney, description);
//                boolean detailsAdded = false;
//                if (orderId > 0) {
//                    detailsAdded = orderDAO.addOrderDetails(orderId, orderDetails.toString());
//                }
//                if (orderId > 0 && detailsAdded) {
//                    out.print("{\"success\":true}");
//                    System.out.println("Order and details added successfully for user ID: " + userId);
//                } else {
//                    out.print("{\"success\":false,\"error\":\"Failed to add order or details\"}");
//                    System.out.println("Failed to add order or details for user ID: " + userId);
//                }
//            } else {
//                out.print("{\"success\":false,\"error\":\"Guest order not implemented\"}");
//                System.out.println("Guest order not implemented, user ID is -1");
//            }
//        } catch (Exception e) {
//            out.print("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
//        }
//        out.flush();
//    }
//
//
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }
//}
