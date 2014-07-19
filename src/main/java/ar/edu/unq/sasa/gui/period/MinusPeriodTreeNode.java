package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.Minus;
import ar.edu.unq.sasa.model.time.Period;

/**
 * @author Nahuel Garbezza
 *
 */
public class MinusPeriodTreeNode extends CompositePeriodTreeNode {

	public MinusPeriodTreeNode(PeriodTreeNode leftTree, PeriodTreeNode rightTree) {
		super(leftTree, rightTree);
	}

	@Override
	public Period makePeriod() throws PeriodException {
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
	public boolean matchPeriodType(boolean simple, boolean or, boolean and,
			boolean minus) {
		return minus && !simple;
	}
}
