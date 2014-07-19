package ar.edu.unq.sasa.gui.util;

/**
 * Representa un conversor de {@link Object} a {@link String}.
 * 
 * @author Nahuel Garbezza
 *
 */
public class ObjectToStringConverter {
	
	/**
	 * Comportamiento por defecto.
	 * 
	 * @param obj
	 * @return
	 */
	public String convert(Object obj) {
		return obj.toString();
	}
}
