package ar.edu.unq.sasa.model.academic;

import java.util.Map;

import ar.edu.unq.sasa.model.exceptions.departments.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * CLASSROOMREQUEST Subclass of the abstract {@link Request}. It represents the
 * petition of a classroom, did by a {@link Professor}.
 * 
 * CONSIDERATIONS The only creational method is the huge constructor.
 */
public class ClassroomRequest extends Request {

	public int capacity;

	public ClassroomRequest(Period desHours, Subject aSubject,
			Professor aProfessor, long anID,
			Map<Resource, Integer> reqResources,
			Map<Resource, Integer> optResources, int aCapacity)
			throws RequestException {
		super(desHours, aSubject, aProfessor, anID, reqResources, optResources);

		if (aCapacity <= 0)
			throw new RequestException("Capacity given is not correct");
		this.capacity = aCapacity;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + capacity;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassroomRequest other = (ClassroomRequest) obj;
		if (capacity != other.capacity)
			return false;
		return true;
	}

	@Override
	public boolean isClassroomRequest() {
		return true;		
	}
}
