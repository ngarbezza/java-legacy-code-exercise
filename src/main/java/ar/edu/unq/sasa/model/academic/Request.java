package ar.edu.unq.sasa.model.academic;

import java.util.Map;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public abstract class Request {

	public Period desiredHours;

	public Subject subject;

	public Professor professor;

	public final long id;

	// TODO model requirement, and make optional and required polymorphic
	public Map<Resource, Integer> requiredResources;

	public Map<Resource, Integer> optionalResources;

	private boolean asignated = false;

	public Request(Period aPeriod, Subject aSubject, Professor aProfessor, long anID,
			Map<Resource, Integer> reqResources, Map<Resource, Integer> optResources) {
		desiredHours = aPeriod;
		subject = aSubject;
		professor = aProfessor;
		id = anID;
		requiredResources = reqResources;
		optionalResources = optResources;
	}

	public void setDesiredHours(Period aPeriod) {
		desiredHours = aPeriod;
	}

	public void setRequiredResources(Map<Resource, Integer> reqResources) {
		requiredResources = reqResources;
	}

	public void setOptionalResources(Map<Resource, Integer> optResources) {
		optionalResources = optResources;
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
		return id;
	}

	public Map<Resource, Integer> getRequiredResources() {
		return requiredResources;
	}

	public Map<Resource, Integer> getOptionalResources() {
		return optionalResources;
	}

	public boolean isAsignated() {
		return asignated;
	}

	public void setAsignated(Boolean estado) {
		asignated = estado;
	}

	public abstract boolean isClassroomRequest();

	public boolean getAsignated() {
		return isAsignated();
	}

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
