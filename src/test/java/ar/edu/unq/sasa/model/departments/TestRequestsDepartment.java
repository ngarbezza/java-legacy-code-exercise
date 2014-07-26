package ar.edu.unq.sasa.model.departments;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.MobileResourcesRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.data.University;
import ar.edu.unq.sasa.model.departments.RequestsDepartment;
import ar.edu.unq.sasa.model.exceptions.departments.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class TestRequestsDepartment {
	private RequestsDepartment requestsDepartment;
	private MobileResourcesRequest mobileResourcesRequest;
	private ClassroomRequest classroomRequest;
	private University university;

	@Before
	public void setUp() {
		university = new University();
		requestsDepartment = new RequestsDepartment(university);
		mobileResourcesRequest = createMock(MobileResourcesRequest.class);
		classroomRequest = createMock(ClassroomRequest.class);
	}

	@Test
	public void testCreateClassroomRequest() throws RequestException {
		Map<Resource, Integer> requiredResources = null;
		Map<Resource, Integer> optionalResources = null;
		Period desiredHours = null;
		Subject subject = null;
		Professor professor = null;
		ClassroomRequest req1 = new ClassroomRequest(desiredHours, subject, professor, 0, requiredResources, optionalResources, 25);
		ClassroomRequest req2 = requestsDepartment.createClassroomRequest(requiredResources, optionalResources, desiredHours, subject, professor, 25);
		assertEquals(req1.getRequiredResources(), req2.getRequiredResources());
		assertEquals(req1.getOptionalResources(), req2.getOptionalResources());
		assertEquals(req1.getDesiredHours(), req2.getDesiredHours());
		assertEquals(req1.getSubject(), req2.getSubject());
		assertEquals(req1.getProfessor(), req2.getProfessor());
	}

	@Test
	public void testCreatePositiveCapacityClassroomRequest(){
		Map<Resource, Integer> requiredResources = null;
		Map<Resource, Integer> optionalResources = null;
		Period desiredHours = null;
		Subject subject = null;
		Professor professor = null;
		int capacity = -10;
		try {
			requestsDepartment.createClassroomRequest(requiredResources, optionalResources, desiredHours, subject, professor, capacity);
			fail("La capacidad debe ser positiva");
		}
		catch (RequestException e){}
	}

	@Test
	public void testDeleteRequest(){
		university.addRequest(classroomRequest);
		requestsDepartment.deleteRequest(classroomRequest);
		assertFalse(requestsDepartment.getRequests().contains(classroomRequest));
	}

	@Test
	public void testModifyRequestDesiredHours() {
		Period desiredHours = null;
		mobileResourcesRequest.setDesiredHours(desiredHours);
		expectLastCall();
		replay(mobileResourcesRequest);
		requestsDepartment.modifyRequest(mobileResourcesRequest, desiredHours);
		verify(mobileResourcesRequest);
	}

	@Test
	public void testModifyRequestCapacity() throws RequestException {
		int capacity = 10;
		classroomRequest.setCapacity(capacity);
		expectLastCall();
		replay(classroomRequest);
		requestsDepartment.modifyRequest(classroomRequest, capacity);
		verify(classroomRequest);
	}

	@Test
	public void testModifyPositiveRequestCapacity() throws RequestException {
		int capacity = -10;
		try {
			requestsDepartment.modifyRequest(classroomRequest, capacity);
			fail("La capacidad debe ser positiva");
		}
		catch (RequestException e){}
	}

	@Test
	public void testModifyRequestRequiredResources() throws RequestException {
		Map<Resource, Integer> requiredResources = null;
		mobileResourcesRequest.setRequiredResources(requiredResources);
		expectLastCall();
		replay(mobileResourcesRequest);
		requestsDepartment.modifyRequestRequiredResources(mobileResourcesRequest, requiredResources);
		verify(mobileResourcesRequest);
	}

	@Test
	public void testModifyRequestOptionalResources() throws RequestException {
		Map<Resource, Integer> optionalResources = null;
		mobileResourcesRequest.setOptionalResources(optionalResources);
		expectLastCall();
		replay(mobileResourcesRequest);
		requestsDepartment.modifyRequestOptionalResources(mobileResourcesRequest, optionalResources);
		verify(mobileResourcesRequest);
	}

}
