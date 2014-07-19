package ar.edu.unq.sasa.model.exceptions.handlers;

/**
 * Clase que cuelga de Exception que sirve para representar excepciones
 * de los {@link Request}.
 * 
 * @author Cristian Suarez
 * 
 */
public class RequestException extends Exception {
	
	public RequestException(String message) {
		super(message);
	}
}
