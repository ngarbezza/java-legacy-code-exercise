package ar.edu.unq.sasa.model.time.repetition;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Test Case para la clase {@link Monthly}.
 * 
 * @author Nahuel
 *
 */
public class TestMonthly extends TestCase {

	private Monthly monthlyRep;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		this.monthlyRep = new Monthly(
			new GregorianCalendar(2010, Calendar.AUGUST, 12));
	}
	
	/**
	 * Test method for {@link sasa.model.time.date.Monthly#containsInSomeRepetition(java.util.Calendar, java.util.Calendar)}.
	 */
	public void test_containsInSomeRepetitionWhenTheConditionIsSatisfied() {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);
		// todos los resultados semanales posibles
		Calendar c1 = new GregorianCalendar(2010, Calendar.JULY, 8);
		Calendar c2 = new GregorianCalendar(2010, Calendar.AUGUST, 8);
		
		assertTrue(monthlyRep.containsInSomeRepetition(c1, start));
		assertTrue(monthlyRep.containsInSomeRepetition(c2, start));
	}
	
	/**
	 * Test method for {@link sasa.model.time.date.Monthly#containsInSomeRepetition(java.util.Calendar, java.util.Calendar)}.
	 */
	public void test_containsInSomeRepetitionWhenTheConditionIsntSatisfied() {
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

	/**
	 * Test method for {@link sasa.model.time.date.Monthly#thereIsSomeDayIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	public void test_thereIsSomeDayInWhenTheConditionIsSatisfied() throws Exception {
		SimplePeriod mockSDF = createNiceMock(SimplePeriod.class);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 28);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 28)))
			.andReturn(true);
		replay(mockSDF);
		assertTrue(monthlyRep.thereIsSomeDayIn(mockSDF, start));
		verify(mockSDF);
	}
	
	/**
	 * Test method for {@link sasa.model.time.date.Monthly#thereIsSomeDayIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	public void test_thereIsSomeDayInWhenTheConditionIsntSatisfied() throws Exception {
		SimplePeriod mockSDF = createNiceMock(SimplePeriod.class);
		replay(mockSDF);	// el mock devuelve siempre false
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 14);
		assertFalse(monthlyRep.thereIsSomeDayIn(mockSDF, start));
	}

	/**
	 * Test method for {@link sasa.model.time.date.Monthly#isAllDaysIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	public void test_isAllDaysInWhenTheConditionIsSatisfied() throws Exception {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);		
		SimplePeriod mockSDF = createMock(SimplePeriod.class);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 8))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.AUGUST, 8))).andReturn(true);
		replay(mockSDF);
		assertTrue(monthlyRep.isAllDaysIn(mockSDF, start));
		verify(mockSDF);
	}
	
	/**
	 * Test method for {@link sasa.model.time.date.Monthly#isAllDaysIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	public void test_isAllDaysInWhenTheConditionIsntSatisfied() throws Exception {
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 8);		
		SimplePeriod mockSDF = createMock(SimplePeriod.class);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 8))).andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.AUGUST, 8))).andReturn(false);
		replay(mockSDF);
		assertFalse(monthlyRep.isAllDaysIn(mockSDF, start));
		verify(mockSDF);
	}
}