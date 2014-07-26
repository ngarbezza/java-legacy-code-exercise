package ar.edu.unq.sasa.model.time.repetition;

import static ar.edu.unq.sasa.model.time.CalendarUtils.compareEquals;
import static ar.edu.unq.sasa.model.time.CalendarUtils.compareGreater;
import static ar.edu.unq.sasa.model.time.CalendarUtils.compareLess;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Clase abstracta que representa aquellas repeticiones que requieren una fecha
 * de finalización.
 */
public abstract class EndingRepetition extends Repetition {

	private Calendar end;

	public EndingRepetition(Calendar end) {
		this.end = end;
	}

	public Calendar getEnd() {
		return end;
	}

	@Override
	public boolean containsInSomeRepetition(Calendar c, Calendar start) {
		if (isOutOfBounds(c, start))
			return false;
		Calendar current = start;
		while (hasNextDate(current)) {
			current = getNextDate(current);
			if (compareEquals(c, current))
				return true;
		}
		return false;
	}

	@Override
	public boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start) {
		Calendar current = start;
		while (hasNextDate(current)) {
			current = getNextDate(current);
			if (sp.containsDate(current))
				return true;
		}
		return false;
	}

	@Override
	public boolean isAllDaysIn(SimplePeriod sp, Calendar start) {
		Calendar current = start;
		while (hasNextDate(current)) {
			current = getNextDate(current);
			if (!sp.containsDate(current))
				return false;
		}
		return true;
	}

	protected boolean isOutOfBounds(Calendar c, Calendar start) {
		return compareLess(c, start) || compareEquals(c, start)
			||compareGreater(c, getEnd());
	}

	protected boolean hasNextDate(Calendar c) {
		return !compareGreater(getNextDate(c), getEnd());
	}

	protected abstract Calendar getNextDate(Calendar c);

	@Override
	protected String getRepetitionText() {
		return " hasta el " + new SimpleDateFormat("dd/MM/yyyy").format(getEnd().getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EndingRepetition other = (EndingRepetition) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
	}
}
