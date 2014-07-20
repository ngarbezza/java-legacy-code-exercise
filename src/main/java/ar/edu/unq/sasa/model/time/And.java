package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * Operador que sirve para combinar {@link Period}'s por medio de
 * la condición lógica Y (and).
 *
 */
public class And extends CompositePeriod {

	public And(Period left, Period right) {
		super(left, right);
	}

	/**
	 * En este caso, un {@link Period} va a estar contenido si ambos operandos
	 * lo contienen.
	 */
	@Override
	public boolean contains(Period p) throws PeriodException {
		return getLeftPeriod().contains(p) && getRightPeriod().contains(p);
	}

	/**
	 * En este caso, una fecha va a estar incluida si los dos operandos
	 * la contienen.
	 */
	@Override
	public boolean contains(Calendar c) throws PeriodException {
		return getLeftPeriod().contains(c) && getRightPeriod().contains(c);
	}

	@Override
	public boolean intersectsWith(Period p) throws PeriodException {
		return getLeftPeriod().intersectsWith(p)
			&& getRightPeriod().intersectsWith(p);
	}
	
	@Override
	protected boolean intersectsWithSimple(SimplePeriod simple) throws PeriodException {
		return this.intersectsWith(simple);
	}
	
	@Override
	protected boolean isIn(SimplePeriod sp) throws PeriodException {
		return getLeftPeriod().isIn(sp) && getRightPeriod().isIn(sp);
	}
	
	@Override
	public And copy() throws PeriodException {
		return new And(getLeftPeriod().copy(), getRightPeriod().copy());
	}

	@Override
	public String toString() {
		return "( " + getLeftPeriod() + " ) \n Y \n( " + getRightPeriod() + " )\n";
	}
	
	@Override
	public List<Period> convertToConcrete() throws PeriodException {
		List<Period> result = new LinkedList<Period>();
		for (Period p1 : getLeftPeriod().convertToConcrete())
			for (Period p2 : getRightPeriod().convertToConcrete())
				result.add(new And(p1, p2));
		return result;
	}

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) throws PeriodException {
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		intervals.addAll(getLeftPeriod().hourIntervalsInADay(c));
		intervals.addAll(getRightPeriod().hourIntervalsInADay(c));
		return intervals;
	}
}
