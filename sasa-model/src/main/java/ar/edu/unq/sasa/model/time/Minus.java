package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

public class Minus extends CompositePeriod {

	public Minus(Period left, Period right) {
		super(left, right);
	}

	@Override
	public Boolean contains(Period aPeriod) {
		return getLeftPeriod().contains(aPeriod) && !getRightPeriod().contains(aPeriod);
	}

	@Override
	public Boolean contains(Calendar aDate) {
		return getLeftPeriod().contains(aDate) && !getRightPeriod().contains(aDate);
	}

	@Override
	public Boolean intersectsWith(Period aPeriod) {
		return getLeftPeriod().intersectsWith(aPeriod) && !getRightPeriod().intersectsWith(aPeriod);
	}

	@Override
	protected Boolean intersectsWithSimple(SimplePeriod aSimplePeriod) {
		return intersectsWith(aSimplePeriod);
	}

	@Override
	protected Boolean isIn(SimplePeriod aSimplePeriod) {
		return getLeftPeriod().isIn(aSimplePeriod) && !getRightPeriod().isIn(aSimplePeriod);
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
	public List<HourInterval> hourIntervalsInADay(Calendar aDate) {
		List<HourInterval> leftIntervals = getLeftPeriod().hourIntervalsInADay(aDate);
		List<HourInterval> rightIntervals = getRightPeriod().hourIntervalsInADay(aDate);
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