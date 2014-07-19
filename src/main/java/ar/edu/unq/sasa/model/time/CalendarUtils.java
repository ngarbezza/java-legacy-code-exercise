package ar.edu.unq.sasa.model.time;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;

/**
 * Métodos útiles para trabajar con {@link Calendar}'s y para reducir su 
 * complejidad.
 * 
 * @author Nahuel Garbezza
 *
 */
public class CalendarUtils {
	/**
	 * Compara dos {@link Calendar} teniendo en cuenta sólo el año, mes y día.
	 * 
	 * @param c1
	 *            el primer {@link Calendar} a comparar.
	 * @param c2
	 *            el segundo {@link Calendar} a comparar.
	 * @return true si hay igualdad, false en caso contrario.
	 */
	public static boolean compareEquals(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null)
			return false;
		return 
			c1.get(YEAR) == c2.get(YEAR) && c1.get(MONTH) == c2.get(MONTH)
			&& c1.get(DAY_OF_MONTH) == c2.get(DAY_OF_MONTH);
	}
	
	/**
	 * Compara dos {@link Calendar} teniendo en cuenta sólo el año, mes y día.
	 * 
	 * @param c1
	 *            el primer {@link Calendar} a comparar.
	 * @param c2
	 *            el segundo {@link Calendar} a comparar.
	 * @return true c1 es anterior a c2, false en caso contrario.
	 */
	public static boolean compareLess(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null)
			return false;
		return (c1.get(YEAR) < c2.get(YEAR))? true : (c1.get(YEAR) == c2.get(YEAR))?
			(c1.get(MONTH) < c2.get(MONTH))? true : (c1.get(MONTH) == c2.get(MONTH))?
			(c1.get(DAY_OF_MONTH) < c2.get(DAY_OF_MONTH)) : false : false;
	}
	
	/**
	 * Compara dos {@link Calendar} teniendo en cuenta sólo el año, mes y día.
	 * 
	 * @param c1
	 *            el primer {@link Calendar} a comparar.
	 * @param c2
	 *            el segundo {@link Calendar} a comparar.
	 * @return true si c1 es posterior a c2, false en caso contrario.
	 */
	public static boolean compareGreater(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null)
			return false;
		return (c1.get(YEAR) > c2.get(YEAR))? true : (c1.get(YEAR) == c2.get(YEAR))?
			(c1.get(MONTH) > c2.get(MONTH))? true : (c1.get(MONTH) == c2.get(MONTH))?
			(c1.get(DAY_OF_MONTH) > c2.get(DAY_OF_MONTH)) : false : false;
	}
}
