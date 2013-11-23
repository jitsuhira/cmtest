package jp.classmethod.testing.examples.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * CREATE DATABASE cmtest;
 * CREATE TABLE users ( id BIGINT NOT NULL, name VARCHAR(64), age TINYINT );
 */
public class JdbcUserRepository {

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }
    static final String FIND_ALL_SQL = "SELECT * FROM users ORDER BY id";
    static final String UPDATE_SQL = "UPDATE users SET name=?, age=? WHERE id = ?";

    public List<User> findAll() throws SQLException {
        try (Connection conn = getConnection(); PreparedStatement stat = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet rs = stat.executeQuery();
            LinkedList<User> result = new LinkedList<>();
            while (rs.next()) {
                User user = new User();
                user.id = rs.getLong("id");
                user.name = rs.getString("name");
                user.age = rs.getInt("age");
                result.add(user);
            }
            return result;
        }
    }

    public void update(User user) throws SQLException {
        try (Connection conn = getConnection(); PreparedStatement stat = conn.prepareStatement(UPDATE_SQL)) {
            stat.setString(1, user.name);
            stat.setInt(2, user.age);
            stat.setLong(3, user.id);
            int update = stat.executeUpdate();
            if (update != 1) throw new SQLException("Can't update user: " + user);
        }
    }

    private Connection getConnection() throws SQLException {
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/cmtest";
        return DriverManager.getConnection(url, user, password);
    }
}
