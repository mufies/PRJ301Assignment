package Controller;

import DAO.OrderDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TrackingServlet", urlPatterns = {"/tracking"})
public class TrackingServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("tracking.jsp");
        rd.forward(request, response);
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    JSONObject jsonResponse = new JSONObject();

    try {
        // Parse request body
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            buffer.append(line);
        }

        // Convert JSON string to JSONObject
        JSONObject jsonRequest = new JSONObject(buffer.toString());
        String action = jsonRequest.getString("action");

        if ("getOrderByShippingCode".equals(action)) {
            String shippingCode = jsonRequest.getString("shippingCode");

            // Check if shipping code is valid
            OrderDAOImpl orderDAO = new OrderDAOImpl();
            JSONObject orderInfo = orderDAO.getOrderSummaryByShippingCode(shippingCode);

            if (orderInfo != null) {
                jsonResponse.put("success", true);
                jsonResponse.put("order", orderInfo);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("errorMessage", "Không tìm thấy đơn hàng với mã vận đơn này");
            }
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("errorMessage", "Hành động không hợp lệ");
        }
    } catch (Exception e) {
        jsonResponse.put("success", false);
        jsonResponse.put("errorMessage", e.getMessage());
        e.printStackTrace();
    }

    out.print(jsonResponse.toString());
}

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
