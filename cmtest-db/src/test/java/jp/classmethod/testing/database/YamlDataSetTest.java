package jp.classmethod.testing.database;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.classmethod.testing.verifier.IterableVerifier;

import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class YamlDataSetTest {

    @Test
    public void 空ファイルの場合_空のDataSetとなる() throws Exception {
        // Setup
        InputStream input = this.getClass().getResourceAsStream("no_tables.yaml");
        List<ITable> tables = new ArrayList<>();
        // Exercise
        YamlDataSet actual = YamlDataSet.load(input);
        // Verify
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getTableNames(), is(new String[] {}));
        verify(actual.iterator(), tables);
        verify(actual.reverseIterator(), tables);
    }

    static void verify(ITableIterator actual, List<ITable> expected) throws Exception {
        List<ITable> actualTables = new LinkedList<>();
        while (actual.next())
            actualTables.add(actual.getTable());
        new IterableVerifier<ITable>().verify(actualTables, expected);
    }
}
