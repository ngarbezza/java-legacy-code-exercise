package ar.edu.unq.sasa.model.time.hour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.util.PreconditionNotMetException;

public class TestTimestamp {

	private Timestamp t_9_50, t_15_35, t_17_00, t_22_15;

	@Before
	public void setUp() {
		t_9_50 = new Timestamp(9, 50);
		t_15_35 = new Timestamp(15, 35);
		t_17_00 = new Timestamp(17);
		t_22_15 = new Timestamp(22, 15);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void failOnConstructWithNegativeHours() {
		new Timestamp(-1, 10);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void failOnConstructWithNegativeMinutes() {
		new Timestamp(13, -4);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void failOnConstructWithMinutesGreaterThanAdmitted() {
		new Timestamp(12, 62);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void failOnConstructWithMinutesLessThanAdmitted() {
		new Timestamp(8, -1);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void failOnConstructWithHoursGreaterThanAdmitted() {
		new Timestamp(25);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void failOnConstructWithHoursLessThanAdmitted() {
		new Timestamp(-1);
	}

	@Test
	public void equalsOnEqualTimestamps() {
		assertTrue(new Timestamp(22, 15).equals(t_22_15));
		assertTrue(new Timestamp(17, 0).equals(t_17_00));
	}

	@Test
	public void equalsOnDifferentTimestamps() {
		assertFalse(new Timestamp(23, 35).equals(t_15_35));
		assertFalse(new Timestamp(9, 05).equals(t_9_50));
	}

	@Test
	public void lessThanWhenTheConditionIsSatisfied() {
		assertTrue(new Timestamp(6, 45).lessThan(t_9_50));
		assertTrue(new Timestamp(15, 8).lessThan(t_15_35));
	}

	@Test
	public void lessThanWhenTheConditionIsntSatisfied() {
		assertFalse(new Timestamp(10, 06).lessThan(t_9_50));
		assertFalse(new Timestamp(15, 44).lessThan(t_15_35));
	}

	@Test
	public void greaterThanWhenTheConditionIsSatisfied() {
		assertTrue(new Timestamp(18, 40).greaterThan(t_17_00));
		assertTrue(new Timestamp(22, 16).greaterThan(t_22_15));
	}

	@Test
	public void greaterThanWhenTheConditionIsntSatisfied() {
		assertFalse(new Timestamp(7, 59).greaterThan(t_9_50));
		assertFalse(new Timestamp(15, 34).greaterThan(t_15_35));
	}

	@Test
	public void greaterEqual() {
		assertTrue(new Timestamp(9, 50).greaterEqual(t_9_50));
		assertTrue(new Timestamp(10).greaterEqual(t_9_50));
		assertFalse(new Timestamp(9, 49).greaterEqual(t_9_50));
	}

	@Test
	public void lessEqual() {
		assertTrue(new Timestamp(9, 49).lessEqual(t_9_50));
		assertTrue(new Timestamp(9).lessEqual(t_9_50));
		assertFalse(new Timestamp(9, 51).lessEqual(t_9_50));
	}

	@Test
	public void add() {
		assertEquals(new Timestamp(9, 55), t_9_50.add(5));   // sumo 5  a t_9_50
		assertEquals(new Timestamp(10, 15), t_9_50.add(25)); // sumo 25 a t_9_50
		assertEquals(new Timestamp(11, 15), t_9_50.add(85)); // sumo 85 a t_9_50
	}

	@Test
	public void substract() {
		assertEquals(new Timestamp(22, 10), t_22_15.substract(5));   // resto 5   a t_22_15
		assertEquals(new Timestamp(21, 50), t_22_15.substract(25));  // resto 25  a t_22_15
		assertEquals(new Timestamp(20, 50), t_22_15.substract(85));  // resto 85  a t_22_15
		assertEquals(new Timestamp(20), t_22_15.substract(135));     // resto 135 a t_22_15
	}

	@Test
	public void minutesBetweenZero() {
		assertEquals(0, t_9_50.minutesBetween(t_9_50));
	}

	@Test
	public void minutesBetweenPositive() {
		assertEquals(345, t_9_50.minutesBetween(t_15_35));
		assertEquals(85, t_15_35.minutesBetween(t_17_00));
		assertEquals(315, t_17_00.minutesBetween(t_22_15));
	}

	@Test
	public void minutesBetweenNegative() {
		assertEquals(-745, t_22_15.minutesBetween(t_9_50));
	}

	@Test
	public void totalMinutesWithSomeHoursAndMinutes() {
		assertEquals(590, t_9_50.totalMinutes());
	}
}
