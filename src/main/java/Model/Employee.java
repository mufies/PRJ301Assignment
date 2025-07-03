package Model;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {

    private int    employeeId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Date hireDate;   // hoặc LocalDate
    private String position;
    private long   salary;
    private String status;


    public Employee() {
    }

    public Employee(int employeeId, String username, String password,
                    String fullName, String email, String phone,
                    String address, Date hireDate, String position,
                    long salary, String status) {
        this.employeeId = employeeId;
        this.username   = username;
        this.password   = password;
        this.fullName   = fullName;
        this.email      = email;
        this.phone      = phone;
        this.address    = address;
        this.hireDate   = hireDate;
        this.position   = position;
        this.salary     = salary;
        this.status     = status;
    }


    public int getEmployeeId()           { return employeeId; }
    public void setEmployeeId(int id)    { this.employeeId = id; }

    public String getUsername()          { return username; }
    public void setUsername(String u)    { this.username = u; }

    public String getPassword()          { return password; }
    public void setPassword(String p)    { this.password = p; }

    public String getFullName()          { return fullName; }
    public void setFullName(String n)    { this.fullName = n; }

    public String getEmail()             { return email; }
    public void setEmail(String e)       { this.email = e; }

    public String getPhone()             { return phone; }
    public void setPhone(String p)       { this.phone = p; }

    public String getAddress()           { return address; }
    public void setAddress(String a)     { this.address = a; }

    public Date getHireDate()            { return hireDate; }
    public void setHireDate(Date d)      { this.hireDate = d; }

    public String getPosition()          { return position; }
    public void setPosition(String pos)  { this.position = pos; }

    public long getSalary()              { return salary; }
    public void setSalary(long s)        { this.salary = s; }

    public String getStatus()            { return status; }
    public void setStatus(String st)     { this.status = st; }

    /* ---------- Convenience ---------- */

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", status='" + status + '\'' +
                '}';
    }
}