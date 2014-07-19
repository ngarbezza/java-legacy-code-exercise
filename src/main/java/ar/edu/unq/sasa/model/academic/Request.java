package ar.edu.unq.sasa.model.academic;

import java.util.Map;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/** 
 * Abstract class REQUEST
 * Class depicting every request a {@link Professor} can do. It contains some 
 * information from the request, and another interest topics. 
 * 
 * CONSIDERATIONS The huge constructor is the only one allowed in instance creation.
 * 
 * @author Gaston Charkiewicz
 * 
 */
public abstract class Request {

	/** 
	 * Desired hour timestamp from the actual request.
	 */
	public Period desiredHours;
	
	/** 
	 * The subject that is going to be teached according to the request.
	 */
	public Subject subject;
	
	/** 
	 * The issuing Professor of the Request.
	 */
	public Professor professor;
	
	/** 
	 * ID of the Request (final because no changes are allowed).
	 */
	public final long id;
	
	/** 
	 * Required resources in the Request.
	 */
	public Map<Resource,Integer> requiredResources;
	
	/**
	 * Para saber si el request esta asignado o no. Por default no lo está.
	 */
	private boolean asignated = false;
	
	/** 
	 * Optional resources of the Request.
	 */
	public Map<Resource,Integer> optionalResources;
	
	public Request (Period desHours,Subject aSubject,
			Professor aProfessor,long anID,Map<Resource,Integer> reqResources,
			Map<Resource,Integer> optResources){
		
		this.desiredHours = desHours;
		this.subject = aSubject;
		this.professor = aProfessor;
		this.id = anID;
		this.requiredResources = reqResources;
		this.optionalResources = optResources;
	}
	
	/**
	 * @param desiredHours the desiredHours to set
	 */
	public void setDesiredHours(Period desiredHours) {
		this.desiredHours = desiredHours;
	}
	
	/**
	 * @param requiredResources the requiredResources to set
	 */
	public void setRequiredResources(Map<Resource, Integer> requiredResources) {
		this.requiredResources = requiredResources;
	}
	
	/**
	 * @param optionalResources the optionalResources to set
	 */
	public void setOptionalResources(Map<Resource, Integer> optionalResources) {
		this.optionalResources = optionalResources;
	}

	public Period getDesiredHours() {
		return desiredHours;
	}

	public Subject getSubject() {
		return subject;
	}

	public Professor getProfessor() {
		return professor;
	}
	
	public long getId() {
		return this.id;
	}

	public Map<Resource, Integer> getRequiredResources() {
		return requiredResources;
	}

	public Map<Resource, Integer> getOptionalResources() {
		return optionalResources;
	}
	
	public boolean isAsignated(){
		return asignated;
	}
	
	public void setAsignated(Boolean estado){
		asignated = estado;
	}
	
	public abstract boolean isClassroomRequest();
	
	public boolean getAsignated() {
		return isAsignated();
	}

	/** hashCode redefinition
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((desiredHours == null) ? 0 : desiredHours.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime
				* result
				+ ((optionalResources == null) ? 0 : optionalResources
						.hashCode());
		result = prime * result
				+ ((professor == null) ? 0 : professor.hashCode());
		result = prime
				* result
				+ ((requiredResources == null) ? 0 : requiredResources
						.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	/** Equals redefinition
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (desiredHours == null) {
			if (other.desiredHours != null)
				return false;
		} else if (!desiredHours.equals(other.desiredHours))
			return false;
		if (id != other.id)
			return false;
		if (optionalResources == null) {
			if (other.optionalResources != null)
				return false;
		} else if (!optionalResources.equals(other.optionalResources))
			return false;
		if (professor == null) {
			if (other.professor != null)
				return false;
		} else if (!professor.equals(other.professor))
			return false;
		if (requiredResources == null) {
			if (other.requiredResources != null)
				return false;
		} else if (!requiredResources.equals(other.requiredResources))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
}
