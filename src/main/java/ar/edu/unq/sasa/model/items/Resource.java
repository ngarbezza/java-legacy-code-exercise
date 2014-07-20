package ar.edu.unq.sasa.model.items;

/**
 * Interfaz que representa recursos en el sistema.
 */
public interface Resource {

	String getName();
	
	void setName(String newName);
	
	boolean isFixedResource();
}
