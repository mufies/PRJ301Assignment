package Controller.employeeServlet;

import DAO.MenuDAOImpl;
import DAO.OrderDAOImpl;
import DAO.UserDAOImpl;
import Model.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

    @WebServlet(name = "employeeCheckingOrder", urlPatterns = {"/employeeCheckingOrder"})
public class employeeCheckingOrder extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        List<Product> productList = menuDAO.getMenuItems()
                .stream()
                .sorted((p1, p2) -> p1.getType().compareToIgnoreCase(p2.getType()))
                .toList();
        request.setAttribute("productList", productList);
        RequestDispatcher rd = request.getRequestDispatcher("employeeOrderCreate.jsp");
        rd.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        OrderDAOImpl orderDAO = new OrderDAOImpl();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Đọc dữ liệu từ request body
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                buffer.append(line);
            }

            JSONObject jsonRequest = new JSONObject(buffer.toString());
            String customerType = jsonRequest.getString("customerType");
            JSONArray items = jsonRequest.getJSONArray("items");
            String description = jsonRequest.optString("description", "");

            // Tạo mã vận chuyển duy nhất
            String shippingCode;
            do {
                String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                StringBuilder sb = new StringBuilder(6);
                for (int i = 0; i < 6; i++) {
                    int index = (int) (alphaNumeric.length() * Math.random());
                    sb.append(alphaNumeric.charAt(index));
                }
                shippingCode = sb.toString();
            } while (orderDAO.isShippingCodeValid(shippingCode));

            // Tính tổng tiền và chuẩn bị chi tiết đơn hàng
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

            int orderId;

            if ("registered".equalsIgnoreCase(customerType)) {
                // Người dùng đã đăng ký
                String username = jsonRequest.getString("username");
                UserDAOImpl userDAO = new UserDAOImpl();
                int userId = userDAO.getUserId(username);

                if (userId <= 0) {
                    throw new Exception("User not found: " + username);
                }

                orderId = orderDAO.addOrder(userId, totalAmount, description, shippingCode);

                if (orderId > 0) {
                    if (!orderDAO.addOrderDetails(orderId, orderDetailsJson.toString())) {
                        throw new Exception("Failed to add order details");
                    }
                } else {
                    throw new Exception("Failed to create order");
                }

            } else {
                // Khách hàng không đăng ký
                String guestName = jsonRequest.getString("name");
                String guestPhone = jsonRequest.getString("phone");
                String guestAddress = jsonRequest.getString("address");

                orderId = orderDAO.addOrderGuest(guestName, guestPhone, guestAddress,
                        totalAmount, description, shippingCode);

                if (orderId > 0) {
                    if (!orderDAO.addOrderDetailsGuest(orderId, orderDetailsJson.toString())) {
                        throw new Exception("Failed to add guest order details");
                    }
                } else {
                    throw new Exception("Failed to create guest order");
                }
            }

            // Trả về phản hồi thành công
            jsonResponse.put("success", true);
            jsonResponse.put("orderId", orderId);
            jsonResponse.put("shippingCode", shippingCode);

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("errorMessage", e.getMessage());
            e.printStackTrace();
        } finally {
            // Đảm bảo phản hồi được gửi đầy đủ
            try {
                response.getWriter().write(jsonResponse.toString());
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
