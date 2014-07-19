package ar.edu.unq.sasa.model.time.repetition;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Clase abstracta que representa un tipo de repetición particular para un
 * {@link SimplePeriod}.
 * 
 * @author Nahuel Garbezza
 * 
 */
public abstract class Repetition {

	/**
	 * Verifica si alguna repetición partiendo de una fecha es igual a otra
	 * fecha, recibida como parámetro.
	 * 
	 * @param c
	 *            la fecha a verificar.
	 * @param start
	 *            la fecha inicial, referencia para la repetición.
	 * @return true si la fecha c es alguna repetición, false en caso contrario.
	 */
	public abstract boolean containsInSomeRepetition(Calendar c, Calendar start);

	/**
	 * Dados una fecha y un {@link SimplePeriod}, no dice si alguna
	 * repetición de esa fecha está incluida en el {@link SimplePeriod},
	 * sin contar la fecha inicial.
	 * 
	 * @param sdf
	 *            {@link SimplePeriod} a verificar.
	 * @param start
	 *            fecha desde donde se empieza a contar la repetición
	 * @return true si alguna fecha cumple con la condición, false en caso
	 *         contrario.
	 * @throws PeriodException 
	 */
	public abstract boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start) throws PeriodException;

	/**
	 * Verifica si todos los días determinados por esta repetición están
	 * incluidos en un {@link SimplePeriod} recibido como parámetro.
	 * 
	 * @param sdf
	 *            el {@link SimplePeriod} para verificar.
	 * @param start
	 *            la fecha desde donde se empieza a contar la repetición.
	 * @return true si se cumple la condición descripta anteriormente, y false
	 *         en caso contrario.
	 * @throws PeriodException 
	 */
	public abstract boolean isAllDaysIn(SimplePeriod sp, Calendar start) throws PeriodException;

	/**
	 * @return una copia del objeto receptor.
	 */
	public abstract Repetition copy();

	/**
	 * Ayuda a resolver el {@link Period#toString()}. 
	 * 
	 * @param start
	 * @return
	 */
	public String toString(Calendar start) {
		return "El día " + new SimpleDateFormat("dd/MM/yyyy").format(start.getTime()) 
			+ getRepetitionText();
	}

	protected abstract String getRepetitionText();
}