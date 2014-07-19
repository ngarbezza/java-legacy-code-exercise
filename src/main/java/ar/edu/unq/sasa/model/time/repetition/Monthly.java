package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

/**
 * Repetición mensual de un evento.
 * 
 * @author Nahuel Garbezza
 *
 */
public class Monthly extends EndingRepetition {

	/**
	 * Constructor de Monthly.
	 * 
	 * @param end la fecha de finalización.
	 */
	public Monthly(Calendar end) {
		super(end);
	}

	/**
	 * @see EndingRepetition#getNextDate(Calendar)
	 */
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
	
	/**
	 * @see sasa.model.time.repetition.Repetition#copy()
	 */
	@Override
	public Monthly copy() {
		return new Monthly((Calendar) getEnd().clone());
	}
}
