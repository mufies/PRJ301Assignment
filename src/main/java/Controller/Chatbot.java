package Controller;

import DAO.MenuDAOImpl;
import DAO.OrderDAOImpl;
import DAO.UserDAOImpl;
import Model.Product;
import Model.User;
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

@WebServlet(name = "Chatbot", urlPatterns = {"/Chatbot"})
public class Chatbot extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Chỉ lấy danh sách món ăn
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        List<Product> products = menuDAO.getMenuItems();

        PrintWriter out = response.getWriter();
        StringBuilder jsonResponse = new StringBuilder();

        jsonResponse.append("{\"products\":[");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            jsonResponse.append("{")
                    .append("\"id\":\"").append(product.getProductId()).append("\",")
                    .append("\"name\":\"").append(escapeJson(product.getProductName())).append("\",")
                    .append("\"price\":").append(product.getPrice())
                    .append("}");
            if (i < products.size() - 1) {
                jsonResponse.append(",");
            }
        }
        jsonResponse.append("]}");

        out.print(jsonResponse.toString());
        out.flush();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }






    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
