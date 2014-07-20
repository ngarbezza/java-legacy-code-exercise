package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

/**
 * Repetición mensual de un evento.
 */
public class Monthly extends EndingRepetition {

	public Monthly(Calendar end) {
		super(end);
	}

	@Override
	protected Calendar getNextDate(Calendar c) {
		Calendar newDate = (Calendar) c.clone();
		newDate.add(Calendar.MONTH, 1);
		return newDate;
	}
	
	@Override
	protected String getRepetitionText() {
		return super.getRepetitionText() + " con repetición mensual";
	}
	
	@Override
	public Monthly copy() {
		return new Monthly((Calendar) getEnd().clone());
	}
}
