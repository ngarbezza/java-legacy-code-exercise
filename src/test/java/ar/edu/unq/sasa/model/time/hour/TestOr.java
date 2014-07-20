package ar.edu.unq.sasa.model.time.hour;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestOr {

	private Or orUnderTest;
	private LogicalHourFulfiller mockLeftOp, mockRightOp;

	@Before
	public void setUp() throws Exception {
		this.mockLeftOp = createMock(LogicalHourFulfiller.class);
		this.mockRightOp= createMock(LogicalHourFulfiller.class);
		this.orUnderTest = new Or(mockLeftOp, mockRightOp);
	}

	@Test
	public void test_constructor() {
		assertSame(mockLeftOp, orUnderTest.getLeftOp());
		assertSame(mockRightOp, orUnderTest.getRightOp());
	}

	@Test
	public void test_containsTimestamp() {
		Timestamp timeSTMock= createMock(Timestamp.class);
		expect(mockLeftOp.contains(timeSTMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.contains(timeSTMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(orUnderTest.contains(timeSTMock));
		assertTrue(orUnderTest.contains(timeSTMock));
		assertTrue(orUnderTest.contains(timeSTMock));
		assertTrue(orUnderTest.contains(timeSTMock));
		verify(mockLeftOp);
		// no puedo verificar el right porque a veces no se evalua,
		// ya que el Or que se asume Short-circuit
	}

	@Test
	public void test_containsLogicalHourFulfiller() {
		LogicalHourFulfiller lhfMock = createMock(LogicalHourFulfiller.class);
		expect(mockLeftOp.contains(lhfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.contains(lhfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(orUnderTest.contains(lhfMock));
		assertTrue(orUnderTest.contains(lhfMock));
		assertTrue(orUnderTest.contains(lhfMock));
		assertTrue(orUnderTest.contains(lhfMock));
		verify(mockLeftOp);
	}

	@Test
	public void test_isIn() {
		HourInterval hiMock = createMock(HourInterval.class);
		expect(mockLeftOp.isIn(hiMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.isIn(hiMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(orUnderTest.isIn(hiMock));
		assertTrue(orUnderTest.isIn(hiMock));
		assertTrue(orUnderTest.isIn(hiMock));
		assertTrue(orUnderTest.isIn(hiMock));
		verify(mockLeftOp);
	}

	@Test
	public void test_intersectsWith() {
		LogicalHourFulfiller lhfMock = createMock(LogicalHourFulfiller.class);
		expect(mockLeftOp.intersectsWith(lhfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.intersectsWith(lhfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(orUnderTest.intersectsWith(lhfMock));
		assertTrue(orUnderTest.intersectsWith(lhfMock));
		assertTrue(orUnderTest.intersectsWith(lhfMock));
		assertTrue(orUnderTest.intersectsWith(lhfMock));
		verify(mockLeftOp);
	}
}