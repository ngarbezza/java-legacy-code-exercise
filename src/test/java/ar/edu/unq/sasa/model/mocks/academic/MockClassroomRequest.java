package ar.edu.unq.sasa.model.mocks.academic;

import java.util.Map;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class MockClassroomRequest extends ClassroomRequest {

	public MockClassroomRequest(Period desHours, Subject subject,
			Professor professor, int anID, Map<Resource, Integer> reqResources,
			Map<Resource, Integer> optResources, int capacity)
			throws RequestException {
		super(desHours, subject, professor, anID, reqResources, optResources, capacity);
	}
}
