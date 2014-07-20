package ar.edu.unq.sasa.model.time;

import static ar.edu.unq.sasa.model.time.CalendarUtils.compareEquals;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.hour.Or;
import ar.edu.unq.sasa.model.time.repetition.None;
import ar.edu.unq.sasa.model.time.repetition.Repetition;

/**
 * @author Nahuel Garbezza
 *
 */
public class SimplePeriod extends Period {

	/**
	 * La fecha de inicio del {@link Period}.
	 */
	private final Calendar start;
	
	/**
	 * El tipo de repetición.
	 */
	private final Repetition repetition;
	
	/**
	 * La condición horaria.
	 */
	private LogicalHourFulfiller hourFulfiller;
	
	/**
	 * Constructor de {@link SimplePeriod}, que setea la repetición
	 * como {@link None}.
	 * 
	 * @param lhf la condición horaria.
	 * @param start la fecha inicial.
	 */
	public SimplePeriod(LogicalHourFulfiller lhf, Calendar start) {
		this(lhf, start, new None());
	}
	
	/**
	 * Constructor de {@link SimplePeriod}.
	 * 
	 * @param lhf la condición horaria.
	 * @param start la fecha inicial.
	 * @param rep la repetición.
	 */
	public SimplePeriod(LogicalHourFulfiller lhf, Calendar start, Repetition rep) {
		this.hourFulfiller = lhf;
		this.start = start;
		this.repetition = rep;
	}
	
	public LogicalHourFulfiller getHourFulfiller() {
		return hourFulfiller;
	}
	
	public void setHourFulfiller(LogicalHourFulfiller hf) {
		this.hourFulfiller = hf;
	}
	
	public Calendar getStart() {
		return start;
	}
	
	public Repetition getRepetition() {
		return repetition;
	}
	
	/**
	 * Agrega una condición horaria a la condición existente.
	 * 
	 * @param lhf
	 */
	public void addHourCondition(LogicalHourFulfiller lhf) {
		this.setHourFulfiller(new Or(getHourFulfiller(), lhf));
	}

	/**
	 * @see sasa.model.time.Period#contains(java.util.Calendar)
	 */
	@Override
	public boolean contains(Calendar c) throws PeriodException {
		Timestamp t;
		try {
			t = new Timestamp(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
		} catch (TimestampException e) {
			throw new PeriodException("Timestamp failed : " + e.getMessage());
		}
		return containsDate(c) && getHourFulfiller().contains(t);
	}

	/**
	 * Con el mismo propósito que {@link SimplePeriod#contains(Calendar)}, pero
	 * en este caso teniendo sólo en cuenta los días, no las horas.
	 * 
	 * @param c
	 * @return
	 */
	public boolean containsDate(Calendar c) {
		return (compareEquals(getStart(), c) || 
				getRepetition().containsInSomeRepetition(c, getStart()));
	}
	
	/**
	 * @see sasa.model.time.Period#contains(sasa.model.time.Period)
	 */
	@Override
	public boolean contains(Period p) throws PeriodException {
		return p.isIn(this);
	}

	/**
	 * @see sasa.model.time.Period#intersectsWith(sasa.model.time.Period)
	 */
	@Override
	public boolean intersectsWith(Period p) throws PeriodException {
		return p.intersectsWithSimple(this);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod sp) throws PeriodException {
		return (sp.containsDate(getStart()) || getRepetition().thereIsSomeDayIn(sp, getStart()))
			&& sp.getHourFulfiller().intersectsWith(getHourFulfiller());
	}

	@Override
	protected boolean isIn(SimplePeriod sp) throws PeriodException {
		return sp.containsDate(getStart()) && getRepetition().isAllDaysIn(sp, getStart())
			&& sp.getHourFulfiller().contains(getHourFulfiller());
	}

	/**
	 * @see sasa.model.time.Period#convertToConcrete()
	 */
	@Override
	public List<Period> convertToConcrete() throws PeriodException {
		List<Period> result = new LinkedList<Period>();
		for (HourInterval hi : getHourFulfiller().getConcreteIntervals())
			result.add(new SimplePeriod(hi, getStart(), getRepetition()));
		return result;
	}

	/**
	 * @see sasa.model.time.Period#isConcrete()
	 */
	@Override
	public boolean isConcrete() {
		return getHourFulfiller().isConcrete();
	}

	/**
	 * @see sasa.model.time.Period#minutesSharedWithPeriod(sasa.model.time.Period)
	 */
	@Override
	public int minutesSharedWithPeriod(Period p) throws PeriodException {
		return p.minutesSharedWithSimplePeriod(this);
	}

	@Override
	protected int minutesSharedWithSimplePeriod(SimplePeriod sp) throws PeriodException {
		return sp.intersectsWithSimple(this)? 
				getHourFulfiller().minutesSharedWith(sp.getHourFulfiller()) : 0;
	}
	
	@Override
	public String toString() {
		return getRepetition().toString(getStart()) + ", " + getHourFulfiller();
	}
	
	/**
	 * @throws PeriodException 
	 * @see sasa.model.time.Period#copy()
	 */
	@Override
	public SimplePeriod copy() throws PeriodException {
		return new SimplePeriod(getHourFulfiller().copy(), 
				(Calendar) getStart().clone(), getRepetition().copy());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hourFulfiller == null) ? 0 : hourFulfiller.hashCode());
		result = prime * result
				+ ((repetition == null) ? 0 : repetition.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimplePeriod))
			return false;
		SimplePeriod other = (SimplePeriod) obj;
		if (hourFulfiller == null) {
			if (other.hourFulfiller != null)
				return false;
		} else if (!hourFulfiller.equals(other.hourFulfiller))
			return false;
		if (repetition == null) {
			if (other.repetition != null)
				return false;
		} else if (!repetition.equals(other.repetition))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	/** 
	 * Asume que el {@link LogicalHourFulfiller} es un {@link HourInterval},
	 * pues este método está pensado para ejecutarse sobre períodos concretos.
	 * 
	 * @see sasa.model.time.Period#hourIntervalsInADay(java.util.Calendar)
	 */
	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		if (containsDate(c))
			intervals.add((HourInterval) getHourFulfiller());
		return intervals;
	}
}
