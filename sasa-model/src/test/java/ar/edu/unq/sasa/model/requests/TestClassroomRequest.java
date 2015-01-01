package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.util.PreconditionNotMetException;
import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

public class TestClassroomRequest {
    private ClassroomRequest classroomRequest;
    private Period desiredHours;

    private Subject subject;
    private Professor professor;

    @Before
    public void setUp() {
        subject = new Subject("AOP Programming");
        LinkedList<Subject> subjects = new LinkedList<>();
        subjects.add(subject);
        professor = new Professor("prof", "123456", "profe@univ.com", subjects);
        desiredHours = new SimplePeriod(new HourInterval(new Timestamp(8), new Timestamp(9)), new GregorianCalendar());
        classroomRequest = new ClassroomRequest(desiredHours, subject, professor, 98346, new HashSet<>(), 45);
    }

    @Test
    public void professorCanTeachTheSubjectInRequest() {
        Subject subjectRequest = classroomRequest.getSubject();
        boolean founded = false;

        for (Subject it : classroomRequest.getProfessor().getSubjects())
            if (it.equals(subjectRequest))
                founded = true;

        assertTrue("The professor cannot teach the subject in request", founded);
    }

    @Test
    public void capacityIsCorrectlySet() {
        assertTrue("Capacity is not rightly set", classroomRequest.getCapacity() > 0);
    }

    @Test(expected = PreconditionNotMetException.class)
    public void aClassroomRequestCannotBeBuiltIfCapacityIsZero() {
        new ClassroomRequest(desiredHours, subject, professor, 15423, new HashSet<>(), 0);
    }

    @Test(expected = PreconditionNotMetException.class)
    public void aClassroomRequestCannotBeBuiltIfCapacityIsLessThanZero() {
        new ClassroomRequest(desiredHours, subject, professor, 15423, new HashSet<>(), -1);
    }
}
