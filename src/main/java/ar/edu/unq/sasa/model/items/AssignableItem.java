package ar.edu.unq.sasa.model.items;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Representa aquellos elementos que pueden asignarse mediante {@link Assignment}s, 
 * en intervalos de tiempo definidos por objetos {@link Period}.
 */
public abstract class AssignableItem {
	private String name;
	private final Map<Period, Assignment> assignments;

	public AssignableItem(String name) {
		this.name = name;
		this.assignments = new HashMap<Period, Assignment>();
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public Map<Period, Assignment> getAssignments() {
		return assignments;
	}

	public void addAssignment(Period period, Assignment assigment) {
		assignments.put(period, assigment);
	}

	public void removeAssignment(Period period) {
		this.getAssignments().remove(period);
	}

	public boolean satisfyTimeRequirements(Request request,
			boolean ignoreCommonAssignments) throws PeriodException {
		Collection<Period> current = request.getDesiredHours().convertToConcrete(); 
		for (Period p : current)
			if (canAssign(p, ignoreCommonAssignments))
				return true;
		return false;
	}

	protected boolean canAssign(Period period, boolean ignoreCommonAssignments) throws PeriodException {
		for (Entry<Period, Assignment> p : this.getAssignments().entrySet())
			if (p.getKey().intersectsWith(period))
				if (ignoreCommonAssignments)
					if (p.getValue().isBookedAssignment())
						return false;
					else
						continue;
				else
					return false;
		return true;
	}

	public boolean satisfyTimeRequirements(Request request)
			throws PeriodException {
		return this.satisfyTimeRequirements(request, false);
	}

	public boolean canAssign(Period period) throws PeriodException {
		return this.canAssign(period, false);
	}

	public boolean isFreeAt(Calendar calendar) throws PeriodException {
		for (Period p : this.getAssignments().keySet())
			if (p.contains(calendar))
				return false;
		return true;
	}

	public Period searchPeriod(Assignment assignment) {
		for (Entry<Period, Assignment> assignments : getAssignments().entrySet())
			if (assignments.getValue() == assignment)
				return assignments.getKey();
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssignableItem other = (AssignableItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
