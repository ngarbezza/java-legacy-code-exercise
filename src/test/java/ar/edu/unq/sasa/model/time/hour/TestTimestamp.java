package ar.edu.unq.sasa.model.time.hour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.exceptions.time.TimestampException;

public class TestTimestamp {

	private Timestamp t_9_50, t_15_35, t_17_00, t_22_15;
	
	@Before
	public void setUp() throws Exception {
		this.t_9_50 = new Timestamp(9, 50);
		this.t_15_35 = new Timestamp(15, 35);
		this.t_17_00 = new Timestamp(17);
		this.t_22_15 = new Timestamp(22, 15);
	}
	
	@Test
	public void test_constructor() throws Exception {
		Timestamp t1 = new Timestamp(0, 45);
		Timestamp t2 = new Timestamp(19);
		
		assertEquals(0, t1.getHour());
		assertEquals(45, t1.getMinutes());
		assertEquals(19, t2.getHour());
		assertEquals(0, t2.getMinutes());
	}
	
	@Test
	public void test_failOnConstructWithNegativeValues() {
		try { 
			new Timestamp(-1);
			fail("The hour should be positive");
		} 
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
		try { 
			new Timestamp(13, -4);
			fail("The minutes should be positive");
		} 
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
	}
	
	@Test
	public void test_failOnSetNegativeValues() {
		try { 
			new Timestamp(12).setHour(-3);
			fail("The hour should be positive");
		} 
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
		
		try { 
			new Timestamp(22, 10).setMinutes(-21);
			fail("The minutes should be positive");
		}
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
	}
	
	@Test
	public void test_failOnConstructWithHoursOrMinutesOutOfRange() {
		try { 
			new Timestamp(25);
			fail("The hour should be less or equal than 23");
		} 
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
		try { 
			new Timestamp(13, 67);
			fail("The minutes should be less or equal than 59");
		} 
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
	}
	
	@Test
	public void test_failOnSetHoursOrMinutesOutOfRange() {
		try { 
			new Timestamp(9).setHour(32);
			fail("The hour should be less or equal than 23");
		} 
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
		
		try { 
			new Timestamp(6, 25).setMinutes(60);
			fail("The minutes should be less or equal than 59");
		}
		catch (TimestampException e) {}		// correct exception
		catch (Exception e) {
			fail("Timestamp has thrown a wrong exception");
		}
	}
	
	@Test
	public void test_equalsOnEqualTimestamps() throws Exception {
		Timestamp t1 = new Timestamp(22, 15);
		Timestamp t2 = new Timestamp(17, 0);
		
		assertTrue("equals() failed", t1.equals(t_22_15));
		assertTrue("equals() failed", t2.equals(t_17_00));	
	}
	
	@Test
	public void test_equalsOnDifferentTimestamps() throws Exception {
		Timestamp t1 = new Timestamp(23, 35);				
		Timestamp t2 = new Timestamp(9, 05);
		
		assertFalse("23:35 != 15:35", t1.equals(t_15_35));
		assertFalse("9:05 != 9:50", t2.equals(t_9_50));
	}
	
	@Test
	public void test_lessThanWhenTheConditionIsSatisfied() throws Exception {
		Timestamp t1 = new Timestamp(6, 45);
		Timestamp t2 = new Timestamp(15, 8);
		
		assertTrue("6:45 must be less than 9:50", t1.lessThan(t_9_50));
		assertTrue("15:08 must be less than 15:35", t2.lessThan(t_15_35));
	}
	
	@Test
	public void test_lessThanWhenTheConditionIsntSatisfied() throws Exception {
		Timestamp t1 = new Timestamp(10, 06);
		Timestamp t2 = new Timestamp(15, 44);
		
		assertFalse("10:06 should not be less than 9:50", t1.lessThan(t_9_50));
		assertFalse("15:44 should not be less than 15:35", t2.lessThan(t_15_35));
	}
	
	@Test
	public void test_greaterThanWhenTheConditionIsSatisfied() throws Exception {
		Timestamp t1 = new Timestamp(18, 40);
		Timestamp t2 = new Timestamp(22, 16);
		
		assertTrue("18:40 must be greater than 17:00", t1.greaterThan(t_17_00));
		assertTrue("22:16 must be greater than 22:15", t2.greaterThan(t_22_15));
	}
	
	@Test
	public void test_greaterThanWhenTheConditionIsntSatisfied() throws Exception {
		Timestamp t1 = new Timestamp(7, 59);
		Timestamp t2 = new Timestamp(15, 34);
		
		assertFalse("7:59 should not be greater than 9:50", t1.greaterThan(t_9_50));
		assertFalse("15:34 should not be greater than 15:35", t2.greaterThan(t_15_35));
	}
	
	@Test
	public void test_greaterEqual() throws Exception {
		Timestamp t1 = new Timestamp(9, 50);
		Timestamp t2 = new Timestamp(10);
		Timestamp t3 = new Timestamp(9, 49);
		
		assertTrue("9:50 must be >= than 9:50", t1.greaterEqual(t_9_50));
		assertTrue("10 must be >= than 9:50", t2.greaterEqual(t_9_50));
		assertFalse("9:49 should not be >= than 9:50", t3.greaterEqual(t_9_50));
	}
	
	@Test
	public void test_lessEqual() throws Exception {
		Timestamp t1 = new Timestamp(9, 49);
		Timestamp t2 = new Timestamp(9);
		Timestamp t3 = new Timestamp(9, 51);
		
		assertTrue("9:49 must be <= than 9:50", t1.lessEqual(t_9_50));
		assertTrue("9 must be <= than 9:50", t2.lessEqual(t_9_50));
		assertFalse("9:51 should not be <= than 9:50", t3.lessEqual(t_9_50));
	}
	
	@Test
	public void test_add() throws Exception {
		Timestamp t1exp = new Timestamp(9, 55); // sumo 5 a t_9_50
		Timestamp t2exp = new Timestamp(10, 15);// sumo 25 a t_9_50
		Timestamp t3exp = new Timestamp(11, 15);// sumo 85 a t_9_50
		
		assertEquals(t1exp, t_9_50.add(5));
		assertEquals(t2exp, t_9_50.add(25));
		assertEquals(t3exp, t_9_50.add(85));
	}
	
	@Test
	public void test_substract() throws Exception {
		Timestamp t1exp = new Timestamp(22, 10); // resto 5 a t_22_15
		Timestamp t2exp = new Timestamp(21, 50);// resto 25 a t_22_15
		Timestamp t3exp = new Timestamp(20, 50);// resto 85 a t_22_15
		Timestamp t4exp = new Timestamp(20);// resto 135 a t_22_15
		
		assertEquals(t1exp, t_22_15.substract(5));
		assertEquals(t2exp, t_22_15.substract(25));
		assertEquals(t3exp, t_22_15.substract(85));
		assertEquals(t4exp, t_22_15.substract(135));
	}
	
	@Test
	public void test_minutesBetween() throws Exception {
		assertEquals(t_9_50.minutesBetween(t_15_35), 345);
		assertEquals(t_15_35.minutesBetween(t_17_00), 85);
		assertEquals(t_17_00.minutesBetween(t_22_15), 315);
		assertEquals(t_22_15.minutesBetween(t_9_50), -745);
		assertEquals(t_9_50.minutesBetween(t_9_50), 0);
	}
}
