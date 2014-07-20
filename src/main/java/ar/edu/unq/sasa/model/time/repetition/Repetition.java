package ar.edu.unq.sasa.model.time.repetition;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Clase abstracta que representa un tipo de repetición particular para un
 * {@link SimplePeriod}.
 */
public abstract class Repetition {

	public abstract boolean containsInSomeRepetition(Calendar c, Calendar start);

	public abstract boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start) throws PeriodException;

	public abstract boolean isAllDaysIn(SimplePeriod sp, Calendar start) throws PeriodException;

	public abstract Repetition copy();

	public String toString(Calendar start) {
		return "El día " + new SimpleDateFormat("dd/MM/yyyy").format(start.getTime())
			+ getRepetitionText();
	}

	protected abstract String getRepetitionText();
}