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

public class TestDaily {

	private Daily dailyRep;

	@Before
	public void setUp() throws Exception {
		Calendar endDate = new GregorianCalendar(2010, Calendar.JULY, 9);
		this.dailyRep = new Daily(endDate); // repetición hasta el 9 de julio
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
	public void test_thereIsSomeDayInWhenTheConditionIsSatisfied() throws Exception {
		// es nice mock para que en todas las llamadas a metodos
		// que retornen booleanos, retorne false (excepto las especificadas).
		SimplePeriod mockSDF = createNiceMock(SimplePeriod.class);
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		// el mock sólo va a decir que sí el 29 de junio,
		// alcanza para que el resultado sea true
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JUNE, 29)))
			.andStubReturn(true);
		replay(mockSDF);
		assertTrue(dailyRep.thereIsSomeDayIn(mockSDF, start));
		verify(mockSDF);
	}

	@Test
	public void test_thereIsSomeDayInWhenTheConditionIsntSatisfied() throws Exception {
		SimplePeriod mockSDF = createNiceMock(SimplePeriod.class);
		replay(mockSDF); // el mock devuelve siempre false
		Calendar start = new GregorianCalendar(2010, Calendar.JUNE, 15);
		assertFalse(dailyRep.thereIsSomeDayIn(mockSDF, start));
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsSatisfied() throws Exception {
		Calendar start = new GregorianCalendar(2010, Calendar.JULY, 5);
		SimplePeriod mockSDF = createMock(SimplePeriod.class);
		for (int i=6; i<10; i++) // obligo a que todos los días en los que se evalúe retornen true
			expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, i)))
				.andReturn(true);
		replay(mockSDF);
		assertTrue(dailyRep.isAllDaysIn(mockSDF, start));
		verify(mockSDF);
	}

	@Test
	public void test_isAllDaysInWhenTheConditionIsntSatisfied() throws Exception {
		Calendar start = new GregorianCalendar(2010, Calendar.JULY, 5);
		SimplePeriod mockSDF = createMock(SimplePeriod.class);
		for (int i=6; i<8; i++) 
			expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, i)))
				.andReturn(true);
		expect(mockSDF.containsDate(new GregorianCalendar(2010, Calendar.JULY, 8))).andReturn(false);
		replay(mockSDF);
		assertFalse(dailyRep.isAllDaysIn(mockSDF, start));
		verify(mockSDF);
	}
}