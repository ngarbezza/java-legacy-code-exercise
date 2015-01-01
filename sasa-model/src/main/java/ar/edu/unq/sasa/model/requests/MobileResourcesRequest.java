package ar.edu.unq.sasa.model.requests;

import java.util.Map;
import java.util.Set;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.time.Period;

public class MobileResourcesRequest extends Request {

	public MobileResourcesRequest(Period aPeriod,
			Subject aSubject, Professor aProfessor, long anID,
			Set<Requirement> listOfRequirements) {
		super(aPeriod, aSubject, aProfessor, anID, listOfRequirements);
	}

	@Override
	public Boolean isClassroomRequest() {
		return false;
	}
}
