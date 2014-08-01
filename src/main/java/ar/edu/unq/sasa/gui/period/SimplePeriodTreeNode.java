package ar.edu.unq.sasa.gui.period;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.CalendarUtils;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.repetition.EndingRepetition;
import ar.edu.unq.sasa.model.time.repetition.None;
import ar.edu.unq.sasa.model.time.repetition.Repetition;

public class SimplePeriodTreeNode extends PeriodTreeNode {

	private static final long serialVersionUID = 2148334845503954485L;

	private Calendar startDate = new GregorianCalendar();
	private Repetition repetition = new None();
	private Timestamp endHour;
	private Timestamp startHour;
	private int minutesInRange;

	public SimplePeriodTreeNode() {
		startDate = new GregorianCalendar();
		repetition = new None();
		endHour = new Timestamp(12, 0);
		startHour = new Timestamp(8, 0);
		minutesInRange = 240;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isCompositePeriodNode() {
		return false;
	}

	public void setStartDate(Calendar aDate) {
		startDate = aDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setRepetition(Repetition aRepetition) {
		repetition = aRepetition;
	}

	public Repetition getRepetition() {
		return repetition;
	}

	public void setStartHour(Timestamp aTimestamp) {
		startHour = aTimestamp;
	}

	public Timestamp getStartHour() {
		return startHour;
	}

	public void setEndHour(Timestamp aTimestamp) {
		endHour = aTimestamp;
	}

	public Timestamp getEndHour() {
		return endHour;
	}

	@Override
	public Period makePeriod() {
		if (!(getRepetition() instanceof None))
			if (!CalendarUtils.compareGreater(((EndingRepetition) repetition).getEnd(), startDate))
				throw new PeriodException("La fecha de finalización debe ser posterior a la de inicio");
		return new SimplePeriod(new HourInterval(startHour, endHour, minutesInRange), startDate, repetition);
	}

	@Override
	public String getDisplayText() {
		return "Condición Simple";
	}

	public void setMinutesInRange(int someMinutes) {
		minutesInRange = someMinutes;
	}

	public int getMinutesInRange() {
		return minutesInRange;
	}

	@Override
	public boolean matchPeriodType(boolean simple, boolean or, boolean and, boolean minus) {
		return simple;
	}

	@Override
	public void updateChanges(NewPeriodWindow pw) {
		pw.updateChangesFromSimple(this);
	}
}
