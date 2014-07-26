package ar.edu.unq.sasa.gui.period;

import javax.swing.tree.DefaultMutableTreeNode;

import ar.edu.unq.sasa.model.time.Period;

/**
 * Clase que sirve para representar los nodos del árbol de las condiciones
 * horarias de la ventana {@link NewPeriodWindow}.
 */
public abstract class PeriodTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -7096834986627776873L;

	public boolean isCompositePeriodNode() {
		return true;
	}

	public abstract String getDisplayText();

	public abstract Period makePeriod();

	public abstract boolean matchPeriodType(boolean simple, boolean or,
			boolean and, boolean minus);

	public abstract void updateChanges(NewPeriodWindow pw);
}
