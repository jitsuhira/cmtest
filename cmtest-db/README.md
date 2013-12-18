# cmtest-db
このモジュールは、JUnitでデータベースのテストを行う場合に便利なヘルパーライブラリです。
内部的にはDbUnitを利用し、YAMLによるフィクスチャ定義や、Ruleを利用して簡潔に記述できることが特徴です。

- [YamlDataSet](#YamlDataSet)
- [DbUnitTester](#DbUnitTester)
	- [DbUnitTesterの宣言](#DbUnitTesterの宣言)
	- [フィクスチャの設定](#フィクスチャの設定)
	- [DbUnitTesterを用いた検証](#DbUnitTesterを用いた検証)

## 依存ライブラリ
- [JUnit](http://junit.org/) 4.11+
- [DbUnit](http://dbunit.sourceforge.net/) 2.4.9
- [SnakeYAML](https://code.google.com/p/snakeyaml/) 1.13
- [cmtest-core](../cmtest-core/)

## Examples

### YamlDataSet
YamlDataSetは、保存形式にYAMLを採用したDbUnitのデータセットです。
FlatXMLDataSetなどよりも簡潔な記述でフィクスチャを定義できます。

YamlDataSetでは次のようにフィクスチャを記述します。
```yaml
roles:
-
  name: admin
  admin: true
-
  name: user
  admin: false
-
  name: guest
  admin: false

users:
-
  id: 1
  name: Tom
  age: 23
  role: admin
-
  id: 2
  name: Mike
  age: 32
  role: user  
```
これはrolseテーブルとusersテーブルにそれぞれ2つのレコードがあるフィクスチャです。
他のデータセットと同様に、最初のレコードからそのテーブルに定義されたカラムを認識するので、テーブルに定義されているカラムはすべて宣言するように注意してください。

テーブルのレコードが0件の場合は次のように空の配列を宣言します。
```yaml
roles: []
users: []
```
その他、YAMLの書式については、[SnakeYAML](https://code.google.com/p/snakeyaml/)のドキュメントを参照してください。

### DbUnitTester 
DbUnitTesterはDbUnitをJUnit4のRuleとして利用するためのクラスです。
また、宣言的にフィクスチャを定義することができるため、テストコードを綺麗にすることができます。

#### DbUnitTesterの宣言
テストクラスでDbUnitTesterを使うためには、Ruleアノテーションを付与したpublicフィールドとして宣言します。
この時、インスタンスの生成にはヘルパーメソッドを使うか、独自の設定を行ったサブクラスを定義すると良いでしょう。

##### ヘルパーメソッドを利用した宣言
JDBCを直接使う場合は、forJdbcメソッドを利用します。
```java
@Rule
public DbUnitTester tester = DbUnitTester.forJdbc("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/cmtest")
            .username("root")
            .password("")
            .create();
```
DataSourceを利用する場合は、forDataSourceメソッドを利用します。
```java
@Rule
public DbUnitTester tester = DbUnitTester.forDataSource(DataSourceManager.getInstance().getDataSource()).create();
```
##### カスタムクラスを利用した宣言
接続情報などはプロジェクト固有の情報となるため、DbUnitTesterのサブクラスを定義すると便利です。
```java
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
```
##### SpringのDIを利用した宣言
SpringFrameworkを利用している場合は、DataSourceをコンストラクタに指定できるDataSourceDbUnitTesterをSpring管理下のインスタンスとして定義し、テストクラスでは@Autowiredでインジェクションすると便利です。
```xml
	<!-- DBUnitTester -->
	<bean id="dbUnitTester" class="jp.classmethod.testing.database.DataSourceDbUnitTester">
		<constructor-arg ref="dataSource" />
	</bean>
```
```java
@Autowired
@Rule
public DbUnitTester tester;
```
#### フィクスチャの設定
DbUnitTesterでは、テストクラスまたはテストメソッドにFixtureアノテーションを宣言することで、リソースファイルに定義したフィクスチャをDataSetとして認識し、テスト開始時にデータベースをセットアップします。
##### テストクラスにフィクスチャを宣言する
通常はテストクラスにフィクスチャを宣言します。
この時、Enclosedテストランナーを併用し、Enclosedテストクラス毎にフィクスチャを適用するとDRYで見通しの良いテストクラスを作ることができます。
例えば、次のテストクラスではusersテーブルにレコードが0件の場合と2件の場合でテストクラスを分割し、それぞれのテストクラスでフィクスチャとなるYAMLを指定しています。
```java
@RunWith(Enclosed.class)
public class UserRepositoryTest {
    @Fixture(resources = "0-user.yaml")
    public static class usersのレコードが0件の場合 {
        @Rule
        public DbUnitTester tester = createDbUnitTester();
        @Test
        public void findAllは0件のレコードを返す() throws Exception {
        }
    }

    @Fixture(resources = "2-users.yaml")
    public static class usersのレコードが2件の場合 {
        @Rule
        public DbUnitTester tester = createDbUnitTester();
        @Test
        public void findAllは2件のレコードを返す() throws Exception {
        }
        @Test
        public void updateでuser01を更新する() throws Exception {
        }
    }
}
```

##### テストメソッドにフィクスチャを宣言する
テスト対象となるクラスが複雑でない場合などは、テストメソッド毎にフィクスチャを宣言することもできます。
```java
public class UserRepositoryTest {
    @Rule
    public DbUnitTester tester = createDbUnitTester();
    @Fixture(resources = "2-users.yaml")
    @Test
    public void updateでuser01を更新する() throws Exception {
    }
}
```

##### フィクスチャを複数指定する
フィクスチャを複数指定することもできます。
```java
public class UserRepositoryTest {
    @Rule
    public DbUnitTester tester = createDbUnitTester();
    @Fixture(resources = {"roles.yaml", "2-users.yaml"})
    @Test
    public void updateでuser01を更新する() throws Exception {
    }
}
```

##### CSVフィクスチャ
フィクスチャにはCSV形式を指定する事ができます。
CSV形式のフィクスチャはDbUnitのCSVDataSetを利用します。

CSV形式を利用する場合は、FixtureアノテーションのtypeにFixture.Type.CSVを、resourceにはCSVファイルを置いたフォルダを指定しています。
```java
@Fixture(type = Fixture.Type.CSV, resources = "fixtures")
@Test
public void findAllは2件のレコードを返す() throws Exception {
}
```
CSVフォルダには、テーブル毎に定義したCSVファイルとCSVファイル名を定義したtable-ordering.txtファイルを置きます。
例えば、usersテーブルとrolesテーブルがある時には次のようにします。

table-ordering.txt
```
roles
users
```

roles.csv
```csv
ID,NAME
1,ADMIN
2,USER
3,GUEST
```

users.csv
```csv
ID,ROLE_ID,NAME
1,1,Administrator
2,2,user01
3,2,user02
```

#### DbUnitTesterを用いた検証
DbUnitTesterはDbUnitのAbstractDatabaseTesterのサブクラスなので、DbUnitで出来ることはすべて行う事ができます。
しかし、通常はDbUnitTesterに追加されているverifyTableメソッドを利用すれば十分な検証ができるでしょう。

以下のサンプルでは、updateメソッドの実行後、期待される状態にテーブルが更新されているかを検証しています。
```java
@Rule
public DbUnitTester tester = createDbUnitTester();

@Fixture(resources = "2-users.yaml")
@Test
public void updateでuser01を更新する() throws Exception {
    // Setup
    JdbcUserRepository sut = new JdbcUserRepository();
    User user = Fixtures.Users.user01();
    user.name = "TOM";
    user.age = 20;
    IDataSet expected = YamlDataSet.load(getClass().getResourceAsStream("2-users-updated.yaml"));
    // Exercise
    sut.update(user);
    // Verify
    tester.verifyTable("users", expected);
}
```

