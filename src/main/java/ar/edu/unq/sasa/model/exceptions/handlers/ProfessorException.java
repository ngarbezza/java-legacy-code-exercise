package ar.edu.unq.sasa.model.exceptions.handlers;

/**
 * Representa casos excepcionales que tengan que ver con profesores.
 */
public class ProfessorException extends Exception {

	private static final long serialVersionUID = -7269939005481652905L;

	public ProfessorException(String msg) {
		super(msg);
	}
}
