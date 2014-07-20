package ar.edu.unq.sasa.model.academic;

import java.util.Map;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * MOBILERESOURCESREQUEST Class depicting a mobile resource/s request. Actually,
 * is only a concrete {@link Request}.
 * 
 * CONSIDERATIONS equals is overriden.
 */
public class MobileResourcesRequest extends Request {

	public MobileResourcesRequest(Period desHours,
			Subject aSubject, Professor aProfessor, long anID,
			Map<Resource, Integer> reqResources,
			Map<Resource, Integer> optResources) {
		super(desHours, aSubject, aProfessor, anID, reqResources, optResources);
	}

	@Override
	public boolean isClassroomRequest() {
		return false;		
	}
}
