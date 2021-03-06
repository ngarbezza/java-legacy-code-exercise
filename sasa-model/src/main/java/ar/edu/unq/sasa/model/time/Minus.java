package ar.edu.unq.sasa.model.time;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Period> result = new LinkedList<>();
        for (Period p1 : getLeftPeriod().convertToConcrete())
            result.addAll(getRightPeriod().convertToConcrete().stream()
                    .map(p2 -> new Minus(p1, p2))
                    .collect(Collectors.toList()));
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
        List<HourInterval> result = new LinkedList<>();
        for (HourInterval hl : leftIntervals)
            for (HourInterval hr : rightIntervals) {
                HourInterval substraction = hl.substract(hr);
                if (substraction != null)
                    result.add(substraction);
            }
        return result;
    }
}
