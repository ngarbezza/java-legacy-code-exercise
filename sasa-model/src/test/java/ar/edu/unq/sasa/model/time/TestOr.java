package ar.edu.unq.sasa.model.time;

import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.repetition.Daily;
import ar.edu.unq.sasa.model.time.repetition.Weekly;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestOr {

    private Or orUnderTest;
    private HourInterval someHourInterval;

    @Before
    public void setUp() {
        someHourInterval = new HourInterval(new Timestamp(10), new Timestamp(14));
        Period leftExpression = new SimplePeriod(someHourInterval, new GregorianCalendar(2015, Calendar.JANUARY, 1),
                new Weekly(new GregorianCalendar(2015, Calendar.JANUARY, 15)));
        Period rightExpression = new SimplePeriod(someHourInterval, new GregorianCalendar(2015, Calendar.JANUARY, 12),
                new Daily(new GregorianCalendar(2015, Calendar.JANUARY, 17)));
        orUnderTest = new Or(leftExpression, rightExpression);
    }

    @Test
    public void containsCalendar() {
        assertTrue(orUnderTest.contains(new GregorianCalendar(2015, Calendar.JANUARY, 1, 11, 0)));
        assertTrue(orUnderTest.contains(new GregorianCalendar(2015, Calendar.JANUARY, 8, 11, 0)));
        assertTrue(orUnderTest.contains(new GregorianCalendar(2015, Calendar.JANUARY, 13, 11, 0)));
        assertFalse(orUnderTest.contains(new GregorianCalendar(2015, Calendar.JANUARY, 2, 11, 0)));
        assertFalse(orUnderTest.contains(new GregorianCalendar(2015, Calendar.JANUARY, 18, 11, 0)));
    }

    @Test
    public void containsLogicalDateFulfiller() {
        assertTrue(orUnderTest.contains(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 1, 11, 0))));
        assertTrue(orUnderTest.contains(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 8, 11, 0))));
        assertTrue(orUnderTest.contains(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 13, 11, 0))));
        assertFalse(orUnderTest.contains(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 2, 11, 0))));
        assertFalse(orUnderTest.contains(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 18, 11, 0))));
    }

    @Test
    public void isIn() {
        assertTrue(orUnderTest.isIn(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 1),
                new Daily(new GregorianCalendar(2015, Calendar.JANUARY, 19)))));
        assertFalse(orUnderTest.isIn(new SimplePeriod(someHourInterval,
                new GregorianCalendar(2015, Calendar.JANUARY, 2),
                new Weekly(new GregorianCalendar(2015, Calendar.JANUARY, 16)))));
    }

    @Test
    public void intersectsWith() {
        assertTrue(orUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2015, Calendar.JANUARY, 1), new Daily(new GregorianCalendar(2015, Calendar.JANUARY, 3)))));
        assertTrue(orUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2015, Calendar.JANUARY, 8), new Weekly(new GregorianCalendar(2015, Calendar.JANUARY, 15)))));
        assertTrue(orUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2015, Calendar.JANUARY, 17))));
        assertFalse(orUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2015, Calendar.JANUARY, 18))));
    }
}
