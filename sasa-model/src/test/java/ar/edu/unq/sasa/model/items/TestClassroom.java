package ar.edu.unq.sasa.model.items;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.exceptions.departments.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TestClassroom {

    private Classroom classroom;
    private FixedResource blackboard;

    @Before
    public void setUp() {
        classroom = new Classroom("37B", 30);
        blackboard = new FixedResource("Pizarrón", 1);
        classroom.addResource(blackboard);
    }

    @Test
    public void testAddResource() {
        FixedResource resource = new FixedResource("Pc", 4);
        Classroom classroom = new Classroom("ZAZA", 20);
        List<FixedResource> emptyList = new LinkedList<>();
        classroom.addResource(resource);
        emptyList.add(resource);
        assertEquals(emptyList, classroom.getResources());
    }

    @Test
    public void testGetResource() {
        FixedResource resource = new FixedResource("Pc", 4);
        Classroom classroom = new Classroom("ZAZA", 20);
        classroom.addResource(resource);
        assertEquals(resource, classroom.getResource("Pc"));
    }

    @Test
    public void testHasResourceTrue() {
        Classroom classroom = new Classroom("ZAZA", 20);
        FixedResource resource = new FixedResource("Pc", 4);
        classroom.addResource(resource);
        assertTrue(classroom.hasResource("Pc"));
    }

    @Test
    public void testHasResourceFalse() {
        Classroom classroom = new Classroom("ZAZA", 20);
        FixedResource resource = new FixedResource("Pc", 4);
        classroom.addResource(resource);
        assertFalse(classroom.hasResource("Proyector"));
    }

    @Test(expected = AssignmentException.class)
    public void testGetAssigment() {
        Period period = new SimplePeriod(null, null);
        ClassroomAssignment assignment = null;
        Classroom classroom = new Classroom("ZAZA", 20);
        classroom.addAssignment(period, assignment);
        classroom.getAssignment(period);
    }

    @Test
    public void testHasExistingResource() {
        Classroom classroom = new Classroom("ZAZA", 20);
        FixedResource resource = new FixedResource("Pc", 4);
        classroom.addResource(resource);
        assertTrue(classroom.hasResource("Pc"));
    }

    @Test(expected = ResourceException.class)
    public void testVerificarErroneo() {
        Classroom classroom = new Classroom("ZAZA", 20);
        FixedResource resource2 = null;
        classroom.verify(resource2);
    }

    @Test
    public void isFreeAt() {
        // TODO make 3 tests:
        // * free if no assignments at all
        // * free if assignments in other periods
        // * not free if assignment in the requested period
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

    @Test
    public void canAssignWithoutIgnoringCommonAssignments() {
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

    @Test
    public void canAssignIgnoringCommonAssignments() {
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
        Booking a3Mock = createMock(Booking.class);
        expect(p3Mock.intersectsWith(periodMock)).andReturn(true).anyTimes();
        expect(a3Mock.isBookedAssignment()).andReturn(true).anyTimes();
        replay(p3Mock, a3Mock);
        classroom.addAssignment(p3Mock, a3Mock);
        assertFalse(classroom.canAssign(periodMock, true));
    }

    @Test
    public void satisfyFixedResourceWhenTheResourceExistsAndTheAmountIsEnough() {
        FixedResource resource = new FixedResource("Pizarrón");
        assertTrue(classroom.satisfyFixedResource(resource, 1));
    }

    @Test
    public void doesNotSatisfyFixedResourceWhenTheResourceExistsButTheAmountIsNotEnough() {
        FixedResource resource = new FixedResource("Pizarrón");
        assertFalse(classroom.satisfyFixedResource(resource, 2));
    }

    @Test
    public void doesNotSatisfyFixedResourceWhenTheResourceDoesNotExist() {
        FixedResource resource = new FixedResource("Cortina");
        assertFalse(classroom.satisfyFixedResource(resource, 1));
    }
}
