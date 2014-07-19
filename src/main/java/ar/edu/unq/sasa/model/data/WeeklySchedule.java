package ar.edu.unq.sasa.model.data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * WEEKLYSCHEDULE Simple class determining a week schedule for 
 * {@link Assignment}s.
 * 
 * CONSIDERATIONS The parametered constructor is going to be the
 *         only accesible one.
 * 
 * @author Gaston Charkiewicz
 * 
 */
public class WeeklySchedule {
	/**
	 * The main information.
	 */
	public Map<Calendar, Map<HourInterval, Assignment>> schedule = new HashMap<Calendar, Map<HourInterval, Assignment>>();

	public Map<Calendar, Map<HourInterval, Assignment>> getSchedule() {
		return schedule;
	}
}
