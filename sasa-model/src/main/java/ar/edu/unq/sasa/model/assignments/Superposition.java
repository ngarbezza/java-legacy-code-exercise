package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Superposition {

    public Map<HourInterval, List<AssignmentByRequest>> superpositionData = new HashMap<>();

    public Map<HourInterval, List<AssignmentByRequest>> getSuperpositionData() {
        return this.superpositionData;
    }
}
