package ar.edu.unq.sasa.model.handlers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.assignments.Satisfaction;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Or;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

/**
 * Test Case para la clase {@link QueryManager}.
 * 
 * @author Grupo
 * 
 */
public class TestQueryManager {
	private QueryManager queryManager;

	@Before
	public void setUp() throws Exception {
		queryManager = new QueryManager();
	}

	/**
	 * Testea que se cree correctamente la satisfaccion entre un Request y un
	 * Classroom.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @throws RequestException
	 * @throws PeriodException
	 * @throws TimestampException
	 */
	@Test
	public void test_satisfactionsFromClassroomAndRequest()	throws RequestException, PeriodException, TimestampException {
		Timestamp timestamp1 = new Timestamp(10);
		Timestamp timestamp11 = new Timestamp(12);
		LogicalHourFulfiller logicHourFul1 = new HourInterval(timestamp1,
				timestamp11);

		Period desHours = new SimplePeriod(logicHourFul1, new GregorianCalendar(2010, Calendar.DECEMBER, 5));

		Subject subject = new Subject("Nombres Felices", 0);
		Professor professor = new Professor("Pablo", 0, "44445555",
				"pablo@gmail.com");
		Map<Resource, Integer> reqResources = new HashMap<Resource, Integer>();
		Map<Resource, Integer> optResources = new HashMap<Resource, Integer>();

		Classroom classroom = new Classroom("Aula 1", 30);
		ClassroomRequest classroomRequest = new ClassroomRequest(desHours,
				subject, professor, 0, reqResources, optResources, 30);

		Map<Resource, Integer> resources1 = new HashMap<Resource, Integer>();
		Map<Period, Float> timeDifference1 = new HashMap<Period, Float>();
		int capacityDifference1 = classroom.getCapacity()
				- classroomRequest.getCapacity();

		Satisfaction satisfaction1 = new Satisfaction(resources1, timeDifference1, capacityDifference1);
		Satisfaction satisfaction2 = queryManager.satisfactionsFromClassroomAndRequest(classroomRequest, classroom);
		assertEquals(satisfaction1, satisfaction2);
	}

	// por Nahuel
	// asumiendo que Period.MIN_HOUR_BLOCK vale 30 siempre
	@Test
	public void test_freeHoursInAnAssignableItemInADayWhenAllIsBusy()
			throws Exception {
		QueryManager qm = new QueryManager();
		AssignableItem itemMock = createMock(AssignableItem.class);
		Calendar day = new GregorianCalendar(2010, Calendar.JUNE, 9);
		for (int i = 0; i < 47; i++) {
			expect(itemMock.isFreeAt(day)).andReturn(false); // siempre ocupada
			day.add(Calendar.MINUTE, 30);
		}
		replay(itemMock);
		assertNull(qm.freeHoursInADay(itemMock, day));
		verify(itemMock);
	}

	// por Nahuel
	// asumiendo que Period.MIN_HOUR_BLOCK vale 30 siempre
	@Test
	public void test_freeHoursInAnAssignableItemInADayOnlyOneIntervalFree()
			throws Exception {
		QueryManager qm = new QueryManager();
		AssignableItem itemMock = createMock(AssignableItem.class);
		Calendar day = new GregorianCalendar(2010, Calendar.JUNE, 9);
		for (int i = 0; i < 22; i++) {		// ocupada hasta las 10:30 hs
			expect(itemMock.isFreeAt(day)).andReturn(false);
			day.add(Calendar.MINUTE, 30);
		}
		for (int i = 22; i < 34; i++) { 	// libre de 10:30 a 17 hs
			expect(itemMock.isFreeAt(day)).andReturn(true);
			day.add(Calendar.MINUTE, 30);
		}
		for (int i = 34; i < 47; i++) { 	// ocupada desde las 17 hs
			expect(itemMock.isFreeAt(day)).andReturn(false);
			day.add(Calendar.MINUTE, 30);
		}
		replay(itemMock);
		SimplePeriod result = qm.freeHoursInADay(itemMock, day);
		assertTrue(result.getHourFulfiller() instanceof HourInterval);
		HourInterval expected = new HourInterval(new Timestamp(10, 30),
				new Timestamp(17));
		assertEquals(expected, result.getHourFulfiller());
		verify(itemMock);
	}

