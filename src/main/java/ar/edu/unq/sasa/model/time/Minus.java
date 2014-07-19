package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * Representa la resta de conjuntos aplicada a los {@link Period}.
 * 
 * @author Nahuel Garbezza
 * 
 */
public class Minus extends CompositePeriod {

	/**
	 * Constructor de Minus.
	 * 
	 * @param left
	 *            el primer operando.
	 * @param right
	 *            el otro operando.
	 */
	public Minus(Period left, Period right) {
		super(left, right);
	}

	/**
	 * @see sasa.model.time.Period#contains(sasa.model.time.Period)
	 */
	@Override
	public boolean contains(Period p) throws PeriodException {
		return getLeftPeriod().contains(p) && !getRightPeriod().contains(p);
	}

	/**
	 * @see sasa.model.time.Period#contains(java.util.Calendar)
	 */
	@Override
	public boolean contains(Calendar c) throws PeriodException {
		return getLeftPeriod().contains(c) && !getRightPeriod().contains(c);
	}

	/**
	 * @see sasa.model.time.Period#intersectsWith(sasa.model.time.Period)
	 */
	@Override
	public boolean intersectsWith(Period p) throws PeriodException {
		return getLeftPeriod().intersectsWith(p) 
			&& !getRightPeriod().intersectsWith(p);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod simple) throws PeriodException {
		return this.intersectsWith(simple);
	}
	
	@Override
	protected boolean isIn(SimplePeriod sdf) throws PeriodException {
		return getLeftPeriod().isIn(sdf) && !getRightPeriod().isIn(sdf);
	}
	
	/**
	 * @see sasa.model.time.Period#convertToConcrete()
	 */
	@Override
	public List<Period> convertToConcrete() throws PeriodException {
		List<Period> result = new LinkedList<Period>();
		for (Period p1 : getLeftPeriod().convertToConcrete())
			for (Period p2 : getRightPeriod().convertToConcrete())
				result.add(new Minus(p1, p2));
		return result;
	}

	@Override
	public String toString() {
		return "( " + getLeftPeriod() + " ) \n MENOS \n( " + getRightPeriod() + " )\n";
	}
	
	/**
	 * @throws PeriodException 
	 * @see sasa.model.time.Period#copy()
	 */
	@Override
	public Minus copy() throws PeriodException {
		return new Minus(getLeftPeriod().copy(), getRightPeriod().copy());
	}

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) throws PeriodException {
		List<HourInterval> leftIntervals = getLeftPeriod().hourIntervalsInADay(c);
		List<HourInterval> rightIntervals = getRightPeriod().hourIntervalsInADay(c);
		List<HourInterval> result = new LinkedList<HourInterval>();
		for (HourInterval hl : leftIntervals)
			for (HourInterval hr : rightIntervals) {
				HourInterval substraction = hl.substract(hr);
				if (substraction != null)
					result.add(substraction);
			}
		return result;
	}
}