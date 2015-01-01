package ar.edu.unq.sasa.model.time;

import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

public abstract class CompositePeriod extends Period {

    private final Period leftPeriod, rightPeriod;

    public CompositePeriod(Period left, Period right) {
        leftPeriod = left;
        rightPeriod = right;
    }

    public Period getLeftPeriod() {
        return leftPeriod;
    }

    public Period getRightPeriod() {
        return rightPeriod;
    }

    @Override
    public Boolean isConcrete() {
        return leftPeriod.isConcrete() && rightPeriod.isConcrete();
    }

    @Override
    public Integer minutesSharedWithPeriod(Period aPeriod) {
        return Math.max(leftPeriod.minutesSharedWithPeriod(aPeriod), rightPeriod.minutesSharedWithPeriod(aPeriod));
    }

    @Override
    protected Integer minutesSharedWithSimplePeriod(SimplePeriod aSimplePeriod) {
        return minutesSharedWithPeriod(aSimplePeriod);
    }

    @Override
    public void setHourFulfiller(LogicalHourFulfiller anHourFulfiller) {
        leftPeriod.setHourFulfiller(anHourFulfiller);
        rightPeriod.setHourFulfiller(anHourFulfiller);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + leftPeriod.hashCode();
        result = prime * result + rightPeriod.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        CompositePeriod other = (CompositePeriod) obj;
        return getClass() == other.getClass()
                && leftPeriod.equals(other.leftPeriod)
                && rightPeriod.equals(other.rightPeriod);
    }
}
