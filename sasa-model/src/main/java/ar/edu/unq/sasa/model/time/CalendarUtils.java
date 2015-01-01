package ar.edu.unq.sasa.model.time;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;

public final class CalendarUtils {

	private CalendarUtils() { }

	public static boolean compareEquals(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null)
			return false;
		return
			c1.get(YEAR) == c2.get(YEAR) && c1.get(MONTH) == c2.get(MONTH)
			&& c1.get(DAY_OF_MONTH) == c2.get(DAY_OF_MONTH);
	}

	public static boolean compareLess(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null)
			return false;
		return (c1.get(YEAR) < c2.get(YEAR)) ? true : (c1.get(YEAR) == c2.get(YEAR))
				? (c1.get(MONTH) < c2.get(MONTH)) ? true : (c1.get(MONTH) == c2.get(MONTH))
						? (c1.get(DAY_OF_MONTH) < c2.get(DAY_OF_MONTH)) : false : false;
	}

	public static boolean compareGreater(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null)
			return false;
		return (c1.get(YEAR) > c2.get(YEAR)) ? true : (c1.get(YEAR) == c2.get(YEAR))
				? (c1.get(MONTH) > c2.get(MONTH)) ? true : (c1.get(MONTH) == c2.get(MONTH))
						? (c1.get(DAY_OF_MONTH) > c2.get(DAY_OF_MONTH)) : false : false;
	}
}
