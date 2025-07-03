package Controller.AdminPageServlet;

import Model.Employee;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@WebServlet(name = "EmployeeManagementServlet", urlPatterns = {"/ayxkix/employee"})
public class EmployeeManagementServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAO.EmployeeDAOImpl employeeDAO = new DAO.EmployeeDAOImpl();
        List<Employee> employeeList = employeeDAO.getAllEmployee();
        System.out.println("Employee List Size: " + employeeList.size());
        request.setAttribute("empList", employeeList);
        RequestDispatcher rd = request.getRequestDispatcher("/employeeManagement.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String hireDate = request.getParameter("hireDate");
        String position = request.getParameter("position");
        String salaryStr = request.getParameter("salary");
        long salary = 0;
        try {
            salary = Long.parseLong(salaryStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Date hireDateObj = null;
        try {
            hireDateObj = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(hireDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        DAO.EmployeeDAOImpl employeeDAO = new DAO.EmployeeDAOImpl();
        if(employeeDAO.addEmployee(username,password,fullName,email,phone,address,hireDateObj,position,salary)){
            response.sendRedirect("/ayxkix/employee");
        } else {
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>Error adding employee</h3>");
            out.println("</body></html>");
        }


    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject(new JSONTokener(request.getReader()));
        int employeeId = json.getInt("employeeId");

        DAO.EmployeeDAOImpl employeeDAO = new DAO.EmployeeDAOImpl();
        if (employeeDAO.deleteEmployee(employeeId)) {
            response.sendRedirect("/ayxkix/employee");
        } else {
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>Error deleting employee</h3>");
            out.println("</body></html>");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
