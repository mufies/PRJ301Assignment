package DAO;

import Model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
                "FROM Orders o JOIN Users u ON o.user_id = u.user_id ";
        String sql2 = "SELECT * FROM OrdersGuest";
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

    public int addOrder(int user_id, int total_money, String description, String shippingCode) {
        String sql = "INSERT INTO Orders (user_id, order_date, total_money, status, description, shipping_code) VALUES (?, GETDATE(), ?, N'Đã nhận đơn', ?,?)";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, user_id);
            ps.setInt(2, total_money);
            ps.setString(3, description);
            ps.setString(4, shippingCode);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // order_id vừa tạo
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm OrderDetails cho order_id, trả về true nếu thành công
    public boolean addOrderDetails(int order_id, String orderDetailsJson) {
        String checkOrderDetailsSql = "SELECT COUNT(*) FROM OrderDetails WHERE order_id = ?";
        String insertOrderDetailSql = "INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection()) {

            // Kiểm tra order_id này đã có trong OrderDetails chưa
            try (PreparedStatement checkStmt = con.prepareStatement(checkOrderDetailsSql)) {
                checkStmt.setInt(1, order_id);
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    // Đã có OrderDetails cho order này
                    return false;
                }
            }

            // Parse JSON và insert từng chi tiết
            JSONArray arr = new JSONArray(orderDetailsJson);
            try (PreparedStatement insertStmt = con.prepareStatement(insertOrderDetailSql)) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    int product_id = obj.getInt("product_id");
                    int quantity = obj.getInt("quantity");
                    int unit_price = obj.getInt("unit_price");

                    insertStmt.setInt(1, order_id);
                    insertStmt.setInt(2, product_id);
                    insertStmt.setInt(3, quantity);
                    insertStmt.setInt(4, unit_price);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int addOrderGuest(String guestName, String guestPhone, String guestAddress, int totalMoney, String description, String shippingCode) {
        String sql = "INSERT INTO OrdersGuest (guest_name, phone, address, order_date, total_money, status, description,shipping_code) VALUES (?, ?, ?, GETDATE(), ?, N'Đã nhận đơn', ?,?)";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, guestName);
            ps.setString(2, guestPhone);
            ps.setString(3, guestAddress);
            ps.setInt(4, totalMoney);
            ps.setString(5, description);
            ps.setString(6, shippingCode);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // order_id vừa tạo
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm OrderDetails cho order_id, trả về true nếu thành công
    public boolean addOrderDetailsGuest(int order_id, String orderDetailsJson) {
        String checkOrderDetailsSql = "SELECT COUNT(*) FROM OrderDetailsGuest WHERE order_id = ?";
        String insertOrderDetailSql = "INSERT INTO OrderDetailsGuest (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection()) {

            // Kiểm tra order_id này đã có trong OrderDetailsGuest chưa
            try (PreparedStatement checkStmt = con.prepareStatement(checkOrderDetailsSql)) {
                checkStmt.setInt(1, order_id);
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    // Đã có OrderDetails cho order này
                    return false;
                }
            }

            // Parse JSON và insert từng chi tiết
            JSONArray arr = new JSONArray(orderDetailsJson);
            try (PreparedStatement insertStmt = con.prepareStatement(insertOrderDetailSql)) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    int product_id = obj.getInt("product_id");
                    int quantity = obj.getInt("quantity");
                    int unit_price = obj.getInt("unit_price");

                    insertStmt.setInt(1, order_id);
                    insertStmt.setInt(2, product_id);
                    insertStmt.setInt(3, quantity);
                    insertStmt.setInt(4, unit_price);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isShippingCodeValid(String shippingCode) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE shipping_code = ?";
        String sql2 = "SELECT COUNT(*) FROM OrdersGuest WHERE shipping_code = ?";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement ps2 = con.prepareStatement(sql2)) {

            ps.setString(1, shippingCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }

            ps2.setString(1, shippingCode);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next() && rs2.getInt(1) > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }



    public JSONObject getOrderSummaryByShippingCode(String shippingCode) {
        String sqlUser = "SELECT u.full_name, o.order_date, o.status, o.description " +
                "FROM Orders o JOIN Users u ON o.user_id = u.user_id WHERE o.shipping_code = ?";
        String sqlGuest = "SELECT guest_name, order_date, status, description FROM OrdersGuest WHERE shipping_code = ?";
        JSONObject orderJson = new JSONObject();

        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement psUser = con.prepareStatement(sqlUser);
             PreparedStatement psGuest = con.prepareStatement(sqlGuest)) {

            psUser.setString(1, shippingCode);
            ResultSet rs = psUser.executeQuery();

            if (rs.next()) {
                orderJson.put("customerName", rs.getString("full_name"));
                orderJson.put("orderDate", rs.getString("order_date"));
                orderJson.put("status", rs.getString("status"));
                orderJson.put("description", rs.getString("description"));
                orderJson.put("items", getOrderItemsDetailByShippingCode(shippingCode, false)); // user
            } else {
                psGuest.setString(1, shippingCode);
                ResultSet rs2 = psGuest.executeQuery();
                if (rs2.next()) {
                    orderJson.put("customerName", rs2.getString("guest_name"));
                    orderJson.put("orderDate", rs2.getString("order_date"));
                    orderJson.put("status", rs2.getString("status"));
                    orderJson.put("description", rs2.getString("description"));
                    orderJson.put("items", getOrderItemsDetailByShippingCode(shippingCode, true)); // guest
                } else {
                    return null; // Không tìm thấy đơn hàng
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return orderJson;
    }


    public JSONArray getOrderItemsDetailByShippingCode(String shippingCode, boolean isGuest) {
        String table, aliasOrder, aliasDetail;
        if (isGuest) {
            table = "OrderDetailsGuest odg JOIN OrdersGuest og ON odg.order_id = og.order_id";
            aliasOrder = "og";
            aliasDetail = "odg";
        } else {
            table = "OrderDetails od JOIN Orders o ON od.order_id = o.order_id";
            aliasOrder = "o";
            aliasDetail = "od";
        }
        String sql = "SELECT p.product_name, " + aliasDetail + ".quantity, " + aliasDetail + ".unit_price " +
                "FROM " + table + " JOIN Products p ON " + aliasDetail + ".product_id = p.product_id " +
                "WHERE " + aliasOrder + ".shipping_code = ?";
        JSONArray items = new JSONArray();
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, shippingCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject item = new JSONObject();
                item.put("productName", rs.getString("product_name"));
                item.put("quantity", rs.getInt("quantity"));
                item.put("price", rs.getInt("unit_price"));
                items.put(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }








}









