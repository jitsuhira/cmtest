package jp.classmethod.testing.examples.baseunits.fixclock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.classmethod.testing.baseunits.FixClock;
import jp.xet.baseunits.time.TimePoint;
import jp.xet.baseunits.timeutil.Clock;

import org.junit.Rule;
import org.junit.Test;

public class FixClockExample {

    @Rule
    public FixClock fixClock = FixClock.at(TimePoint.atUTC(2013, 11, 10, 12, 23, 34));

    @Test
    public void Clockのnowメソッドは固定化された時刻を返す() throws Exception {
        // Exercise
        TimePoint now = Clock.now();
        // Verify
        assertThat(now, is(TimePoint.atUTC(2013, 11, 10, 12, 23, 34)));
    }
}
