package ar.edu.unq.sasa.model.items;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.exceptions.departments.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Representa las aulas del sistema.
 */
public class Classroom extends AssignableItem {

	private int capacity;

	private List<FixedResource> resources;

	public Classroom(String name, int capacity) {
		super(name);
		this.capacity = capacity;
		this.resources = new LinkedList<FixedResource>();
	}

	public List<FixedResource> getResources() {
		return resources;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void addResource(FixedResource resource) {
		resources.add(resource);
	}

	public boolean hasResource(String name) {
		boolean retorno = false;
		for (int i = 0; i < resources.size(); i++)
			if (resources.get(i).getName() == name) {
				retorno = true;
				i = resources.size();
			}
		return retorno;
	}

	public FixedResource getResource(String name) throws ResourceException {
		FixedResource recursoBuscado = null;
		for (int i = 0; i < resources.size(); i++)
			if (resources.get(i).getName() == name) {
				recursoBuscado = resources.get(i);
				i = resources.size();
			}
		this.verificar(recursoBuscado);
		return recursoBuscado;
	}

	public void verificar(FixedResource resource) throws ResourceException {
		if (resource == null)
			throw new ResourceException("Recurso no encontrado");
	}

	public void verificar(Assignment assignment) throws ResourceException, AssignmentException {
    	if (assignment == null)
			throw new AssignmentException("Asignacion no encontrada");
    }

	public ClassroomAssignment getAssignment(Period period) throws ResourceException, AssignmentException {
		ClassroomAssignment retorno = null;
		retorno = (ClassroomAssignment) this.getAssignments().get(period);
		this.verificar(retorno);
		return retorno;
	}

	public boolean satisfyFixedResources(Map<FixedResource, Integer> resources) {
		for (Entry<FixedResource, Integer> res : resources.entrySet())
			if (!this.satisfyFixedResource(res.getKey(), res.getValue()))
				return false;
		return true;
	}

	public boolean satisfyFixedResource(FixedResource res, int amount) {
		// #anySatisfy:
		for (FixedResource r : resources)
			if (r.getName().equals(res.getName()) && r.getAmount() >= amount)
				return true;
		return false;
	}

	public List<ClassroomAssignment> getClassroomAssignments() {
		List<ClassroomAssignment> result = new LinkedList<ClassroomAssignment>();
		for (Assignment a : getAssignments().values())
			if (a.isClassroomAssignment())
				result.add((ClassroomAssignment) a);
		return result;
	}

	public List<BookedAssignment> getBookedAssignments() {
		List<BookedAssignment> result = new LinkedList<BookedAssignment>();
		for (Assignment a : getAssignments().values())
			if (a.isBookedAssignment())
				result.add((BookedAssignment) a);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + capacity;
		result = prime * result
				+ ((resources == null) ? 0 : resources.hashCode());
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
		Classroom other = (Classroom) obj;
		if (capacity != other.capacity)
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
	}
}