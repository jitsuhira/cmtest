/*
 * Copyright 2013 Classmethod, Inc.
 *
 * Licensed under the Apace License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.classmethod.testing.database;

import static jp.classmethod.testing.internal.PreConditions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.classmethod.testing.internal.PreConditions;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

/**
 * JDBCでDbUnitのコネクションを作成するクラス。
 * <p>
 * ユーザ名とパスワードの両方がnullでない場合、JDBCの接続にユーザ名とパスワードを指定する。
 * </p>
 * 
 * @since 1.0
 * @author shuji
 */
public class JdbcDatabaseConnectionManager implements DatabaseConnectionManager {

    /** logger */
    private static Logger log = Logger.getLogger(JdbcDatabaseConnectionManager.class.getName());
    final protected String driverClass;
    final protected String connectionUrl;
    protected String schema = null;
    protected String username = null;
    protected String password = null;

    /**
     * JDBCドライバ名とコネクションURLを指定して、インスタンスを生成する。
     * 
     * @param driverClass JDBCドライバ名
     * @param connectionUrl コネクションURL
     * @throws ClassNotFoundException JDBCドライバはクラスパスに含まれていない場合
     * @since 1.0
     */
    protected JdbcDatabaseConnectionManager(String driverClass, String connectionUrl) throws ClassNotFoundException {
        PreConditions.checkNotEmpty(driverClass, "driverClass");
        PreConditions.checkNotEmpty(connectionUrl, "connectionUrl");
        this.driverClass = driverClass;
        this.connectionUrl = connectionUrl;
        Class.forName(driverClass);
    }

    @Override
    public IDatabaseConnection getConnection() throws Exception {
        Connection conn = null;
        if (username == null && password == null) {
            log.log(Level.FINE, "connect to {0}", connectionUrl);
            conn = DriverManager.getConnection(connectionUrl);
        } else {
            log.log(Level.FINE, "connect to {0} with ''{1}''/''{2}''",
                    new Object[] { connectionUrl, username, password });
            conn = DriverManager.getConnection(connectionUrl, username, password);
        }
        return new DatabaseConnection(conn, schema);
    }

    /**
     * {@link JdbcDatabaseConnectionManager}で{@link DbUnitTester}を生成するためのビルダー。
     * @author shuji
     * @since 1.0
     */
    public static class Builder {
        JdbcDatabaseConnectionManager cm;

        Builder(String driverClass, String connectionUrl) {
            try {
                cm = new JdbcDatabaseConnectionManager(driverClass, connectionUrl);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Driver class not found.", e);
            }
        }

        /**
         * 接続用ユーザ名を指定
         * @param username 接続用ユーザ名
         * @return このオブジェクト
         */
        public Builder username(String username) {
            cm.username = username;
            return this;
        }

        /**
         * 接続用パスワードを指定
         * @param password 接続用パスワード
         * @return このオブジェクト
         */
        public Builder password(String password) {
            cm.password = password;
            return this;
        }

        /**
         * スキーマ名を指定
         * @param schema スキーマ名
         * @return このオブジェクト
         */
        public Builder schema(String schema) {
            cm.schema = schema;
            return this;
        }

        /**
         * {@link DbUnitTester}オブジェクトを生成する。
         * @return {@link DbUnitTester}オブジェクト
         */
        public DbUnitTester create() {
            return createDbUnitTester(null);
        }

        /**
         * データセットを指定して、{@link DbUnitTester}オブジェクトを生成する。
         * @param dataSet データセット
         * @return {@link DbUnitTester}オブジェクト
         */
        public DbUnitTester create(IDataSet dataSet) {
            checkNotNull(dataSet, "dataSet");
            return createDbUnitTester(dataSet);
        }

        private DbUnitTester createDbUnitTester(IDataSet dataSet) {
            DbUnitTester tester = (dataSet == null) ? new DbUnitTester(cm) : new DbUnitTester(cm, dataSet);
            if (cm.schema != null) tester.setSchema(cm.schema);
            return tester;
        }
    }
}
