package jp.classmethod.testing.examples.fixtures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import jp.classmethod.testing.fixtures.FixtureUtils;

public class Persons {

    public static Person Tom() {
        return FixtureUtils.injectTo(new Person())
                    .field("name", "Tom")
                    .field("age", 35)
                    .returnObject();
    }
    
    @Test
    public void setterがないクラスのFixtureを生成する() {
        Person expected = Persons.Tom();
        assertThat(expected.getName(), is("Tom"));
        assertThat(expected.getAge(), is(35));
    }
}
