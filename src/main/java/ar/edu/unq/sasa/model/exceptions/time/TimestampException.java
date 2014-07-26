package ar.edu.unq.sasa.model.exceptions.time;

/**
 * Excepción generada por valores incorrectos en los objetos Timestamp.
 */
public class TimestampException extends Exception {

	// TODO make it a runtime one and remove all throws declarations

	private static final long serialVersionUID = 8986140088416369141L;

	public TimestampException(String message) {
		super(message);
	}
}
