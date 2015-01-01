package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.util.PreconditionNotMetException;

public class TestClassroomRequest {
	private ClassroomRequest classroomRequest;
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
		classroomRequest = new ClassroomRequest(
				desiredHours, subject, professor, 98346, reqResources, optResources, 45);
	}

	@Test
	public void elementsDoesNotRepeat() {
		Set<Resource> optionalResourcesSet = classroomRequest.getOptionalResources().keySet();
		Set<Resource> requiredResourcesSet = classroomRequest.getRequiredResources().keySet();

		assertTrue(classroomRequest.getOptionalResources().size() == optionalResourcesSet.size());
		assertTrue(classroomRequest.getRequiredResources().size() == requiredResourcesSet.size());

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
		Subject subjectRequest = classroomRequest.getSubject();
		boolean founded = false;

		for (Subject it : classroomRequest.getProfessor().getSubjects())
			if (it.equals(subjectRequest))
				founded = true;

		assertTrue("The professor cannot teach the subject in request", founded);
	}

	@Test
	public void capacityIsCorrectlySetted() {
		assertTrue("Capacity is not rightly setted", classroomRequest.getCapacity() > 0);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void aClassroomRequestCannotBeBuiltIfCapacityIsZero() {
		new ClassroomRequest(desiredHours, subject, professor, 15423, reqResources, optResources, 0);
	}

	@Test(expected = PreconditionNotMetException.class)
	public void aClassroomRequestCannotBeBuiltIfCapacityIsLessThanZero() {
		new ClassroomRequest(desiredHours, subject, professor, 15423, reqResources, optResources, -1);
	}
}
