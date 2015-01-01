package ar.edu.unq.sasa.model.exceptions.time;

/**
 * Representa casos excepcionales para las clases que implementan las
 * condiciones de días y horas.
 */
public class PeriodException extends RuntimeException {

	private static final long serialVersionUID = 1331312009696466523L;

	public PeriodException(String message) {
		super(message);
	}
}
