package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

/**
 * Repetición diaria de un evento.
 */
public class Daily extends EndingRepetition {

    public Daily(Calendar end) {
        super(end);
    }

    @Override
    protected Calendar getNextDate(Calendar aDate) {
        Calendar result = (Calendar) aDate.clone();
        result.add(Calendar.DAY_OF_MONTH, 1);
        return result;
    }

    @Override
    public Daily copy() {
        return new Daily((Calendar) getEnd().clone());
    }

    @Override
    protected String getRepetitionText() {
        return super.getRepetitionText() + " con repetición diaria";
    }
}
