package ar.edu.unq.sasa.model.exceptions.handlers;

/**
 * Representa casos excepcionales que tengan que ver con aulas.
 */
public class ClassroomException extends Exception {

	private static final long serialVersionUID = 2573760846099487712L;

	public ClassroomException(String msg) {
		super(msg);
	}
}
