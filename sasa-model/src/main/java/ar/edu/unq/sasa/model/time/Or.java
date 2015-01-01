package ar.edu.unq.sasa.model.time;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Or extends CompositePeriod {

    public Or(Period left, Period right) {
        super(left, right);
    }

    @Override
    public Boolean contains(Period aPeriod) {
        return getLeftPeriod().contains(aPeriod) || getRightPeriod().contains(aPeriod);
    }

    @Override
    public Boolean contains(Calendar aDate) {
        return getLeftPeriod().contains(aDate) || getRightPeriod().contains(aDate);
    }

    @Override
    public Boolean intersectsWith(Period aPeriod) {
        return getLeftPeriod().intersectsWith(aPeriod) || getRightPeriod().intersectsWith(aPeriod);
    }

    @Override
    protected Boolean intersectsWithSimple(SimplePeriod aSimplePeriod) {
        return intersectsWith(aSimplePeriod);
    }

    @Override
    protected Boolean isIn(SimplePeriod aSimplePeriod) {
        return getLeftPeriod().isIn(aSimplePeriod) || getRightPeriod().isIn(aSimplePeriod);
    }

    @Override
    public Or copy() {
        return new Or(getLeftPeriod().copy(), getRightPeriod().copy());
    }

    @Override
    public List<Period> convertToConcrete() {
        List<Period> result = new LinkedList<>();
        result.addAll(getLeftPeriod().convertToConcrete());
        result.addAll(getRightPeriod().convertToConcrete());
        return result;
    }

    @Override
    public Boolean isConcrete() {
        return false;
    }

    @Override
    public String toString() {
        return "( " + getLeftPeriod() + " ) \n O \n( " + getRightPeriod() + " )\n";
    }

    /**
     * En un {@link Or} no es posible determinar los intervalos horarios
     * (pues no genera intervalos concretos).
     */
    @Override
    public List<HourInterval> hourIntervalsInADay(Calendar aDate) {
        throw new UnsupportedOperationException();
    }
}
