package ar.edu.unq.sasa.util;

/**
 * Interfaz parte de la implementación del patrón Observer.
 */
public interface Subscriber {
	
	void update(String aspect, Object value);
}
