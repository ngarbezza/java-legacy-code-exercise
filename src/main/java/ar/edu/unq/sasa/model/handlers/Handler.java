package ar.edu.unq.sasa.model.handlers;

import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.util.Publisher;

/**
 * Es una clase abstracta que contiene los métodos comunes entre los 
 * distintos Handler, es decir, contiene los métodos de alta, baja, 
 * modificación y consulta de los elementos que estén bajo su responsabilidad.
 * 
 * @author Cristian Suarez
 *
 */
public abstract class Handler {
	
	public InformationManager getInformationManager() {
		return InformationManager.getInstance();
	}

	private final Publisher publisher = new Publisher();
	
	public Publisher getPublisher() {
		return publisher;
	}
}
