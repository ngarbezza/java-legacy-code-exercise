package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

/**
 * Repetición semanal de un evento.
 * 
 * @author Nahuel Garbezza
 *
 */
public class Weekly extends EndingRepetition {

	/**
	 * Constructor de Weekly.
	 * 
	 * @param end fecha de finalización de la repetición.
	 */
	public Weekly(Calendar end) {
		super(end);
	}

	@Override
	protected Calendar getNextDate(Calendar c) {
		Calendar newDate = (Calendar) c.clone();
		newDate.add(Calendar.DAY_OF_MONTH, 7);
		return newDate;
	}
	
	@Override
	protected String getRepetitionText() {
		return super.getRepetitionText() + " con repetición semanal";
	}
	
	/**
	 * @see sasa.model.time.repetition.Repetition#copy()
	 */
	@Override
	public Weekly copy() {
		return new Weekly((Calendar) getEnd().clone());
	}
}
