package ar.edu.unq.sasa.model.time.hour;

import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;

/**
 * Operador lógico Or, para condiciones de horas.
 * 
 * @author Nahuel Garbezza
 * 
 */
public class Or extends LogicalHourFulfiller {

	/**
	 * Los operandos del Or.
	 */
	private final LogicalHourFulfiller leftOp, rightOp;

	/**
	 * Constructor de Or.
	 * 
	 * @param left
	 *            un operando.
	 * @param right
	 *            el otro operando.
	 */
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

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#contains(sasa.model.time.hour.Timestamp)
	 */
	@Override
	public boolean contains(Timestamp t) {
		return getLeftOp().contains(t) || getRightOp().contains(t);
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#contains(sasa.model.time.hour.LogicalHourFulfiller)
	 */
	@Override
	public boolean contains(LogicalHourFulfiller lhf) {
		return getLeftOp().contains(lhf) || getRightOp().contains(lhf);
	}

	@Override
	protected boolean isIn(HourInterval hi) {
		return getLeftOp().isIn(hi) || getRightOp().isIn(hi);
	}

	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#intersectsWith(sasa.model.time.hour.LogicalHourFulfiller)
	 */
	@Override
	public boolean intersectsWith(LogicalHourFulfiller lhf) {
		return getLeftOp().intersectsWith(lhf) || getRightOp().intersectsWith(lhf);
	}

	@Override
	protected boolean intersectsWithHourInterval(HourInterval interval) {
		return intersectsWith(interval);
	}
	
	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#getConcreteIntervals()
	 */
	@Override
	public List<HourInterval> getConcreteIntervals() throws PeriodException {
		List<HourInterval> result = getLeftOp().getConcreteIntervals();
		result.addAll(getRightOp().getConcreteIntervals());
		return result;
	}

	/**
	 * Revisa por intersecciones en ambos operandos y retorna la intersección 
	 * de mayor valor (en minutos).
	 * 
	 * @see sasa.model.time.hour.LogicalHourFulfiller#minutesSharedWith(sasa.model.time.hour.LogicalHourFulfiller)
	 */
	@Override
	public int minutesSharedWith(LogicalHourFulfiller hf) {
		return Math.max(getLeftOp().minutesSharedWith(hf), 
				getRightOp().minutesSharedWith(hf));
	}
	
	@Override
	protected int minutesSharedWithHourInterval(HourInterval interval) {
		return this.minutesSharedWith(interval);
	}
	
	/**
	 * @throws PeriodException 
	 * @see sasa.model.time.hour.LogicalHourFulfiller#copy()
	 */
	@Override
	public Or copy() throws PeriodException {
		return new Or(getLeftOp().copy(), getRightOp().copy());
	}
	
	/**
	 * @see sasa.model.time.hour.LogicalHourFulfiller#isConcrete()
	 */
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