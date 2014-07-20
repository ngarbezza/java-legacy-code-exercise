package ar.edu.unq.sasa.model.time.repetition;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.time.SimplePeriod;

public class TestWeekly {

	private Weekly weeklyRep;

	@Before
	public void setUp() throws Exception {
		// repetición hasta el 21 de julio
		this.weeklyRep = new Weekly(new GregorianCalendar(2010, Calendar.JULY, 19));
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
		
		assertTrue(weeklyRep.containsInSomeRepetition(c1, start));
		assertTrue(weeklyRep.containsInSomeRepetition(c2, start));
		assertTrue(weeklyRep.containsInSomeRepetition(c3, start));
		assertTrue(weeklyRep.containsInSomeRepetition(c4, start));
		assertTrue(weeklyRep.containsInSomeRepetition(c5, start));
	}

	@Test
	public void test_containsInSomeRepetitionWhenTheConditionIsntSatisfied() {
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
				weeklyRep.containsInSomeRepetition(start, start));
		assertFalse(weeklyRep.containsInSomeRepetition(c1, start));
		assertFalse(weeklyRep.containsInSomeRepetition(c2, start));
		assertFalse(weeklyRep.containsInSomeRepetition(c3, start));
		assertFalse(weeklyRep.containsInSomeRepetition(c4, start));
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsSatisfied() throws Exception {
		SimplePeriod mockSDF = createNiceMock(SimplePeriod.class);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JUNE, 28)))
			.andReturn(true);
		replay(mockSDF);
		assertTrue(weeklyRep.thereIsSomeDayIn(mockSDF, start));
		verify(mockSDF);
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsntSatisfied() throws Exception {
		SimplePeriod mockSDF = createNiceMock(SimplePeriod.class);
		replay(mockSDF);	// el mock devuelve siempre false
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		assertFalse(weeklyRep.thereIsSomeDayIn(mockSDF, start));
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsSatisfied() throws Exception {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);		
		SimplePeriod mockSDF = createMock(SimplePeriod.class);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JUNE, 21))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JUNE, 28))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 5))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 12))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 19))).andReturn(true);
		replay(mockSDF);
		assertTrue(weeklyRep.isAllDaysIn(mockSDF, start));
		verify(mockSDF);
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsntSatisfied() throws Exception {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		SimplePeriod mockSDF = createMock(SimplePeriod.class);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JUNE, 21))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JUNE, 28))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 5))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 12))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 19))).andReturn(false);
		replay(mockSDF);
		assertFalse(weeklyRep.isAllDaysIn(mockSDF, start));
		verify(mockSDF);
	}
}
