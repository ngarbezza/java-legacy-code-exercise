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

public class TestAnd {

    private And andUnderTest;
    private HourInterval someHourInterval;

    @Before
    public void setUp() {
        someHourInterval = new HourInterval(new Timestamp(10), new Timestamp(14));
        Period leftExpression = new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 22),
                new Weekly(new GregorianCalendar(2014, Calendar.AUGUST, 5)));
        Period rightExpression = new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 24),
                new Daily(new GregorianCalendar(2014, Calendar.AUGUST, 3)));
        andUnderTest = new And(leftExpression, rightExpression);
    }

    @Test
    public void containsCalendar() {
        // TODO split and put good names
        assertFalse(andUnderTest.contains(new GregorianCalendar(2014, Calendar.JULY, 23, 11, 0)));
        assertFalse(andUnderTest.contains(new GregorianCalendar(2014, Calendar.AUGUST, 4, 11, 0)));
        assertFalse(andUnderTest.contains(new GregorianCalendar(2014, Calendar.SEPTEMBER, 2, 11, 0)));
        assertTrue(andUnderTest.contains(new GregorianCalendar(2014, Calendar.JULY, 29, 11, 0)));
    }

    @Test
    public void containsLogicalDateFulfiller() {
        assertFalse(andUnderTest.contains(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 23))));
        assertFalse(andUnderTest.contains(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 4))));
        assertFalse(andUnderTest.contains(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 12))));
        assertTrue(andUnderTest.contains(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 29))));
    }

    @Test
    public void isIn() {
        assertTrue(andUnderTest.isIn(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 21), new Daily(new GregorianCalendar(2014, Calendar.AUGUST, 6)))));
        assertFalse(andUnderTest.isIn(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 21), new Weekly(new GregorianCalendar(2014, Calendar.AUGUST, 6)))));
    }

    @Test
    public void intersectsWith() {
        assertTrue(andUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 21), new Daily(new GregorianCalendar(2014, Calendar.AUGUST, 6)))));
        assertFalse(andUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 21), new Weekly(new GregorianCalendar(2014, Calendar.AUGUST, 6)))));
        assertFalse(andUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.AUGUST, 10))));
        assertFalse(andUnderTest.intersectsWith(new SimplePeriod(someHourInterval, new GregorianCalendar(2014, Calendar.JULY, 8))));
    }
}
