package Controller;

import DAO.MenuDAOImpl;
import DAO.UserDAOImpl;
import Model.CartItem;
import Model.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import utils.JwtUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.json.JSONObject;


@WebServlet(name = "MenuServlet", urlPatterns = {"/menu"})
public class MenuServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products;
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        products = menuDAO.getMenuItems();
        request.setAttribute("products", products);
        RequestDispatcher rd = request.getRequestDispatcher("menu.jsp");
        rd.forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CartItem> cart;
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();

        JSONObject json = new JSONObject(requestBody);
        if (!json.has("jwt")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing JWT\"}");
            return;
        }
        String jwt = json.getString("jwt");
        if (jwt == null || jwt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Empty JWT\"}");
            return;
        }
        String username = JwtUtils.getUsernameFromToken(jwt);
        UserDAOImpl userDAO = new UserDAOImpl();
        int userID = userDAO.getUserId(username);
        cart = userDAO.getUserCart(userID);

        JSONArray cartJson = new JSONArray();
        for (CartItem product : cart) {
            JSONObject obj = new JSONObject();
            obj.put("productId", product.getProductId());
            obj.put("productName", product.getProductName());
            obj.put("price", product.getPrice());
            obj.put("image", product.getImage());
            obj.put("description", product.getDescription());
            obj.put("type", product.getType());
            obj.put("quantity", product.getQuantity());
            cartJson.put(obj);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(cartJson.toString());

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = null;
//        if (authorizationHeader == null) {
//            System.out.println("Authorization header is missing");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            token = authorizationHeader.substring(7);
//            System.out.println("Token received: " + token);
//        }
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();

        JSONObject json = new JSONObject(requestBody);
        String jwt = json.getString("jwt");
        String username = JwtUtils.getUsernameFromToken(jwt);
        System.out.println("Username extracted from token: " + username);
        UserDAOImpl userDAO = new UserDAOImpl();
        int userID = userDAO.getUserId(username);
        int productID = Integer.parseInt(json.getString("productId"));
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        System.out.println("Adding product with ID: " + productID + " to cart for user ID: " + userID);
        if(menuDAO.addOrUpdateCart(userID, productID)) {
            System.out.println("Product added to cart successfully");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\":true, \"message\":\"Product added to cart successfully\"}");
            out.flush();

        } else {
            System.out.println("Failed to add product to cart");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false, \"message\":\"Failed to add product to cart\"}");
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();

        JSONObject json = new JSONObject(requestBody);
        String jwt = json.getString("jwt");
        String username = JwtUtils.getUsernameFromToken(jwt);
        System.out.println("Username extracted from token: " + username);
        UserDAOImpl userDAO = new UserDAOImpl();
        int userID = userDAO.getUserId(username);
        int productID = Integer.parseInt(json.getString("productId"));
        MenuDAOImpl menuDAO = new MenuDAOImpl();

        if(menuDAO.removeFromCart(userID, productID)) {
            System.out.println("Product removed from cart successfully");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\":true, \"message\":\"Product removed from cart successfully\"}");
            out.flush();
        } else {
            System.out.println("Failed to remove product from cart");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"success\":false, \"message\":\"Failed to remove product from cart\"}");
            out.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
