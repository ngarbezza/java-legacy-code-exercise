package ar.edu.unq.sasa.model.time.repetition;

import java.util.Calendar;

/**
 * Repetición semanal de un evento.
 */
public class Weekly extends EndingRepetition {

    public Weekly(Calendar end) {
        super(end);
    }

    @Override
    protected Calendar getNextDate(Calendar aDate) {
        Calendar newDate = (Calendar) aDate.clone();
        newDate.add(Calendar.DAY_OF_MONTH, 7);
        return newDate;
    }

    @Override
    protected String getRepetitionText() {
        return super.getRepetitionText() + " con repetición semanal";
    }

    @Override
    public Weekly copy() {
        return new Weekly((Calendar) getEnd().clone());
    }
}
