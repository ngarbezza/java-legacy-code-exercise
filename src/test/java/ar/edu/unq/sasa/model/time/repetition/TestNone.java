package ar.edu.unq.sasa.model.time.repetition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test Case para la clase {@link None}.
 * 
 * @author Nahuel
 *
 */
public class TestNone {

	private Repetition noneUnderTest;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		this.noneUnderTest = new None();
	}

	/**
	 * Test method for {@link sasa.model.time.date.None#containsInSomeRepetition(java.util.Calendar, java.util.Calendar)}.
	 */
	@Test
	public void testContainsInSomeRepetition() {
		assertFalse(noneUnderTest.containsInSomeRepetition(null, null));
	}

	/**
	 * Test method for {@link sasa.model.time.date.None#thereIsSomeDayIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	@Test
	public void testThereIsSomeDayIn() throws Exception {
		assertFalse(noneUnderTest.thereIsSomeDayIn(null, null));
	}

	/**
	 * Test method for {@link sasa.model.time.date.None#isAllDaysIn(sasa.model.time.date.SimpleDateFulfiller, java.util.Calendar)}.
	 * @throws Exception 
	 */
	@Test
	public void testIsAllDaysIn() throws Exception {
		assertTrue(noneUnderTest.isAllDaysIn(null, null));
	}

}
