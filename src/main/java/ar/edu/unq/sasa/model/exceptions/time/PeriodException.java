package ar.edu.unq.sasa.model.exceptions.time;

/**
 * Representa casos excepcionales para las clases que implementan las
 * condiciones de días y horas.
 */
public class PeriodException extends Exception {

	public PeriodException(String message) {
		super(message);
	}
}
