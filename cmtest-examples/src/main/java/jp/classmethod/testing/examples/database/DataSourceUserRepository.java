package jp.classmethod.testing.examples.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUserRepository extends AbstractUserRepository {

    @Override
    protected Connection getConnection() throws SQLException {
        return DataSourceManager.getInstance().getDataSource().getConnection();
    }

}