	// por Nahuel
	@Test
	public void test_freeHoursInAnAssignableItemInADayMoreThanOneIntervalFree()
			throws Exception {
		QueryManager qm = new QueryManager();
		AssignableItem itemMock = createMock(AssignableItem.class);
		Calendar day = new GregorianCalendar(2010, Calendar.JUNE, 9);
		for (int i = 0; i < 22; i++) {	// ocupada hasta las 10:30 hs
			expect(itemMock.isFreeAt(day)).andReturn(false);
			day.add(Calendar.MINUTE, 30);
		}
		for (int i = 22; i < 24; i++) {	// libre de 10:30 a 12 hs
			expect(itemMock.isFreeAt(day)).andReturn(true);
			day.add(Calendar.MINUTE, 30);
		}
		for (int i = 24; i < 30; i++) {	// ocupada de 12 a 14:30 hs
			expect(itemMock.isFreeAt(day)).andReturn(false);
			day.add(Calendar.MINUTE, 30);
		}
		for (int i = 30; i < 34; i++) {	// libre de 14:30 a 17 hs
			expect(itemMock.isFreeAt(day)).andReturn(true);
			day.add(Calendar.MINUTE, 30);
		}		
		for (int i = 34; i < 47; i++) {	// ocupada desde las 17 hs
			expect(itemMock.isFreeAt(day)).andReturn(false);
			day.add(Calendar.MINUTE, 30);
		}
		replay(itemMock);
		SimplePeriod result = qm.freeHoursInADay(itemMock, day);
		assertTrue(result.getHourFulfiller() instanceof Or);
		HourInterval hiLeftExp = new HourInterval(new Timestamp(10, 30),
				new Timestamp(12));
		HourInterval hiRightExp = new HourInterval(new Timestamp(14, 30),
				new Timestamp(17));
		Or expected = new Or(hiLeftExp, hiRightExp);	
		assertEquals(expected, result.getHourFulfiller());
		verify(itemMock);
	}
	
	// por Nahuel
	@Test
	public void test_classroomsThatSatisfyCapacityRequirement() throws Exception {
		Classroom c1Mock = createMock(Classroom.class);
		Classroom c2Mock = createMock(Classroom.class);
		Classroom c3Mock = createMock(Classroom.class);
		Classroom c4Mock = createMock(Classroom.class);
		expect(c1Mock.getCapacity()).andReturn(15);
		expect(c2Mock.getCapacity()).andReturn(22);
		expect(c3Mock.getCapacity()).andReturn(8);
		expect(c4Mock.getCapacity()).andReturn(10);
		replay(c1Mock, c2Mock, c3Mock, c4Mock);
		InformationManager im = InformationManager.getInstance();
		im.addClassroom(c1Mock);
		im.addClassroom(c2Mock);
		im.addClassroom(c3Mock);
		im.addClassroom(c4Mock);
		QueryManager qm = new QueryManager();
		ClassroomRequest req = createMock(ClassroomRequest.class);
		expect(req.getCapacity()).andReturn(15).times(4);
		replay(req);
		Collection<Classroom> result = qm.classroomsThatSatisfyCapacityRequirement(req);
		assertEquals("2 aulas deben cumplir la condición", 2, result.size());
		assertTrue(result.contains(c1Mock));
		assertTrue(result.contains(c1Mock));
		assertFalse(result.contains(c3Mock));
		assertFalse(result.contains(c4Mock));
		verify(c1Mock, c2Mock, c3Mock, c4Mock, req);
	}
}
