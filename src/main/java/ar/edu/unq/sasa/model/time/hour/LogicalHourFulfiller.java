package ar.edu.unq.sasa.model.time.hour;

import java.util.List;

/**
 * Representa todo lo referente a condiciones de intervalos flexibles de horas.
 */
public abstract class LogicalHourFulfiller {

	public abstract boolean contains(Timestamp t);

	public abstract boolean contains(LogicalHourFulfiller lhf);

	public abstract boolean intersectsWith(LogicalHourFulfiller lhf);

	protected abstract boolean intersectsWithHourInterval(HourInterval hi);

	protected abstract boolean isIn(HourInterval hi);

	public abstract List<HourInterval> getConcreteIntervals();

	public abstract int minutesSharedWith(LogicalHourFulfiller hf);

	protected abstract int minutesSharedWithHourInterval(HourInterval hi);

	public abstract LogicalHourFulfiller copy();

	public abstract boolean isConcrete();
}