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

	// TODO model status?
	private Boolean asignated = false;

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

	public Boolean isAsignated() {
		return asignated;
	}

	public void setAsignated(Boolean estado) {
		asignated = estado;
	}

	public abstract Boolean isClassroomRequest();

	public Boolean getAsignated() {
		return isAsignated();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (int) (id ^ (id >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		Request other = (Request) obj;
		return getClass() == other.getClass() && id == other.id;
	}
}
