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
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/** TESTPROFESSOR
 *  Test suite for the Professor Class.
 * @author MEKODA
 * CONSIDERATIONS
 * Instance variable classroomRequest is going to be the only unsafe class; the 
 * another ones are mock objects (excepting Maps and List)
 */
public class TestClassroomRequest extends TestCase {
	public ClassroomRequest classroomRequest;
	public ClassroomRequest fclassroomRequest;
	public Period desHours;
	public Period period;
	
	public Subject subject;
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
		
		try {
			this.classroomRequest = new ClassroomRequest(this.desHours,this.subject,this.professor, 98346,
					this.reqResources,this.optResources,45);
		} catch (RequestException e) {
			// try-catch block just to avoid the warning!
			e.printStackTrace();
		}
	}
	

	/**
	 * 
	 */
	public void test_shouldBeConstructedCorrectly(){
		
		assertNotNull("desiredHours is Null",this.classroomRequest.getDesiredHours());
		assertTrue("desiredHours is from an undesired class",this.classroomRequest.getDesiredHours() instanceof Period);
		//----------------------------------------------------------------//
		assertNotNull("subject is Null",this.classroomRequest.getSubject());
		assertTrue("subject is from an undesired class",this.classroomRequest.getSubject() instanceof Subject);
		//----------------------------------------------------------------//
		assertNotNull("professor is Null",this.classroomRequest.getProfessor());
		assertTrue("professor is from an undesired class",this.classroomRequest.getProfessor() instanceof Professor);
		//----------------------------------------------------------------//
		assertNotNull("id is Null",this.classroomRequest.getId());
		//----------------------------------------------------------------//
		assertNotNull("capacity is Null",this.classroomRequest.getCapacity());
		//----------------------------------------------------------------//
		assertNotNull("requiredResources is Null",this.classroomRequest.getRequiredResources());
		assertTrue("requiredResources is from an undesired class",this.classroomRequest.getRequiredResources() instanceof Map<?, ?>);
		//----------------------------------------------------------------//
		assertNotNull("optionalResources is Null",this.classroomRequest.getOptionalResources());
		assertTrue("optionalResources is from an undesired class",this.classroomRequest.getOptionalResources() instanceof Map<?, ?>);
	}


	/**
	 * Tests if the request haves repeated elements (in the resources lists).
	 */	
	public void test_elementsDoesNotRepeat(){
		Set<Resource> optResources = this.classroomRequest.getOptionalResources().keySet();
		Set<Resource> reqResources = this.classroomRequest.getRequiredResources().keySet();
		
		assertTrue(this.classroomRequest.getOptionalResources().size()==optResources.size());
		assertTrue(this.classroomRequest.getRequiredResources().size()==reqResources.size());
		
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
	 * Tests if the professor in the request can teach the subject he 
	 * declared is going to teach.
	 */	
	public void test_professorCanTeachTheSubjectInRequest(){
		Subject subjectRequest = this.classroomRequest.getSubject();
		boolean founded = false;
		
		for(Subject it : this.classroomRequest.getProfessor().getSubjects()){
			if(it.equals(subjectRequest))
				{founded = true;}
		}
		
		assertTrue("The professor cannot teach the subject in request",founded);
	}


	/**
	 * Determines if the capacity value is possible.
	 */	
	public void test_capacityIsCorrectlySetted(){
		assertTrue("Capacity is not rightly setted",this.classroomRequest.getCapacity()>0);
	}
	
}
