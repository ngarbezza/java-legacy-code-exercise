package ar.edu.unq.sasa.model.time;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.time.hour.HourInterval;

/**
 * Operador que sirve para combinar {@link Period}'s por medio de
 * la condición lógica O (or).
 *
 */
public class Or extends CompositePeriod {

	public Or(Period left, Period right) {
		super(left, right);
	}

	@Override
	public boolean contains(Period p) {
		return getLeftPeriod().contains(p) || getRightPeriod().contains(p);
	}

	@Override
	public boolean contains(Calendar c) {
		return getLeftPeriod().contains(c) || getRightPeriod().contains(c);
	}

	@Override
	public boolean intersectsWith(Period p) {
		return getLeftPeriod().intersectsWith(p)
			|| getRightPeriod().intersectsWith(p);
	}

	@Override
	protected boolean intersectsWithSimple(SimplePeriod simple) {
		return intersectsWith(simple);
	}

	@Override
	protected boolean isIn(SimplePeriod sdf) {
		return getLeftPeriod().isIn(sdf) || getRightPeriod().isIn(sdf);
	}

	@Override
	public Or copy() {
		return new Or(getLeftPeriod().copy(), getRightPeriod().copy());
	}

	@Override
	public List<Period> convertToConcrete() {
		List<Period> result = new LinkedList<Period>();
		result.addAll(getLeftPeriod().convertToConcrete());
		result.addAll(getRightPeriod().convertToConcrete());
		return result;
	}

	@Override
	public boolean isConcrete() {
		return false;
	}

	@Override
	public String toString() {
		return "( " + getLeftPeriod() + " ) \n O \n( " + getRightPeriod() + " )\n";
	}

	/**
	 * En un {@link Or} no es posible determinar los intervalos horarios
	 * (pues no genera intervalos concretos).
	 */
	@Override
	public List<HourInterval> hourIntervalsInADay(Calendar c) {
		throw new UnsupportedOperationException();
	}
}