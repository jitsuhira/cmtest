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

import static jp.classmethod.testing.internal.PreConditions.checkNotEmpty;
import static jp.classmethod.testing.internal.PreConditions.checkNotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sql.DataSource;

import org.dbunit.AbstractDatabaseTester;
import org.dbunit.Assertion;
import org.dbunit.IOperationListener;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * JUnit4のRuleを使うデータベーステストクラス。
 * <p>
 * DbUnitのテスターに処理を委譲し、独自の拡張を行っている。
 * </p>
 * <p>
 * <h4>フィクスチャ指定の拡張</h4>
 * {@link Fixture}アノテーションをテストクラス、またはテストメソッドに宣言することで、
 * フィクスチャファイルの読み込み、初期データとしてのフィクスチャ指定、を宣言的に行うことができる。
 * 特定のテストクラスで共通のフィクスチャを設定したい場合はテストクラスに、
 * テストメソッド毎にフィクスチャを設定する場合ははテストメソッドにアノテーションを宣言すること。
 * </p>
 * 
 * @since 1.0
 * @author shuji
 */
public class DbUnitTester extends AbstractDatabaseTester implements TestRule {

    final DatabaseConnectionManager connectionManager;
    final Queue<IDatabaseConnection> connections = new LinkedBlockingQueue<>();

    /**
     * 空のデータセットでインスタンスを生成する。
     * @param connectionManager DbUnitのコネクション管理を行うオブジェクト
     * @since 1.0
     */
    public DbUnitTester(DatabaseConnectionManager connectionManager) {
        this(connectionManager, new DefaultDataSet());
    }

    /**
     * データセットを指定し、インスタンスを生成する。
     * @param connectionManager DbUnitのコネクション管理を行うオブジェクト
     * @param dataSet データセット
     * @since 1.0
     */
    public DbUnitTester(DatabaseConnectionManager connectionManager, IDataSet dataSet) {
        checkNotNull(connectionManager, "connectionManager");
        checkNotNull(dataSet, "dataSet");
        this.connectionManager = connectionManager;
        setDataSet(dataSet);
        setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        setTearDownOperation(DatabaseOperation.NONE);
        setOperationListener(IOperationListener.NO_OP_OPERATION_LISTENER);
    }

    @Override
    public IDatabaseConnection getConnection() throws Exception {
        IDatabaseConnection conn = connectionManager.getConnection();
        connections.add(conn);
        return conn;
    }

    /**
     * テーブル名を指定し、期待されるデータセットのテーブルとテスト対象データベースのテーブルのデータを検証する。
     * @param tableName 検証するテーブル名
     * @param expected 期待されるデータセット
     * @param ignoreCols 検証時に無視するカラム
     * @throws Exception
     * @since 1.0
     */
    public void verifyTable(String tableName, IDataSet expected, String... ignoreCols) throws Exception {
        verifyTable(tableName, expected.getTable(tableName), ignoreCols);
    }

    /**
     * テーブル名を指定し、期待されるテーブルとテスト対象データベースのテーブルのデータを検証する。
     * @param tableName 検証するテーブル名
     * @param expected 期待されるテーブル
     * @param ignoreCols 検証時に無視するカラム
     * @throws Exception
     * @since 1.0
     */
    public void verifyTable(String tableName, ITable expected, String... ignoreCols) throws Exception {
        Assertion.assertEqualsIgnoreCols(expected, getTable(tableName), ignoreCols);
    }

    /**
     * テーブル名を指定し、テーブルデータを取得する。
     * @param tableName テーブル名
     * @return テーブルデータ
     * @since 1.0
     */
    public ITable getTable(String tableName) throws Exception {
        return getConnection().createDataSet().getTable(tableName);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        setDataSetFromAnnotation(description);
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                onSetup();
                try {
                    base.evaluate();
                } finally {
                    closeAllConnections();
                    onTearDown();
                }
            }
        };
    }

    private void closeAllConnections() {
        for (;;) {
            IDatabaseConnection conn = connections.poll();
            if (conn == null) return;
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataSetFromAnnotation(Description description) {
        Class<?> testClass = description.getTestClass();
        Fixture fixture = findFixtureAnnotation(description);
        if (fixture == null) return;
        Fixture.Type type = fixture.type();
        String[] resources = fixture.resources();
        if (resources == null || resources.length == 0) return;
        try {
            if (resources.length == 1) {
                setDataSet(loadDataSet(type, resources[0], testClass));
            } else {
                IDataSet[] dataSets = new IDataSet[resources.length];
                for (int i = 0; i < resources.length; i++) {
                    dataSets[i] = loadDataSet(type, resources[i], testClass);
                }
                setDataSet(new CompositeDataSet(dataSets));
            }
        } catch (YAMLException e) {
            throw new YAMLException("Cant load fixture: " + Arrays.toString(resources), e);
        } catch (DataSetException | URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
    
    private IDataSet loadDataSet(Fixture.Type type, String resource, Class<?> testClass) 
            throws DataSetException, URISyntaxException {
        switch (type) {
        case CSV:
            URL url = testClass.getResource(resource);
            if (url == null) throw new AssertionError("Can't find resource: " + resource);
            return new CsvDataSet(new File(url.toURI()));
        case YAML:
            InputStream input = getResourceAsStream(testClass, resource);
            if (input == null) throw new AssertionError("Can't find resource: " + resource);
            return YamlDataSet.load(input);
        default:
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    private Fixture findFixtureAnnotation(Description description) {
        Fixture fixture = description.getAnnotation(Fixture.class);
        if (fixture != null) return fixture;
        return description.getTestClass().getAnnotation(Fixture.class);
    }

    private InputStream getResourceAsStream(Class<?> testClass, String resourceName) {
        if (resourceName.startsWith("/")) {
            return getClass().getResourceAsStream(resourceName);
        } else {
            return getClass().getResourceAsStream(
                    "/" + testClass.getPackage().getName().replaceAll("\\.", "/") + "/" + resourceName);
        }
    }

    /**
     * JDBCによる接続を行うテスターを構築するためのビルダを生成する。
     * 
     * @param driverClass JDBCドライバ名
     * @param connectionUrl コネクションURL
     * @return
     * @since 1.0
     */
    public static JdbcDatabaseConnectionManager.Builder forJdbc(String driverClass, String connectionUrl) {
        checkNotEmpty(driverClass, "driverClass");
        checkNotEmpty(connectionUrl, "connectionUrl");
        return new JdbcDatabaseConnectionManager.Builder(driverClass, connectionUrl);
    }

    /**
     * {@link DataSource}からテスターを構築するためのビルダを生成する。
     * 
     * @param dataSource
     * @return
     * @since 1.0
     */
    public static DataSourceDatabaseConnectionManager.Builder forDataSource(DataSource dataSource) {
        checkNotNull(dataSource, "dataSource");
        return new DataSourceDatabaseConnectionManager.Builder(dataSource);
    }

}
