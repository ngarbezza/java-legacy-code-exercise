package ar.edu.unq.sasa.model.time;

import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

import java.util.Calendar;
import java.util.List;

/**
 * Representa el conjunto de días y horas o intervalos de los mismos.
 * Es capaz de responder consultas y operaciones sobre estos conjuntos.
 * Ej: ver si el tiempo de un período se intersecta en algún momento con
 * el tiempo de otro.
 */
public abstract class Period {

    public static final int MIN_HOUR_BLOCK = 30;

    public abstract Boolean intersectsWith(Period aPeriod);

    protected abstract Boolean intersectsWithSimple(SimplePeriod aSimplePeriod);

    public abstract Boolean contains(Calendar aDate);

    public abstract Boolean contains(Period aPeriod);

    protected abstract Boolean isIn(SimplePeriod aSimplePeriod);

    public abstract List<Period> convertToConcrete();

    public abstract Boolean isConcrete();

    public abstract Integer minutesSharedWithPeriod(Period aPeriod);

    protected abstract Integer minutesSharedWithSimplePeriod(SimplePeriod aSimplePeriod);

    public abstract Period copy();

    public abstract void setHourFulfiller(LogicalHourFulfiller anHourFulfiller);

    public abstract List<HourInterval> hourIntervalsInADay(Calendar aDate);
}
