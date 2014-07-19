package ar.edu.unq.sasa.model.assignments;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * {@link Assignment} que se utiliza para representar asignaciones hechas a un
 * {@link Classroom}. Conoce además las asignaciones que se hicieron a los 
 * {@link Resource}s relacionados al ClassroomAssignment del cual fue
 * generada esta ClassroomAssignment.
 * 
 * @author Cristian Suarez
 */
public class ClassroomAssignment extends AssignmentByRequest {
	
	private Classroom classroom;
	
	private Satisfaction satisfaction;
	
	private List<ResourceAssignment> resourcesAssignments;
	
	private Map<Resource, Integer> tempSatisfactionResources = new HashMap<Resource, Integer>();

	public ClassroomAssignment(ClassroomRequest classroomRequest, Classroom aClassroom, List<ResourceAssignment> resourcesAssignmentsList) {
		super(classroomRequest);
		classroom = aClassroom;
		resourcesAssignments = resourcesAssignmentsList; 
	}

	/**
	 * Crea una {@link Satisfaction} para el {@link ClassroomRequest} y el 
	 * {@link Classroom} de esta asignación.
	 * 
	 * @return Satisfaction
	 * @throws PeriodException 
	 */
	public Satisfaction createSatisfaction() throws PeriodException {
		int capacityDifference = classroom.getCapacity() - this.getRequest().getCapacity();
		
		tempSatisfactionResources.putAll(this.getRequest().getRequiredResources());
		tempSatisfactionResources.putAll(this.getRequest().getOptionalResources());
		
		substractResources();
		substractZeros();
		
		Period period = obtainPeriod();
		Map<Period, Float> PeriodSuperpositions = createPeriodSuperpositions(period);
		
		satisfaction = new Satisfaction(tempSatisfactionResources, PeriodSuperpositions, capacityDifference);
		return satisfaction;
	}

	private void substractResources() {
		for (ResourceAssignment rAssignment : resourcesAssignments){
			for (Entry<Resource, Integer> entry : tempSatisfactionResources.entrySet()){
				if (entry.getKey().getName().equals(rAssignment.getAssignableItem().getName())){
					tempSatisfactionResources.put(entry.getKey(), entry.getValue()-1);
					break;
				}
			}
		}
		
		for (FixedResource fixedResource : classroom.getResources()){
			for (Entry<Resource, Integer> entry : tempSatisfactionResources.entrySet()){
				if (entry.getKey().getName().equals(fixedResource.getName())) {
					int subs = entry.getValue() - fixedResource.getAmount();
					tempSatisfactionResources.put(entry.getKey(), subs);
					break;
				}
			}
		}
	}

	public void substractZeros() {
		try {
			for (Entry<Resource, Integer> entry : tempSatisfactionResources.entrySet()) {
				if (entry.getValue() <= 0) {
					tempSatisfactionResources.remove(entry.getKey());
				}
			}
		} catch (ConcurrentModificationException e) {
			substractZeros();
		}
	}

	private Map<Period, Float> createPeriodSuperpositions(Period period) throws PeriodException {
		Map<Period, Float> periodSuperpositions = new HashMap<Period, Float>();
		float hours;
		for (Entry<Period, Assignment> entryClass : classroom.getAssignments().entrySet()){
			if (! entryClass.getValue().equals(this)){
				if (entryClass.getKey().intersectsWith(period)){
					hours = entryClass.getKey().minutesSharedWithPeriod(period) / 60;
					periodSuperpositions.put(entryClass.getKey(), hours);
				}
			}
		}
		return periodSuperpositions;
	}

	private Period obtainPeriod() {
		Period period = null;
		for (Entry<Period, Assignment> entryClass : classroom.getAssignments().entrySet()){
			if (entryClass.getValue().equals(this)){
				period = entryClass.getKey();
				break;
			}
		}
		return period;
	}
	
	public void setAssignableItem(Classroom aClassroom){
		classroom = aClassroom;
	}
	
	public Satisfaction getSatisfaction(){
		return satisfaction;
	}
	
	public List<ResourceAssignment> getResourcesAssignments(){
		return resourcesAssignments;
	}
	
	@Override
	public Classroom getAssignableItem() {
		return classroom;
	}

	public ClassroomRequest getRequest() {
		return (ClassroomRequest) super.getRequest();
	}

	/** 
	 * Para hacer Double Dispatching
	 */
	@Override
	public boolean isBookedAssignment() {
		return false;
	}
	
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
}