package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestClassroomRequest {
	private ClassroomRequest classroomRequest;
	private Period desiredHours;

	private Subject subject;
	private Professor professor;

	private Map<Resource,Integer> reqResources;
	private Map<Resource,Integer> optResources;

	@Before
	public void setUp() throws RequestException {
		subject = new Subject("AOP Programming", 53455);
		LinkedList<Subject> subjects = new LinkedList<Subject>();
		subjects.add(subject);
		professor = new Professor("prof", 1, "123456", "profe@univ.com", subjects);
		desiredHours = new SimplePeriod(new HourInterval(new Timestamp(8), new Timestamp(9)), new GregorianCalendar());
		reqResources = new HashMap<Resource,Integer>();
		optResources = new HashMap<Resource,Integer>();
		classroomRequest = new ClassroomRequest(desiredHours, subject, professor, 98346, reqResources, optResources, 45);
	}

	@Test
	public void test_elementsDoesNotRepeat() {
		Set<Resource> optResources = this.classroomRequest.getOptionalResources().keySet();
		Set<Resource> reqResources = this.classroomRequest.getRequiredResources().keySet();

		assertTrue(this.classroomRequest.getOptionalResources().size()==optResources.size());
		assertTrue(this.classroomRequest.getRequiredResources().size()==reqResources.size());

		for(Resource it1: optResources){
			int repQt = 0;
			for(Resource it2: optResources)
				if(it1.equals(it2))
					repQt++;
			assertTrue("Elements have repetitions",repQt<=1); //Because the element checked with itself counts
		}
	}

	@Test
	public void test_professorCanTeachTheSubjectInRequest() {
		Subject subjectRequest = this.classroomRequest.getSubject();
		boolean founded = false;

		for(Subject it : this.classroomRequest.getProfessor().getSubjects())
			if(it.equals(subjectRequest))
				founded = true;

		assertTrue("The professor cannot teach the subject in request",founded);
	}

	@Test
	public void test_capacityIsCorrectlySetted() {
		assertTrue("Capacity is not rightly setted",this.classroomRequest.getCapacity()>0);
	}

}
