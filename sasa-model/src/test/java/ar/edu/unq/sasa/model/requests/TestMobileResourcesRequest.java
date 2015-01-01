package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class TestMobileResourcesRequest {

    private MobileResourcesRequest mobileResourcesRequest;
    private Period desiredHours;
    private Subject subject;
    private Professor professor;
    private Map<Resource, Integer> reqResources;
    private Map<Resource, Integer> optResources;

    @Before
    public void setUp() {
        subject = new Subject("AOP Programming");
        LinkedList<Subject> subjects = new LinkedList<Subject>();
        subjects.add(subject);
        professor = new Professor("prof", "123456", "profe@univ.com", subjects);
        desiredHours = new SimplePeriod(new HourInterval(new Timestamp(8), new Timestamp(9)), new GregorianCalendar());
        reqResources = new HashMap<Resource, Integer>();
        optResources = new HashMap<Resource, Integer>();

        mobileResourcesRequest = new MobileResourcesRequest(
                desiredHours, subject, professor, 98346, reqResources, optResources);
    }

    @Test
    public void elementsDoesNotRepeat() {
        Set<Resource> optionalResourcesSet = mobileResourcesRequest.getOptionalResources().keySet();
        Set<Resource> requiredResourcesSet = mobileResourcesRequest.getRequiredResources().keySet();

        assertTrue(mobileResourcesRequest.getOptionalResources().size() == optionalResourcesSet.size());
        assertTrue(mobileResourcesRequest.getRequiredResources().size() == requiredResourcesSet.size());

        for (Resource it1 : optionalResourcesSet) {
            int repQt = 0;
            for (Resource it2 : optionalResourcesSet)
                if (it1.equals(it2))
                    repQt++;
            assertTrue("Elements have repetitions", repQt <= 1); //Because the element checked with itself counts
        }
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
