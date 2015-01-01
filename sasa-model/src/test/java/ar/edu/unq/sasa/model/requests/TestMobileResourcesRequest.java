package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

public class TestMobileResourcesRequest {

    private MobileResourcesRequest mobileResourcesRequest;

    @Before
    public void setUp() {
        Subject subject = new Subject("AOP Programming");
        LinkedList<Subject> subjects = new LinkedList<>();
        subjects.add(subject);
        Professor professor = new Professor("prof", "123456", "profe@univ.com", subjects);
        Period desiredHours = new SimplePeriod(new HourInterval(new Timestamp(8), new Timestamp(9)), new GregorianCalendar());

        mobileResourcesRequest = new MobileResourcesRequest(desiredHours, subject, professor, 98346, new HashSet<>());
    }

    @Test
    public void professorCanTeachTheSubjectInRequest() {
        Subject subjectRequest = this.mobileResourcesRequest.getSubject();
        boolean founded = false;

        for (Subject it : mobileResourcesRequest.getProfessor().getSubjects())
            if (it.equals(subjectRequest))
                founded = true;

        assertTrue("Professor cannot teach the subject in request", founded);
    }
}
