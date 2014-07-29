package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

public abstract class CompositePeriodTreeNode extends PeriodTreeNode {

	private static final long serialVersionUID = 6847993863027730974L;

	public CompositePeriodTreeNode(PeriodTreeNode left,	PeriodTreeNode right) {
		add(left);
		add(right);
	}

	@Override
	public void updateChanges(NewPeriodWindow pw) { }

	public abstract void selectOrDeselect(JRadioButton or, JRadioButton and, JRadioButton minus);
}
