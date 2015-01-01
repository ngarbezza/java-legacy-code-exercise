package ar.edu.unq.sasa.gui.util;

import ar.edu.unq.sasa.model.time.Period;

/**
 * Representa aquellos objetos que puedan asignársele
 * y obtener un Periodo de tiempo.
 */
public interface PeriodHolder {
	
	void setPeriod(Period p);
	
	Period getPeriod();
}
