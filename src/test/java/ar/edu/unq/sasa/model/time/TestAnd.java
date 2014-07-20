package ar.edu.unq.sasa.model.time;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

/**
 * Test Case para la clase {@link And}.
 * 
 * @author Nahuel
 *
 */
public class TestAnd {
	
	private And andUnderTest;
	private Period mockLeftOp, mockRightOp;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		this.mockLeftOp = createMock(Period.class);
		this.mockRightOp= createMock(Period.class);
		this.andUnderTest = new And(mockLeftOp, mockRightOp);
	}
	
	/**
	 * Test method for {@link And#And(LogicalDateFulfiller, LogicalDateFulfiller)}.
	 */
	@Test
	public void test_constructor() {
		assertSame(mockLeftOp, andUnderTest.getLeftPeriod());
		assertSame(mockRightOp, andUnderTest.getRightPeriod());
	}
	
	/**
	 * Test method for {@link And#contains(Calendar)}.
	 * @throws Exception 
	 */
	@Test
	public void test_containsCalendar() throws Exception {
		Calendar calendarMock= createMock(Calendar.class);
		expect(mockLeftOp.contains(calendarMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.contains(calendarMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(andUnderTest.contains(calendarMock));
		assertFalse(andUnderTest.contains(calendarMock));
		assertFalse(andUnderTest.contains(calendarMock));
		assertTrue(andUnderTest.contains(calendarMock));
		verify(mockLeftOp); 
		// no puedo verificar el right porque a veces no se evalua,
		// ya que el Or que se asume Short-circuit
	}
	
	/**
	 * Test method for {@link And#contains(LogicalDateFulfiller)}.
	 * @throws Exception 
	 */
	@Test
	public void test_containsLogicalDateFulfiller() throws Exception {
		Period ldfMock = createMock(Period.class);
		expect(mockLeftOp.contains(ldfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.contains(ldfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(andUnderTest.contains(ldfMock));
		assertFalse(andUnderTest.contains(ldfMock));
		assertFalse(andUnderTest.contains(ldfMock));
		assertTrue(andUnderTest.contains(ldfMock));
		verify(mockLeftOp);
	}
	
	/**
	 * Test method for {@link And#isIn(SimpleDateFulfiller)}.
	 * @throws Exception 
	 */
	@Test
	public void test_isIn() throws Exception {
		SimplePeriod sdfMock = createMock(SimplePeriod.class);
		expect(mockLeftOp.isIn(sdfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.isIn(sdfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(andUnderTest.isIn(sdfMock));
		assertFalse(andUnderTest.isIn(sdfMock));
		assertFalse(andUnderTest.isIn(sdfMock));
		assertTrue(andUnderTest.isIn(sdfMock));
		verify(mockLeftOp);
	}
	
	/**
	 * Test method for {@link And#intersectsWith(LogicalDateFulfiller)}.
	 * @throws Exception 
	 */
	@Test
	public void test_intersectsWith() throws Exception {
		Period ldfMock = createMock(Period.class);
		expect(mockLeftOp.intersectsWith(ldfMock))
			.andReturn(false).andReturn(false)
			.andReturn(true).andReturn(true);
		expect(mockRightOp.intersectsWith(ldfMock))
			.andReturn(false).andReturn(true)
			.andReturn(false).andReturn(true);
		replay(mockLeftOp, mockRightOp);
		assertFalse(andUnderTest.intersectsWith(ldfMock));
		assertFalse(andUnderTest.intersectsWith(ldfMock));
		assertFalse(andUnderTest.intersectsWith(ldfMock));
		assertTrue(andUnderTest.intersectsWith(ldfMock));
		verify(mockLeftOp);
	}
}