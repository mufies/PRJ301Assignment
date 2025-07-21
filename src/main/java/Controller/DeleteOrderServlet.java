package Controller;

import DAO.OrderDAOImpl;
import Model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/deleteOrder")
public class DeleteOrderServlet extends HttpServlet {
    private OrderDAOImpl orderDAO = new OrderDAOImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        boolean deleted = orderDAO.deleteOrderIfPending(orderId);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if (deleted) {
            out.print("{\"success\": true, \"message\": \"Đơn hàng đã được xoá.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"Chỉ có thể xoá đơn hàng đang ở trạng thái 'Chờ xử lý'.\"}");
        }
        out.flush();
    }
}