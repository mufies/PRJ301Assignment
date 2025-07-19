package DAO;

import Model.Dbconnect;
import Model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl {
    public List<Employee> getAllEmployee() {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT * FROM Employee";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int employeeId = rs.getInt("employee_Id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                java.util.Date hireDate = rs.getDate("hire_date");
                String position = rs.getString("position");
                long salary = rs.getLong("salary");
                String status = rs.getString("status");

                Employee employee = new Employee(employeeId, username, password, fullName, email, phone, address, hireDate, position, salary, status);
                employees.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }

    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;

        String sql = "SELECT * FROM Employee WHERE employee_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                java.util.Date hireDate = rs.getDate("hire_date");
                String position = rs.getString("position");
                long salary = rs.getLong("salary");
                String status = rs.getString("status");

                employee = new Employee(employeeId, username, password, fullName, email, phone, address, hireDate, position, salary, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employee;
    }

    public boolean updateEmployee(int employeeId, String username, String password, String fullName, String email, String phone, String address, String position, long salary) {
        String sql = "UPDATE Employee SET username = ?, password = ?, full_name = ?, email = ?, phone = ?, address = ?, position = ?, salary = ? WHERE employee_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.setString(7, position);
            ps.setLong(8, salary);
            ps.setInt(9, employeeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addEmployee(String username, String password, String fullName, String email, String phone, String address, java.util.Date hireDate, String position, long salary) {
        String sql = "INSERT INTO Employee (username, password, full_name, email, phone, address, hire_date, position, salary,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,'Inactive')";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.setDate(7, new java.sql.Date(hireDate.getTime()));
            ps.setString(8, position);
            ps.setLong(9, salary);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM Employee WHERE employee_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getEmployeeByUsername(String username) {
        int employeeId = -1;
        String sql = "SELECT employee_id FROM Employee WHERE username = ?";

        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                employeeId = rs.getInt("employee_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeId;
    }

    public boolean updateEmployee(int employeeId, String password, String fullName, String email, String phone, String address) {
        String sql = "UPDATE Employee SET  password = ?, full_name = ?, email = ?, phone = ?, address = ? WHERE employee_id = ?";
        try (Dbconnect db = new Dbconnect();
             java.sql.Connection con = db.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, password);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setInt(6, employeeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
