package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.requests.MobileResourcesRequest;
import ar.edu.unq.sasa.model.requests.Requirement;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

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
    private Set<Requirement> requirements;

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
        Subject subject = new Subject("Nombres Felices");
        Professor professor = new Professor("Pablo", "44445555", "pablo@gmail.com");
        long anID = 0;
        mobileResourcesRequest = new MobileResourcesRequest(desHours, subject, professor, anID, new HashSet<>());

        // ClassroomRequest
        int capacity = 20;
        Period desHours2 = period2;
        classroomRequest = new ClassroomRequest(desHours2, subject, professor, anID, new HashSet<>(), capacity);

        // requirements for some requests
        Requirement computer = new Requirement(new FixedResource("Computadora"), 20, false);
        Requirement naturalLight = new Requirement(new FixedResource("Luz Natural"), 1, true);
        requirements = new HashSet<>();
        requirements.add(computer);
        requirements.add(naturalLight);
    }

    @Test
    public void assignResourceAssignment() {
        ResourceAssignment resourceAssignment1 =
                assignmentsDepartment.assignResourceAssignment(mobileResourcesRequest, mobileResource1, period1);
        ResourceAssignment resourceAssignment2 = new ResourceAssignment(mobileResourcesRequest, mobileResource1);

        assertEquals(resourceAssignment1, resourceAssignment2);
        assertSame(university.getAssignmentByRequest(mobileResourcesRequest), resourceAssignment1);
    }

    @Test
    public void assignBooking() {
        Booking booking1 =
                assignmentsDepartment.assignBooking(classroom1, "Limpieza", period1);
        Booking booking2 = new Booking("Limpieza", classroom1);

        Booking storedBooking = null;
        for (Assignment assignment : university.getAssignments())
            if (assignment.equals(booking1)) {
                storedBooking = (Booking) assignment;
                break;
            }

        assertEquals(booking1, booking2);
        assertSame(storedBooking, booking1);
    }

    @Test
    public void assignClassroomAssignment() {
        ClassroomAssignment classroomAssignment1 =
                assignmentsDepartment.assignClassroomAssignment(classroomRequest, classroom1, period1);
        List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<>();
        ClassroomAssignment classroomAssignment2 =
                new ClassroomAssignment(classroomRequest, classroom1, resourcesAssignmentsList);
        classroom1.addAssignment(period2, classroomAssignment2);
        classroomAssignment2.createSatisfaction();

        Assignment storedBooking = null;
        for (Assignment assignment : university.getAssignments())
            if (assignment.equals(classroomAssignment1))
                storedBooking = assignment;

        assertEquals(classroomAssignment1, classroomAssignment2);
        assertSame(storedBooking, classroomAssignment1);
    }

    @Test
    public void assignRequestInAClassroom() {
        ClassroomAssignment classroomAssignment1 =
                assignmentsDepartment.assignRequestInAClassroom(classroomRequest, classroom1);
        ClassroomAssignment classroomAssignment2 =
                assignmentsDepartment.assignClassroomAssignment(classroomRequest, classroom1, period2);

        assertEquals(classroomAssignment1, classroomAssignment2);
    }

    @Test
    public void assignRequestInMostSatisfactoryClassroom() {
        university.getRequestsDepartment().addRequest(classroomRequest);
        ClassroomAssignment assignment = assignmentsDepartment.assignRequestInMostSatisfactoryClassroom(classroomRequest);
        assertEquals(classroomRequest, assignment.getRequest());
        assertTrue(university.getAssignments().contains(assignment));
    }

    @Test
    public void modifyBookedAssignmentCause() {
        Booking booking = assignmentsDepartment.assignBooking(classroom1, "Restauración", period1);
        String newCause = "Robado";
        assignmentsDepartment.modifyBookedAssignmentCause(booking, newCause);

        assertEquals(booking.getCause(), newCause);
    }

    @Test
    public void deleteResourceAssignmentFromARequest() {
        ResourceAssignment resourceAssignment =
                assignmentsDepartment.assignResourceAssignment(mobileResourcesRequest, mobileResource1, period1);
        assignmentsDepartment.deleteResourceAssignment(resourceAssignment);
        Map<Period, Assignment> map = new HashMap<>();

        assertEquals(resourceAssignment.getAssignableItem().getAssignments(), map);

        boolean exists = false;
        for (Assignment assignment : university.getAssignments())
            if (assignment.equals(resourceAssignment))
                exists = true;

        assertFalse(exists);
    }

    @Test
    public void deleteClassroomAssignment() {
        ClassroomAssignment classroomAssignment =
                assignmentsDepartment.assignClassroomAssignment(classroomRequest, classroom1, period1);
        assignmentsDepartment.deleteClassroomAssignmentFromARequest(classroomRequest);
        Map<Period, Assignment> map = new HashMap<>();

        assertEquals(classroomAssignment.getAssignableItem().getAssignments(), map);

        boolean exists = false;
        for (Assignment assignment : university.getAssignments())
            if (assignment.equals(classroomAssignment))
                exists = true;

        assertFalse(exists);
    }

    @Test
    public void deleteBookedAssignment() {
        Booking booking =
                assignmentsDepartment.assignBooking(classroom1, "Reparación", period1);
        assignmentsDepartment.deleteAssignment(booking);
        Map<Period, Assignment> map = new HashMap<>();

        assertEquals(booking.getAssignableItem().getAssignments(), map);

        boolean exists = false;
        for (Assignment assignment : university.getAssignments())
            if (assignment.equals(booking))
                exists = true;

        assertFalse(exists);
    }

    @Test
    public void searchForAssignment() {
        ResourceAssignment resourceAssignment1 =
                assignmentsDepartment.assignResourceAssignment(mobileResourcesRequest, mobileResource1, period1);
        ResourceAssignment resourceAssignment2 =
                (ResourceAssignment) assignmentsDepartment.searchForAssignment(mobileResource1, period1);

        assertSame(resourceAssignment1, resourceAssignment2);
    }

    @Test
    public void testMoveAssignmentClassroom() {

        Period desiredHours6 = period1;
        Subject subject6 = new Subject("Inglés 1");
        Professor profesorErnesto = new Professor("Ernesto", "42165035", "a@zaza.com");

        // crear pedido
        ClassroomRequest pedido6 = new ClassroomRequest(desiredHours6, subject6, profesorErnesto, 0, requirements, 20);
        Classroom oldClassroom = new Classroom("Aula 3", 20);

        // crear asignación y aula para asignar
        ClassroomAssignment classroomAssignment =
                assignmentsDepartment.assignRequestInAClassroom(pedido6, oldClassroom);
        Classroom newClassroom = new Classroom("Aula 4", 20);
        ClassroomAssignment newAssignment =
                assignmentsDepartment.moveAssignmentOfClassroom(classroomAssignment, newClassroom);

        assertEquals(newClassroom, newAssignment.getAssignableItem());
    }

    @Test
    public void testMoveAssignmentHour() {
        Period desiredHours = period1;
        Subject subject = new Subject("Inglés 1");
        Professor profesorErnesto = new Professor("Ernesto", "42165035", "a@zaza.com");
        ClassroomRequest pedido = new ClassroomRequest(desiredHours, subject, profesorErnesto, 52, requirements, 20);
        Classroom oldClassroom = new Classroom("Aula 3", 20);
        ClassroomAssignment assignment = assignmentsDepartment.assignRequestInAClassroom(pedido, oldClassroom);

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

        assertEquals(assignmentPeriod, desiredHours);
    }

    @Test
    public void testMoveAssignmentDate() {
        Period desiredHours = period1;
        Subject subject = new Subject("Inglés 1");
        Professor profesorErnesto = new Professor("Ernesto", "42165035", "a@zaza.com");
        ClassroomRequest pedido = new ClassroomRequest(desiredHours, subject, profesorErnesto, 52, requirements, 20);
        Classroom oldClassroom = new Classroom("Aula 3", 20);
        ClassroomAssignment assignment = assignmentsDepartment.assignRequestInAClassroom(pedido, oldClassroom);

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
