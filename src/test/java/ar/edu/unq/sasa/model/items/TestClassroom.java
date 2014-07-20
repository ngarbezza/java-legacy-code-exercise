package ar.edu.unq.sasa.model.items;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.exceptions.handlers.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.mocks.assignments.MockClassroomAssignment;
import ar.edu.unq.sasa.model.mocks.items.MockFixedResource;
import ar.edu.unq.sasa.model.mocks.time.MockPeriod;
import ar.edu.unq.sasa.model.time.Period;

/**
 * @author Diego
 *
 */
public class TestClassroom {	
	// Por Diego
	@Test
	public void testConstructor() {
		Classroom classroom = new Classroom("ZAZA", 20);
		String name = classroom.getName();
		int cap = classroom.getCapacity();
		List<FixedResource> resources = classroom.getResources();
		Map<Period, Assignment> assigments = classroom.getAssignments();
		List<FixedResource> emptyList = new LinkedList<FixedResource>();
		Map<Period, Assignment> emptyMap = new HashMap<Period, Assignment>();
		assertEquals(20, cap);
		assertEquals("ZAZA", name);
		assertEquals(emptyList, resources);
		assertEquals(emptyMap, assigments);
	}
	// Por Diego
	@Test
	public void testAddResource() {
		MockFixedResource resource = new MockFixedResource("Pc", 4);
		Classroom classroom = new Classroom("ZAZA", 20);
		List<FixedResource> emptyList = new LinkedList<FixedResource>();
		classroom.addResource(resource);
		emptyList.add(resource);
		assertEquals(emptyList, classroom.getResources());
	}
	// Por Diego
	@Test
	public void testGetResource() throws ResourceException {
		MockFixedResource resource = new MockFixedResource("Pc", 4);
		Classroom classroom = new Classroom("ZAZA", 20);
		classroom.addResource(resource);
		assertEquals(resource, classroom.getResource("Pc"));
	}
	// Por Diego
	@Test
	public void testHasResourceTrue() {
		MockFixedResource resource = new MockFixedResource("Pc", 4);
		Classroom classroom = new Classroom("ZAZA", 20);
		classroom.addResource(resource);
		assertEquals(true, classroom.hasResource("Pc"));
	}
	// Por Diego
	@Test
	public void testHasResourceFalse() {
		MockFixedResource resource = new MockFixedResource("Pc", 4);
		Classroom classroom = new Classroom("ZAZA", 20);
		classroom.addResource(resource);
		assertEquals(false, classroom.hasResource("Proyector"));
	}

	// Por Diego
	@Test
	public void testGetAssigment() throws AssignmentException,ResourceException {
		MockPeriod period = new MockPeriod();
		MockClassroomAssignment assignment = null;
		Classroom classroom = new Classroom("ZAZA", 20);
		classroom.addAssignment(period, assignment);
		try {
			classroom.getAssignment(period);
		} catch (AssignmentException e) {
			System.out.println("Exception capturada,se paso como parametro '"
					+ period + "' es null");
		}
     }
	// Por Diego
	@Test
	public void testVerificarCorrecto() throws ResourceException {
		Classroom classroom = new Classroom("ZAZA", 20);
		MockFixedResource resource = new MockFixedResource("Pc", 4);
		classroom.addResource(resource);
		try {
			classroom.verificar(resource);
		} catch (ResourceException e) {
			System.out.println("Exception capturada,se paso como parametro '"
					+ resource + "' es null");
		}
	}
	// Por Diego
	@Test
	public void testVerificarErroneo() throws ResourceException {
		Classroom classroom = new Classroom("ZAZA", 20);
		MockFixedResource resource2 = null;
		try {
			classroom.verificar(resource2);
		} catch (ResourceException e) {
			System.out.println("Exception capturada, se paso como parametro: '"
					+ resource2 + "'");
		}
	}
	
