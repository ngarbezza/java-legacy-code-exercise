package ar.edu.unq.sasa.model.time;

import static ar.edu.unq.sasa.model.time.CalendarUtils.compareEquals;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Or;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.repetition.None;
import ar.edu.unq.sasa.model.time.repetition.Repetition;

public class SimplePeriod extends Period {

	private final Calendar start;

	private final Repetition repetition;

	private LogicalHourFulfiller hourFulfiller;

	public SimplePeriod(LogicalHourFulfiller lhf, Calendar start) {
		this(lhf, start, new None());
	}

	public SimplePeriod(LogicalHourFulfiller lhf, Calendar start, Repetition rep) {
		this.hourFulfiller = lhf;
		this.start = start;
		this.repetition = rep;
	}

	public LogicalHourFulfiller getHourFulfiller() {
		return hourFulfiller;
	}

	@Override
	public void setHourFulfiller(LogicalHourFulfiller hf) {
		this.hourFulfiller = hf;
	}

	public Calendar getStart() {
		return start;
	}

	public Repetition getRepetition() {
		return repetition;
	}

	public void addHourCondition(LogicalHourFulfiller lhf) {
		this.setHourFulfiller(new Or(getHourFulfiller(), lhf));
	}

	@Override
	public boolean contains(Calendar c) {
		Timestamp t;
		try {
			t = new Timestamp(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
		} catch (TimestampException e) {
			throw new PeriodException("Timestamp failed : " + e.getMessage());
		}
		return containsDate(c) && getHourFulfiller().contains(t);
	}

	public boolean containsDate(Calendar c) {
		return (compareEquals(getStart(), c) ||
				getRepetition().containsInSomeRepetition(c, getStart()));
	}

	@Override
	public boolean contains(Period p) {
		return p.isIn(this);
	}

	@Override
	public boolean intersectsWith(Period p) {
		return p.intersectsWithSimple(this);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod sp) {
		return (sp.containsDate(getStart()) || getRepetition().thereIsSomeDayIn(sp, getStart()))
			&& sp.getHourFulfiller().intersectsWith(getHourFulfiller());
	}

	@Override
	protected boolean isIn(SimplePeriod sp) {
		return sp.containsDate(getStart()) && getRepetition().isAllDaysIn(sp, getStart())
			&& sp.getHourFulfiller().contains(getHourFulfiller());
	}

	@Override
	public List<Period> convertToConcrete() {
		List<Period> result = new LinkedList<Period>();
		for (HourInterval hi : getHourFulfiller().getConcreteIntervals())
			result.add(new SimplePeriod(hi, getStart(), getRepetition()));
		return result;
	}

	@Override
	public boolean isConcrete() {
		return getHourFulfiller().isConcrete();
	}

	@Override
	public int minutesSharedWithPeriod(Period p) {
		return p.minutesSharedWithSimplePeriod(this);
	}

	@Override
	protected int minutesSharedWithSimplePeriod(SimplePeriod sp) {
		return sp.intersectsWithSimple(this)?
				getHourFulfiller().minutesSharedWith(sp.getHourFulfiller()) : 0;
	}

	@Override
	public String toString() {
		return getRepetition().toString(getStart()) + ", " + getHourFulfiller();
	}

	@Override
	public SimplePeriod copy() {
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

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		if (containsDate(c))
			intervals.add((HourInterval) getHourFulfiller());
		return intervals;
	}
}
