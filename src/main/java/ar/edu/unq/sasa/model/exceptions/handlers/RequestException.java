package ar.edu.unq.sasa.model.exceptions.handlers;

import ar.edu.unq.sasa.model.academic.Request;

/**
 * Clase que cuelga de Exception que sirve para representar excepciones
 * de los {@link Request}.
 */
public class RequestException extends Exception {
	
	public RequestException(String message) {
		super(message);
	}
}
