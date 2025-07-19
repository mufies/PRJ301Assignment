package DAO;

import Model.Dbconnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CartDAOImpl {
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Dbconnect db = new Dbconnect();
             Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            // Không cần kiểm tra rowsAffected > 0, vì xóa hết cũng coi là thành công
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
