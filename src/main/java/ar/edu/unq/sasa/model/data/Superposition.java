package ar.edu.unq.sasa.model.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.unq.sasa.model.assignments.AssignmentByRequest;
import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * SUPERPOSITION Class that represents a superposition of 2
 * 	{@link AssignmentByRequest}.
 *
 * CONSIDERATIONS Parametered constructor is going to be the only
 *         defined const. method.
 *
 */
public class Superposition {
	
	public Map<HourInterval, List<AssignmentByRequest>> superpositionData =
		new HashMap<HourInterval, List<AssignmentByRequest>>();

	public Map<HourInterval, List<AssignmentByRequest>> getSuperpositionData() {
		return this.superpositionData;
	}
}