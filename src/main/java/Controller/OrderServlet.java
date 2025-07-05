package Controller;

import DAO.OrderDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        String Order = orderDAO.getOrderByEachMonth();
        String OrderGuest = orderDAO.getOrderByEachMonthGuest();
        String getProductsType = orderDAO.getOrderDetails();
        response.setContentType("application/json; charset=UTF-8");
        String requestedWith = request.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX request - return JSON
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"order\": " + Order +
                    ", \"orderGuest\": " + OrderGuest +
                    ", \"trendingProducts\": " + getProductsType + "}");

            out.flush();
        }




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
