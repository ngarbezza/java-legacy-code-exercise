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
 */
public abstract class Period {
	
	public static final int MIN_HOUR_BLOCK = 30;

	public abstract boolean intersectsWith(Period p) throws PeriodException;
	
	protected abstract boolean intersectsWithSimple(SimplePeriod sp) throws PeriodException;
	
	public abstract boolean contains(Calendar c) throws PeriodException;
	
	public abstract boolean contains(Period p) throws PeriodException;
	
	protected abstract boolean isIn(SimplePeriod sp) throws PeriodException;
	
	public abstract List<Period> convertToConcrete() throws PeriodException;
	
	public abstract boolean isConcrete();
	
	public abstract int minutesSharedWithPeriod(Period p) throws PeriodException;
	
	protected abstract int minutesSharedWithSimplePeriod(SimplePeriod sp) throws PeriodException;
	
	public abstract Period copy() throws PeriodException;
	
	public abstract void setHourFulfiller(LogicalHourFulfiller hf);
	
	public abstract List<HourInterval> hourIntervalsInADay(Calendar c) throws PeriodException;
}
