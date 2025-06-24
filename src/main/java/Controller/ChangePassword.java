package Controller;

import DAO.UserDAOImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JwtUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ChangePassword", urlPatterns = {"/ChangePassword"})
public class ChangePassword extends HttpServlet {

    public static boolean isJwtExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt() != null && jwt.getExpiresAt().before(new java.util.Date());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("changePassword.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String email = (String) request.getSession().getAttribute("email");

        if (code == null || code.isEmpty()) {
            out.print("{\"success\":false, \"message\":\"Mã xác thực không được để trống\"}");
            return;
        }
        if (newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            out.print("{\"success\":false, \"message\":\"Mật khẩu không được để trống\"}");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            out.print("{\"success\":false, \"message\":\"Mật khẩu không khớp\"}");
            return;
        }
        try {
            String subject = Jwts.parser()
                    .setSigningKey("idkwhattowritesoyesimusingthiszzz".getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(code)
                    .getBody()
                    .getSubject();


            if(subject == null || subject.isEmpty()) {
                out.print("{\"success\":false, \"message\":\"Mã xác thực không hợp lệ\"}");
                return;
            }
            if (subject.equals(email) && !isJwtExpired(code)) {
                UserDAOImpl userDAOImpl = new UserDAOImpl();
                boolean isUpdated = userDAOImpl.forgotPassword(email, newPassword);
                if (isUpdated) {
                    out.print("{\"success\":true, \"message\":\"Mật khẩu đã được cập nhật thành công\"}");
                } else {
                    out.print("{\"success\":false, \"message\":\"Cập nhật mật khẩu thất bại\"}");
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
