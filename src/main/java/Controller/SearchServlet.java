package Controller;

import DAO.MenuDAOImpl;
import Model.Product;
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

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json; charset=UTF-8");
    PrintWriter out = response.getWriter();
    JSONArray foodsJson = new JSONArray();
    MenuDAOImpl  menuDAO = new MenuDAOImpl();
    List<Product> foods = menuDAO.getMenuItems();
for (Product product : foods) {
    JSONObject obj = new JSONObject();
    obj.put("id", product.getProductId());
    obj.put("name", product.getProductName());
    obj.put("type", product.getType());
    obj.put("img", product.getImage());
    obj.put("price", product.getPrice());
    foodsJson.put(obj);
}

    out.print(foodsJson);
    out.flush();
}
}
