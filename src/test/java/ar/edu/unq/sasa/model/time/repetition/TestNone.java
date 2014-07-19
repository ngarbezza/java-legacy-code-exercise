package ar.edu.unq.sasa.model.time.repetition;

import junit.framework.TestCase;

/**
 * Test Case para la clase {@link None}.
 * 
 * @author Nahuel
 *
 */
public class TestNone extends TestCase {

	private Repetition noneUnderTest;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		this.noneUnderTest = new None();
	}

	/**
	 * Test method for {@link sasa.model.time.date.None#containsInSomeRepetition(java.util.Calendar, java.util.Calendar)}.
	 */
	public void testContainsInSomeRepetition() {
		assertFalse(noneUnderTest.containsInSomeRepetition(null, null));
	}

	/**
	 * Test method for {@link sasa.model.time.date.None#thereIsSomeDayIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	public void testThereIsSomeDayIn() throws Exception {
		assertFalse(noneUnderTest.thereIsSomeDayIn(null, null));
	}

	/**
	 * Test method for {@link sasa.model.time.date.None#isAllDaysIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	public void testIsAllDaysIn() throws Exception {
		assertTrue(noneUnderTest.isAllDaysIn(null, null));
	}

}
