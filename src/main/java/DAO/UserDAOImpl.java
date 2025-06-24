package DAO;
import Model.*;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl {

    public boolean registerUser(String username, String password) {
        if (username == null || username.length() <= 5 || password == null || password.length() <= 8) {
            return false;
        }

        String checkSql = "select * from Users where username = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setString(1, username);
            java.sql.ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        String sql = "insert into Users values (?,?)";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public User loginUser(String username, String password) {
        String sql = "select * from Users where username = ? and password = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getUserRank(String username) {
        String sql = "select count(userid) from BuyHistory where username = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count >= 100) {
                    return "VIP";
                } else if (count >= 50) {
                    return "Gold";
                } else if (count >= 20) {
                    return "Silver";
                } else {
                    return "Bronze";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateUserRank(String username, String rank) {
        String sql = "update ShopUser set rank = ? where username = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rank);
            ps.setString(2, username);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean forgotPassword(String email, String newPassword) {
        String sql = "update Users set password = ? where email = ?";
        try (Dbconnect db = new Dbconnect();

             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String sql = "select * from Users where email = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            java.sql.ResultSet rs = ps.executeQuery();

            return rs.next(); // If a record exists, email is registered

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // If no record found, email is not registered
    }
    public int getUserId(String username) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<CartItem> getUserCart(int userid) {
        List<CartItem> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, p.price, p.image, p.description, p.type, c.quantity " +
              "FROM Products p INNER JOIN Cart c ON p.product_id = c.product_id WHERE c.user_id = ?";


        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userid);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem product = new CartItem();
                System.out.println("Fetching product from cart: " + rs.getString("product_name"));
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("price"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setType(rs.getString("type"));
                product.setQuantity(rs.getInt("quantity"));
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  products;
    }


}
