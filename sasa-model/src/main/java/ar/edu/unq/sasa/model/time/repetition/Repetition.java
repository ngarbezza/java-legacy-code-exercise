package ar.edu.unq.sasa.model.time.repetition;

import ar.edu.unq.sasa.model.time.SimplePeriod;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class Repetition {

    public abstract Boolean containsInSomeRepetition(Calendar c, Calendar start);

    public abstract Boolean thereIsSomeDayIn(SimplePeriod sp, Calendar start);

    public abstract Boolean isAllDaysIn(SimplePeriod sp, Calendar start);

    public abstract Repetition copy();

    public Boolean isNone() {
        return false;
    }

    public String toString(Calendar start) {
        return "El día " + new SimpleDateFormat("dd/MM/yyyy").format(start.getTime())
                + getRepetitionText();
    }

    protected abstract String getRepetitionText();
}
