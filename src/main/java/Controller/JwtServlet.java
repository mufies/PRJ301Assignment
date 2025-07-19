package Controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JwtUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "JwtServlet", urlPatterns = {"/JwtServlet"})
public class JwtServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Đọc JSON từ request body
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();

            JsonObject responseJson = new JsonObject();

            if (jsonObject.has("isJwtValid")) {
                String token = jsonObject.get("isJwtValid").getAsString();
                String username = JwtUtils.getUsernameFromToken(token);
                boolean isValid = JwtUtils.validateToken(token, username);
                responseJson.addProperty("isJwtValid", isValid);

            }
            else if (jsonObject.has("getRole")) {
                String token = jsonObject.get("getRole").getAsString();
                String role = JwtUtils.getRoleFromToken(token);
                if (role != null) {
                    responseJson.addProperty("getRole", role);
                } else {
                    responseJson.addProperty("error", "Invalid token or user not found");
                }

            }
            else {
                // Request không hợp lệ
                responseJson.addProperty("error", "Invalid request format");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            out.write(responseJson.toString());

        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Server error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(errorResponse.toString());
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }




    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
