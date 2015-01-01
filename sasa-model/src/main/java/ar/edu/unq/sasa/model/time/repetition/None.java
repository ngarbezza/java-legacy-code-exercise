package ar.edu.unq.sasa.model.time.repetition;

import ar.edu.unq.sasa.model.time.SimplePeriod;

import java.util.Calendar;

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
