/*
 * Copyright 2013 Classmethod, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package jp.classmethod.testing.rules;

import java.util.TimeZone;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnitテストでデフォルトのタイムゾーンを指定するルール。
 * 
 * <p>使い方は以下の通り。</p>
 * <blockquote><pre>
 * // テスト時のタイムゾーンをUTCに設定する
 * &#064;ClassRule
 * public static TestRule TIME_ZONE = DefaultTimeZone.toUTC();
 * </pre></blockquote>
 * <blockquote><pre>
 * // テスト時のタイムゾーンをAmerica/Los_Angelesに設定する
 * &#064;ClassRule
 * public static TestRule TIME_ZONE = DefaultTimeZone.to(&quot;America/Los_Angeles&quot;);
 * </pre></blockquote>
 * 
 * @since 1.0
 * @author shuji
 */
public class DefaultTimeZone implements TestRule {

    /** UTC */
    private static final DefaultTimeZone UTC = new DefaultTimeZone("Universal");
    /** Time Zone */
    public final TimeZone testingTimeZone;

    /**
     * TimeZoneのIDを指定し、 インスタンスを生成する。
     * 
     * <p>IDは {@link TimeZone}で利用できる文字列</p>
     * 
     * @param timzeZoneId TimeZoneのID
     */
    public DefaultTimeZone(String timzeZoneId) {
        this(TimeZone.getTimeZone(timzeZoneId));
    }

    /**
     * {@link TimeZone}を指定し、 インスタンスを生成する。
     * 
     * @param timeZone TimeZone
     */
    public DefaultTimeZone(TimeZone timeZone) {
        if (timeZone == null) throw new NullPointerException("timeZone can't be null.");
        testingTimeZone = timeZone;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                TimeZone defaultTimeZone = TimeZone.getDefault();
                TimeZone.setDefault(testingTimeZone);
                try {
                    base.evaluate();
                } finally {
                    TimeZone.setDefault(defaultTimeZone);
                }
            }
        };
    }

    /**
     * UTCでDefaultTimeZoneルールのインスタンスを取得する。
     * @return UTCに設定したDefaultTimeZoneインスタンス
     */
    public static DefaultTimeZone toUTC() {
        return UTC;
    }

    /**
     * タイムゾーンIDを指定し、DefaultTimeZoneルールのインスタンスを取得する。
     * @return タイムゾーンIDのタイムゾーンに設定したDefaultTimeZoneインスタンス
     */
    public static DefaultTimeZone to(String timeZoneId) {
        return new DefaultTimeZone(timeZoneId);
    }

}
