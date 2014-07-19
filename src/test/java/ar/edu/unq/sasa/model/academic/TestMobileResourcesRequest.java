package ar.edu.unq.sasa.model.academic;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/** TESTMOBILERESOURCESREQUEST
 *  Test suite for tme MobileResource class.
 * @author MEKODA
 * CONSIDERATIONS
 * I use mock object for unsafe not-test-focus classes.
 */
public class TestMobileResourcesRequest extends TestCase {
	public MobileResourcesRequest mobileResourcesRequest;
	public Period desHours;
	public Period period;
	
	public Subject subject;
	public Subject subjectForList;
	public Professor professor;
	public List<Subject> subjectList;
	
	public Map<Resource,Integer> reqResources;
	public MobileResource projector;
	public Map<Resource,Integer> optResources;
	
	public void setUp(){
		this.subject = createMock(Subject.class);
		expect(this.subject.getName()).andReturn("AOP Programming");
		expect(this.subject.getId()).andReturn((long)53455);
		//----------------------------------------------------------//
		this.professor = createMock(Professor.class);
		this.subjectList = new LinkedList<Subject>();
		this.subjectList.add(this.subject);
		expect(this.professor.getSubjects()).andReturn(this.subjectList);
		//----------------------------------------------------------//
		this.projector = createMock(MobileResource.class);
		this.period = createMock(Period.class);
		this.desHours = createMock(Period.class);
		this.reqResources = new HashMap<Resource,Integer>();
		this.optResources = new HashMap<Resource,Integer>();
		//----------------------------------------------------------//
		replay(this.subject);
		replay(this.professor);
		
		this.mobileResourcesRequest = new MobileResourcesRequest(this.desHours,this.subject,this.professor, 98346,
				this.reqResources,this.optResources);
	}
	
	/**
	 * Tests the correct working of the creational method.
	 */
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
	
	/**
	 * Test against repeated elements.
	 */
	public void test_elementsDoesNotRepeat(){
		Set<Resource> optResources = this.mobileResourcesRequest.getOptionalResources().keySet();
		Set<Resource> reqResources = this.mobileResourcesRequest.getRequiredResources().keySet();
		
		assertTrue(this.mobileResourcesRequest.getOptionalResources().size()==optResources.size());
		assertTrue(this.mobileResourcesRequest.getRequiredResources().size()==reqResources.size());
		
		for(Resource it1: optResources){
			int repQt = 0;
			for(Resource it2: optResources){
				if(it1.equals(it2))
				{repQt++;}
			}
			assertTrue("Elements have repetitions",repQt<=1); //Because the element checked with itself counts
		}
	}
	
	/**
	 * Checks if the professor can teach the declared subject in the request.
	 */
	public void test_professorCanTeachTheSubjectInRequest(){
		Subject subjectRequest = this.mobileResourcesRequest.getSubject();
		boolean founded = false;
		
		for(Subject it : this.mobileResourcesRequest.getProfessor().getSubjects())
			if(it.equals(subjectRequest))
				{founded = true;}
		
		assertTrue("Professor cannot teach the subject in request",founded);
	}
}
