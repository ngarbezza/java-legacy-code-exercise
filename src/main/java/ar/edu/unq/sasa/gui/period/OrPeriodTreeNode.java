package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

import ar.edu.unq.sasa.model.time.Or;
import ar.edu.unq.sasa.model.time.Period;

public class OrPeriodTreeNode extends CompositePeriodTreeNode {

	private static final long serialVersionUID = -7739530302365327327L;

	public OrPeriodTreeNode(PeriodTreeNode leftTree, PeriodTreeNode rightTree) {
		super(leftTree, rightTree);
	}

	@Override
	public String getDisplayText() {
		return "Condición lógica O";
	}

	@Override
	public void selectOrDeselect(JRadioButton or, JRadioButton and, JRadioButton minus) {
		or.setSelected(true);
		and.setSelected(false);
		minus.setSelected(false);
	}

	@Override
	public Period makePeriod() {
		return new Or(((PeriodTreeNode) getChildAt(0)).makePeriod(),
				((PeriodTreeNode) getChildAt(1)).makePeriod());
	}

	@Override
	public boolean matchPeriodType(Boolean simple, Boolean or, Boolean and,	Boolean minus) {
		return or && !simple;
	}
}
