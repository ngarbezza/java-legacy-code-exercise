package ar.edu.unq.sasa.model.time.repetition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestNone {

	private None noneRepetition;
	private SimplePeriod someSimplePeriod;
	private Calendar someDate;

	@Before
	public void setUp() throws PeriodException, TimestampException {
		noneRepetition = new None();
		someDate = new GregorianCalendar();
		HourInterval someHourInterval = new HourInterval(new Timestamp(16), new Timestamp(18));
		someSimplePeriod = new SimplePeriod(someHourInterval, someDate, noneRepetition);
	}

	@Test
	public void testContainsInSomeRepetition() {
		assertFalse(noneRepetition.containsInSomeRepetition(someDate, someDate));
	}

	@Test
	public void testThereIsSomeDayIn() throws Exception {
		assertFalse(noneRepetition.thereIsSomeDayIn(someSimplePeriod, someDate));
	}

	@Test
	public void testIsAllDaysIn() throws Exception {
		assertTrue(noneRepetition.isAllDaysIn(someSimplePeriod, someDate));
	}
}