	// por Nahuel
	@Test
	public void test_isFreeAt() throws Exception {
		Classroom classroom = new Classroom("La 37B", 30);
		Calendar mockCalendar = createMock(Calendar.class);
		Period p1Mock = createMock(Period.class);
		Period p2Mock = createMock(Period.class);
		Period p3Mock = createMock(Period.class);
		expect(p1Mock.contains(mockCalendar)).andReturn(false).anyTimes();
		expect(p2Mock.contains(mockCalendar)).andReturn(false).anyTimes();
		expect(p3Mock.contains(mockCalendar)).andReturn(true).anyTimes();
		replay(p1Mock, p2Mock, p3Mock);
		classroom.addAssignment(p1Mock, createMock(Assignment.class));
		classroom.addAssignment(p2Mock, createMock(Assignment.class));
		assertTrue(classroom.isFreeAt(mockCalendar));
		classroom.addAssignment(p3Mock, createMock(Assignment.class));
		assertFalse(classroom.isFreeAt(mockCalendar));
	}
	
	// por Nahuel
	@Test
	public void test_canAssignWithoutIgnoringCommonAssignments() throws Exception {
		Classroom classroom = new Classroom("La 37B", 30);
		Period periodMock = createMock(Period.class);
		Period p1Mock = createMock(Period.class);
		Period p2Mock = createMock(Period.class);
		expect(p1Mock.intersectsWith(periodMock)).andReturn(false).anyTimes();
		expect(p2Mock.intersectsWith(periodMock)).andReturn(true).anyTimes();
		replay(p1Mock, p2Mock);
		classroom.addAssignment(p1Mock, createMock(Assignment.class));
		assertTrue(classroom.canAssign(periodMock, false));
		classroom.addAssignment(p2Mock, createMock(Assignment.class));
		assertFalse(classroom.canAssign(periodMock, false));
	}
	
	// por Nahuel
	@Test
	public void test_canAssignIgnoringCommonAssignments() throws Exception {
		Classroom classroom = new Classroom("La 37B", 30);
		Period periodMock = createMock(Period.class);
		Period p1Mock = createMock(Period.class);
		Period p2Mock = createMock(Period.class);
		Assignment a1Mock = createMock(Assignment.class);
		Assignment a2Mock = createMock(Assignment.class);
		expect(p1Mock.intersectsWith(periodMock)).andReturn(false).anyTimes();
		expect(p2Mock.intersectsWith(periodMock)).andReturn(true).anyTimes();
		expect(a1Mock.isBookedAssignment()).andReturn(true).anyTimes();
		expect(a2Mock.isBookedAssignment()).andReturn(false).anyTimes(); 
		replay(p1Mock, p2Mock, a1Mock, a2Mock);
		classroom.addAssignment(p1Mock, a1Mock);
		classroom.addAssignment(p2Mock, a2Mock);
		// en este punto todavía se debería poder asignar
		assertTrue(classroom.canAssign(periodMock, true));
		// creamos una combinación Period-Assignment tal que no se pueda asignar
		Period p3Mock = createMock(Period.class);
		BookedAssignment a3Mock = createMock(BookedAssignment.class);
		expect(p3Mock.intersectsWith(periodMock)).andReturn(true).anyTimes();
		expect(a3Mock.isBookedAssignment()).andReturn(true).anyTimes();
		replay(p3Mock, a3Mock);
		classroom.addAssignment(p3Mock, a3Mock);
		assertFalse(classroom.canAssign(periodMock, true));
	}
	
	// por Nahuel
	@Test
	public void test_satisfyFixedResource() throws Exception {
		Classroom classroom = new Classroom("La 37B", 30);
		FixedResource resMock = createMock(FixedResource.class);
		expect(resMock.getName()).andReturn("Pizarron").anyTimes();
		FixedResource r1Mock = createMock(FixedResource.class);
		expect(r1Mock.getName()).andReturn("Pizarron").anyTimes();
		expect(r1Mock.getAmount()).andReturn(1).anyTimes();
		replay(resMock, r1Mock);
		classroom.addResource(r1Mock);
		assertFalse(classroom.satisfyFixedResource(resMock, 2));
		assertTrue(classroom.satisfyFixedResource(resMock, 1));
	}
}
