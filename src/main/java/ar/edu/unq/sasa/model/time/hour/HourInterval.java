package ar.edu.unq.sasa.model.time.hour;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Representa un intervalo de horas, igual que un intervalo matemático
 * "cerrado".
 * 
 * @author Nahuel Garbezza
 * 
 */
public class HourInterval extends LogicalHourFulfiller {

	/**
	 * Horarios de inicio y fin del intervalo.
	 */
	private final Timestamp start, end;

	/**
	 * La cantidad de minutos dentro del rango especificado.
	 */
	private final int minutesInRange;

	/**
	 * Constructor de {@link HourInterval}, que no recibe la cantidad
	 * de minutos, y la setea como toda la duración entre inicio y fin.
	 * Esto hace que con este constructor se armen {@link HourInterval}
	 * concretos.
	 * 
	 * @param start la hora inicial.
	 * @param end la hora final.
	 * @throws PeriodException 
	 */
	public HourInterval(Timestamp start, Timestamp end) throws PeriodException {
		this(start, end, start.minutesBetween(end));
	}
	
	/**
	 * Constructor de {@link HourInterval}.
	 * 
	 * @param start la hora inicial.
	 * @param end la hora final.
	 * @param range la cantidad de minutos del rango.
	 * @throws PeriodException 
	 */
	public HourInterval(Timestamp start, Timestamp end, int range) throws PeriodException {
		if (start.greaterEqual(end))
			throw new PeriodException("La hora inicial debe ser menor a la final");
		if (range > start.minutesBetween(end))
			throw new PeriodException("La cantidad de horas en el rango es incorrecta");
		this.minutesInRange = range;
		this.start = start;
		this.end = end;
	}

	public Timestamp getStart() {
		return start;
	}

	public Timestamp getEnd() {
		return end;
	}
	
	public int getMinutesInRange() {
		return minutesInRange;
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#contains(sasa.model.time.hour.Timestamp)
	 */
	@Override
	public boolean contains(Timestamp t) {
		return t.greaterEqual(getStart()) && t.lessEqual(getEnd());
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#contains(sasa.model.time.hour.LogicalHourFulfiller)
	 */
	@Override
	public boolean contains(LogicalHourFulfiller lhf) {
		return lhf.isIn(this);
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#isIn(sasa.model.time.hour.HourInterval)
	 */
	@Override
	protected boolean isIn(HourInterval hi) {
		return getStart().greaterEqual(hi.getStart()) && getEnd().lessEqual(hi.getEnd());
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#intersectsWith(sasa.model.time.hour.LogicalHourFulfiller)
	 */
	@Override
	public boolean intersectsWith(LogicalHourFulfiller lhf) {
		return lhf.intersectsWithHourInterval(this);
	}

	/**
	 * Compara el receptor con otro {@link HourInterval}, chequeando por 
	 * intersección.
	 * 
	 * @param hi
	 *            el otro HourInterval.
	 * @return true si hay intersección, false en caso contrario.
	 */
	@Override
	protected boolean intersectsWithHourInterval(HourInterval hi) {
		return !(getStart().greaterEqual(hi.getEnd()) || getEnd().lessEqual(hi.getStart()));
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#getConcreteIntervals()
	 */
	@Override
	public List<HourInterval> getConcreteIntervals() throws PeriodException {
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		try {
			Timestamp currentStart = getStart();
			Timestamp currentEnd = getStart().add(getMinutesInRange());

			while (getEnd().greaterEqual(currentEnd)) {
				intervals.add(new HourInterval(currentStart, currentEnd));
				currentStart = currentStart.add(Period.MIN_HOUR_BLOCK);
				currentEnd = currentEnd.add(Period.MIN_HOUR_BLOCK);
			}
		} catch (TimestampException e) {
			throw new PeriodException("Timestamp failed : " + e.getMessage());
		}
		return intervals;
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#minutesSharedWith(sasa.model.time.hour.LogicalHourFulfiller)
	 */
	@Override
	public int minutesSharedWith(LogicalHourFulfiller hf) {
		return hf.minutesSharedWithHourInterval(this);
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#minutesSharedWith(sasa.model.time.hour.HourInterval)
	 */
	@Override
	protected int minutesSharedWithHourInterval(HourInterval interval) {
		if (!this.intersectsWith(interval))
			return 0;
		else
			if (getStart().greaterEqual(interval.getStart()))
				if (getEnd().greaterEqual(interval.getEnd()))
					return getStart().minutesBetween(interval.getEnd());
				else
					return getStart().minutesBetween(getEnd());
			else
				if (getEnd().greaterEqual(interval.getEnd()))
					return interval.getStart().minutesBetween(interval.getEnd());
				else
					return interval.getStart().minutesBetween(getEnd());
	}

	/**
	 * Retorna una copia del objeto receptor.
	 * 
	 * @return
	 * @throws PeriodException 
	 */
	@Override
	public HourInterval copy() throws PeriodException {
		return new HourInterval(getStart(), getEnd());
	}
	
	@Override
	public boolean isConcrete() {
		return getMinutesInRange() == getStart().minutesBetween(getEnd());
	}
	
	@Override
	public String toString() {
		return "\n con " + (float) getMinutesInRange() / 60 + " horas de duración entre las " + getStart() + " y las " + getEnd();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HourInterval))
			return false;
		HourInterval other = (HourInterval) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	public HourInterval substract(HourInterval hr) throws PeriodException {
		if (getEnd().lessEqual(hr.getStart()) || hr.getEnd().lessEqual(getStart()))
			return null;		// disjuntos
		if (getStart().greaterEqual(hr.getStart()) && getEnd().lessEqual(hr.getEnd()))
			return null;		// resta completa
		if (hr.getStart().greaterEqual(getStart()))
			if (hr.getEnd().greaterEqual(getEnd()))
				return new HourInterval(getStart(), hr.getStart());
			else
				return hr;
		else
			if (hr.getEnd().lessEqual(getEnd()))
				return new HourInterval(hr.getEnd(), getEnd());
		return null;
	}
}