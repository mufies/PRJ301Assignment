package Controller;

import DAO.UserDAOImpl;
import Model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utils.JwtUtils;

import java.io.BufferedReader;
import java.io.IOException;@WebServlet("/profile")
public class GetUserInfoServlet extends HttpServlet {

    private String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

     RequestDispatcher dispatcher = request.getRequestDispatcher("settingsUser.jsp");
     dispatcher.forward(request, response);
}

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json;charset=UTF-8");
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || authHeader.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }

    if (authHeader.startsWith("getData ")) {
        String jwt = authHeader.substring(8);
        getUserInfo(jwt, response);
    } else if (authHeader.startsWith("updateData ")) {
        String jwt = authHeader.substring(11);

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        if (sb.length() > 0) {
            JSONObject jsonBody = new JSONObject(sb.toString());
            updateUserInfo(jwt, jsonBody, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}

private void getUserInfo(String jwt, HttpServletResponse response) throws IOException {
    try {
        String username = JwtUtils.getUsernameFromToken(jwt);
        UserDAOImpl dao = new UserDAOImpl();
        User user = dao.getUserByUsername(username);

        if (user != null) {
            JSONObject json = new JSONObject();
            json.put("password", user.getPassword());
            json.put("full_name", user.getFullName());
            json.put("email", user.getEmail());
            json.put("phone", user.getPhoneNumber());
            json.put("address", user.getAddress());
            response.getWriter().write(json.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

private void updateUserInfo(String jwt, JSONObject jsonBody, HttpServletResponse response) {
    try {
        String username = JwtUtils.getUsernameFromToken(jwt);
        UserDAOImpl dao = new UserDAOImpl();
        User user = dao.getUserByUsername(username);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        System.out.println(user.getPassword());

        user.setPassword(jsonBody.optString("password", user.getPassword()));
        user.setFullName(jsonBody.optString("full_name", user.getFullName()));
        user.setEmail(jsonBody.optString("email", user.getEmail()));
        user.setPhoneNumber(jsonBody.optString("phone", user.getPhoneNumber()));
        user.setAddress(jsonBody.optString("address", user.getAddress()));

        System.out.println(user.getPassword());

        boolean success = dao.updateUser(user.getUsername(), user.getFullName(), user.getEmail(),
                user.getPhoneNumber(), user.getAddress(), user.getPassword());
        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    } catch (Exception e) {
        e.printStackTrace();
        try {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
}
