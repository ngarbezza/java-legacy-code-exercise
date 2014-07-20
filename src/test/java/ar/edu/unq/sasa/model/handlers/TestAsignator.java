package ar.edu.unq.sasa.model.handlers;

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
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestAsignator {
	private InformationManager informationManager;
	private Asignator asignator;
	private MobileResourcesRequest mobileResourcesRequest;
	private ClassroomRequest classroomRequest;
	private Period period1;
	private Period period2;
	private MobileResource mobileResource1;
	private Classroom classroom1;
	private Classroom classroom2;

	@Before
	public void setUp() throws Exception {
		// InformationManager
		informationManager = InformationManager.getInstance();
		informationManager.getClassrooms().clear();

		// Period 1
		Timestamp timestamp1 = new Timestamp(10);
		Timestamp timestamp11 = new Timestamp(12);
		LogicalHourFulfiller logicHourFul1 = new HourInterval(timestamp1,
				timestamp11);
		period1 = new SimplePeriod(logicHourFul1, new GregorianCalendar(2010,
				Calendar.DECEMBER, 5));

		// Period 2
		Timestamp timestamp2 = new Timestamp(15);
		Timestamp timestamp22 = new Timestamp(18);
		LogicalHourFulfiller logicHourFul2 = new HourInterval(timestamp2,
				timestamp22);
		period2 = new SimplePeriod(logicHourFul2, new GregorianCalendar(2010,
				Calendar.DECEMBER, 5));

		// MobileResource 1
		mobileResource1 = new MobileResource("Proyector", 0);

		// Classroom 1
		classroom1 = new Classroom("Aula 1", 30);
		informationManager.addClassroom(classroom1);

		// Classroom 2
		classroom2 = new Classroom("Aula 2", 29);
		informationManager.addClassroom(classroom2);

		// Asignator
		asignator = new Asignator();

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
		classroomRequest = new ClassroomRequest(desHours2, subject, professor,
				anID, reqResources2, optResources2, capacity);

	}

	/**
	 * @author Cristian Suarez
	 */
	@Test
	public void test_ShouldConstructCorrectly() {
		assertSame(asignator.getInformationManager(), informationManager);
	}

	/**
	 * @author Cristian Suarez
	 */
	@Test
	public void test_asignateResourceAssignment() throws AssignmentException {
		ResourceAssignment resourceAssignment1 = asignator
				.asignateResourceAssignment(mobileResourcesRequest,
						mobileResource1, period1);
		ResourceAssignment resourceAssignment2 = new ResourceAssignment(
				mobileResourcesRequest, mobileResource1);

		assertEquals(resourceAssignment1, resourceAssignment2);
		assertSame(informationManager
				.getAssignmentByRequest(mobileResourcesRequest),
				resourceAssignment1);
	}

	/**
	 * @author Cristian Suarez
	 * @throws PeriodException
	 */
	@Test
	public void test_asignateBookedAssignment() throws PeriodException {
		BookedAssignment bookedAssignment1 = asignator
				.asignateBookedAssignment(classroom1, "Limpieza", period1);
		BookedAssignment bookedAssignment2 = new BookedAssignment("Limpieza",
				classroom1);

		BookedAssignment storedBookeadAssignment = null;
		for (Assignment assignment : informationManager.getAssignments()) {
			if (assignment.equals(bookedAssignment1)) {
				storedBookeadAssignment = (BookedAssignment) assignment;
				break;
			}
		}

		assertEquals(bookedAssignment1, bookedAssignment2);
		assertSame(storedBookeadAssignment, bookedAssignment1);
	}

	/**
	 * @author Cristian Suarez
	 * @throws PeriodException
	 */
	@Test
	public void test_asignateClassroomAssignment() throws PeriodException {
		ClassroomAssignment classroomAssignment1 = asignator
				.asignateClassroomAssignment(classroomRequest, classroom1,
						period1);
		List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<ResourceAssignment>();
		ClassroomAssignment classroomAssignment2 = new ClassroomAssignment(
				classroomRequest, classroom1, resourcesAssignmentsList);
		classroom1.addAssignment(period2, classroomAssignment2);
		classroomAssignment2.createSatisfaction();

		ClassroomAssignment storedBookeadAssignment = null;
		for (Assignment assignment : informationManager.getAssignments()) {
			if (assignment.equals(classroomAssignment1)) {
				storedBookeadAssignment = (ClassroomAssignment) assignment;
			}
		}

		assertEquals(classroomAssignment1, classroomAssignment2);
		assertSame(storedBookeadAssignment, classroomAssignment1);
	}

	/**
	 * @author Cristian Suarez
	 */
	@Test
	public void test_asignateRequestInAClassroom() throws PeriodException {
		ClassroomAssignment classroomAssignment1 = asignator
				.asignateRequestInAClassroom(classroomRequest, classroom1);
		ClassroomAssignment classroomAssignment2 = asignator
				.asignateClassroomAssignment(classroomRequest, classroom1,
						period2);

		assertEquals(classroomAssignment1, classroomAssignment2);
	}

	/**
	 * @author Cristian Suarez
	 * @throws AssignmentException
	 * @throws RequestException
	 */
	@Test
	public void test_asignateRequestInMostSatisfactoryClassroom()
			throws PeriodException, ResourceException, AssignmentException,
			RequestException {
		InformationManager.getInstance().addRequest(classroomRequest);
		ClassroomAssignment asig = asignator.asignateRequestInMostSatisfactoryClassroom(classroomRequest);
		assertEquals(classroomRequest, asig.getRequest());
		assertTrue(InformationManager.getInstance().getAssignments().contains(asig));
	}

	/**
	 * @author Cristian Suarez
	 * @throws PeriodException
	 */
	@Test
	public void test_modifyBookedAssignmentCause() throws PeriodException {
		BookedAssignment bookedAssignment = asignator.asignateBookedAssignment(
				classroom1, "Restauracion", period1);
		String newCause = "Robado";
		asignator.modifyBookedAssignmentCause(bookedAssignment, newCause);

		assertEquals(bookedAssignment.getCause(), newCause);
	}

	/**
	 * @author Cristian Suarez
	 */
	@Test
	public void test_deleteResourceAssignmentFromARequest() {
		ResourceAssignment resourceAssignment = asignator
				.asignateResourceAssignment(mobileResourcesRequest,
						mobileResource1, period1);
		asignator.deleteResourceAssignment(resourceAssignment);
		Map<Period, Assignment> map = new HashMap<Period, Assignment>();

		assertEquals(resourceAssignment.getAssignableItem().getAssignments(),
				map);

		boolean exists = false;
		for (Assignment assignment : informationManager.getAssignments()) {
			if (assignment.equals(resourceAssignment)) {
				exists = true;
			}
		}

		assertFalse(exists);
	}

	/**
	 * @author Cristian Suarez
	 * @throws PeriodException
	 */
	@Test
	public void test_deleteClassroomAssignment() throws AssignmentException,
			PeriodException {
		ClassroomAssignment classroomAssignment = asignator
				.asignateClassroomAssignment(classroomRequest, classroom1,
						period1);
		asignator.deleteClassroomAssignmentFromARequest(classroomRequest);
		Map<Period, Assignment> map = new HashMap<Period, Assignment>();

		assertEquals(classroomAssignment.getAssignableItem().getAssignments(),
				map);

		boolean exists = false;
		for (Assignment assignment : informationManager.getAssignments()) {
			if (assignment.equals(classroomAssignment)) {
				exists = true;
			}
		}

		assertFalse(exists);
	}

	/**
	 * @author Cristian Suarez
	 * @throws PeriodException
	 */
	@Test
	public void test_deleteBookedAssignment() throws PeriodException {
		BookedAssignment bookedAssignment = asignator.asignateBookedAssignment(
				classroom1, "Reparacion", period1);
		asignator.deleteAssignment(bookedAssignment);
		Map<Period, Assignment> map = new HashMap<Period, Assignment>();

		assertEquals(bookedAssignment.getAssignableItem().getAssignments(), map);

		boolean exists = false;
		for (Assignment assignment : informationManager.getAssignments()) {
			if (assignment.equals(bookedAssignment)) {
				exists = true;
			}
		}

		assertFalse(exists);
	}

	/**
	 * @author Cristian Suarez
	 */
	@Test
	public void test_searchForAssignment() {
		ResourceAssignment resourceAssignment1 = asignator
				.asignateResourceAssignment(mobileResourcesRequest,
						mobileResource1, period1);
		ResourceAssignment resourceAssignment2 = (ResourceAssignment) asignator
				.searchForAssignment(mobileResource1, period1);

		assertSame(resourceAssignment1, resourceAssignment2);
	}

	/**
	 * @author Diego Campos
	 */
	@Test
	public void testMoveAssignmentClassroom() throws AssignmentException,
			TimestampException, RequestException, PeriodException {

		// crear requisitos para los pedidos
		// obligatorios
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
		Professor profesorErnesto = new Professor("Ernesto", 0, "42165035",
				"a@zaza.com");

		// crear pedido
		ClassroomRequest pedido6 = new ClassroomRequest(desiredHours6,
				subject6, profesorErnesto, 0, requiredResources,
				optionalResources, 20);
		Classroom oldClassroom = new Classroom("Aula 3", 20);

		// crear asignacion y aula para asignar
		ClassroomAssignment classroomAssignment = asignator
				.asignateRequestInAClassroom(pedido6, oldClassroom);
		Classroom newClassroom = new Classroom("Aula 4", 20);
		ClassroomAssignment newAssignment = asignator
				.moveAssignmentOfClassroom(classroomAssignment, newClassroom);

		assertEquals(newClassroom, newAssignment.getAssignableItem());
	}

	/**
	 * @author Diego Campos
	 */
	@Test
	public void testMoveAssignmentHour() throws AssignmentException,
			TimestampException, RequestException, PeriodException {

		// crear requisitos para los pedidos
		// obligatorios
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
		ClassroomAssignment assignment = asignator.asignateRequestInAClassroom(
				pedido, oldClassroom);

		/*
		 * GregorianCalendar calendar = new GregorianCalendar(2010 , 4, 3);
		 * Timestamp start = new Timestamp(9) ; Timestamp end = new
		 * Timestamp(11, 30); LogicalHourFulfiller hour = new
		 * HourInterval(start, end); Period newDate = new SimplePeriod(hour,
		 * calendar);
		 */

		// nueva hora
		Timestamp start = new Timestamp(9);
		Timestamp end = new Timestamp(11, 30);
		LogicalHourFulfiller hour = new HourInterval(start, end);

		asignator.moveAssignmentOfHour(assignment, hour);

		Period assignmentPeriod = null;
		for (Entry<Period, Assignment> currentEntry : (assignment
				.getAssignableItem().getAssignments().entrySet())) {
			if (currentEntry.getValue().equals(assignment)) {
				assignmentPeriod = currentEntry.getKey();
				break;
			}
		}

		desiredHours.setHourFulfiller(hour);
		Period newPeriod = desiredHours;

		assertEquals(assignmentPeriod, newPeriod);
	}

	/**
	 * @author Diego Campos
	 */
	@Test
	public void testMoveAssignmentDate() throws AssignmentException,
			TimestampException, RequestException, PeriodException {
		// crear requisitos para los pedidos
		// obligatorios
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
		Professor profesorErnesto = new Professor("Ernesto", 0, "42165035",
				"a@zaza.com");

		// crear pedido
		ClassroomRequest pedido = new ClassroomRequest(desiredHours, subject,
				profesorErnesto, 52, requiredResources, optionalResources, 20);
		Classroom oldClassroom = new Classroom("Aula 3", 20);
		ClassroomAssignment assignment = asignator.asignateRequestInAClassroom(
				pedido, oldClassroom);

		// nueva fecha
		Period newDate = period2;

		asignator.moveAssignmentOfPeriod(assignment, newDate);

		Period assignmentPeriod = null;
		for (Entry<Period, Assignment> currentEntry : (assignment
				.getAssignableItem().getAssignments().entrySet())) {
			if (currentEntry.getValue().equals(assignment)) {
				assignmentPeriod = currentEntry.getKey();
				break;
			}
		}

		assertEquals(assignmentPeriod, newDate);
	}
}