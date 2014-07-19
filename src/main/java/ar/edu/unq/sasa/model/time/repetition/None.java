package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Representa la "no repetición", ésta se utiliza para eventos que sólo ocurren
 * una vez.
 * 
 * @author Nahuel Garbezza
 * 
 */
public class None extends Repetition {

	/**
	 * @see sasa.model.time.repetition.Repetition#containsInSomeRepetition(java.util.Calendar, java.util.Calendar)
	 */
	@Override
	public boolean containsInSomeRepetition(Calendar c, Calendar start) {
		return false;
	}

	/**
	 * @see sasa.model.time.repetition.Repetition#isAllDaysIn(sasa.model.time.SimplePeriod, java.util.Calendar)
	 */
	@Override
	public boolean isAllDaysIn(SimplePeriod sp, Calendar start) {
		return true;
	}

	/**
	 * @see sasa.model.time.repetition.Repetition#thereIsSomeDayIn(sasa.model.time.SimplePeriod, java.util.Calendar)
	 */
	@Override
	public boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start) {
		return false;
	}
	
	/**
	 * @see sasa.model.time.repetition.Repetition#copy()
	 */
	@Override
	public Repetition copy() {
		return new None();
	}

	@Override
	protected String getRepetitionText() {
		return "";
	}
}
