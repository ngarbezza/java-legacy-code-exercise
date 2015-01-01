package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

import ar.edu.unq.sasa.model.time.And;
import ar.edu.unq.sasa.model.time.Period;

public class AndPeriodTreeNode extends CompositePeriodTreeNode {

	private static final long serialVersionUID = -4791881651281643560L;

	public AndPeriodTreeNode(PeriodTreeNode leftTree, PeriodTreeNode rightTree) {
		super(leftTree, rightTree);
	}

	@Override
	public Period makePeriod() {
		return new And(((PeriodTreeNode) getChildAt(0)).makePeriod(),
				((PeriodTreeNode) getChildAt(1)).makePeriod());
	}

	@Override
	public void selectOrDeselect(JRadioButton or, JRadioButton and, JRadioButton minus) {
		or.setSelected(false);
		and.setSelected(true);
		minus.setSelected(false);
	}

	@Override
	public String getDisplayText() {
		return "Condición Lógica Y";
	}

	@Override
	public boolean matchPeriodType(Boolean simple, Boolean or, Boolean and, Boolean minus) {
		return and && !simple;
	}
}
