package ar.edu.unq.sasa.model.time.hour;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Representa un intervalo de horas, igual que un intervalo matemático
 * "cerrado".
 */
public class HourInterval extends LogicalHourFulfiller {

	private final Timestamp start, end;

	private final int minutesInRange;

	public HourInterval(Timestamp aStartTimestamp, Timestamp anEndTimestamp) {
		this(aStartTimestamp, anEndTimestamp, aStartTimestamp.minutesBetween(anEndTimestamp));
	}

	public HourInterval(Timestamp aStartTimestamp, Timestamp anEndTimestamp, int someMinutes) {
		if (aStartTimestamp.greaterEqual(anEndTimestamp))
			throw new PeriodException("La hora inicial debe ser menor a la final");
		if (someMinutes > aStartTimestamp.minutesBetween(anEndTimestamp))
			throw new PeriodException("La cantidad de horas en el rango es incorrecta");
		minutesInRange = someMinutes;
		start = aStartTimestamp;
		end = anEndTimestamp;
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

	@Override
	public boolean contains(Timestamp t) {
		return t.greaterEqual(getStart()) && t.lessEqual(getEnd());
	}

	@Override
	public boolean contains(LogicalHourFulfiller lhf) {
		return lhf.isIn(this);
	}

	@Override
	protected boolean isIn(HourInterval hi) {
		return getStart().greaterEqual(hi.getStart()) && getEnd().lessEqual(hi.getEnd());
	}

	@Override
	public boolean intersectsWith(LogicalHourFulfiller lhf) {
		return lhf.intersectsWithHourInterval(this);
	}

	@Override
	protected boolean intersectsWithHourInterval(HourInterval hi) {
		return !(getStart().greaterEqual(hi.getEnd()) || getEnd().lessEqual(hi.getStart()));
	}

	@Override
	public List<HourInterval> getConcreteIntervals() {
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		Timestamp currentStart = getStart();
		Timestamp currentEnd = getStart().add(getMinutesInRange());

		while (getEnd().greaterEqual(currentEnd)) {
			intervals.add(new HourInterval(currentStart, currentEnd));
			currentStart = currentStart.add(Period.MIN_HOUR_BLOCK);
			currentEnd = currentEnd.add(Period.MIN_HOUR_BLOCK);
		}
		return intervals;
	}

	@Override
	public int minutesSharedWith(LogicalHourFulfiller hf) {
		return hf.minutesSharedWithHourInterval(this);
	}

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

	@Override
	public HourInterval copy() {
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

	public HourInterval substract(HourInterval hr) {
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