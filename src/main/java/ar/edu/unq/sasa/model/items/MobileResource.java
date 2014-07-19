package ar.edu.unq.sasa.model.items;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;

/**
 * Representa aquellos recursos denominados "móviles" porque pueden ser asignados
 * al igual que las aulas.
 * 
 * @author Campos Diego
 */
public class MobileResource extends AssignableItem implements Resource {

	private final int id;
	
	public MobileResource(String name, int id) {
		super(name);
		this.id = id;
	}

	@Override
	public void setName(String newName) {
		super.setName(newName);
	}

	@Override
	public boolean isFixedResource() {
		return false;
	}

	public int getId() {
		return id;
	}

	public List<ResourceAssignment> getResourceAssignments() {
		List<ResourceAssignment> result = new LinkedList<ResourceAssignment>();
		for (Assignment a : getAssignments().values())
			if (a.isResourceAssignment())
				result.add((ResourceAssignment) a);
		return result;
	}
}
