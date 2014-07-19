package ar.edu.unq.sasa.model.exceptions.handlers;

/**
 * Clase que cuelga de Exception que sirve para representar excepciones
 * de los {@link Resource}.
 * 
 * @author Cristian Suarez
 *
 */
public class ResourceException extends Exception{
	
	public ResourceException(String message){
		super(message);
	}
}
