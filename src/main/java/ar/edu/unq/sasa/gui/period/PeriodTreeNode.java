package ar.edu.unq.sasa.gui.period;

import javax.swing.tree.DefaultMutableTreeNode;

import ar.edu.unq.sasa.model.time.Period;

public abstract class PeriodTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -7096834986627776873L;

	public boolean isCompositePeriodNode() {
		return true;
	}

	public abstract String getDisplayText();

	public abstract Period makePeriod();

	public abstract boolean matchPeriodType(Boolean simple, Boolean or, Boolean and, Boolean minus);

	public abstract void updateChanges(NewPeriodWindow pw);
}
