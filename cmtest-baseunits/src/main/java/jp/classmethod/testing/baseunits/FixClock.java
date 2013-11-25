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
package jp.classmethod.testing.baseunits;

import jp.xet.baseunits.time.TimePoint;
import jp.xet.baseunits.time.TimeSource;
import jp.xet.baseunits.timeutil.Clock;
import jp.xet.baseunits.timeutil.FixedTimeSource;
import jp.xet.baseunits.timeutil.SystemClock;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * {@link Clock}の現在時刻を制御するルール。
 * 
 * @since 1.0
 * @author shuji
 */
public class FixClock implements TestRule {

    private TimeSource timeSource;

    FixClock(TimeSource timeSource) {
        this.timeSource = timeSource;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                TimeSource defaultTimeSource = Clock.timeSource();
                Clock.setTimeSource(timeSource);
                try {
                    base.evaluate();
                } finally {
                    Clock.setTimeSource(defaultTimeSource);
                }
            }
        };
    }

    /**
     * 固定化した{@link TimePoint}を取得する
     * 
     * @return 固定した時刻
     * @since 1.0
     */
    public TimePoint now() {
        return timeSource.now();
    }

    /**
     * 現在時刻で{@link Clock}を固定するルールを作成する.
     * 
     * @return 現在時刻で{@link Clock}を固定するルール
     * @since 1.0
     */
    public static FixClock atNow() {
        return at(SystemClock.timeSource().now());
    }

    /**
     * 指定した{@link TimePoint}で{@link Clock}を固定するルールを作成する.
     * 
     * @return 指定した{@link TimePoint}で{@link Clock}を固定するルール
     * @since 1.0
     */
    public static FixClock at(TimePoint fixedTime) {
        return new FixClock(new FixedTimeSource(fixedTime));
    }
}
