package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

import ar.edu.unq.sasa.model.time.SimplePeriod;

public class None extends Repetition {

	@Override
	public Boolean containsInSomeRepetition(Calendar aDate, Calendar startDate) {
		return false;
	}

	@Override
	public Boolean isAllDaysIn(SimplePeriod aSimplePeriod, Calendar startDate) {
		return true;
	}

	@Override
	public Boolean thereIsSomeDayIn(SimplePeriod aSimplePeriod, Calendar startDate) {
		return false;
	}

	@Override
	public Boolean isNone() {
		return true;
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
