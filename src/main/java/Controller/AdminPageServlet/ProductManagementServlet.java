package Controller.AdminPageServlet;

import DAO.MenuDAOImpl;
import DAO.ProductDAOImpl;
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
import org.json.JSONObject;
import org.json.JSONTokener;
@WebServlet(name = "ProductManagementServlet", urlPatterns = {"/ayxkix/product"})
public class ProductManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        List<Product> productList = menuDAO.getMenuItems()
                .stream()
                .sorted((p1, p2) -> p1.getType().compareToIgnoreCase(p2.getType()))
                .toList();
        request.setAttribute("productList", productList);
        RequestDispatcher rd = request.getRequestDispatcher("/productManagement.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String price = request.getParameter("price");
        String type = request.getParameter("type");
        String image = request.getParameter("image");
        ProductDAOImpl productDAO = new ProductDAOImpl();

        try {
            if (productId == null || productId.isEmpty()) {
                if (!productName.isEmpty() && !price.isEmpty() && !type.isEmpty()) {
                    if (productDAO.addProduct(productName, price, type, image)) {
                        response.sendRedirect("/ayxkix/product");
                        return;
                    }
                }
            } else {
                if (productDAO.updateProduct(productId, productName, price, type, image)) {
                    response.sendRedirect("/ayxkix/product");
                    return;
                }
            }
            // Nếu lỗi
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>Error processing product. Please try again.</h3>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JSONObject json = new JSONObject(new JSONTokener(request.getReader()));
            String productId = json.getString("productId");
            String productName = json.getString("productName");
            String price = json.getString("price");
            String type = json.getString("type");
            String image = json.getString("image");
            ProductDAOImpl productDAO = new ProductDAOImpl();
            if (productDAO.updateProduct(productId, productName, price, type, image)) {
                response.sendRedirect("/ayxkix/product");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JSONObject json = new JSONObject(new JSONTokener(request.getReader()));
            String productId = String.valueOf(json.getInt("productId"));
            ProductDAOImpl productDAO = new ProductDAOImpl();
            if (productDAO.deleteProduct(productId)) {
                response.sendRedirect("/ayxkix/product");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
