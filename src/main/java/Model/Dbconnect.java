package Model;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Dbconnect implements AutoCloseable {
    SQLServerDataSource ds = new SQLServerDataSource();

    public Dbconnect() {
        this.ds.setUser("sa");
        this.ds.setPassword("123");
        this.ds.setPortNumber(1433);
        this.ds.setDatabaseName("PRJAssignment");
        this.ds.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }


    @Override
    public void close() throws Exception {

    }
}
