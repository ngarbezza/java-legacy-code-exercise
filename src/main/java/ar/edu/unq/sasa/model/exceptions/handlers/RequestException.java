package ar.edu.unq.sasa.model.exceptions.handlers;

import ar.edu.unq.sasa.model.academic.Request;

/**
 * Clase que cuelga de Exception que sirve para representar excepciones
 * de los {@link Request}.
 */
public class RequestException extends Exception {

	private static final long serialVersionUID = -6896894711717621482L;

	public RequestException(String message) {
		super(message);
	}
}
