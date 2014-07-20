package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

import ar.edu.unq.sasa.model.time.SimplePeriod;

/**
 * Representa la "no repetición", ésta se utiliza para eventos que sólo ocurren
 * una vez.
 */
public class None extends Repetition {

	@Override
	public boolean containsInSomeRepetition(Calendar c, Calendar start) {
		return false;
	}

	@Override
	public boolean isAllDaysIn(SimplePeriod sp, Calendar start) {
		return true;
	}

	@Override
	public boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start) {
		return false;
	}
	
	@Override
	public Repetition copy() {
		return new None();
	}

	@Override
	protected String getRepetitionText() {
		return "";
	}
}
