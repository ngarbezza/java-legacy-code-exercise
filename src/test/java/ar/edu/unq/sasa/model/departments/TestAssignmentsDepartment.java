package ar.edu.unq.sasa.model.departments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.MobileResourcesRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestAssignmentsDepartment {

	private University university;
	private AssignmentsDepartment assignmentsDepartment;
	private MobileResourcesRequest mobileResourcesRequest;
	private ClassroomRequest classroomRequest;
	private Period period1;
	private Period period2;
	private MobileResource mobileResource1;
	private Classroom classroom1;
	private Classroom classroom2;

	@Before
	public void setUp() {
		university = new University();
		assignmentsDepartment = new AssignmentsDepartment(university);

		// Period 1
		Timestamp timestamp1 = new Timestamp(10);
		Timestamp timestamp11 = new Timestamp(12);
		LogicalHourFulfiller logicHourFul1 = new HourInterval(timestamp1, timestamp11);
		period1 = new SimplePeriod(logicHourFul1, new GregorianCalendar(2010, Calendar.DECEMBER, 5));

		// Period 2
		Timestamp timestamp2 = new Timestamp(15);
		Timestamp timestamp22 = new Timestamp(18);
		LogicalHourFulfiller logicHourFul2 = new HourInterval(timestamp2, timestamp22);
		period2 = new SimplePeriod(logicHourFul2, new GregorianCalendar(2010, Calendar.DECEMBER, 5));

		// MobileResource 1
		mobileResource1 = new MobileResource("Proyector", 0);

		// Classroom 1
		classroom1 = new Classroom("Aula 1", 30);
		university.getClassroomsDepartment().addClassroom(classroom1);

		// Classroom 2
		classroom2 = new Classroom("Aula 2", 29);
		university.getClassroomsDepartment().addClassroom(classroom2);

		// MobileResourcesRequest
		Period desHours = period1;
		Subject subject = new Subject("Nombres Felices", 0);
		Professor professor = new Professor("Pablo", 0, "44445555",
				"pablo@gmail.com");
		long anID = 0;
		Map<Resource, Integer> reqResources = new HashMap<Resource, Integer>();
		Map<Resource, Integer> optResources = new HashMap<Resource, Integer>();
		mobileResourcesRequest = new MobileResourcesRequest(desHours, subject,
				professor, anID, reqResources, optResources);

		// ClassroomRequest
		int capacity = 20;
		Period desHours2 = period2;
		Map<Resource, Integer> reqResources2 = new HashMap<Resource, Integer>();
		Map<Resource, Integer> optResources2 = new HashMap<Resource, Integer>();
		classroomRequest = new ClassroomRequest(desHours2, subject, professor, anID, reqResources2, optResources2, capacity);
	}

	@Test
	public void test_asignateResourceAssignment() {
		ResourceAssignment resourceAssignment1 = assignmentsDepartment.asignateResourceAssignment(mobileResourcesRequest, mobileResource1, period1);
		ResourceAssignment resourceAssignment2 = new ResourceAssignment(mobileResourcesRequest, mobileResource1);

		assertEquals(resourceAssignment1, resourceAssignment2);
		assertSame(university.getAssignmentByRequest(mobileResourcesRequest), resourceAssignment1);
	}

	@Test
	public void test_asignateBookedAssignment() {
		BookedAssignment bookedAssignment1 = assignmentsDepartment.asignateBookedAssignment(classroom1, "Limpieza", period1);
		BookedAssignment bookedAssignment2 = new BookedAssignment("Limpieza", classroom1);

		BookedAssignment storedBookeadAssignment = null;
		for (Assignment assignment : university.getAssignments())
			if (assignment.equals(bookedAssignment1)) {
				storedBookeadAssignment = (BookedAssignment) assignment;
				break;
			}

		assertEquals(bookedAssignment1, bookedAssignment2);
		assertSame(storedBookeadAssignment, bookedAssignment1);
	}

	@Test
	public void test_asignateClassroomAssignment() {
		ClassroomAssignment classroomAssignment1 = assignmentsDepartment.asignateClassroomAssignment(classroomRequest, classroom1, period1);
		List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<ResourceAssignment>();
		ClassroomAssignment classroomAssignment2 = new ClassroomAssignment(classroomRequest, classroom1, resourcesAssignmentsList);
		classroom1.addAssignment(period2, classroomAssignment2);
		classroomAssignment2.createSatisfaction();

		ClassroomAssignment storedBookeadAssignment = null;
		for (Assignment assignment : university.getAssignments())
			if (assignment.equals(classroomAssignment1))
				storedBookeadAssignment = (ClassroomAssignment) assignment;

		assertEquals(classroomAssignment1, classroomAssignment2);
		assertSame(storedBookeadAssignment, classroomAssignment1);
	}

	@Test
	public void test_asignateRequestInAClassroom() {
		ClassroomAssignment classroomAssignment1 = assignmentsDepartment.asignateRequestInAClassroom(classroomRequest, classroom1);
		ClassroomAssignment classroomAssignment2 = assignmentsDepartment.asignateClassroomAssignment(classroomRequest, classroom1, period2);

		assertEquals(classroomAssignment1, classroomAssignment2);
	}

	@Test
	public void test_asignateRequestInMostSatisfactoryClassroom() {
		university.addRequest(classroomRequest);
		ClassroomAssignment asig = assignmentsDepartment.asignateRequestInMostSatisfactoryClassroom(classroomRequest);
		assertEquals(classroomRequest, asig.getRequest());
		assertTrue(university.getAssignments().contains(asig));
	}

	@Test
	public void test_modifyBookedAssignmentCause() {
		BookedAssignment bookedAssignment = assignmentsDepartment.asignateBookedAssignment(classroom1, "Restauracion", period1);
		String newCause = "Robado";
		assignmentsDepartment.modifyBookedAssignmentCause(bookedAssignment, newCause);

		assertEquals(bookedAssignment.getCause(), newCause);
	}

	@Test
	public void test_deleteResourceAssignmentFromARequest() {
		ResourceAssignment resourceAssignment = assignmentsDepartment.asignateResourceAssignment(mobileResourcesRequest, mobileResource1, period1);
		assignmentsDepartment.deleteResourceAssignment(resourceAssignment);
		Map<Period, Assignment> map = new HashMap<Period, Assignment>();

		assertEquals(resourceAssignment.getAssignableItem().getAssignments(), map);

		boolean exists = false;
		for (Assignment assignment : university.getAssignments())
			if (assignment.equals(resourceAssignment))
				exists = true;

		assertFalse(exists);
	}

	@Test
	public void test_deleteClassroomAssignment() {
		ClassroomAssignment classroomAssignment = assignmentsDepartment.asignateClassroomAssignment(classroomRequest, classroom1, period1);
		assignmentsDepartment.deleteClassroomAssignmentFromARequest(classroomRequest);
		Map<Period, Assignment> map = new HashMap<Period, Assignment>();

		assertEquals(classroomAssignment.getAssignableItem().getAssignments(), map);

		boolean exists = false;
		for (Assignment assignment : university.getAssignments())
			if (assignment.equals(classroomAssignment))
				exists = true;

		assertFalse(exists);
	}

	@Test
	public void test_deleteBookedAssignment() {
		BookedAssignment bookedAssignment = assignmentsDepartment.asignateBookedAssignment(classroom1, "Reparacion", period1);
		assignmentsDepartment.deleteAssignment(bookedAssignment);
		Map<Period, Assignment> map = new HashMap<Period, Assignment>();

		assertEquals(bookedAssignment.getAssignableItem().getAssignments(), map);

		boolean exists = false;
		for (Assignment assignment : university.getAssignments())
			if (assignment.equals(bookedAssignment))
				exists = true;

		assertFalse(exists);
	}

	@Test
	public void test_searchForAssignment() {
		ResourceAssignment resourceAssignment1 = assignmentsDepartment.asignateResourceAssignment(mobileResourcesRequest, mobileResource1, period1);
		ResourceAssignment resourceAssignment2 = (ResourceAssignment) assignmentsDepartment.searchForAssignment(mobileResource1, period1);

		assertSame(resourceAssignment1, resourceAssignment2);
	}

	@Test
	public void testMoveAssignmentClassroom() {
		// crear requisitos para los pedidos obligatorios
		Map<Resource, Integer> requiredResources = new HashMap<Resource, Integer>();
		FixedResource resource = new FixedResource("Computadora");
		requiredResources.put(resource, 20);
		// opcionales
		Map<Resource, Integer> optionalResources = new HashMap<Resource, Integer>();
		FixedResource resource2 = new FixedResource("Luz Natural");
		optionalResources.put(resource2, 1);
		// fecha y hora
		Period desiredHours6 = period1;
		// profesor y materia
		Subject subject6 = new Subject("Ingles 1", 000);
		Professor profesorErnesto = new Professor("Ernesto", 0, "42165035", "a@zaza.com");

		// crear pedido
		ClassroomRequest pedido6 = new ClassroomRequest(desiredHours6,
				subject6, profesorErnesto, 0, requiredResources,
				optionalResources, 20);
		Classroom oldClassroom = new Classroom("Aula 3", 20);

		// crear asignacion y aula para asignar
		ClassroomAssignment classroomAssignment = assignmentsDepartment.asignateRequestInAClassroom(pedido6, oldClassroom);
		Classroom newClassroom = new Classroom("Aula 4", 20);
		ClassroomAssignment newAssignment = assignmentsDepartment.moveAssignmentOfClassroom(classroomAssignment, newClassroom);

		assertEquals(newClassroom, newAssignment.getAssignableItem());
	}

	@Test
	public void testMoveAssignmentHour() {
		// crear requisitos para los pedidos obligatorios
		Map<Resource, Integer> requiredResources = new HashMap<Resource, Integer>();
		FixedResource resource = new FixedResource("Computadora", 20);
		requiredResources.put(resource, 20);
		// opcionales
		Map<Resource, Integer> optionalResources = new HashMap<Resource, Integer>();
		FixedResource resource2 = new FixedResource("Luz Natural", 1);
		optionalResources.put(resource2, 1);
		// fecha y hora
		Period desiredHours = period1;
		// profesor y materia
		Subject subject = new Subject("Ingles 1", 000);
		Professor profesorErnesto = new Professor("Ernesto", 0, "42165035", "a@zaza.com");

		// crear pedido
		ClassroomRequest pedido = new ClassroomRequest(desiredHours, subject,
				profesorErnesto, 52, requiredResources, optionalResources, 20);
		Classroom oldClassroom = new Classroom("Aula 3", 20);
		ClassroomAssignment assignment = assignmentsDepartment.asignateRequestInAClassroom(pedido, oldClassroom);

		// nueva hora
		Timestamp start = new Timestamp(9);
		Timestamp end = new Timestamp(11, 30);
		LogicalHourFulfiller hour = new HourInterval(start, end);

		assignmentsDepartment.moveAssignmentOfHour(assignment, hour);

		Period assignmentPeriod = null;
		for (Entry<Period, Assignment> currentEntry : (assignment
				.getAssignableItem().getAssignments().entrySet()))
			if (currentEntry.getValue().equals(assignment)) {
				assignmentPeriod = currentEntry.getKey();
				break;
			}

		desiredHours.setHourFulfiller(hour);
		Period newPeriod = desiredHours;

		assertEquals(assignmentPeriod, newPeriod);
	}

	@Test
	public void testMoveAssignmentDate() {
		// crear requisitos para los pedidos obligatorios
		Map<Resource, Integer> requiredResources = new HashMap<Resource, Integer>();
		FixedResource resource = new FixedResource("Computadora", 20);
		requiredResources.put(resource, 20);
		// opcionales
		Map<Resource, Integer> optionalResources = new HashMap<Resource, Integer>();
		FixedResource resource2 = new FixedResource("Luz Natural", 1);
		optionalResources.put(resource2, 1);
		// fecha y hora
		Period desiredHours = period1;
		// profesor y materia
		Subject subject = new Subject("Ingles 1", 000);
		Professor profesorErnesto = new Professor("Ernesto", 0, "42165035", "a@zaza.com");

		// crear pedido
		ClassroomRequest pedido = new ClassroomRequest(desiredHours, subject,
				profesorErnesto, 52, requiredResources, optionalResources, 20);
		Classroom oldClassroom = new Classroom("Aula 3", 20);
		ClassroomAssignment assignment = assignmentsDepartment.asignateRequestInAClassroom(pedido, oldClassroom);

		// nueva fecha
		Period newDate = period2;

		assignmentsDepartment.moveAssignmentOfPeriod(assignment, newDate);

		Period assignmentPeriod = null;
		for (Entry<Period, Assignment> currentEntry : (assignment
				.getAssignableItem().getAssignments().entrySet()))
			if (currentEntry.getValue().equals(assignment)) {
				assignmentPeriod = currentEntry.getKey();
				break;
			}

		assertEquals(assignmentPeriod, newDate);
	}
}