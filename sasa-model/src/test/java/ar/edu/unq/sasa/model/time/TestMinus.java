package ar.edu.unq.sasa.model.time;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class TestMinus {

	private Minus minusUnderTest;
	private Period mockLeftOp, mockRightOp;

	@Before
	public void setUp() {
		mockLeftOp = createMock(Period.class);
		mockRightOp = createMock(Period.class);
		minusUnderTest = new Minus(mockLeftOp, mockRightOp);
	}

	@Test
	public void containsCalendar() {
		Calendar calendarMock = createMock(Calendar.class);
		expect(mockLeftOp.contains(calendarMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.contains(calendarMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(minusUnderTest.contains(calendarMock));
		assertFalse(minusUnderTest.contains(calendarMock));
		assertTrue(minusUnderTest.contains(calendarMock));
		assertFalse(minusUnderTest.contains(calendarMock));
		verify(mockLeftOp);
		// no puedo verificar el right porque a veces no se evalua,
		// ya que el Or que se asume Short-circuit
	}

	@Test
	public void containsLogicalDateFulfiller() {
		Period ldfMock = createMock(Period.class);
		expect(mockLeftOp.contains(ldfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.contains(ldfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(minusUnderTest.contains(ldfMock));
		assertFalse(minusUnderTest.contains(ldfMock));
		assertTrue(minusUnderTest.contains(ldfMock));
		assertFalse(minusUnderTest.contains(ldfMock));
		verify(mockLeftOp);
	}

	@Test
	public void isIn() {
		SimplePeriod sdfMock = createMock(SimplePeriod.class);
		expect(mockLeftOp.isIn(sdfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.isIn(sdfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(minusUnderTest.isIn(sdfMock));
		assertFalse(minusUnderTest.isIn(sdfMock));
		assertTrue(minusUnderTest.isIn(sdfMock));
		assertFalse(minusUnderTest.isIn(sdfMock));
		verify(mockLeftOp);
	}

	@Test
	public void intersectsWith() {
		Period ldfMock = createMock(Period.class);
		expect(mockLeftOp.intersectsWith(ldfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.intersectsWith(ldfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(minusUnderTest.intersectsWith(ldfMock));
		assertFalse(minusUnderTest.intersectsWith(ldfMock));
		assertTrue(minusUnderTest.intersectsWith(ldfMock));
		assertFalse(minusUnderTest.intersectsWith(ldfMock));
		verify(mockLeftOp);
	}
}