package ar.edu.unq.sasa.model.time.repetition;

import ar.edu.unq.sasa.model.time.SimplePeriod;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static ar.edu.unq.sasa.model.time.CalendarUtils.*;

public abstract class EndingRepetition extends Repetition {

    private Calendar end;

    public EndingRepetition(Calendar anEndDate) {
        end = anEndDate;
    }

    public Calendar getEnd() {
        return end;
    }

    @Override
    public Boolean containsInSomeRepetition(Calendar aDate, Calendar startDate) {
        if (isOutOfBounds(aDate, startDate))
            return false;
        Calendar current = startDate;
        while (hasNextDate(current)) {
            current = getNextDate(current);
            if (compareEquals(aDate, current))
                return true;
        }
        return false;
    }

    @Override
    public Boolean thereIsSomeDayIn(SimplePeriod aSimplePeriod, Calendar startDate) {
        Calendar current = startDate;
        while (hasNextDate(current)) {
            current = getNextDate(current);
            if (aSimplePeriod.containsDate(current))
                return true;
        }
        return false;
    }

    @Override
    public Boolean isAllDaysIn(SimplePeriod aSimplePeriod, Calendar startDate) {
        Calendar current = startDate;
        while (hasNextDate(current)) {
            current = getNextDate(current);
            if (!aSimplePeriod.containsDate(current))
                return false;
        }
        return true;
    }

    protected boolean isOutOfBounds(Calendar aDate, Calendar startDate) {
        return compareLess(aDate, startDate) || compareEquals(aDate, startDate) || compareGreater(aDate, end);
    }

    protected boolean hasNextDate(Calendar aDate) {
        return !compareGreater(getNextDate(aDate), end);
    }

    protected abstract Calendar getNextDate(Calendar aDate);

    @Override
    protected String getRepetitionText() {
        return " hasta el " + new SimpleDateFormat("dd/MM/yyyy").format(end.getTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + end.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        EndingRepetition other = (EndingRepetition) obj;
        return getClass() == obj.getClass() && end.equals(other.end);
    }
}
