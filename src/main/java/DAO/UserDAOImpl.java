package DAO;
import Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Admin loginAdmin(String username, String password) {
        String sql = "select * from Admin where username = ? and password = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setFullName(rs.getString("full_name"));
                admin.setEmail(rs.getString("email"));
                admin.setPhone(rs.getString("phone"));
                return admin;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = """
        SELECT 
            u.user_id,
            u.full_name AS name,
            u.username,
            u.password,
            u.full_name,
            u.email,
            u.phone,
            u.address,
            COALESCE(SUM(o.total_money), 0) AS total_spent
        FROM dbo.Users u
        LEFT JOIN dbo.Orders o ON u.user_id = o.user_id
        GROUP BY 
            u.user_id, u.username, u.password, u.full_name, u.email, u.phone, u.address
    """;

        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFullName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone"));
                user.setTotalSpent(rs.getLong("total_spent"));
                user.setAddress(rs.getString("address"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }




    public boolean deleteUserById(int userId) {
        String sql = """
        UPDATE Users
        SET
            username = CONCAT('deleted_user_', CAST(RAND() * 100000 AS INT)),
            password = CONCAT('deleted_pwd_', CAST(RAND() * 100000 AS INT)),
            full_name = 'account deleted'
        WHERE user_id = ? AND full_name != 'account deleted'
    """;

        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean updateUser(int userId, String fullName, String email, String phone, String address, String password) {
        String sql = "UPDATE Users SET full_name = ?, email = ?, phone = ?, address = ?, password = ? WHERE user_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, password);
            ps.setInt(6, userId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }









}
