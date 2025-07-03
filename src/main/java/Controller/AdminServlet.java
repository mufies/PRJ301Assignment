package Controller;

import DAO.OrderDAOImpl;
import Model.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/ayxkix"})
public class AdminServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        int curWeekOrd = orderDAO.getCurWeekOrderCount();
        int preWeekOrd = orderDAO.getPreWeekOrderCount();
        request.setAttribute("curWeekOrd", curWeekOrd);
        request.setAttribute("preWeekOrd", preWeekOrd);

        long curWeekRevenue = orderDAO.getCurWeekRev();
        long preWeekRevenue = orderDAO.getPreWeekRev();
        request.setAttribute("curWeekRevenue", curWeekRevenue);
        request.setAttribute("preWeekRevenue", preWeekRevenue);
        List<Product> mostRecentProductOrdered = orderDAO.mostRecentProductOrdered();
        request.setAttribute("mostRecentProductOrdered", mostRecentProductOrdered);
        RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
