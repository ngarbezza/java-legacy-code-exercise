package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * Representa la resta de conjuntos aplicada a los {@link Period}.
 */
public class Minus extends CompositePeriod {

	public Minus(Period left, Period right) {
		super(left, right);
	}

	@Override
	public boolean contains(Period p) {
		return getLeftPeriod().contains(p) && !getRightPeriod().contains(p);
	}

	@Override
	public boolean contains(Calendar c) {
		return getLeftPeriod().contains(c) && !getRightPeriod().contains(c);
	}

	@Override
	public boolean intersectsWith(Period p) {
		return getLeftPeriod().intersectsWith(p)
			&& !getRightPeriod().intersectsWith(p);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod simple) {
		return this.intersectsWith(simple);
	}

	@Override
	protected boolean isIn(SimplePeriod sdf) {
		return getLeftPeriod().isIn(sdf) && !getRightPeriod().isIn(sdf);
	}

	@Override
	public List<Period> convertToConcrete() {
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

	@Override
	public Minus copy() {
		return new Minus(getLeftPeriod().copy(), getRightPeriod().copy());
	}

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
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