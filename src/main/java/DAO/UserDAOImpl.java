package DAO;
import Model.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean registerUser(String username, String password) {
    if (username == null || username.length() <= 5 || password == null || password.length() <= 8) {
        return false;
    }

        String checkSql = "select * from ShopUser where username = ?";
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




        String sql = "insert into ShopUser values (?,?)";
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

    @Override
    public User loginUser(String username, String password) {
        String sql = "select * from ShopUser where username = ? and password = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
