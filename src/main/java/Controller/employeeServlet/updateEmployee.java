package Controller.employeeServlet;

import DAO.EmployeeDAOImpl;
import Model.Employee;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;
import utils.JwtUtils;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/updateEmployee")
public class updateEmployee extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("employeeProfile.jsp");
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
          getEmployeeInfo(jwt, response);
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
              updateEmployeeInfo(jwt, jsonBody, response);
          } else {
              response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          }
      } else {
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
  }

  private void updateEmployeeInfo(String jwt, JSONObject jsonBody, HttpServletResponse response) {
      try {
          // Verify JWT user has permission
          String username = JwtUtils.getUsernameFromToken(jwt);

          int employeeId = jsonBody.getInt("employee_id");
          String password = jsonBody.getString("password");
          String fullName = jsonBody.getString("fullName");
          String email = jsonBody.getString("email");
          String phone = jsonBody.getString("phone");
          String address = jsonBody.getString("address");

          // Only update personal information
          EmployeeDAOImpl dao = new EmployeeDAOImpl();
          boolean success = dao.updateEmployee(
              employeeId,
              password,
              fullName,
              email,
              phone,
              address
          );

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

    private void getEmployeeInfo(String jwt, HttpServletResponse response) throws IOException {
        try {
            String username = JwtUtils.getUsernameFromToken(jwt);
            EmployeeDAOImpl dao = new EmployeeDAOImpl();
            int employeeId = dao.getEmployeeByUsername(username);

            if (employeeId != -1) {
                Employee employee = dao.getEmployeeById(employeeId);

                if (employee != null) {
                    JSONObject json = new JSONObject();
                    json.put("employeeId", employee.getEmployeeId());
                    json.put("username", employee.getUsername());
                    json.put("password", employee.getPassword());
                    json.put("fullName", employee.getFullName());
                    json.put("email", employee.getEmail());
                    json.put("phone", employee.getPhone());
                    json.put("address", employee.getAddress());


                    response.getWriter().write(json.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


}