package ar.edu.unq.sasa.gui.period;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
import ar.edu.unq.sasa.model.time.CalendarUtils;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;
import ar.edu.unq.sasa.model.time.repetition.EndingRepetition;
import ar.edu.unq.sasa.model.time.repetition.None;
import ar.edu.unq.sasa.model.time.repetition.Repetition;

/**
 * @author Nahuel Garbezza
 *
 */
public class SimplePeriodTreeNode extends PeriodTreeNode {
	
	private Calendar startDate = new GregorianCalendar();
	private Repetition repetition = new None();
	private Timestamp endHour;
	private Timestamp startHour;
	private int minutesInRange;
	
	public SimplePeriodTreeNode() throws TimestampException {
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
	
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setRepetition(Repetition repetition) {
		this.repetition = repetition;
	}

	public Repetition getRepetition() {
		return repetition;
	}

	public void setStartHour(Timestamp startHour) {
		this.startHour = startHour;
	}

	public Timestamp getStartHour() {
		return startHour;
	}

	public void setEndHour(Timestamp endHour) {
		this.endHour = endHour;
	}

	public Timestamp getEndHour() {
		return endHour;
	}

	public Period makePeriod() throws PeriodException {
		if (!(getRepetition() instanceof None))
			if (!CalendarUtils.compareGreater(((EndingRepetition)getRepetition()).getEnd(), getStartDate()))
				throw new PeriodException("La fecha de finalización debe ser posterior a la de inicio");
		return new SimplePeriod(new HourInterval(getStartHour(), getEndHour(), 
				getMinutesInRange()), getStartDate(), getRepetition());
	}
	
	@Override
	public String getDisplayText() {
		return "Condición Simple";
	}

	public void setMinutesInRange(int minutesInRange) {
		this.minutesInRange = minutesInRange;
	}

	public int getMinutesInRange() {
		return minutesInRange;
	}

	@Override
	public boolean matchPeriodType(boolean simple, boolean or, boolean and,
			boolean minus) {
		return simple;
	}

	@Override
	public void updateChanges(NewPeriodWindow pw) {
		pw.updateChangesFromSimple(this);		
	}
}
