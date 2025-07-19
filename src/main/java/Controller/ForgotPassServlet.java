package Controller;

import DAO.UserDAOImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

@WebServlet(name = "ForgotPassServlet", urlPatterns = {"/forgotpassword"})
public class ForgotPassServlet extends HttpServlet {



    public static String generateCode(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256,"idkwhattowritesoyesimusingthiszzz".getBytes(StandardCharsets.UTF_8))
                .compact();
    }



    private void sendEmail(String toEmail, String code) throws MessagingException {
        String fromEmail = "nga585144@gmail.com";
        String password = "cmnr uozo ncvm lrmy";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("MammamKorea"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Mã xác thực quên mật khẩu");
        message.setText("Mã xác thực của bạn là: " + code);

        Transport.send(message);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }



@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String email = request.getParameter("email");
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    if (email == null || email.isEmpty()) {
        out.print("{\"success\":false, \"message\":\"Email không được để trống\"}");
        return;
    }

    if(!new UserDAOImpl().checkEmailExists(email)) {
        out.print("{\"success\":false, \"message\":\"Email không tồn tại\"}");
        return;
    }

    String code = generateCode(email);

    try {
        sendEmail(email, code);
        request.getSession().setAttribute("email", email);
        request.getSession().setAttribute("resetCode", code);

        // Return success response to show the modal instead of redirecting
        out.print("{\"success\":true, \"message\":\"Mã xác thực đã được gửi đến email của bạn\"}");
    } catch (MessagingException e) {
        e.printStackTrace();
        out.print("{\"success\":false, \"message\":\"Gửi email thất bại\"}");
    }
}

//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String code = request.getParameter("code");
//        String newPassword = request.getParameter("newPassword");
//        String confirmPassword = request.getParameter("confirmPassword");
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter out = response.getWriter();
//        String email = (String) request.getSession().getAttribute("email");
//        if (email == null || email.isEmpty()) {
//            out.print("{\"success\":false, \"message\":\"Email không hợp lệ\"}");
//            return;
//        }
//        if (code == null || code.isEmpty()) {
//            out.print("{\"success\":false, \"message\":\"Mã xác thực không được để trống\"}");
//            return;
//        }
//        if (newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
//            out.print("{\"success\":false, \"message\":\"Mật khẩu không được để trống\"}");
//            return;
//        }
//        if (!newPassword.equals(confirmPassword)) {
//            out.print("{\"success\":false, \"message\":\"Mật khẩu không khớp\"}");
//            return;
//        }
//        try {
//            String subject = Jwts.parser()
//                    .setSigningKey("idkwhattowritesoyesimusingthis".getBytes(StandardCharsets.UTF_8))
//                    .parseClaimsJws(code)
//                    .getBody()
//                    .getSubject();
//
//
//            if (subject.equals(email)) {
//                UserDAOImpl userDAOImpl = new UserDAOImpl();
//                boolean isUpdated = userDAOImpl.updatePassword(email, newPassword);
//                if (isUpdated) {
//                    out.print("{\"success\":true, \"message\":\"Mật khẩu đã được cập nhật thành công\"}");
//                } else {
//                    out.print("{\"success\":false, \"message\":\"Cập nhật mật khẩu thất bại\"}");
//                }
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
