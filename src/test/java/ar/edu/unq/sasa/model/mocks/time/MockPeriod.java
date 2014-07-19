package ar.edu.unq.sasa.model.mocks.time;

import java.util.Calendar;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

public class MockPeriod extends Period{

	@Override
	public boolean contains(Calendar c) throws PeriodException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Period p) throws PeriodException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Period> convertToConcrete() throws PeriodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Period copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean intersectsWith(Period p) throws PeriodException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod sp)
			throws PeriodException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConcrete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isIn(SimplePeriod sp) throws PeriodException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int minutesSharedWithPeriod(Period p) throws PeriodException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int minutesSharedWithSimplePeriod(SimplePeriod sp)
			throws PeriodException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHourFulfiller(LogicalHourFulfiller hf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c)
			throws PeriodException {
		// TODO Auto-generated method stub
		return null;
	}

}
