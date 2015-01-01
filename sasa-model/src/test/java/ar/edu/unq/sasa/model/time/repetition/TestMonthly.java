package ar.edu.unq.sasa.model.time.repetition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestMonthly {

	private Monthly monthlyRep;
	private SimplePeriod period;

	@Before
	public void setUp() {
		monthlyRep = new Monthly(new GregorianCalendar(2010, Calendar.AUGUST, 12));
		HourInterval someHourInterval = new HourInterval(new Timestamp(13), new Timestamp(16));
		period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JULY, 8), monthlyRep);
	}

	@Test
	public void containsInSomeRepetitionWhenTheConditionIsSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);
		// todos los resultados semanales posibles
		Calendar c1 = new GregorianCalendar(2010, Calendar.JULY, 8);
		Calendar c2 = new GregorianCalendar(2010, Calendar.AUGUST, 8);

		assertTrue(monthlyRep.containsInSomeRepetition(c1, start));
		assertTrue(monthlyRep.containsInSomeRepetition(c2, start));
	}

	@Test
	public void containsInSomeRepetitionWhenTheConditionIsntSatisfied() {
		// 8/6 : es fecha inicial.
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);
		// 15/7 : está dentro del rango pero no cumple con la condición mensual.
		Calendar c1 = new GregorianCalendar(2010, Calendar.JULY, 15);
		// 8/9 : cumple con requisito mensual pero está fuera de rango.
		Calendar c2 = new GregorianCalendar(2010, Calendar.SEPTEMBER, 8);
		// 23/8 : fuera de rango y tampoco cumple con condición mensual.
		Calendar c3 = new GregorianCalendar(2010, Calendar.AUGUST, 23);
		// 1/6 : anterior a fecha inicial.
		Calendar c4 = new GregorianCalendar(2010, Calendar.JUNE, 1);
		assertFalse("The start date should not be included in the repetition",
				monthlyRep.containsInSomeRepetition(start, start));
		assertFalse(monthlyRep.containsInSomeRepetition(c1, start));
		assertFalse(monthlyRep.containsInSomeRepetition(c2, start));
		assertFalse(monthlyRep.containsInSomeRepetition(c3, start));
		assertFalse(monthlyRep.containsInSomeRepetition(c4, start));
	}

	@Test
	public void thereIsSomeDayInWhenTheConditionIsSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);
		assertTrue(monthlyRep.thereIsSomeDayIn(period, start));
	}

	@Test
	public void thereIsSomeDayInWhenTheConditionIsntSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		assertFalse(monthlyRep.thereIsSomeDayIn(period, start));
	}

	@Test
	public void isAllDaysInWhenTheConditionIsSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);
		assertTrue(monthlyRep.isAllDaysIn(period, start));
	}

	@Test
	public void isAllDaysInWhenTheConditionIsntSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 9);
		assertFalse(monthlyRep.isAllDaysIn(period, start));
	}
}