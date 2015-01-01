package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

import ar.edu.unq.sasa.model.time.Minus;
import ar.edu.unq.sasa.model.time.Period;

public class MinusPeriodTreeNode extends CompositePeriodTreeNode {

	private static final long serialVersionUID = 3131410217663034999L;

	public MinusPeriodTreeNode(PeriodTreeNode leftTree, PeriodTreeNode rightTree) {
		super(leftTree, rightTree);
	}

	@Override
	public Period makePeriod() {
		return new Minus(((PeriodTreeNode) getChildAt(0)).makePeriod(),
				((PeriodTreeNode) getChildAt(1)).makePeriod());
	}

	@Override
	public void selectOrDeselect(JRadioButton or, JRadioButton and, JRadioButton minus) {
		or.setSelected(false);
		and.setSelected(false);
		minus.setSelected(true);
	}

	@Override
	public String getDisplayText() {
		return "Condición lógica Menos";
	}

	@Override
	public boolean matchPeriodType(Boolean simple, Boolean or, Boolean and, Boolean minus) {
		return minus && !simple;
	}
}
