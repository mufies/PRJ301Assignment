package DAO;

import Model.Dbconnect;

public class ProductDAOImpl {
    public boolean addProduct( String productName, String price, String type, String image) {
        String sql = "INSERT INTO Products (product_name, price, type, image) VALUES ( ?, ?, ?, ?)";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, productName);
            ps.setString(2, price);
            ps.setString(3, type);
            ps.setString(4, image);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(String productId, String productName, String price, String type, String image) {
        String sql = "UPDATE Products SET product_name = ?, price = ?, type = ?, image = ? WHERE product_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, productName);
            ps.setString(2, price);
            ps.setString(3, type);
            ps.setString(4, image);
            ps.setString(5, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


}
