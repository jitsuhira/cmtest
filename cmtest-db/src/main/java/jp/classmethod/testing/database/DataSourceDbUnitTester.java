package jp.classmethod.testing.database;

import javax.sql.DataSource;

/**
 * コンストラクタに{@link DataSource}を指定して作成する{@link DbUnitTester}。
 * 
 * このクラスはSpring Framework等でデータベースを扱うテストをする場合などに利用する。
 * すなわち、DataSourceはコンテキストファイルなどに定義され、DbUnitTesterはテストクラスにDIされることを想定している。
 * 
 * @author shuji
 * @since 1.0
 */
public class DataSourceDbUnitTester extends DbUnitTester {

    /**
     * {@link DataSource}を指定してインスタンスを生成する。
     * @param dataSource データソース
     * @since 1.0
     */
    public DataSourceDbUnitTester(DataSource dataSource) {
        super(new DataSourceDatabaseConnectionManager(dataSource));
    }

}
