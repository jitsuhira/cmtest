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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.RowOutOfBoundsException;
import org.dbunit.dataset.datatype.DataType;
import org.yaml.snakeyaml.Yaml;

/**
 * 
 * @author shuji
 * @since 1.0
 */
public class YamlDataSet implements IDataSet {

    private final List<String> tableNames;
    private final Map<String, YamlTable> tables = new LinkedHashMap<>();

    YamlDataSet(Map<String, List<Map<String, Object>>> tables) {
        if (tables != null) {
            this.tableNames = new ArrayList<>(tables.keySet());
        } else {
            this.tableNames = Collections.emptyList();
        }
        for (String tableName : tableNames) {
            List<Map<String, Object>> rows = tables.get(tableName);
            this.tables.put(tableName, new YamlTable(tableName, rows));
        }
    }

    @Override
    public String[] getTableNames() throws DataSetException {
        return tableNames.toArray(new String[tableNames.size()]);
    }

    @Override
    public ITableMetaData getTableMetaData(String tableName) throws DataSetException {
        return tables.get(tableName).tableMetaData;
    }

    @Override
    public ITable getTable(String tableName) throws DataSetException {
        return tables.get(tableName);
    }

    @Override
    @Deprecated
    public ITable[] getTables() throws DataSetException {
        List<ITable> list = new LinkedList<>();
        for (String tableName : this.tableNames) {
            list.add(tables.get(tableName));
        }
        return list.toArray(new ITable[list.size()]);
    }

    @Override
    public ITableIterator iterator() throws DataSetException {
        return new YamlTableIterator(tables.values().iterator());
    }

    @Override
    public ITableIterator reverseIterator() throws DataSetException {
        ArrayList<YamlTable> reverseList = new ArrayList<>(tables.values());
        Collections.reverse(reverseList);
        return new YamlTableIterator(reverseList.iterator());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (YamlTable table : tables.values()) {
            str.append(table.toString()).append("\n");
        }
        return str.toString();
    }

    static class YamlTableIterator implements ITableIterator {
        Iterator<YamlTable> iter;
        YamlTable table = null;

        YamlTableIterator(Iterator<YamlTable> iter) {
            this.iter = iter;
        }

        @Override
        public boolean next() throws DataSetException {
            if (iter.hasNext()) {
                table = iter.next();
                return true;
            } else {
                table = null;
                return false;
            }
        }

        @Override
        public ITable getTable() throws DataSetException {
            return table;
        }

        @Override
        public ITableMetaData getTableMetaData() throws DataSetException {
            return table != null ? table.tableMetaData : null;
        }
    }

    @Override
    public boolean isCaseSensitiveTableNames() {
        return false;
    }

    public static class YamlTable implements ITable {

        final List<Map<String, Object>> rows;
        final ITableMetaData tableMetaData;
        final Column[] columns;

        public YamlTable(final String tableName, List<Map<String, Object>> rows) {
            this.rows = rows;
            ArrayList<Column> cols = new ArrayList<>();
            final ArrayList<String> columnNames = new ArrayList<>();
            if (!rows.isEmpty()) {
                for (Entry<String, Object> entry : rows.get(0).entrySet()) {
                    columnNames.add(entry.getKey());
                    cols.add(new Column(entry.getKey(), DataType.UNKNOWN));
                }
            }
            columns = cols.toArray(new Column[cols.size()]);
            tableMetaData = new ITableMetaData() {

                @Override
                public String getTableName() {
                    return tableName;
                }

                @Override
                public Column[] getColumns() throws DataSetException {
                    return columns;
                }

                @Override
                public Column[] getPrimaryKeys() throws DataSetException {
                    return new Column[0];
                }

                @Override
                public int getColumnIndex(String columnName) throws DataSetException {
                    return columnNames.indexOf(columnName);
                }

            };
        }

        @Override
        public ITableMetaData getTableMetaData() {
            return tableMetaData;
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public Object getValue(int row, String column) throws DataSetException {
            if (row < 0 || rows.size() <= row) {
                throw new RowOutOfBoundsException();
            }
            return rows.get(row).get(column);
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append(tableMetaData.getTableName()).append("\n");
            str.append("-------------------------------------------\n");
            for (Column column : columns) {
                str.append(column.getColumnName()).append("\t");
            }
            str.append("\n");
            for (Map<String, Object> row : rows) {
                for (Column column : columns) {
                    str.append(row.get(column.getColumnName())).append("\t");
                }
                str.append("\n");
            }
            str.append("-------------------------------------------\n");
            return str.toString();
        }
    }

    @SuppressWarnings("unchecked")
    public static YamlDataSet load(InputStream input) {
        return new YamlDataSet((Map<String, List<Map<String, Object>>>) new Yaml().load(input));
    }

}
