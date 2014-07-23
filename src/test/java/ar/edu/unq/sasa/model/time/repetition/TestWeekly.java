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

public class TestWeekly {

	private Weekly weeklyRepetition;
	private HourInterval someHourInterval;

	@Before
	public void setUp() throws Exception {
		// repetición hasta el 21 de julio
		weeklyRepetition = new Weekly(new GregorianCalendar(2010, Calendar.JULY, 19));
		someHourInterval = new HourInterval(new Timestamp(7), new Timestamp(10));
	}

	@Test
	public void test_containsInSomeRepetitionWhenTheConditionIsSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		// todos los resultados semanales posibles
		Calendar c1 = new GregorianCalendar(2010, Calendar.JUNE, 21);
		Calendar c2 = new GregorianCalendar(2010, Calendar.JUNE, 28);
		Calendar c3 = new GregorianCalendar(2010, Calendar.JULY, 5);
		Calendar c4 = new GregorianCalendar(2010, Calendar.JULY, 12);
		Calendar c5 = new GregorianCalendar(2010, Calendar.JULY, 19);

		assertTrue(weeklyRepetition.containsInSomeRepetition(c1, start));
		assertTrue(weeklyRepetition.containsInSomeRepetition(c2, start));
		assertTrue(weeklyRepetition.containsInSomeRepetition(c3, start));
		assertTrue(weeklyRepetition.containsInSomeRepetition(c4, start));
		assertTrue(weeklyRepetition.containsInSomeRepetition(c5, start));
	}

	@Test
	public void test_containsInSomeRepetitionWhenTheConditionIsntSatisfied() {
		// TODO hacer tests separados por cada caso
		// 14/6 : es fecha inicial.
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		// 15/7 : está dentro del rango pero no cumple con la condición semanal.
		Calendar c1 = new GregorianCalendar(2010, Calendar.JULY, 15);
		// 26/7 : cumple con requisito semanal pero está fuera de rango.
		Calendar c2 = new GregorianCalendar(2010, Calendar.JULY, 26);
		// 23/7 : fuera de rango y tampoco cumple con condición semanal.
		Calendar c3 = new GregorianCalendar(2010, Calendar.JULY, 23);
		// 9/6 : anterior a fecha inicial.
		Calendar c4 = new GregorianCalendar(2010, Calendar.JUNE, 9);
		assertFalse("The start date should not be included in the repetition",
				weeklyRepetition.containsInSomeRepetition(start, start));
		assertFalse(weeklyRepetition.containsInSomeRepetition(c1, start));
		assertFalse(weeklyRepetition.containsInSomeRepetition(c2, start));
		assertFalse(weeklyRepetition.containsInSomeRepetition(c3, start));
		assertFalse(weeklyRepetition.containsInSomeRepetition(c4, start));
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsSatisfied() throws Exception {
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 28));
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		assertTrue(weeklyRepetition.thereIsSomeDayIn(period, start));
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsntSatisfied() throws Exception {
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 13));
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		assertFalse(weeklyRepetition.thereIsSomeDayIn(period, start));
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsSatisfied() throws Exception {
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 21), weeklyRepetition);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		assertTrue(weeklyRepetition.isAllDaysIn(period, start));
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsntSatisfied() throws Exception {
		SimplePeriod period = new SimplePeriod(someHourInterval, new GregorianCalendar(2010, Calendar.JUNE, 21), weeklyRepetition);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		assertFalse(weeklyRepetition.isAllDaysIn(period, start));
	}
}
