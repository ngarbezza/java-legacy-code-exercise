package ar.edu.unq.sasa.model.time.hour;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestOr {

	private Or orUnderTest;
	private LogicalHourFulfiller mockLeftOp, mockRightOp;

	@Before
	public void setUp() {
		mockLeftOp = new HourInterval(new Timestamp(12), new Timestamp(13, 30));
		mockRightOp = new HourInterval(new Timestamp(13), new Timestamp(17, 15));
		orUnderTest = new Or(mockLeftOp, mockRightOp);
	}

	@Test
	public void containsTimestamp() {
		assertTrue(orUnderTest.contains(new Timestamp(13, 15)));
		assertTrue(orUnderTest.contains(new Timestamp(16)));
		assertTrue(orUnderTest.contains(new Timestamp(12)));
		// TODO split in another test
		assertFalse(orUnderTest.contains(new Timestamp(11, 30)));
		assertFalse(orUnderTest.contains(new Timestamp(18)));
	}

	@Test
	public void containsLogicalHourFulfiller() {
		assertTrue(orUnderTest.contains(new HourInterval(new Timestamp(12), new Timestamp(12, 15))));
		assertTrue(orUnderTest.contains(new HourInterval(new Timestamp(13, 15), new Timestamp(13, 30))));
		assertTrue(orUnderTest.contains(new HourInterval(new Timestamp(16), new Timestamp(17))));
		// TODO split in another test
		assertFalse(orUnderTest.contains(new HourInterval(new Timestamp(9), new Timestamp(10, 15))));
		assertFalse(orUnderTest.contains(new HourInterval(new Timestamp(18), new Timestamp(22))));
	}

	@Test
	public void isIn() {
		assertTrue(orUnderTest.isIn(new HourInterval(new Timestamp(11), new Timestamp(18))));
		// TODO split in another test(s)
		assertFalse(orUnderTest.isIn(new HourInterval(new Timestamp(12, 30), new Timestamp(16))));
		assertFalse(orUnderTest.isIn(new HourInterval(new Timestamp(10, 15), new Timestamp(13))));
		assertFalse(orUnderTest.isIn(new HourInterval(new Timestamp(16), new Timestamp(18))));
	}

	@Test
	public void intersectsWith() {
		assertTrue(orUnderTest.intersectsWith(new HourInterval(new Timestamp(11), new Timestamp(18))));
		// TODO split in another test(s)
		assertTrue(orUnderTest.intersectsWith(new HourInterval(new Timestamp(12, 30), new Timestamp(16))));
		assertTrue(orUnderTest.intersectsWith(new HourInterval(new Timestamp(10, 15), new Timestamp(13))));
		assertTrue(orUnderTest.intersectsWith(new HourInterval(new Timestamp(16), new Timestamp(18))));
		// TODO split in another test(s)
		assertFalse(orUnderTest.intersectsWith(new HourInterval(new Timestamp(10), new Timestamp(11, 55))));
		assertFalse(orUnderTest.intersectsWith(new HourInterval(new Timestamp(17, 30), new Timestamp(19, 30))));
	}
}