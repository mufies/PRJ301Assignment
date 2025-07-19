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

    public RegisterResult registerUser(String username, String password, String email, String phone, String address, String fullName) {

        // ==== VALIDATION CƠ BẢN ====
        if (username == null || username.length() <= 5) {
            return new RegisterResult(false, "❌ Tên đăng nhập phải có ít nhất 6 ký tự.");
        }
        if (password == null || password.length() <= 8) {
            return new RegisterResult(false, "❌ Mật khẩu phải có ít nhất 9 ký tự.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return new RegisterResult(false, "❌ Họ tên không được để trống.");
        }

        // ==== VALIDATION EMAIL ====
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (email == null || !email.matches(emailRegex)) {
            return new RegisterResult(false, "❌ Email không hợp lệ.");
        }

        String phoneRegex = "^\\d{10,11}$";
        if (phone == null || !phone.matches(phoneRegex)) {
            return new RegisterResult(false, "❌ Số điện thoại không hợp lệ (10-11 chữ số).");
        }

        String checkSql = "SELECT username, email, phone FROM Users WHERE username = ? OR email = ? OR phone = ?";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setString(1, username);
            checkPs.setString(2, email);
            checkPs.setString(3, phone);

            ResultSet rs = checkPs.executeQuery();
            while (rs.next()) {
                String existingUsername = rs.getString("username");
                String existingEmail = rs.getString("email");
                String existingPhone = rs.getString("phone");

                if (username.equals(existingUsername)) {
                    return new RegisterResult(false, "❌ Tên đăng nhập đã tồn tại.");
                }
                if (email.equals(existingEmail)) {
                    return new RegisterResult(false, "❌ Email đã được sử dụng.");
                }
                if (phone.equals(existingPhone)) {
                    return new RegisterResult(false, "❌ Số điện thoại đã được sử dụng.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResult(false, "❌ Lỗi hệ thống khi kiểm tra tài khoản: " + e.getMessage());
        }

    String checkAdminSql = "SELECT username, email, phone FROM Admin WHERE username = ? OR email = ? OR phone = ?";
    try (Dbconnect db = new Dbconnect();
         Connection con = db.getConnection();
         PreparedStatement checkAdminPs = con.prepareStatement(checkAdminSql)) {

        checkAdminPs.setString(1, username);
        checkAdminPs.setString(2, email);
        checkAdminPs.setString(3, phone);
        ResultSet rs = checkAdminPs.executeQuery();

        if (rs.next()) {
            if (username.equals(rs.getString("username"))) {
                return new RegisterResult(false, "❌ Tên đăng nhập đã được sử dụng bởi Admin.");
            }
            if (email.equals(rs.getString("email"))) {
                return new RegisterResult(false, "❌ Email đã được sử dụng bởi Admin.");
            }
            if (phone.equals(rs.getString("phone"))) {
                return new RegisterResult(false, "❌ Số điện thoại đã được sử dụng bởi Admin.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        return new RegisterResult(false, "❌ Lỗi hệ thống khi kiểm tra Admin: " + e.getMessage());
    }

    // Check Employee table
    String checkEmpSql = "SELECT username, email, phone FROM Employee WHERE username = ? OR email = ? OR phone = ?";
    try (Dbconnect db = new Dbconnect();
         Connection con = db.getConnection();
         PreparedStatement checkEmpPs = con.prepareStatement(checkEmpSql)) {

        checkEmpPs.setString(1, username);
        checkEmpPs.setString(2, email);
        checkEmpPs.setString(3, phone);
        ResultSet rs = checkEmpPs.executeQuery();

        if (rs.next()) {
            if (username.equals(rs.getString("username"))) {
                return new RegisterResult(false, "❌ Tên đăng nhập đã được sử dụng bởi nhân viên.");
            }
            if (email.equals(rs.getString("email"))) {
                return new RegisterResult(false, "❌ Email đã được sử dụng bởi nhân viên.");
            }
            if (phone.equals(rs.getString("phone"))) {
                return new RegisterResult(false, "❌ Số điện thoại đã được sử dụng bởi nhân viên.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        return new RegisterResult(false, "❌ Lỗi hệ thống khi kiểm tra nhân viên: " + e.getMessage());
    }

        // ==== THỰC HIỆN ĐĂNG KÝ ====
        String insertSql = "INSERT INTO Users (username, password, full_name, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(insertSql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, address);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return new RegisterResult(true, "✅ Đăng ký thành công.");
            } else {
                return new RegisterResult(false, "❌ Không thể đăng ký tài khoản. Vui lòng thử lại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResult(false, "❌ Lỗi hệ thống khi đăng ký: " + e.getMessage());
        }
    }

    public class RegisterResult {
        private boolean success;
        private String message;

        public RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
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

    public boolean updateUser(String username, String fullName, String email, String phone, String address, String password) {
        String sql = "UPDATE Users SET full_name = ?, email = ?, phone = ?, address = ?, password = ? WHERE username = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, password);
            ps.setString(6, username);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public Employee loginEmployee(String username, String password) {
        String sql = "SELECT * FROM Employee WHERE username = ? AND password = ?";
        try (Dbconnect db = new Dbconnect();
             Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setUsername(rs.getString("username"));
                employee.setPassword(rs.getString("password"));
                employee.setFullName(rs.getString("full_name"));
                employee.setEmail(rs.getString("email"));
                employee.setPhone(rs.getString("phone"));
                return employee;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT username, password, full_name, email, phone, address FROM Users WHERE username = ?";

        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                return user;
            } else {
                System.out.println("Không có dữ liệu cho username: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }









}
