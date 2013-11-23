package jp.classmethod.testing.examples.database;

import javax.sql.DataSource;

import snaq.db.DBPoolDataSource;

public class DataSourceManager {

    private static DataSourceManager INSTANCE = new DataSourceManager();

    private DataSource dataSource = null;

    public static DataSourceManager getInstance() {
        return INSTANCE;
    }

    DataSourceManager() {
        this.dataSource = createDataSource("dataSource");
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private DBPoolDataSource createDataSource(String name) {
        DBPoolDataSource ds = new DBPoolDataSource();
        ds.setName(name);
        ds.setDescription("DataSource by DbPool");
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/cmtest");
        ds.setUser("root");
        ds.setPassword("");
        ds.setMinPool(5);
        ds.setMaxPool(10);
        ds.setMaxSize(30);
        ds.setValidationQuery("SELECT COUNT(*) FROM users");
        return ds;
    }
}
