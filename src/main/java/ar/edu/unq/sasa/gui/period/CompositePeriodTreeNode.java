package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

public abstract class CompositePeriodTreeNode extends PeriodTreeNode {

	public CompositePeriodTreeNode(PeriodTreeNode left,	PeriodTreeNode right) {
		add(left);
		add(right);
	}

	@Override
	public void updateChanges(NewPeriodWindow pw) {}
	
	public abstract void selectOrDeselect(JRadioButton or, JRadioButton and, JRadioButton minus);
}
