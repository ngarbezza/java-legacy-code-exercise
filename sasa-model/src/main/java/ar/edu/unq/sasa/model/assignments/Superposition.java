package ar.edu.unq.sasa.model.assignments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

public class Superposition {

	public Map<HourInterval, List<AssignmentByRequest>> superpositionData =
		new HashMap<HourInterval, List<AssignmentByRequest>>();

	public Map<HourInterval, List<AssignmentByRequest>> getSuperpositionData() {
		return this.superpositionData;
	}
}