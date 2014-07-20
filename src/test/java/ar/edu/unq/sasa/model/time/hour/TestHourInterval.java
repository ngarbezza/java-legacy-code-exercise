package ar.edu.unq.sasa.model.time.hour;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestHourInterval {

	private HourInterval hInterval;

	@Before
	public void setUp() throws Exception {
		this.hInterval = new HourInterval(new Timestamp(17, 30), new Timestamp(19));
	}

	@Test
	public void test_constructor() throws Exception {
		Timestamp t1 = new Timestamp(1, 20);
		Timestamp t2 = new Timestamp(5, 30);
		HourInterval hi = new HourInterval(t1, t2);

		assertSame(hi.getStart(), t1);
		assertSame(hi.getEnd(), t2);
	}

	@Test
	public void test_containsTimestampWhenTheConditionIsSatisfied() throws Exception {
		Timestamp t1 = new Timestamp(17, 30);
		Timestamp t2 = new Timestamp(19);
		Timestamp t3 = new Timestamp(18, 22);

		assertTrue("The interval 17:30 h ~ 19 h must contain the timestamp 17:30 h",
				hInterval.contains(t1));
		assertTrue("The interval 17:30 h ~ 19 h must contain the timestamp 19 h",
				hInterval.contains(t2));
		assertTrue("The interval 17:30 h ~ 19 h must contain the timestamp 18:22 h",
				hInterval.contains(t3));
	}

	@Test
	public void test_containsTimestampWhenTheConditionIsntSatisfied() throws Exception {
		Timestamp t1 = new Timestamp(17);
		Timestamp t2 = new Timestamp(22);

		assertFalse("The interval 17:30 h ~ 19 h should not contain the timestamp 17 h",
				hInterval.contains(t1));
		assertFalse("The interval 17:30 h ~ 19 h should not contain the timestamp 22 h",
				hInterval.contains(t2));
	}

	@Test
	public void test_containsLogicalHourFulfiller() {
		// sólo se testea por envío de mensaje, pues este método
		// se resuelve por double-dispatching.
		LogicalHourFulfiller mock = createMock(LogicalHourFulfiller.class);
		expect(mock.isIn(this.hInterval)).andReturn(false);
		replay(mock);
		assertFalse(this.hInterval.contains(mock));
		verify(mock);
	}

	@Test
	public void test_isInWhenTheConditionIsSatisfied() throws Exception {
		HourInterval hi1 = new HourInterval(new Timestamp(9), new Timestamp(22));
		HourInterval hi2 = new HourInterval(new Timestamp(17, 30), new Timestamp(23));
		HourInterval hi3 = new HourInterval(new Timestamp(16, 30), new Timestamp(19));
		
		assertTrue("The interval 17:30 ~ 19 must be in 9 ~ 22", 
				hInterval.isIn(hi1));
		assertTrue("The interval 17:30 ~ 19 must be in 17:30 ~ 23",
				hInterval.isIn(hi2));
		assertTrue("The interval 17:30 ~ 19 must be in 16:30 ~ 19",
				hInterval.isIn(hi3));
	}

	@Test
	public void test_isInWhenTheConditionIsntSatisfied() throws Exception {
		HourInterval hi1 = new HourInterval(new Timestamp(9), new Timestamp(12));
		HourInterval hi2 = new HourInterval(new Timestamp(19, 30), new Timestamp(23));
		HourInterval hi3 = new HourInterval(new Timestamp(16), new Timestamp(18));
		HourInterval hi4 = new HourInterval(new Timestamp(18, 30), new Timestamp(21, 30));

		assertFalse("The interval 17:30 ~ 19 should not be in 9 ~ 12",
				hInterval.isIn(hi1));
		assertFalse("The interval 17:30 ~ 19 should not be in 19:30 ~ 23", 
				hInterval.isIn(hi2));
		assertFalse("The interval 17:30 ~ 19 should not be in 16 ~ 18", 
				hInterval.isIn(hi3));
		assertFalse("The interval 17:30 ~ 19 should not be in 18:30 ~ 21:30", 
				hInterval.isIn(hi4));
	}

	@Test
	public void test_intersectsWithWhenTheConditionIsSatisfied() throws Exception {
		// lo contiene, por lo tanto lo intersecta
		HourInterval hi1 = new HourInterval(new Timestamp(18), new Timestamp(18, 30));
		// es contenido, por lo tanto hay intersección también
		HourInterval hi2 = new HourInterval(new Timestamp(16), new Timestamp(22));
		// intervalos contenidos en parte
		HourInterval hi3 = new HourInterval(new Timestamp(11), new Timestamp(18));
		HourInterval hi4 = new HourInterval(new Timestamp(18, 30), new Timestamp(21));

		assertTrue(hInterval.intersectsWith(hi1));
		assertTrue(hInterval.intersectsWith(hi2));
		assertTrue(hInterval.intersectsWith(hi3));
		assertTrue(hInterval.intersectsWith(hi4));
	}

	@Test
	public void test_intersectsWithWhenTheConditionIsntSatisfied() throws Exception {
		// intervalos totalmente disjuntos
		HourInterval hi1 = new HourInterval(new Timestamp(8), new Timestamp(12));
		HourInterval hi2 = new HourInterval(new Timestamp(19, 30), new Timestamp(22));
		// cuando los extremos se cruzan no hay intersección
		HourInterval hi3 = new HourInterval(new Timestamp(16, 30), new Timestamp(17, 30));
		HourInterval hi4 = new HourInterval(new Timestamp(19), new Timestamp(20, 30));

		assertFalse(hInterval.intersectsWith(hi1));
		assertFalse(hInterval.intersectsWith(hi2));
		assertFalse(hInterval.intersectsWith(hi3));
		assertFalse(hInterval.intersectsWith(hi4));
	}
	
	@Test
	public void test_getConcreteIntervals() throws Exception {
		HourInterval hi1 = new HourInterval(new Timestamp(12), new Timestamp(16, 30), 120);
		HourInterval hi2 = new HourInterval(new Timestamp(12), new Timestamp(16, 30), 60);
		HourInterval hi3 = new HourInterval(new Timestamp(12), new Timestamp(16, 30), 150);
		HourInterval hi4 = new HourInterval(new Timestamp(12), new Timestamp(16, 30), 270);
				
		assertEquals(hi1.getConcreteIntervals().size(), 6);
		assertEquals(hi2.getConcreteIntervals().size(), 8);
		assertEquals(hi3.getConcreteIntervals().size(), 5);
		assertEquals(hi4.getConcreteIntervals().size(), 1);
	}
}
