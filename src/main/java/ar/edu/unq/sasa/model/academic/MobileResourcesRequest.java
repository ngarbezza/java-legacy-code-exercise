package ar.edu.unq.sasa.model.academic;

import java.util.Map;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class MobileResourcesRequest extends Request {

	public MobileResourcesRequest(Period aPeriod,
			Subject aSubject, Professor aProfessor, long anID,
			Map<Resource, Integer> reqResources,
			Map<Resource, Integer> optResources) {
		super(aPeriod, aSubject, aProfessor, anID, reqResources, optResources);
	}

	@Override
	public Boolean isClassroomRequest() {
		return false;
	}
}
