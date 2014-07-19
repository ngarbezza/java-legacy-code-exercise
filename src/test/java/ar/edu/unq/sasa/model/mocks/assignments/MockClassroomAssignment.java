package ar.edu.unq.sasa.model.mocks.assignments;

import java.util.List;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.items.Classroom;

public class MockClassroomAssignment extends ClassroomAssignment{

	public MockClassroomAssignment(ClassroomRequest classroomRequest,
			Classroom classroom,
			List<ResourceAssignment> resourcesAssignmentsList) {
		super(classroomRequest, classroom, resourcesAssignmentsList);
	}

}
