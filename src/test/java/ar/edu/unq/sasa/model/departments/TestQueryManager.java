package ar.edu.unq.sasa.model.departments;

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
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Satisfaction;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Or;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestQueryManager {

	private QueryManager queryManager;
	private University university;

	@Before
	public void setUp() {
		university = new University();
		queryManager = new QueryManager(university);
	}

	@Test
	public void satisfactionsFromClassroomAndRequest()	{
		LogicalHourFulfiller logicHourFul1 = new HourInterval(new Timestamp(10), new Timestamp(12));

		Period desHours = new SimplePeriod(logicHourFul1, new GregorianCalendar(2010, Calendar.DECEMBER, 5));

		Subject subject = new Subject("Nombres Felices");
		Professor professor = new Professor("Pablo", "44445555", "pablo@gmail.com");
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

	// asumiendo que Period.MIN_HOUR_BLOCK vale 30 siempre
	@Test
	public void freeHoursInAnAssignableItemInADayWhenAllIsBusy() {
		AssignableItem itemMock = createMock(AssignableItem.class);
		Calendar day = new GregorianCalendar(2010, Calendar.JUNE, 9);
		for (int i = 0; i < 47; i++) {
			expect(itemMock.isFreeAt(day)).andReturn(false); // siempre ocupada
			day.add(Calendar.MINUTE, 30);
		}
		replay(itemMock);
		assertNull(queryManager.freeHoursInADay(itemMock, day));
		verify(itemMock);
	}

	// asumiendo que Period.MIN_HOUR_BLOCK vale 30 siempre
	@Test
	public void freeHoursInAnAssignableItemInADayOnlyOneIntervalFree() {
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
		SimplePeriod result = queryManager.freeHoursInADay(itemMock, day);
		assertTrue(result.getHourFulfiller() instanceof HourInterval);
		HourInterval expected = new HourInterval(new Timestamp(10, 30),	new Timestamp(17));
		assertEquals(expected, result.getHourFulfiller());
		verify(itemMock);
	}

	@Test
	public void freeHoursInAnAssignableItemInADayMoreThanOneIntervalFree() {
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
		SimplePeriod result = queryManager.freeHoursInADay(itemMock, day);
		HourInterval hiLeftExp = new HourInterval(new Timestamp(10, 30), new Timestamp(12));
		HourInterval hiRightExp = new HourInterval(new Timestamp(14, 30), new Timestamp(17));
		Or expected = new Or(hiLeftExp, hiRightExp);
		assertEquals(expected, result.getHourFulfiller());
		verify(itemMock);
	}

	@Test
	public void classroomsThatSatisfyCapacityRequirement() {
		Classroom classroom1 = new Classroom("Aula 30", 15);
		Classroom classroom2 = new Classroom("Aula 62", 22);
		Classroom classroom3 = new Classroom("Aula 15", 8);
		Classroom classroom4 = new Classroom("Aula 22", 10);
		university.getClassroomsDepartment().addClassroom(classroom1);
		university.getClassroomsDepartment().addClassroom(classroom2);
		university.getClassroomsDepartment().addClassroom(classroom3);
		university.getClassroomsDepartment().addClassroom(classroom4);
		ClassroomRequest request = new ClassroomRequest(null, null, null, 0L, null, null, 15);
		Collection<Classroom> result = queryManager.classroomsThatSatisfyCapacityRequirement(request);
		assertEquals(2, result.size());
		assertTrue(result.contains(classroom1));
		assertTrue(result.contains(classroom1));
		assertFalse(result.contains(classroom3));
		assertFalse(result.contains(classroom4));
	}
}
