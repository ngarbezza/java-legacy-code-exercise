package ar.edu.unq.sasa.model.exceptions.handlers;

import ar.edu.unq.sasa.model.items.Resource;

/**
 * Clase que cuelga de Exception que sirve para representar excepciones
 * de los {@link Resource}.
 */
public class ResourceException extends Exception{

	private static final long serialVersionUID = -6980449779045511669L;

	public ResourceException(String message){
		super(message);
	}
}
