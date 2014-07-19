package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

/**
 * Representa el conjunto de días y horas o intervalos de los mismos. 
 * Es capaz de responder consultas y operaciones sobre estos conjuntos. 
 * Ej: ver si el tiempo de un período se intersecta en algún momento con 
 * el tiempo de otro.
 * 
 * @author Nahuel Garbezza
 *
 */
public abstract class Period {
	
	/**
	 * EL mínimo bloque horario (expresado en minutos).
	 */
	public static final int MIN_HOUR_BLOCK = 30;

	/**
	 * Verifica si el receptor y otro {@link Period} comparten algún momento.
	 * 
	 * @param p el otro {@link Period} a comparar.
	 * @return true si hay intersección, false en caso contrario.
	 * @throws PeriodException
	 */
	public abstract boolean intersectsWith(Period p) throws PeriodException;
	
	protected abstract boolean intersectsWithSimple(SimplePeriod sp) throws PeriodException;
	
	/**
	 * Verifica si el objeto receptor contiene una determinada fecha.
	 * 
	 * @param c la fecha a verificar.
	 * @return true si la fecha está incluida, false en caso contrario.
	 * @throws PeriodException
	 */
	public abstract boolean contains(Calendar c) throws PeriodException;
	
	/**
	 * Verifica si ej objeto receptor contiene en su totalidad a otro {@link Period}
	 * recibido como parámetro.
	 * 
	 * @param p el {@link Period} a verificar.
	 * @return true si el parámetro está totalmente incluido en el receptor, false en
	 * 	caso contrario.
	 * @throws PeriodException
	 */
	public abstract boolean contains(Period p) throws PeriodException;
	protected abstract boolean isIn(SimplePeriod sp) throws PeriodException;
	
	/**
	 * Retorna todas las combinaciones de {@link Period} concretos que se 
	 * pueden generar partiendo del receptor. Si el {@link Period} es concreto,
	 * entonces se lo retorna directamente.
	 * 
	 * @return
	 * @throws PeriodException
	 */
	public abstract List<Period> convertToConcrete() throws PeriodException;
	
	/**
	 * Verifica si un {@link Period} es concreto. Se dice que es concreto si 
	 * no hay ambigüedad a la hora de determinar alguna de sus condiciones.
	 * 
	 * @return true si es concreto, false en caso contrario.
	 */
	public abstract boolean isConcrete();
	
	/**
	 * Compara el receptor con un {@link Period} recibido como parámetro, 
	 * retornando la cantidad de minutos que comparten. En caso de compartir
	 * en más de un momento, se retorna el de mayor duración. En caso de no 
	 * compartir ningún momento, se retorna 0.
	 * 
	 * @param p el otro {@link Period} a verificar.
	 * @return
	 * @throws PeriodException
	 */
	public abstract int minutesSharedWithPeriod(Period p) throws PeriodException;
	protected abstract int minutesSharedWithSimplePeriod(SimplePeriod sp) throws PeriodException;
	
	/**
	 * @return una copia del objeto receptor.
	 * @throws PeriodException 
	 */
	public abstract Period copy() throws PeriodException;
	
	public abstract void setHourFulfiller(LogicalHourFulfiller hf);
	
	/**
	 * Retorna todos los posibles intervalos horarios concretos
	 * que se correspondan con una determinada fecha.
	 * 
	 * @param c la fecha en la que están los intervalos horarios.
	 * @return una lista con todos los intervalos horarios descriptos anteriormente.
	 * @throws PeriodException 
	 */
	public abstract List<HourInterval> hourIntervalsInADay(Calendar c) throws PeriodException;
}
