package ar.edu.unq.sasa.model.exceptions.departments;

/**
 * Representa casos excepcionales en el ámbito de asignaciones.
 */
public class AssignmentException extends Exception {

	private static final long serialVersionUID = 6077532736630559771L;

	public AssignmentException(String message) {
		super(message);
	}
}
