package ar.edu.unq.sasa.model.departments;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.exceptions.departments.RequestException;

public class TestRequestsDepartment {

	private RequestsDepartment requestsDepartment;
	private University university;

	@Before
	public void setUp() {
		university = new University();
		requestsDepartment = new RequestsDepartment(university);
	}

	@Test
	public void testDeleteRequest() throws RequestException {
		Request classroomRequest = new ClassroomRequest(null, null, null, 0, null, null, 1);
		university.addRequest(classroomRequest);
		requestsDepartment.deleteRequest(classroomRequest);
		assertFalse(requestsDepartment.getRequests().contains(classroomRequest));
	}

}
