package ar.edu.unq.sasa.model.time.hour;

import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;

/**
 * Operador lógico Or, para condiciones de horas.
 */
public class Or extends LogicalHourFulfiller {

	private final LogicalHourFulfiller leftOp, rightOp;

	public Or(LogicalHourFulfiller left, LogicalHourFulfiller right) {
		this.leftOp = left;
		this.rightOp = right;
	}

	public LogicalHourFulfiller getLeftOp() {
		return leftOp;
	}

	public LogicalHourFulfiller getRightOp() {
		return rightOp;
	}

	@Override
	public boolean contains(Timestamp t) {
		return getLeftOp().contains(t) || getRightOp().contains(t);
	}

	@Override
	public boolean contains(LogicalHourFulfiller lhf) {
		return getLeftOp().contains(lhf) || getRightOp().contains(lhf);
	}

	@Override
	protected boolean isIn(HourInterval hi) {
		return getLeftOp().isIn(hi) || getRightOp().isIn(hi);
	}

	@Override
	public boolean intersectsWith(LogicalHourFulfiller lhf) {
		return getLeftOp().intersectsWith(lhf) || getRightOp().intersectsWith(lhf);
	}

	@Override
	protected boolean intersectsWithHourInterval(HourInterval interval) {
		return intersectsWith(interval);
	}
	
	@Override
	public List<HourInterval> getConcreteIntervals() throws PeriodException {
		List<HourInterval> result = getLeftOp().getConcreteIntervals();
		result.addAll(getRightOp().getConcreteIntervals());
		return result;
	}

	@Override
	public int minutesSharedWith(LogicalHourFulfiller hf) {
		return Math.max(getLeftOp().minutesSharedWith(hf),
				getRightOp().minutesSharedWith(hf));
	}
	
	@Override
	protected int minutesSharedWithHourInterval(HourInterval interval) {
		return this.minutesSharedWith(interval);
	}
	
	@Override
	public Or copy() throws PeriodException {
		return new Or(getLeftOp().copy(), getRightOp().copy());
	}
	
	@Override
	public boolean isConcrete() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftOp == null) ? 0 : leftOp.hashCode());
		result = prime * result + ((rightOp == null) ? 0 : rightOp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Or other = (Or) obj;
		if (leftOp == null) {
			if (other.leftOp != null)
				return false;
		} else if (!leftOp.equals(other.leftOp))
			return false;
		if (rightOp == null) {
			if (other.rightOp != null)
				return false;
		} else if (!rightOp.equals(other.rightOp))
			return false;
		return true;
	}
}