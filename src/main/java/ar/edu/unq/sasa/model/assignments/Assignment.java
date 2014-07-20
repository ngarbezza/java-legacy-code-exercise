package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.items.AssignableItem;

/**
 * Interfaz que sirve para englobar a todos los tipos de Assignment que hay.
 */
public interface Assignment {
	
	AssignableItem getAssignableItem();
	
	Request getRequest();
	
	boolean isBookedAssignment();

	boolean isClassroomAssignment();

	boolean isResourceAssignment();
}
