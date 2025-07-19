package Controller;

import DAO.MenuDAOImpl;
import DAO.OrderDAOImpl;
import DAO.UserDAOImpl;
import Model.Product;
import Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Chatbot", urlPatterns = {"/Chatbot"})
public class Chatbot extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Chỉ lấy danh sách món ăn
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        List<Product> products = menuDAO.getMenuItems();

        PrintWriter out = response.getWriter();
        StringBuilder jsonResponse = new StringBuilder();

        jsonResponse.append("{\"products\":[");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            jsonResponse.append("{")
                    .append("\"id\":\"").append(product.getProductId()).append("\",")
                    .append("\"name\":\"").append(escapeJson(product.getProductName())).append("\",")
                    .append("\"price\":").append(product.getPrice())
                    .append("}");
            if (i < products.size() - 1) {
                jsonResponse.append(",");
            }
        }
        jsonResponse.append("]}");

        out.print(jsonResponse.toString());
        out.flush();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // ✅ Đọc JSON từ request body thay vì parameter
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        try {
            JSONObject requestJson = new JSONObject(sb.toString());
            String userMessage = requestJson.optString("message", "");

            if (userMessage.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\":\"Missing message parameter\"}");
                return;
            }

            // Load danh sách món
            MenuDAOImpl menuDAO = new MenuDAOImpl();
            List<Product> products = menuDAO.getMenuItems();

            StringBuilder productList = new StringBuilder();
            for (Product p : products) {
                productList.append("- ").append(p.getProductName()).append(": ").append(p.getPrice()).append("₫\n");
            }

            String prompt = String.format(
                    "Bạn là nhân viên tư vấn món ăn cho nhà hàng.\n" +
                            "Dưới đây là danh sách món hiện có:\n\n%s\n" +
                            "Người dùng hỏi: %s\n" +
                            "Hãy trả lời bằng tiếng Việt tự nhiên.",
                    productList.toString(),
                    userMessage
            );

            String result = callGeminiAPI(prompt);
            JSONObject json = new JSONObject();
            json.put("reply", result);
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject json = new JSONObject();
            json.put("error", "Lỗi xử lý API: " + e.getMessage());
            out.write(json.toString());
        }
        out.flush();
    }


    private String callGeminiAPI(String prompt) throws IOException {
        String apiKey = ""; // 🔒 Chỉ dùng ở server-side, KHÔNG gửi ra client

        String apiURL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;

        JSONObject payload = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        parts.put(new JSONObject().put("text", prompt));
        content.put("parts", parts);
        contents.put(content);
        payload.put("contents", contents);

        JSONObject config = new JSONObject();
        config.put("temperature", 0.7);
        config.put("topK", 40);
        config.put("topP", 0.8);
        config.put("maxOutputTokens", 1024);
        payload.put("generationConfig", config);

        java.net.URL url = new java.net.URL(apiURL);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (java.io.OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONObject json = new JSONObject(response.toString());
            return json.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        }
    }






    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
