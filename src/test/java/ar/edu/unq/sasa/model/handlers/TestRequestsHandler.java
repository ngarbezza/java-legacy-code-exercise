package ar.edu.unq.sasa.model.handlers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Map;

import junit.framework.TestCase;
import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.MobileResourcesRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Testing para la clase {@link RequestsHandler}.
 * 
 * @author Cristian
 * 
 */
public class TestRequestsHandler extends TestCase {
	private RequestsHandler reqHand;
	private MobileResourcesRequest mobileResourcesRequest;
	private ClassroomRequest classroomRequest;
	
	public void setUp(){
		reqHand = RequestsHandler.getInstance();
		mobileResourcesRequest = createMock(MobileResourcesRequest.class);
		classroomRequest = createMock(ClassroomRequest.class);
	}
	
	public void testShouldConstructCorrectly() {
		assertSame(reqHand.getInformationManager(), InformationManager.getInstance());
	}
	
	public void testCreateClassroomRequest() throws RequestException {
		Map<Resource, Integer> requiredResources = null;
		Map<Resource, Integer> optionalResources = null;
		Period desiredHours = null;
		Subject subject = null;
		Professor professor = null;
		ClassroomRequest req1 = new ClassroomRequest(desiredHours, subject, professor, 0, requiredResources, optionalResources, 25);
		ClassroomRequest req2 = reqHand.createClassroomRequest(requiredResources, optionalResources, desiredHours, subject, professor, 25);
		assertEquals(req1.getRequiredResources(), req2.getRequiredResources());
		assertEquals(req1.getOptionalResources(), req2.getOptionalResources());
		assertEquals(req1.getDesiredHours(), req2.getDesiredHours());
		assertEquals(req1.getSubject(), req2.getSubject());
		assertEquals(req1.getProfessor(), req2.getProfessor());
	}
	
	public void testCreatePositiveCapacityClassroomRequest(){
		Map<Resource, Integer> requiredResources = null;
		Map<Resource, Integer> optionalResources = null;
		Period desiredHours = null;
		Subject subject = null;
		Professor professor = null;
		int capacity = -10;
		try {
			reqHand.createClassroomRequest(requiredResources, optionalResources, desiredHours, subject, professor, capacity);
			fail("La capacidad debe ser positiva");
		}
		catch (RequestException e){}
	}
	
	public void testDeleteRequest(){
		InformationManager.getInstance().addRequest(classroomRequest);
		reqHand.deleteRequest(classroomRequest);
		assertFalse(InformationManager.getInstance().getRequests().contains(classroomRequest));
	}
	
	public void testModifyRequestDesiredHours() {
		Period desiredHours = null;
		mobileResourcesRequest.setDesiredHours(desiredHours);
		expectLastCall();
		replay(mobileResourcesRequest);
		reqHand.modifyRequest(mobileResourcesRequest, desiredHours);
		verify(mobileResourcesRequest);
	}
	
	public void testModifyRequestCapacity() throws RequestException {
		int capacity = 10;
		classroomRequest.setCapacity(capacity);
		expectLastCall();
		replay(classroomRequest);
		reqHand.modifyRequest(classroomRequest, capacity);
		verify(classroomRequest);
	}
	
	public void testModifyPositiveRequestCapacity() throws RequestException {
		int capacity = -10;
		try {
			reqHand.modifyRequest(classroomRequest, capacity);
			fail("La capacidad debe ser positiva");
		}
		catch (RequestException e){}
	}
	
	public void testModifyRequestRequiredResources() throws RequestException {
		Map<Resource, Integer> requiredResources = null;
		mobileResourcesRequest.setRequiredResources(requiredResources);
		expectLastCall();
		replay(mobileResourcesRequest);
		reqHand.modifyRequestRequiredResources(mobileResourcesRequest, requiredResources);
		verify(mobileResourcesRequest);
	}
	
	public void testModifyRequestOptionalResources() throws RequestException {
		Map<Resource, Integer> optionalResources = null;
		mobileResourcesRequest.setOptionalResources(optionalResources);
		expectLastCall();
		replay(mobileResourcesRequest);
		reqHand.modifyRequestOptionalResources(mobileResourcesRequest, optionalResources);
		verify(mobileResourcesRequest);
	}
	
}
