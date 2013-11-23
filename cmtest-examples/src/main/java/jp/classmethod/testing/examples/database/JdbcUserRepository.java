package jp.classmethod.testing.examples.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUserRepository extends AbstractUserRepository {

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    protected Connection getConnection() throws SQLException {
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/cmtest";
        return DriverManager.getConnection(url, user, password);
    }

}
