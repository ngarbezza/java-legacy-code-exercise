package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

/**
 * Repetición diaria de un evento.
 * 
 * @author Nahuel Garbezza
 * 
 */
public class Daily extends EndingRepetition {

	/**
	 * Constructor de Daily.
	 * 
	 * @param end
	 *            fecha de finalización de la repetición.
	 */
	public Daily(Calendar end) {
		super(end);
	}

	@Override
	protected Calendar getNextDate(Calendar c) {
		Calendar result = (Calendar) c.clone();
		result.add(Calendar.DAY_OF_MONTH, 1);
		return result;
	}

	/**
	 * @see sasa.model.time.repetition.Repetition#copy()
	 */
	@Override
	public Daily copy() {
		return new Daily((Calendar) getEnd().clone());
	}

	@Override
	protected String getRepetitionText() {
		return super.getRepetitionText() + " con repetición diaria";
	}
}
