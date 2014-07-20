package ar.edu.unq.sasa.model.time;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.repetition.Repetition;
import ar.edu.unq.sasa.model.time.repetition.Weekly;

public class TestSimplePeriod {

	private SimplePeriod periodUnderTest;

	@Before
	public void setUp() throws Exception {
		// se crea un período, todos los miércoles empezando desde el 9 de junio
		// hasta el 15 de octubre, de 12 a 15:30 horas.
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 9);
		Calendar end = new GregorianCalendar(2010, Calendar.OCTOBER, 15);
		LogicalHourFulfiller lhf = new HourInterval(new Timestamp(12), new Timestamp(15, 30));
		periodUnderTest = new SimplePeriod(lhf, start, new Weekly(end));
	}

	@Test
	public void test_constructor() {
		Calendar startMock = createMock(Calendar.class);
		Repetition repMock = createMock(Repetition.class);
		LogicalHourFulfiller lhf = createMock(LogicalHourFulfiller.class);
		SimplePeriod p = new SimplePeriod(lhf, startMock, repMock);

		assertSame(p.getStart(), startMock);
		assertSame(p.getRepetition(), repMock);
		assertSame(p.getHourFulfiller(), lhf);
	}

	@Test
	public void test_containsCalendarSatisfiedInDaysAndHours() throws Exception {
		Calendar date1 = new GregorianCalendar(2010, Calendar.JUNE, 9, 12, 0); 
		Calendar date2 = new GregorianCalendar(2010, Calendar.JUNE, 9, 15, 30); 
		Calendar date3 = new GregorianCalendar(2010, Calendar.JUNE, 9, 14, 30); 
		Calendar date4 = new GregorianCalendar(2010, Calendar.OCTOBER, 13, 12, 0);
		Calendar date5 = new GregorianCalendar(2010, Calendar.OCTOBER, 13, 15, 30);
		Calendar date6 = new GregorianCalendar(2010, Calendar.OCTOBER, 13, 14, 30);
		Calendar date7 = new GregorianCalendar(2010, Calendar.JULY, 21, 12, 0);
		Calendar date8 = new GregorianCalendar(2010, Calendar.AUGUST, 4, 15, 30);
		Calendar date9 = new GregorianCalendar(2010, Calendar.SEPTEMBER, 15, 13, 0);
		
		assertTrue(periodUnderTest.contains(date1));
		assertTrue(periodUnderTest.contains(date2));
		assertTrue(periodUnderTest.contains(date3));
		assertTrue(periodUnderTest.contains(date4));
		assertTrue(periodUnderTest.contains(date5));
		assertTrue(periodUnderTest.contains(date6));
		assertTrue(periodUnderTest.contains(date7));
		assertTrue(periodUnderTest.contains(date8));
		assertTrue(periodUnderTest.contains(date9));	
	}
	
	@Test
	public void test_containsCalendarSatisfiedInDaysButNotInHours() throws Exception {
		Calendar date1 = new GregorianCalendar(2010, Calendar.JUNE, 30, 11, 00);
		Calendar date2 = new GregorianCalendar(2010, Calendar.OCTOBER, 6, 17, 30);
		
		assertFalse("30 de junio, 11:00 hs : anterior a fecha estipulada", 
				periodUnderTest.contains(date1));
		assertFalse("6 de octubre, 17:30 hs : posterior a fecha estipulada", 
				periodUnderTest.contains(date2));
	}
	
	public void test_containsCalendarSatisfiedInHoursButNotInDays() throws Exception {
		Calendar date1 = new GregorianCalendar(2010, Calendar.MAY, 26, 13, 30);
		Calendar date2 = new GregorianCalendar(2010, Calendar.OCTOBER, 20, 14, 00);
		Calendar date3 = new GregorianCalendar(2010, Calendar.JULY, 9, 12, 30);
		
		assertFalse("26 de mayo, 13:30 hs : es miercoles pero es anterior a fecha inicial",
				periodUnderTest.contains(date1));
		assertFalse("20 de octubre, 14:00 hs : es miercoles pero es posterior a fecha inicial",
				periodUnderTest.contains(date2));
		assertFalse("9 de julio, 12:30 hs : esta dentro del rango pero no es miercoles",
				periodUnderTest.contains(date3));
	}
	
	@Test
	public void test_containsCalendarSatisfiedNeitherDaysNorHours() throws Exception {
		Calendar date1 = new GregorianCalendar(2010, Calendar.JUNE, 8, 22, 0);
		Calendar date2 = new GregorianCalendar(2010, Calendar.JULY, 23, 7, 30);
		Calendar date3 = new GregorianCalendar(2010, Calendar.SEPTEMBER, 10, 23, 30);
		
		assertFalse(periodUnderTest.contains(date1));
		assertFalse(periodUnderTest.contains(date2));
		assertFalse(periodUnderTest.contains(date3));
	}
	
	@Test
	public void test_containsPeriodSatisfiedInDaysAndHours() throws Exception {
		LogicalHourFulfiller lhf1 = new HourInterval(new Timestamp(13), new Timestamp(15, 30));
		Period period1 = new SimplePeriod(lhf1, new GregorianCalendar(2010, Calendar.JULY, 28));
		LogicalHourFulfiller lhf2 = new HourInterval(new Timestamp(12), new Timestamp(14, 30));
		Period period2 = new SimplePeriod(lhf2, new GregorianCalendar(2010, Calendar.AUGUST, 11), 
				new Weekly(new GregorianCalendar(2010, Calendar.SEPTEMBER, 22)));
		
		assertTrue(periodUnderTest.contains(period1));
		assertTrue(periodUnderTest.contains(period2));
	}
	
	@Test
	public void test_containsPeriodSatisfiedInDaysButNotInHours() throws Exception {
		LogicalHourFulfiller lhf1 = new HourInterval(new Timestamp(9), new Timestamp(13));
		Period period1 = new SimplePeriod(lhf1, new GregorianCalendar(2010, Calendar.JULY, 28));
		LogicalHourFulfiller lhf2 = new HourInterval(new Timestamp(16, 30), new Timestamp(19));
		Period period2 = new SimplePeriod(lhf2, new GregorianCalendar(2010, Calendar.AUGUST, 11), 
				new Weekly(new GregorianCalendar(2010, Calendar.SEPTEMBER, 22)));
		
		assertFalse(periodUnderTest.contains(period1));
		assertFalse(periodUnderTest.contains(period2));
	}
	
	@Test
	public void test_containsPeriodSatisfiedInHoursButNotInDays() throws Exception {
		LogicalHourFulfiller lhf1 = new HourInterval(new Timestamp(12), new Timestamp(13));
		Period period1 = new SimplePeriod(lhf1, new GregorianCalendar(2010, Calendar.JUNE, 21));
		LogicalHourFulfiller lhf2 = new HourInterval(new Timestamp(13), new Timestamp(14));
		Period period2 = new SimplePeriod(lhf2, new GregorianCalendar(2010, Calendar.AUGUST, 11), 
				new Weekly(new GregorianCalendar(2010, Calendar.DECEMBER, 15)));
		
		assertFalse(periodUnderTest.contains(period1));
		assertFalse(periodUnderTest.contains(period2));
	}

	@Test
	public void test_addHourCondition() throws Exception {
		Calendar expected = new GregorianCalendar(2010, Calendar.JULY, 21, 17, 30);
		assertFalse(periodUnderTest.contains(expected));
		periodUnderTest.addHourCondition(new HourInterval(new Timestamp(16), new Timestamp(18)));
		assertTrue(periodUnderTest.contains(expected));
	}
	
	@Test
	public void test_minutesSharedWithPeriodSatisfiedInDaysButNotInHours() throws Exception {
		LogicalHourFulfiller lhf = new HourInterval(new Timestamp(7), new Timestamp(9));
		Period period = new SimplePeriod(lhf, new GregorianCalendar(2010, Calendar.JUNE, 23));
		
		assertEquals(periodUnderTest.minutesSharedWithPeriod(period), 0);
	}
	
	@Test
	public void test_minutesSharedWithPeriodSatisfiedInHoursButNotInDays() throws Exception {
		LogicalHourFulfiller lhf = new HourInterval(new Timestamp(9), new Timestamp(13));
		Period period = new SimplePeriod(lhf, new GregorianCalendar(2010, Calendar.JUNE, 6));
		
		assertEquals(periodUnderTest.minutesSharedWithPeriod(period), 0);
	}
	
	@Test
	public void test_minutesSharedWithPeriodSatisfiedInDaysAndHours() throws Exception {
		LogicalHourFulfiller lhf = new HourInterval(new Timestamp(9), new Timestamp(13, 30));
		Period period = new SimplePeriod(lhf, new GregorianCalendar(2010, Calendar.JUNE, 23));
		
		assertEquals(periodUnderTest.minutesSharedWithPeriod(period), 90);
	}
}