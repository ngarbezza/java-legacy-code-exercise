package ar.edu.unq.sasa.model.time.repetition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestDaily {

	private Daily dailyRep;
	private LogicalHourFulfiller someHourInterval;

	@Before
	public void setUp() {
		Calendar endDate = new GregorianCalendar(2010, Calendar.JULY, 9);
		someHourInterval = new HourInterval(new Timestamp(7), new Timestamp(10));
		dailyRep = new Daily(endDate); // repetición hasta el 9 de julio
	}

	@Test
	public void test_containsInSomeRepetitionWhenTheConditionIsSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		Calendar current = (Calendar) start.clone();
		current.add(Calendar.DAY_OF_MONTH, 1);
		while (!dailyRep.getEnd().equals(current)) {
			assertTrue(dailyRep.containsInSomeRepetition(current, start));
			current.add(Calendar.DAY_OF_MONTH, 1); // verifico todos los días
		}
	}

	@Test
	public void test_containsInSomeRepetitionWhenTheConditionIsntSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		Calendar c1 = new GregorianCalendar(2010, Calendar.MAY, 28);
		Calendar c2 = new GregorianCalendar(2010, Calendar.JULY, 11);

		assertFalse("The start date should not be included in the repetition",
				dailyRep.containsInSomeRepetition(start, start));
		assertFalse("Any date before the start date should not be included",
				dailyRep.containsInSomeRepetition(c1, start));
		assertFalse("Any date after the end date should not be included",
				dailyRep.containsInSomeRepetition(c2, start));
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsSatisfied() {
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 28), dailyRep);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		assertTrue(dailyRep.thereIsSomeDayIn(period, start));
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsntSatisfied() {
		Repetition dailyRepetitionUntilJune14 = new Daily(new GregorianCalendar(2010, Calendar.JUNE, 14));
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 2), dailyRepetitionUntilJune14);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		assertFalse(dailyRep.thereIsSomeDayIn(period, start));
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsSatisfied() {
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 16), dailyRep);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		assertTrue(dailyRep.isAllDaysIn(period, start));
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsntSatisfied() {
		Repetition dailyRepetitionUntilJune14 = new Daily(new GregorianCalendar(2010, Calendar.JUNE, 14));
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 2), dailyRepetitionUntilJune14);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);
		assertFalse(dailyRep.isAllDaysIn(period, start));
	}
}