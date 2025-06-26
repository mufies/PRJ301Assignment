package DAO;

import Model.Dbconnect;
import Model.Product;
import com.sun.mail.imap.protocol.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOImpl {
    public List<Product> getMenuItems() {
        List<Product> items = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setType(rs.getString("type"));
                items.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
public boolean addOrUpdateCart(int userID, int productId) {
    String sql = "MERGE INTO Cart AS target " +
            "USING (SELECT ? AS user_id, ? AS product_id, 1 AS quantity) AS source " +
            "ON target.user_id = source.user_id AND target.product_id = source.product_id " +
            "WHEN MATCHED THEN " +
            "    UPDATE SET quantity = target.quantity + 1 " +
            "WHEN NOT MATCHED THEN " +
            "    INSERT (user_id, product_id, quantity) VALUES (source.user_id, source.product_id, source.quantity);";
    try (Dbconnect db = new Dbconnect();
         java.sql.Connection con = db.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, userID);
        ps.setInt(2, productId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    public boolean removeFromCart(int userID, int productId) {
        String sql =
                "IF EXISTS (SELECT 1 FROM Cart WHERE user_id = ? AND product_id = ? AND quantity > 1) " +
                        "BEGIN " +
                        "    UPDATE Cart SET quantity = quantity - 1 WHERE user_id = ? AND product_id = ?; " +
                        "END " +
                        "ELSE " +
                        "BEGIN " +
                        "    DELETE FROM Cart WHERE user_id = ? AND product_id = ?; " +
                        "END";

        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set all 6 parameters
            ps.setInt(1, userID);      // for IF EXISTS
            ps.setInt(2, productId);
            ps.setInt(3, userID);      // for UPDATE
            ps.setInt(4, productId);
            ps.setInt(5, userID);      // for DELETE
            ps.setInt(6, productId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }










}
