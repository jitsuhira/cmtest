package jp.classmethod.testing.examples.database;

import static jp.classmethod.testing.examples.database.UserVerifier.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.classmethod.testing.database.DbUnitTester;
import jp.classmethod.testing.database.Fixture;
import jp.classmethod.testing.database.YamlDataSet;

import org.dbunit.dataset.IDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class JdbcUserRepositoryTest {

    @Fixture(resources = "0-user.yaml")
    public static class usersのレコードが0件の場合 {
        @Rule
        public DbUnitTester tester = createDbUnitTester();

        @Test
        public void findAllは0件のレコードを返す() throws Exception {
            // Setup
            JdbcUserRepository sut = new JdbcUserRepository();
            List<User> expected = Collections.emptyList();
            // Exercise
            List<User> actual = sut.findAll();
            // Verify
            verify(actual, expected);
        }
    }

    @Fixture(resources = "2-users.yaml")
    public static class usersのレコードが2件の場合 {
        @Rule
        public DbUnitTester tester = createDbUnitTester();

        @Test
        public void findAllは2件のレコードを返す() throws Exception {
            // Setup
            JdbcUserRepository sut = new JdbcUserRepository();
            List<User> expected = Arrays.asList(Fixtures.Users.user01(), Fixtures.Users.user02());
            // Exercise
            List<User> actual = sut.findAll();
            // Verify
            verify(actual, expected);
        }

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

    }

    static DbUnitTester createDbUnitTester() {
        return DbUnitTester.forJdbc("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/cmtest")
                .username("root")
                .password("")
                .create();
    }

}
