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
	public boolean contains(Period p) {
		return getLeftPeriod().contains(p) && getRightPeriod().contains(p);
	}

	@Override
	public boolean contains(Calendar c) {
		return getLeftPeriod().contains(c) && getRightPeriod().contains(c);
	}

	@Override
	public boolean intersectsWith(Period p) {
		return getLeftPeriod().intersectsWith(p)
			&& getRightPeriod().intersectsWith(p);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod simple) {
		return this.intersectsWith(simple);
	}

	@Override
	protected boolean isIn(SimplePeriod sp) {
		return getLeftPeriod().isIn(sp) && getRightPeriod().isIn(sp);
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
