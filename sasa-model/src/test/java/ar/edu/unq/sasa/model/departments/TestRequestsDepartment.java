package ar.edu.unq.sasa.model.departments;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.University;

public class TestRequestsDepartment {

	private RequestsDepartment requestsDepartment;

	@Before
	public void setUp() {
		requestsDepartment = new RequestsDepartment(new University());
	}

	@Test
	public void testDeleteRequest() {
		Request classroomRequest = new ClassroomRequest(null, null, null, 0, null, null, 1);
		requestsDepartment.addRequest(classroomRequest);
		requestsDepartment.deleteRequest(classroomRequest);
		assertFalse(requestsDepartment.getRequests().contains(classroomRequest));
	}

}
