package ar.edu.unq.sasa.model.time.repetition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestNone {

	private Repetition noneUnderTest;

	@Before
	public void setUp() throws Exception {
		this.noneUnderTest = new None();
	}

	@Test
	public void testContainsInSomeRepetition() {
		assertFalse(noneUnderTest.containsInSomeRepetition(null, null));
	}

	@Test
	public void testThereIsSomeDayIn() throws Exception {
		assertFalse(noneUnderTest.thereIsSomeDayIn(null, null));
	}

	@Test
	public void testIsAllDaysIn() throws Exception {
		assertTrue(noneUnderTest.isAllDaysIn(null, null));
	}
}
