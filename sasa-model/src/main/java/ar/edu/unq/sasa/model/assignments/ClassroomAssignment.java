package ar.edu.unq.sasa.model.assignments;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class ClassroomAssignment extends AssignmentByRequest {

	private Classroom classroom;

	private Satisfaction satisfaction;

	private List<ResourceAssignment> resourcesAssignments;

	private Map<Resource, Integer> tempSatisfactionResources = new HashMap<Resource, Integer>();

	public ClassroomAssignment(ClassroomRequest classroomRequest, Classroom aClassroom,
			List<ResourceAssignment> resourcesAssignmentsList) {
		super(classroomRequest);
		classroom = aClassroom;
		resourcesAssignments = resourcesAssignmentsList;
	}

	public Satisfaction createSatisfaction() {
		int capacityDifference = classroom.getCapacity() - getRequest().getCapacity();

		tempSatisfactionResources.putAll(getRequest().getRequiredResources());
		tempSatisfactionResources.putAll(getRequest().getOptionalResources());

		substractResources();
		substractZeros();

		Map<Period, Float> periodSuperpositions = createPeriodSuperpositions(getPeriod());

		satisfaction = new Satisfaction(tempSatisfactionResources, periodSuperpositions, capacityDifference);
		return satisfaction;
	}

	private void substractResources() {
		for (ResourceAssignment rAssignment : resourcesAssignments)
			for (Entry<Resource, Integer> entry : tempSatisfactionResources.entrySet())
				if (entry.getKey().getName().equals(rAssignment.getAssignableItem().getName())) {
					tempSatisfactionResources.put(entry.getKey(), entry.getValue() - 1);
					break;
				}

		for (FixedResource fixedResource : classroom.getResources())
			for (Entry<Resource, Integer> entry : tempSatisfactionResources.entrySet())
				if (entry.getKey().getName().equals(fixedResource.getName())) {
					int subs = entry.getValue() - fixedResource.getAmount();
					tempSatisfactionResources.put(entry.getKey(), subs);
					break;
				}
	}

	public void substractZeros() {
		try {
			for (Entry<Resource, Integer> entry : tempSatisfactionResources.entrySet())
				if (entry.getValue() <= 0)
					tempSatisfactionResources.remove(entry.getKey());
		} catch (ConcurrentModificationException e) {
			// TODO fix this
			substractZeros();
		}
	}

	private Map<Period, Float> createPeriodSuperpositions(Period period) {
		Map<Period, Float> periodSuperpositions = new HashMap<Period, Float>();
		float hours;
		for (Entry<Period, Assignment> entryClass : classroom.getAssignments().entrySet())
			if (!entryClass.getValue().equals(this))
				if (entryClass.getKey().intersectsWith(period)) {
					hours = entryClass.getKey().minutesSharedWithPeriod(period) / 60;
					periodSuperpositions.put(entryClass.getKey(), hours);
				}
		return periodSuperpositions;
	}

	public void setAssignableItem(Classroom aClassroom) {
		classroom = aClassroom;
	}

	public Satisfaction getSatisfaction() {
		return satisfaction;
	}

	public List<ResourceAssignment> getResourcesAssignments() {
		return resourcesAssignments;
	}

	@Override
	public Classroom getAssignableItem() {
		return classroom;
	}

	@Override
	public ClassroomRequest getRequest() {
		return (ClassroomRequest) super.getRequest();
	}

	@Override
	public boolean isBookedAssignment() {
		return false;
	}

	@Override
	public boolean isClassroomAssignment() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((resourcesAssignments == null) ? 0 : resourcesAssignments
						.hashCode());
		result = prime * result
				+ ((satisfaction == null) ? 0 : satisfaction.hashCode());
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
		ClassroomAssignment other = (ClassroomAssignment) obj;
		if (classroom == null) {
			if (other.classroom != null)
				return false;
		} else if (!classroom.equals(other.classroom))
			return false;
		if (resourcesAssignments == null) {
			if (other.resourcesAssignments != null)
				return false;
		} else if (!resourcesAssignments.equals(other.resourcesAssignments))
			return false;
		if (satisfaction == null) {
			if (other.satisfaction != null)
				return false;
		} else if (!satisfaction.equals(other.satisfaction))
			return false;
		return true;
	}

	@Override
	public boolean isResourceAssignment() {
		return false;
	}

	public Period getPeriod() {
		for (Entry<Period, Assignment> entry : getAssignableItem().getAssignments().entrySet())
			if (entry.getValue().equals(this))
				return entry.getKey();
		return null;
	}
}
