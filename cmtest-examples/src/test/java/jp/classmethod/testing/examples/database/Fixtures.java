package jp.classmethod.testing.examples.database;

import jp.classmethod.testing.fixtures.FixtureUtils;

public class Fixtures {

    public static class Users {
        public static User user01() {
            return FixtureUtils.injectTo(new User())
                    .field("id", 1L)
                    .field("name", "Tom")
                    .field("age", 23)
                    .returnObject();
        }
        public static User user02() {
            return FixtureUtils.injectTo(new User())
                    .field("id", 2L)
                    .field("name", "Mike")
                    .field("age", 32)
                    .returnObject();
        }
    }
}
