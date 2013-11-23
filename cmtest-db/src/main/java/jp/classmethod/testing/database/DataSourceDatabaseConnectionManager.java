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
 */package jp.classmethod.testing.database;

import static jp.classmethod.testing.internal.PreConditions.checkNotNull;

import javax.sql.DataSource;

import jp.classmethod.testing.internal.PreConditions;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

/**
 * {@link DataSource}からDbUnitのコネクションを作成するクラス
 * 
 * @since 1.0
 * @author shuji
 */
public class DataSourceDatabaseConnectionManager implements DatabaseConnectionManager {

    protected final DataSource dataSource;
    protected String schema = null;

    /**
     * データソースを指定して、インスタンスを生成する。
     * @param dataSource データソース
     */
    protected DataSourceDatabaseConnectionManager(DataSource dataSource) {
        PreConditions.checkNotNull(dataSource, "dataSource");
        this.dataSource = dataSource;
    }

    @Override
    public IDatabaseConnection getConnection() throws Exception {
        assert dataSource != null;
        return new DatabaseConnection(dataSource.getConnection(), schema);
    }

    /**
     * {@link DataSourceDatabaseConnectionManager}で{@link DbUnitTester}を生成するためのビルダー。
     * @author shuji
     * @since 1.0
     */
    public static class Builder {
        DataSourceDatabaseConnectionManager cm;

        Builder(DataSource dataSource) {
            cm = new DataSourceDatabaseConnectionManager(dataSource);
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
