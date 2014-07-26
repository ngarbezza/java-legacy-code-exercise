package ar.edu.unq.sasa.model.mocks.time;

import java.util.List;

import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class MockLogicalHourFulfiller extends LogicalHourFulfiller {

	@Override
	public boolean contains(Timestamp t) {
		return false;
	}

	@Override
	public boolean contains(LogicalHourFulfiller lhf) {
		return false;
	}

	@Override
	public List<HourInterval> getConcreteIntervals() {
		return null;
	}

	@Override
	public boolean intersectsWith(LogicalHourFulfiller lhf) {
		return false;
	}

	@Override
	protected boolean intersectsWithHourInterval(HourInterval interval) {
		return false;
	}

	@Override
	protected boolean isIn(HourInterval hi) {
		return false;
	}

	@Override
	public int minutesSharedWith(LogicalHourFulfiller hourFulfiller) {
		return 0;
	}

	@Override
	protected int minutesSharedWithHourInterval(HourInterval interval) {
		return 0;
	}

	@Override
	public LogicalHourFulfiller copy() {
		return null;
	}

	@Override
	public boolean isConcrete() {
		// TODO Auto-generated method stub
		return false;
	}

}
