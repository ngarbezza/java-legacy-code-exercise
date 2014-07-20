package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.Or;
import ar.edu.unq.sasa.model.time.Period;

public class OrPeriodTreeNode extends CompositePeriodTreeNode {

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
	public Period makePeriod() throws PeriodException {
		return new Or(((PeriodTreeNode) getChildAt(0)).makePeriod(),
				((PeriodTreeNode) getChildAt(1)).makePeriod());
	}

	@Override
	public boolean matchPeriodType(boolean simple, boolean or, boolean and,
			boolean minus) {
		return or && !simple;
	}
}
