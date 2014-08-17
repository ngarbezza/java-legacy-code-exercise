package ar.edu.unq.sasa.model.academic;

import static ar.edu.unq.sasa.util.Preconditions.precondition;

import java.util.Map;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class ClassroomRequest extends Request {

	public Integer capacity;

	public ClassroomRequest(Period aPeriod, Subject aSubject, Professor aProfessor,
			long anID, Map<Resource, Integer> reqResources, Map<Resource, Integer> optResources, Integer aCapacity) {
		super(aPeriod, aSubject, aProfessor, anID, reqResources, optResources);

		precondition("Capacity should be positive", aCapacity > 0);
		capacity = aCapacity;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer aCapacity) {
		capacity = aCapacity;
	}

	@Override
	public Boolean isClassroomRequest() {
		return true;
	}
}
