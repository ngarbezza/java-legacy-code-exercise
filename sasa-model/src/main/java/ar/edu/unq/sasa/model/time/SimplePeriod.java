package ar.edu.unq.sasa.model.time;

import static ar.edu.unq.sasa.model.time.CalendarUtils.compareEquals;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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

	public SimplePeriod(LogicalHourFulfiller lhf, Calendar aStartDate) {
		this(lhf, aStartDate, new None());
	}

	public SimplePeriod(LogicalHourFulfiller lhf, Calendar aStartDate, Repetition aRepetition) {
		hourFulfiller = lhf;
		start = aStartDate;
		repetition = aRepetition;
	}

	public LogicalHourFulfiller getHourFulfiller() {
		return hourFulfiller;
	}

	@Override
	public void setHourFulfiller(LogicalHourFulfiller anHourFulfiller) {
		hourFulfiller = anHourFulfiller;
	}

	public Calendar getStart() {
		return start;
	}

	public Repetition getRepetition() {
		return repetition;
	}

	public void addHourCondition(LogicalHourFulfiller anHourFulfiller) {
		setHourFulfiller(new Or(getHourFulfiller(), anHourFulfiller));
	}

	@Override
	public Boolean contains(Calendar aDate) {
		Timestamp timestamp = new Timestamp(aDate.get(Calendar.HOUR_OF_DAY), aDate.get(Calendar.MINUTE));
		return containsDate(aDate) && getHourFulfiller().contains(timestamp);
	}

	public boolean containsDate(Calendar aDate) {
		return (compareEquals(getStart(), aDate) || getRepetition().containsInSomeRepetition(aDate, getStart()));
	}

	@Override
	public Boolean contains(Period aPeriod) {
		return aPeriod.isIn(this);
	}

	@Override
	public Boolean intersectsWith(Period aPeriod) {
		return aPeriod.intersectsWithSimple(this);
	}

	@Override
	protected Boolean intersectsWithSimple(SimplePeriod aSimplePeriod) {
		return (aSimplePeriod.containsDate(getStart()) || getRepetition().thereIsSomeDayIn(aSimplePeriod, getStart()))
			&& aSimplePeriod.getHourFulfiller().intersectsWith(getHourFulfiller());
	}

	@Override
	protected Boolean isIn(SimplePeriod aSimplePeriod) {
		return aSimplePeriod.containsDate(getStart()) && getRepetition().isAllDaysIn(aSimplePeriod, getStart())
			&& aSimplePeriod.getHourFulfiller().contains(getHourFulfiller());
	}

	@Override
	public List<Period> convertToConcrete() {
		List<Period> result = new LinkedList<Period>();
		for (HourInterval hi : getHourFulfiller().getConcreteIntervals())
			result.add(new SimplePeriod(hi, getStart(), getRepetition()));
		return result;
	}

	@Override
	public Boolean isConcrete() {
		return getHourFulfiller().isConcrete();
	}

	@Override
	public Integer minutesSharedWithPeriod(Period aPeriod) {
		return aPeriod.minutesSharedWithSimplePeriod(this);
	}

	@Override
	protected Integer minutesSharedWithSimplePeriod(SimplePeriod aSimplePeriod) {
		return aSimplePeriod.intersectsWithSimple(this)
				? getHourFulfiller().minutesSharedWith(aSimplePeriod.getHourFulfiller()) : 0;
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
