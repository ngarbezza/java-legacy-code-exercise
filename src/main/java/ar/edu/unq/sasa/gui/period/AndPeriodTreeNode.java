package ar.edu.unq.sasa.gui.period;

import javax.swing.JRadioButton;

import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.And;
import ar.edu.unq.sasa.model.time.Period;

/**
 * @author Nahuel Garbezza
 *
 */
public class AndPeriodTreeNode extends CompositePeriodTreeNode {

	public AndPeriodTreeNode(PeriodTreeNode leftTree, PeriodTreeNode rightTree) {
		super(leftTree, rightTree);
	}

	@Override
	public Period makePeriod() throws PeriodException {
		return new And(((PeriodTreeNode) getChildAt(0)).makePeriod(),
				((PeriodTreeNode) getChildAt(1)).makePeriod());
	}
	
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
	public boolean matchPeriodType(boolean simple, boolean or, boolean and,
			boolean minus) {
		return and && !simple;
	}
}
