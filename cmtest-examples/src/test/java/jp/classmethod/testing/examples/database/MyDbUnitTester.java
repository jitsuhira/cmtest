package jp.classmethod.testing.examples.database;

import jp.classmethod.testing.database.DbUnitTester;
import jp.classmethod.testing.database.JdbcDatabaseConnectionManager;

public class MyDbUnitTester extends DbUnitTester {

    public MyDbUnitTester() {
        super(new MyJdbcDatabaseConnectionManager());
    }

    static class MyJdbcDatabaseConnectionManager extends JdbcDatabaseConnectionManager {

        MyJdbcDatabaseConnectionManager() {
            super("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/cmtest");
            super.username = "root";
            super.password = "";
        }

    }

}
