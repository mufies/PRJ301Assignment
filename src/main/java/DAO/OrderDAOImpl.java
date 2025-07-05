package DAO;

import Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl {






    public String getOrderByEachMonth() {
        String sql = "SELECT MONTH(order_date) AS month, SUM(total_money) AS total_money " +
                "FROM Orders WHERE status = N'Đã giao' " +
                "GROUP BY MONTH(order_date) " +
                "ORDER BY month";
        StringBuilder jsonBuilder = new StringBuilder("[");
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                jsonBuilder.append("{")
                        .append("\"month\":").append(rs.getInt("month")).append(",")
                        .append("\"total_money\":").append(rs.getLong("total_money"))
                        .append("},");
            }
            if (jsonBuilder.length() > 1) {
                jsonBuilder.setLength(jsonBuilder.length() - 1); // Remove last comma
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    public String getOrderByEachMonthGuest() {
        String sql = "SELECT MONTH(order_date) AS month, SUM(total_money) AS total_money " +
                "FROM OrdersGuest WHERE status = N'Đã giao' " +
                "GROUP BY MONTH(order_date) " +
                "ORDER BY month";
        StringBuilder jsonBuilder = new StringBuilder("[");
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                jsonBuilder.append("{")
                        .append("\"month\":").append(rs.getInt("month")).append(",")
                        .append("\"total_money\":").append(rs.getLong("total_money"))
                        .append("},");
            }
            if (jsonBuilder.length() > 1 && jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
                jsonBuilder.setLength(jsonBuilder.length() - 1); // Xóa dấu phẩy cuối cùng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
public int getCurWeekOrderCount() {
    String sql = "SELECT COUNT(*) FROM Orders WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, GETDATE()) = 0";
    String sql2 = "SELECT COUNT(*) FROM OrdersGuest WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, GETDATE()) = 0";
    int totalCount = 0;
    try (Dbconnect db = new Dbconnect();
         java.sql.Connection con = db.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.PreparedStatement ps2 = con.prepareStatement(sql2)) {

        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            totalCount += rs.getInt(1);
        }

        java.sql.ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            totalCount += rs2.getInt(1);
        }
        return totalCount;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public int getPreWeekOrderCount() {
    String sql = "SELECT COUNT(*) FROM Orders WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, GETDATE()) = 1";
    String sql2 = "SELECT COUNT(*) FROM OrdersGuest WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, GETDATE()) = 1";
    int totalCount = 0;
    try (Dbconnect db = new Dbconnect();
         java.sql.Connection con = db.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.PreparedStatement ps2 = con.prepareStatement(sql2)) {

        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            totalCount += rs.getInt(1);
        }

        java.sql.ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            totalCount += rs2.getInt(1);
        }
        return totalCount;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

public long getCurWeekRev()
{
String sql = "SELECT SUM(total_money) FROM Orders WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, DATEADD(HOUR, 7, GETDATE())) = 0";
String sql2 = "SELECT SUM(total_money) FROM OrdersGuest WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, DATEADD(HOUR, 7, GETDATE())) = 0";
    long totalRev = 0;
    try (Dbconnect db = new Dbconnect();
         java.sql.Connection con = db.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.PreparedStatement ps2 = con.prepareStatement(sql2)) {

        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            totalRev += rs.getLong(1);
        }

        java.sql.ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            totalRev += rs2.getLong(1);
        }
        return totalRev;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public long getPreWeekRev() {
    String sql = "SELECT SUM(total_money) FROM Orders WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, DATEADD(HOUR, 7, GETDATE())) = 1";
    String sql2 = "SELECT SUM(total_money) FROM OrdersGuest WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, DATEADD(HOUR, 7, GETDATE())) = 1";
    long totalRev = 0;
    try (Dbconnect db = new Dbconnect();
         java.sql.Connection con = db.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.PreparedStatement ps2 = con.prepareStatement(sql2)) {

        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            totalRev += rs.getLong(1);
        }

        java.sql.ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            totalRev += rs2.getLong(1);
        }
        return totalRev;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

public String getOrderDetails()
{
    String sql = "SELECT \n" +
            "    p.type, \n" +
            "    COUNT(od.product_id) AS order_count\n" +
            "FROM \n" +
            "    OrderDetails od\n" +
            "JOIN \n" +
            "    Products p ON od.product_id = p.product_id\n" +
            "JOIN OrderDetailsGuest odg on odg.product_id = p.product_id\t\n" +
            "GROUP BY \n" +
            "    p.type\n" +
            "ORDER BY \n" +
            "    order_count DESC;";

    StringBuilder jsonBuilder = new StringBuilder("[");
    try (Dbconnect db = new Dbconnect();
         java.sql.Connection con = db.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            jsonBuilder.append("{")
                    .append("\"product_type\":\"").append(rs.getString("type")).append("\",")
                    .append("\"order_count\":").append(rs.getInt("order_count"))
                    .append("},");
        }
        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 1); // Remove last comma
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    jsonBuilder.append("]");
    return jsonBuilder.toString();
}

    public List<Product> mostRecentProductOrdered() {
        List<Product> products = new ArrayList<>();
        String sql = """
        SELECT TOP 5 
            p.product_id, 
            p.product_name, 
            p.price, 
            p.image, 
            p.description, 
            p.type
        FROM Products p
        JOIN (
            SELECT 
                product_id,
                COUNT(*) AS order_count
            FROM (
                SELECT od.product_id
                FROM OrderDetails od
                JOIN Orders o ON od.order_id = o.order_id
                WHERE o.order_date >= DATEADD(DAY, -7, GETDATE())
                UNION ALL
                SELECT odg.product_id
                FROM OrderDetailsGuest odg
                JOIN OrdersGuest og ON odg.order_id = og.order_id
                WHERE og.order_date >= DATEADD(DAY, -7, GETDATE())
            ) combined
            GROUP BY product_id
        ) recent ON p.product_id = recent.product_id
        ORDER BY recent.order_count DESC
        """;

        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }


private Product mapResultSetToProduct(java.sql.ResultSet rs) throws java.sql.SQLException {
    Product product = new Product();
    product.setProductId(rs.getInt("product_id"));
    product.setProductName(rs.getString("product_name"));
    product.setPrice(rs.getDouble("price"));
    product.setImage(rs.getString("image"));
    product.setDescription(rs.getString("description"));
    product.setType(rs.getString("type"));
    return product;
}

    public List<Order> getOrderForThisWeek()
    {
        String sql = "SELECT o.order_id, u.full_name, o.order_date, o.total_money, o.status, o.description " +
                "FROM Orders o JOIN Users u ON o.user_id = u.user_id " +
                "WHERE o.status = N'Đã giao' AND DATEDIFF(WEEK, o.order_date, GETDATE()) = 0";;
        String sql2 = "SELECT * FROM OrdersGuest WHERE status = N'Đã giao' AND DATEDIFF(WEEK, order_date, GETDATE()) = 0";
        List<Order> orders = new ArrayList<>();
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement ps2 = con.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery();
             ResultSet rs2 = ps2.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerName(rs.getString("full_name"));
                order.setOrderDate(rs.getString("order_date"));
                order.setTotalAmount(rs.getLong("total_money"));
                order.setStatus(rs.getString("status"));
                order.setDescription(rs.getString("description"));
                order.setLoggedIn(true);
                orders.add(order);
            }

            while (rs2.next()) {
                Order order = new Order();
                order.setOrderId(rs2.getInt("order_id"));
                order.setCustomerName(rs2.getString("guest_name"));
                order.setOrderDate(rs2.getString("order_date"));
                order.setTotalAmount(rs2.getLong("total_money"));
                order.setStatus(rs2.getString("status"));
                order.setDescription(rs2.getString("description"));
                order.setLoggedIn(false);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return orders;
    }

public List<OrderItem> getOrderItems(int orderId) {
    String sql = "SELECT p.product_name, p.image, od.quantity, od.unit_price " +
            "FROM OrderDetails od " +
            "JOIN Products p ON od.product_id = p.product_id " +
            "WHERE od.order_id = ?";

    List<OrderItem> orderItems = new ArrayList<>();
    try (Dbconnect db = new Dbconnect();
         Connection con = db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            OrderItem item = new OrderItem();
            item.setProductName(rs.getString("product_name"));
            item.setImageUrl(rs.getString("image"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getLong("unit_price"));
            orderItems.add(item);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return orderItems;
}

public List<OrderItem> getGuestOrderItems(int orderId) {
    String sql = "SELECT p.product_name, p.image, odg.quantity, odg.unit_price " +
            "FROM OrderDetailsGuest odg " +
            "JOIN Products p ON odg.product_id = p.product_id " +
            "WHERE odg.order_id = ?";

    List<OrderItem> orderItems = new ArrayList<>();
    try (Dbconnect db = new Dbconnect();
         Connection con = db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            OrderItem item = new OrderItem();
            item.setProductName(rs.getString("product_name"));
            item.setImageUrl(rs.getString("image"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getLong("unit_price"));
            orderItems.add(item);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return orderItems;
}

    public List<Order> getAllOrders()
    {
        String sql = "SELECT o.order_id, u.full_name, o.order_date, o.total_money, o.status, o.description " +
                "FROM Orders o JOIN Users u ON o.user_id = u.user_id " +
                "WHERE o.status = N'Đã giao'";
        String sql2 = "SELECT * FROM OrdersGuest WHERE status = N'Đã giao'";
        List<Order> orders = new ArrayList<>();
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement ps2 = con.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery();
             ResultSet rs2 = ps2.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerName(rs.getString("full_name"));
                order.setOrderDate(rs.getString("order_date"));
                order.setTotalAmount(rs.getLong("total_money"));
                order.setStatus(rs.getString("status"));
                order.setDescription(rs.getString("description"));
                order.setLoggedIn(true);
                orders.add(order);
            }

            while (rs2.next()) {
                Order order = new Order();
                order.setOrderId(rs2.getInt("order_id"));
                order.setCustomerName(rs2.getString("guest_name"));
                order.setOrderDate(rs2.getString("order_date"));
                order.setTotalAmount(rs2.getLong("total_money"));
                order.setStatus(rs2.getString("status"));
                order.setDescription(rs2.getString("description"));
                order.setLoggedIn(false);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }










}
