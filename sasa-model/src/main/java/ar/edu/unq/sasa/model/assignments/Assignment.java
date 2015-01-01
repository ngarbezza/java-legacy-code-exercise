package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.items.AssignableItem;

/**
 * Interfaz que representa una asignación.
 */
public interface Assignment {

	// TODO conocer el Period?

	// TODO conocer recursos asignados?

	AssignableItem getAssignableItem();

	Request getRequest();

	boolean isBookedAssignment();

	boolean isClassroomAssignment();

	boolean isResourceAssignment();
}
