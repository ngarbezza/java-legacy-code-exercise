package ar.edu.unq.sasa.model.time;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

/**
 * Representa un {@link Period} complejo, compuesto de dos
 * {@link Period} relacionados por algún operador lógico. 
 * 
 * @author Nahuel Garbezza
 *
 */
public abstract class CompositePeriod extends Period {

	/**
	 * Los operandos del Or.
	 */
	private final Period leftPeriod, rightPeriod; 
	
	/**
	 * Constructor de CompositePeriod.
	 * 
	 * @param left el primer operando.
	 * @param right el otro operando.
	 */
	public CompositePeriod(Period left, Period right) {
		this.leftPeriod = left;
		this.rightPeriod = right;
	}
	
	public Period getLeftPeriod() {
		return leftPeriod;
	}
	
	public Period getRightPeriod() {
		return rightPeriod;
	}
	
	/**
	 * @see sasa.model.time.Period#isConcrete()
	 */
	@Override
	public boolean isConcrete() {
		return getLeftPeriod().isConcrete() && getRightPeriod().isConcrete();
	}

	/**
	 * @see sasa.model.time.Period#minutesSharedWithPeriod(sasa.model.time.Period)
	 */
	@Override
	public int minutesSharedWithPeriod(Period p) throws PeriodException {
		return Math.max(getLeftPeriod().minutesSharedWithPeriod(p), getRightPeriod().minutesSharedWithPeriod(p));
	}
	
	@Override
	protected int minutesSharedWithSimplePeriod(SimplePeriod sp) throws PeriodException {
		return this.minutesSharedWithPeriod(sp);
	}
	
	@Override
	public void setHourFulfiller(LogicalHourFulfiller hf) {
		this.getLeftPeriod().setHourFulfiller(hf);
		this.getRightPeriod().setHourFulfiller(hf);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((leftPeriod == null) ? 0 : leftPeriod.hashCode());
		result = prime * result
		+ ((rightPeriod == null) ? 0 : rightPeriod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CompositePeriod))
			return false;
		CompositePeriod other = (CompositePeriod) obj;
		if (leftPeriod == null) {
			if (other.leftPeriod != null)
				return false;
		} else if (!leftPeriod.equals(other.leftPeriod))
			return false;
		if (rightPeriod == null) {
			if (other.rightPeriod != null)
				return false;
		} else if (!rightPeriod.equals(other.rightPeriod))
			return false;
		return true;
	}
}
