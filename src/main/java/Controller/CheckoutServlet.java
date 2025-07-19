package Controller;

import DAO.CartDAOImpl;
import DAO.OrderDAOImpl;
import DAO.UserDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.JwtUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher("checkout.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        String mvd;
        while (true)
        {


                String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                StringBuilder sb = new StringBuilder(6);
                for (int i = 0; i < 6; i++) {
                    int index = (int) (alphaNumeric.length() * Math.random());
                    sb.append(alphaNumeric.charAt(index));
                }
           mvd = sb.toString();
            if (!orderDAO.isShippingCodeValid(mvd)) {
                break; // Nếu mã duy nhất, thoát khỏi vòng lặp
            }

        }
        try {
            // Đọc body request
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                buffer.append(line);
            }

            JSONObject jsonRequest = new JSONObject(buffer.toString());
            String action = jsonRequest.getString("action");

            if ("postUserOrder".equals(action)) {
                // Đơn hàng cho user đã đăng nhập
                String jwt = jsonRequest.getString("jwt");
                JSONArray items = jsonRequest.getJSONArray("items");

                // Xác thực JWT và lấy username
                String username = JwtUtils.getUsernameFromToken(jwt);
                if (username == null) throw new Exception("Invalid JWT token");

                UserDAOImpl userDAO = new UserDAOImpl();
                int userId = userDAO.getUserId(username);

                if (userId > 0) {
                    int totalAmount = 0;
                    JSONArray orderDetailsJson = new JSONArray();

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        int productId = item.getInt("productId");
                        int quantity = item.getInt("quantity");
                        int unitPrice = item.getInt("price");
                        totalAmount += quantity * unitPrice;

                        JSONObject orderDetail = new JSONObject();
                        orderDetail.put("product_id", productId);
                        orderDetail.put("quantity", quantity);
                        orderDetail.put("unit_price", unitPrice);
                        orderDetailsJson.put(orderDetail);
                    }

                    String description = jsonRequest.optString("description", "");

                    int orderId = orderDAO.addOrder(userId, totalAmount, description,mvd);

                    if (orderId > 0 && orderDAO.addOrderDetails(orderId, orderDetailsJson.toString())) {
                        // Xóa cart sau khi đặt hàng thành công
                        CartDAOImpl cartDAO = new CartDAOImpl();
                        cartDAO.clearCart(userId);

                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("success", true);
                        jsonResponse.put("shippingCode", mvd);
                        out.print(jsonResponse.toString());
                    } else {
                        throw new Exception("Failed to create order");
                    }
                } else {
                    throw new Exception("Invalid user authentication");
                }
            } else if ("postGuestOrder".equals(action)) {
                // Đơn hàng cho khách vãng lai
                JSONArray items = jsonRequest.getJSONArray("items");
                String guestName = jsonRequest.getString("name");
                String guestPhone = jsonRequest.getString("phone");

                String phoneRegex = "^\\d{10,11}$";
                if (!guestPhone.matches(phoneRegex)) {
                    throw new Exception("❌ Số điện thoại không hợp lệ.");
                }
                String guestAddress = jsonRequest.getString("address");
                if (guestAddress.isEmpty() || guestAddress.length() < 5) {
                    throw new Exception("❌ Vui lòng nhập địa chỉ giao hàng hợp lệ.");
                }
                String description = jsonRequest.optString("description", "");

                int totalAmount = 0;
                JSONArray orderDetailsJson = new JSONArray();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    int productId = item.getInt("productId");
                    int quantity = item.getInt("quantity");
                    int unitPrice = item.getInt("price");
                    totalAmount += unitPrice * quantity;

                    JSONObject orderDetail = new JSONObject();
                    orderDetail.put("product_id", productId);
                    orderDetail.put("quantity", quantity);
                    orderDetail.put("unit_price", unitPrice);
                    orderDetailsJson.put(orderDetail);
                }

                int orderId = orderDAO.addOrderGuest(guestName, guestPhone, guestAddress, totalAmount, description,mvd);

                if (orderId > 0 && orderDAO.addOrderDetailsGuest(orderId, orderDetailsJson.toString())) {
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("success", true);
                    jsonResponse.put("shippingCode", mvd);
                    out.print(jsonResponse.toString());
                } else {
                    throw new Exception("Failed to create guest order");
                }
            } else {
                throw new Exception("Unknown action: " + action);
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("errorMessage", e.getMessage());
            out.print(jsonResponse.toString());
            e.printStackTrace();
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
