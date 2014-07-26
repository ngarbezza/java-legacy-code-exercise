package ar.edu.unq.sasa.model.mocks.time;

import java.util.Calendar;
import java.util.List;

import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

public class MockPeriod extends Period{

	@Override
	public boolean contains(Calendar c) {
		return false;
	}

	@Override
	public boolean contains(Period p) {
		return false;
	}

	@Override
	public List<Period> convertToConcrete() {
		return null;
	}

	@Override
	public Period copy() {
		return null;
	}

	@Override
	public boolean intersectsWith(Period p) {
		return false;
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod sp) {
		return false;
	}

	@Override
	public boolean isConcrete() {
		return false;
	}

	@Override
	protected boolean isIn(SimplePeriod sp) {
		return false;
	}

	@Override
	public int minutesSharedWithPeriod(Period p) {
		return 0;
	}

	@Override
	protected int minutesSharedWithSimplePeriod(SimplePeriod sp) {
		return 0;
	}

	@Override
	public void setHourFulfiller(LogicalHourFulfiller hf) {
	}

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
		return null;
	}

}
