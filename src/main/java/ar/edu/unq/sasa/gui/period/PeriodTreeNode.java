package ar.edu.unq.sasa.gui.period;

import javax.swing.tree.DefaultMutableTreeNode;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Clase que sirve para representar los nodos del árbol de las condiciones
 * horarias de la ventana {@link NewPeriodWindow}.
 */
public abstract class PeriodTreeNode extends DefaultMutableTreeNode {
	
	public boolean isCompositePeriodNode() {
		return true;
	}

	public abstract String getDisplayText();

	public abstract Period makePeriod() throws PeriodException;

	public abstract boolean matchPeriodType(boolean simple, boolean or,
			boolean and, boolean minus);

	public abstract void updateChanges(NewPeriodWindow pw);
}
