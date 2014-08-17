package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

public class And extends CompositePeriod {

	public And(Period left, Period right) {
		super(left, right);
	}

	@Override
	public Boolean contains(Period aPeriod) {
		return getLeftPeriod().contains(aPeriod) && getRightPeriod().contains(aPeriod);
	}

	@Override
	public Boolean contains(Calendar aDate) {
		return getLeftPeriod().contains(aDate) && getRightPeriod().contains(aDate);
	}

	@Override
	public Boolean intersectsWith(Period aPeriod) {
		return getLeftPeriod().intersectsWith(aPeriod) && getRightPeriod().intersectsWith(aPeriod);
	}

	@Override
	protected Boolean intersectsWithSimple(SimplePeriod aSimplePeriod) {
		return intersectsWith(aSimplePeriod);
	}

	@Override
	protected Boolean isIn(SimplePeriod aSimplePeriod) {
		return getLeftPeriod().isIn(aSimplePeriod) && getRightPeriod().isIn(aSimplePeriod);
	}

	@Override
	public And copy() {
		return new And(getLeftPeriod().copy(), getRightPeriod().copy());
	}

	@Override
	public String toString() {
		return "( " + getLeftPeriod() + " ) \n Y \n( " + getRightPeriod() + " )\n";
	}

	@Override
	public List<Period> convertToConcrete() {
		List<Period> result = new LinkedList<Period>();
		for (Period p1 : getLeftPeriod().convertToConcrete())
			for (Period p2 : getRightPeriod().convertToConcrete())
				result.add(new And(p1, p2));
		return result;
	}

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		intervals.addAll(getLeftPeriod().hourIntervalsInADay(c));
		intervals.addAll(getRightPeriod().hourIntervalsInADay(c));
		return intervals;
	}
}
