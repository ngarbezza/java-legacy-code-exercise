package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.requests.Request;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertFalse;

public class TestRequestsDepartment {

    private RequestsDepartment requestsDepartment;

    @Before
    public void setUp() {
        requestsDepartment = new RequestsDepartment(new University());
    }

    @Test
    public void testDeleteRequest() {
        Request classroomRequest = new ClassroomRequest(null, null, null, 0, new HashSet<>(), 1);
        requestsDepartment.addRequest(classroomRequest);
        requestsDepartment.deleteRequest(classroomRequest);
        assertFalse(requestsDepartment.getRequests().contains(classroomRequest));
    }

}
