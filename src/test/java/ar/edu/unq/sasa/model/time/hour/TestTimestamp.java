package ar.edu.unq.sasa.model.time.hour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.exceptions.time.TimestampException;

public class TestTimestamp {

	private Timestamp t_9_50, t_15_35, t_17_00, t_22_15;

	@Before
	public void setUp() {
		t_9_50 = new Timestamp(9, 50);
		t_15_35 = new Timestamp(15, 35);
		t_17_00 = new Timestamp(17);
		t_22_15 = new Timestamp(22, 15);
	}

	@Test
	public void constructor() {
		Timestamp t1 = new Timestamp(0, 45);
		Timestamp t2 = new Timestamp(19);

		assertEquals(0, t1.getHour());
		assertEquals(45, t1.getMinutes());
		assertEquals(19, t2.getHour());
		assertEquals(0, t2.getMinutes());
	}

	@Test(expected = TimestampException.class)
	public void failOnConstructWithNegativeHours() {
		new Timestamp(-1, 10);
	}

	@Test(expected = TimestampException.class)
	public void failOnConstructWithNegativeMinutes() {
		new Timestamp(13, -4);
	}

	@Test(expected = TimestampException.class)
	public void failOnSetNegativeHours() {
		new Timestamp(12).setHour(-3);
	}

	@Test(expected = TimestampException.class)
	public void failOnSetNegativeMinutes() {
		new Timestamp(22, 10).setMinutes(-21);
	}

	@Test(expected = TimestampException.class)
	public void failOnConstructWithMinutesGreaterThanAdmitted() {
		new Timestamp(12, 62);
	}

	@Test(expected = TimestampException.class)
	public void failOnConstructWithMinutesLessThanAdmitted() {
		new Timestamp(8, -1);
	}

	@Test(expected = TimestampException.class)
	public void failOnConstructWithHoursGreaterThanAdmitted() {
		new Timestamp(25);
	}

	@Test(expected = TimestampException.class)
	public void failOnConstructWithHoursLessThanAdmitted() {
		new Timestamp(-1);
	}

	@Test
	public void equalsOnEqualTimestamps() {
		Timestamp t1 = new Timestamp(22, 15);
		Timestamp t2 = new Timestamp(17, 0);

		assertTrue("equals() failed", t1.equals(t_22_15));
		assertTrue("equals() failed", t2.equals(t_17_00));
	}

	@Test
	public void equalsOnDifferentTimestamps() {
		Timestamp t1 = new Timestamp(23, 35);
		Timestamp t2 = new Timestamp(9, 05);

		assertFalse("23:35 != 15:35", t1.equals(t_15_35));
		assertFalse("9:05 != 9:50", t2.equals(t_9_50));
	}

	@Test
	public void lessThanWhenTheConditionIsSatisfied() {
		Timestamp t1 = new Timestamp(6, 45);
		Timestamp t2 = new Timestamp(15, 8);

		assertTrue("6:45 must be less than 9:50", t1.lessThan(t_9_50));
		assertTrue("15:08 must be less than 15:35", t2.lessThan(t_15_35));
	}

	@Test
	public void lessThanWhenTheConditionIsntSatisfied() {
		Timestamp t1 = new Timestamp(10, 06);
		Timestamp t2 = new Timestamp(15, 44);

		assertFalse("10:06 should not be less than 9:50", t1.lessThan(t_9_50));
		assertFalse("15:44 should not be less than 15:35", t2.lessThan(t_15_35));
	}

	@Test
	public void greaterThanWhenTheConditionIsSatisfied() {
		Timestamp t1 = new Timestamp(18, 40);
		Timestamp t2 = new Timestamp(22, 16);

		assertTrue("18:40 must be greater than 17:00", t1.greaterThan(t_17_00));
		assertTrue("22:16 must be greater than 22:15", t2.greaterThan(t_22_15));
	}

	@Test
	public void greaterThanWhenTheConditionIsntSatisfied() {
		Timestamp t1 = new Timestamp(7, 59);
		Timestamp t2 = new Timestamp(15, 34);

		assertFalse("7:59 should not be greater than 9:50", t1.greaterThan(t_9_50));
		assertFalse("15:34 should not be greater than 15:35", t2.greaterThan(t_15_35));
	}

	@Test
	public void greaterEqual() {
		Timestamp t1 = new Timestamp(9, 50);
		Timestamp t2 = new Timestamp(10);
		Timestamp t3 = new Timestamp(9, 49);

		assertTrue("9:50 must be >= than 9:50", t1.greaterEqual(t_9_50));
		assertTrue("10 must be >= than 9:50", t2.greaterEqual(t_9_50));
		assertFalse("9:49 should not be >= than 9:50", t3.greaterEqual(t_9_50));
	}

	@Test
	public void lessEqual() {
		Timestamp t1 = new Timestamp(9, 49);
		Timestamp t2 = new Timestamp(9);
		Timestamp t3 = new Timestamp(9, 51);

		assertTrue("9:49 must be <= than 9:50", t1.lessEqual(t_9_50));
		assertTrue("9 must be <= than 9:50", t2.lessEqual(t_9_50));
		assertFalse("9:51 should not be <= than 9:50", t3.lessEqual(t_9_50));
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
