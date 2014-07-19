package ar.edu.unq.sasa.model.time.hour;

import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;

/**
 * Representa todo lo referente a condiciones de intervalos flexibles de horas.
 * 
 * @author Nahuel Garbezza
 * 
 */
public abstract class LogicalHourFulfiller {
	
	/**
	 * Verifica si un {@link Timestamp} está incluido en el receptor.
	 * 
	 * @param t
	 *            el {@link Timestamp} a verificar.
	 * @return true si está incluido, false en caso contrario.
	 */
	public abstract boolean contains(Timestamp t);

	/**
	 * Verifica si un {@link LogicalHourFulfiller} está incluido en el receptor.
	 * 
	 * @param lhf
	 *            el {@link LogicalHourFulfiller} a verificar.
	 * @return true si está incluido, false en caso contrario.
	 */
	public abstract boolean contains(LogicalHourFulfiller lhf);

	/**
	 * Verifica si un {@link LogicalHourFulfiller} intersecta en algún momento con el
	 * receptor.
	 * 
	 * @param lhf
	 *            el {@link LogicalHourFulfiller} a verificar.
	 * @return true si existe la intersección, false en caso contrario.
	 */
	public abstract boolean intersectsWith(LogicalHourFulfiller lhf);

	protected abstract boolean intersectsWithHourInterval(HourInterval hi);

	/**
	 * Verifica si el receptor está incluido en un {@link HourInterval} recibido.
	 * 
	 * @param hi
	 *            el {@link HourInterval} a verificar.
	 * @return true si está incluido, false en caso contrario.
	 */
	protected abstract boolean isIn(HourInterval hi);

	/**
	 * Retorna una colección de {@link HourInterval}, que va a estar formada 
	 * por todas las posibilidades de {@link HourInterval} concretos (en donde 
	 * la duración especificada coincide con el rango horario). En otras palabras,
	 * divide al objeto receptor en intervalos concretos, por ejemplo: si el objeto
	 * receptor indica un rango de dos horas y la duración especificada es de una
	 * hora, y teniendo 30 minutos de bloque mínimo horario, el resultado es de 3 
	 * intervalos concretos de exactamente una hora cada uno. 
	 * 
	 * @return
	 * @throws PeriodException
	 *             si en la creación de Timestamps se lanza una excepción.
	 */
	public abstract List<HourInterval> getConcreteIntervals()
			throws PeriodException;

	/**
	 * Retorna un entero que representa a los minutos que se superponen entre 2
	 * {@link LogicalHourFulfiller}s. Si por cualquier motivo no existe la
	 * superposición, se retorna 0.
	 * 
	 * @param hf
	 *            el {@link LogicalHourFulfiller} para comparar.
	 * @return la cantidad de minutos de superposición, y 0 si no existe.
	 */
	public abstract int minutesSharedWith(LogicalHourFulfiller hf);

	protected abstract int minutesSharedWithHourInterval(HourInterval hi);

	/**
	 * @return una copia del objeto receptor
	 * @throws PeriodException 
	 */
	public abstract LogicalHourFulfiller copy() throws PeriodException;

	/**
	 * Verifica que un {@link LogicalHourFulfiller} sea concreto.
	 * 
	 * @return true si es concreto, false en caso contrario.
	 */
	public abstract boolean isConcrete();
}