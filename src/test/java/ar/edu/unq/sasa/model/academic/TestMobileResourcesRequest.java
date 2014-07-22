package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestMobileResourcesRequest {

	private MobileResourcesRequest mobileResourcesRequest;
	private Period desiredHours;
	private Subject subject;
	private Professor professor;
	private Map<Resource,Integer> reqResources;
	private Map<Resource,Integer> optResources;

	@Before
	public void setUp() throws PeriodException, TimestampException{
		subject = new Subject("AOP Programming", 53455);
		LinkedList<Subject> subjects = new LinkedList<Subject>();
		subjects.add(subject);
		professor = new Professor("prof", 1, "123456", "profe@univ.com", subjects);
		desiredHours = new SimplePeriod(new HourInterval(new Timestamp(8), new Timestamp(9)), new GregorianCalendar());
		reqResources = new HashMap<Resource,Integer>();
		optResources = new HashMap<Resource,Integer>();

		mobileResourcesRequest = new MobileResourcesRequest(desiredHours, subject, professor, 98346, reqResources, optResources);
	}

	@Test
	public void test_shouldBeConstructedCorrectly(){

		assertNotNull("desiredHours is Null",this.mobileResourcesRequest.getDesiredHours());
		assertTrue("desiredHours is from an undesired class",this.mobileResourcesRequest.getDesiredHours() instanceof Period);
		//----------------------------------------------------------------//
		assertNotNull("subject is Null",this.mobileResourcesRequest.getSubject());
		assertTrue("subject is from an undesired class",this.mobileResourcesRequest.getSubject() instanceof Subject);
		//----------------------------------------------------------------//
		assertNotNull("professor is Null",this.mobileResourcesRequest.getProfessor());
		assertTrue("professor is from an undesired class",this.mobileResourcesRequest.getProfessor() instanceof Professor);
		//----------------------------------------------------------------//
		assertNotNull("id is Null",this.mobileResourcesRequest.getId());
		//----------------------------------------------------------------//
		assertNotNull("requiredResources is Null",this.mobileResourcesRequest.getRequiredResources());
		assertTrue("requiredResources is from an undesired class",this.mobileResourcesRequest.getRequiredResources() instanceof Map<?, ?>);
		//----------------------------------------------------------------//
		assertNotNull("optionalResources is Null",this.mobileResourcesRequest.getOptionalResources());
		assertTrue("optionalResources is from an undesired class",this.mobileResourcesRequest.getOptionalResources() instanceof Map<?, ?>);
	}

	@Test
	public void test_elementsDoesNotRepeat(){
		Set<Resource> optResources = this.mobileResourcesRequest.getOptionalResources().keySet();
		Set<Resource> reqResources = this.mobileResourcesRequest.getRequiredResources().keySet();

		assertTrue(this.mobileResourcesRequest.getOptionalResources().size()==optResources.size());
		assertTrue(this.mobileResourcesRequest.getRequiredResources().size()==reqResources.size());

		for(Resource it1: optResources){
			int repQt = 0;
			for(Resource it2: optResources)
				if(it1.equals(it2))
					repQt++;
			assertTrue("Elements have repetitions",repQt<=1); //Because the element checked with itself counts
		}
	}

	@Test
	public void test_professorCanTeachTheSubjectInRequest(){
		Subject subjectRequest = this.mobileResourcesRequest.getSubject();
		boolean founded = false;

		for(Subject it : this.mobileResourcesRequest.getProfessor().getSubjects())
			if(it.equals(subjectRequest))
				founded = true;

		assertTrue("Professor cannot teach the subject in request",founded);
	}
}
