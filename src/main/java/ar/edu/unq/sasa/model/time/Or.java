package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * Operador que sirve para combinar {@link Period}'s por medio de
 * la condición lógica O (or).
 * 
 * @author Nahuel Garbezza
 * 
 */
public class Or extends CompositePeriod {

	/**
	 * Constructor de Or.
	 * 
	 * @param left
	 *            el primer operando.
	 * @param right
	 *            el otro operando.
	 */
	public Or(Period left, Period right) {
		super(left, right);
	}

	/**
	 * @see sasa.model.time.Period#contains(sasa.model.time.Period)
	 */
	@Override
	public boolean contains(Period p) throws PeriodException {
		return getLeftPeriod().contains(p) || getRightPeriod().contains(p);
	}

	/**
	 * @see sasa.model.time.Period#contains(java.util.Calendar)
	 */
	@Override
	public boolean contains(Calendar c) throws PeriodException {
		return getLeftPeriod().contains(c) || getRightPeriod().contains(c);
	}

	/**
	 * @see sasa.model.time.Period#intersectsWith(sasa.model.time.Period)
	 */
	@Override
	public boolean intersectsWith(Period p) throws PeriodException {
		return getLeftPeriod().intersectsWith(p) 
			|| getRightPeriod().intersectsWith(p);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod simple) throws PeriodException {
		return intersectsWith(simple);
	}
	
	@Override
	protected boolean isIn(SimplePeriod sdf) throws PeriodException {
		return getLeftPeriod().isIn(sdf) || getRightPeriod().isIn(sdf);
	}

	/**
	 * @throws PeriodException 
	 * @see sasa.model.time.Period#copy()
	 */
	@Override
	public Or copy() throws PeriodException {
		return new Or(getLeftPeriod().copy(), getRightPeriod().copy());
	}

	/**
	 * @see sasa.model.time.Period#convertToConcrete()
	 */
	@Override
	public List<Period> convertToConcrete() throws PeriodException {
		List<Period> result = new LinkedList<Period>();
		result.addAll(getLeftPeriod().convertToConcrete());
		result.addAll(getRightPeriod().convertToConcrete());
		return result;
	}
	
	@Override
	public boolean isConcrete() {
		return false;
	}	

	@Override
	public String toString() {
		return "( " + getLeftPeriod() + " ) \n O \n( " + getRightPeriod() + " )\n";
	}

	/**
	 * En un {@link Or} no es posible determinar los intervalos horarios 
	 * (pues no genera intervalos concretos).
	 * 
	 * @see sasa.model.time.Period#hourIntervalsInADay(java.util.Calendar)
	 */
	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
		throw new UnsupportedOperationException();
	}
}