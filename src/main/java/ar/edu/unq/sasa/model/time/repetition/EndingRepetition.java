package ar.edu.unq.sasa.model.time.repetition;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Clase abstracta que representa aquellas repeticiones que requieren una fecha
 * de finalización.
 * 
 * @author Nahuel Garbezza
 * 
 */
public abstract class EndingRepetition extends Repetition {

	/**
	 * La fecha de finalización.
	 */
	private Calendar end;

	/**
	 * Constructor de EndingRepetition.
	 * 
	 * @param end
	 *            fecha de finalización de la repetición.
	 */
	public EndingRepetition(Calendar end) {
		this.end = end;
	}

	public Calendar getEnd() {
		return end;
	}

	/**
	 * @see sasa.model.time.repetition.Repetition#containsInSomeRepetition(java.util.Calendar, java.util.Calendar)
	 */
	@Override
	public boolean containsInSomeRepetition(Calendar c, Calendar start) {
		if (isOutOfBounds(c, start))
			return false;
		Calendar current = start;
		while (hasNextDate(current)) {
			current = getNextDate(current);
			if (compareEquals(c, current))
				return true;
		}
		return false;
	}

	/**
	 * @see sasa.model.time.repetition.Repetition#thereIsSomeDayIn(sasa.model.time.SimplePeriod, java.util.Calendar)
	 */
	@Override
	public boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start) 
			throws PeriodException {
		Calendar current = start;
		while (hasNextDate(current)) {
			current = getNextDate(current);
			if (sp.containsDate(current))
				return true;
		}
		return false;
	}

	/**
	 * @see sasa.model.time.repetition.Repetition#isAllDaysIn(sasa.model.time.SimplePeriod, java.util.Calendar)
	 */
	@Override
	public boolean isAllDaysIn(SimplePeriod sp, Calendar start) 
			throws PeriodException {
		Calendar current = start;
		while (hasNextDate(current)) {
			current = getNextDate(current);
			if (!sp.containsDate(current))
				return false;
		}
		return true;
	}

	/**
	 * Verifica si una fecha está dentro del rango en donde se lleva a cabo la
	 * repetición.
	 * 
	 * @param c
	 *            la fecha a verificar.
	 * @param startDate
	 *            la fecha inicial de la repetición.
	 * @return true si la fecha c está fuera de rango, false en caso contrario.
	 */
	protected boolean isOutOfBounds(Calendar c, Calendar start) {
		return compareLess(c, start) || compareEquals(c, start) 
			||compareGreater(c, getEnd());
	}

	/**
	 * Verifica si la fecha siguiente a la dada existe en la repetición.
	 * 
	 * @param c
	 *            la fecha a verificar.
	 * @return true si existe la fecha siguiente, false en caso contrario.
	 */
	protected boolean hasNextDate(Calendar c) {
		return !compareGreater(getNextDate(c), getEnd());
	}

	/**
	 * Obtiene la fecha siguiente en la repetición con respecto a una fecha
	 * dada.
	 * 
	 * @param c
	 *            la fecha de referencia.
	 * @return la fecha próxima a c, dependiendo del tipo de repetición.
	 */
	protected abstract Calendar getNextDate(Calendar c);

	@Override
	protected String getRepetitionText() {
		return " hasta el " + new SimpleDateFormat("dd/MM/yyyy").format(getEnd().getTime());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EndingRepetition other = (EndingRepetition) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
	}
}
