package ar.edu.unq.sasa.model.departments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Satisfaction;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

/**
 * Trata y analiza consultas de mayor complejidad de las que podrían manejar
 * los departamentos (nota: es el anfitrión de las futuras consultas).
 */
public class QueryManager extends Department {

	public QueryManager(University university) {
		super(university);
	}

	public Satisfaction satisfactionsFromClassroomAndRequest(ClassroomRequest classroomRequest, Classroom classroom) {
		return getAssignmentsDepartment().asignateRequestInAClassroom(classroomRequest, classroom).getSatisfaction();
	}

	public Collection<Classroom> classroomsThatSatisfyTheWholeRequest(ClassroomRequest req) {
		return this.classroomsThatSatisfyTheWholeRequest(req, false);
	}

	public Collection<Classroom> classroomsThatSatisfyARequestRegardlessOfTheirAssignments(ClassroomRequest req) {
		return this.classroomsThatSatisfyTheWholeRequest(req, true);
	}

	protected Collection<Classroom> classroomsThatSatisfyTheWholeRequest(
			ClassroomRequest aRequest, boolean ignoreCommonAssignments) {
		Collection<Classroom> result = this
				.classroomsThatSatisfyCapacityRequirement(aRequest);
		result.retainAll(this.classroomsThatSatisfyTimeRequirements(aRequest,
				ignoreCommonAssignments));

		Map<FixedResource, Integer> resources = new HashMap<FixedResource, Integer>();
		Map<Resource, Integer> totalResources = aRequest.getRequiredResources();
		totalResources.putAll(aRequest.getOptionalResources());
		for (Entry<Resource, Integer> res : totalResources.entrySet())
			if (res.getKey().isFixedResource())
				resources.put((FixedResource) res.getKey(), res.getValue());

		result.retainAll(this.classroomsThatSatisfyFixedResources(resources));
		return result;
	}

	public Collection<Classroom> classroomsThatSatisfyTimeRequirements(
			ClassroomRequest aRequest, boolean ignoreCommonAssignments) {
		Set<Classroom> result = new HashSet<Classroom>();
		for (Classroom c : getClassroomsDepartment().getClassrooms())
			if (c.satisfyTimeRequirements(aRequest, ignoreCommonAssignments))
				result.add(c);
		return result;
	}

	private Collection<Classroom> classroomsThatSatisfyFixedResources(Map<FixedResource, Integer> resources) {
		Set<Classroom> result = new HashSet<Classroom>();
		for (Classroom c : getClassroomsDepartment().getClassrooms())
			if (c.satisfyFixedResources(resources))
				result.add(c);
		return result;
	}

	public Collection<Classroom> classroomsThatSatisfyTimeRequirements(ClassroomRequest req) {
		return this.classroomsThatSatisfyTimeRequirements(req, false);
	}

	public Collection<Classroom> classroomsThatSatisfyCapacityRequirement(ClassroomRequest req) {
		Set<Classroom> result = new HashSet<Classroom>();
		for (Classroom c : getClassroomsDepartment().getClassrooms())
			if (c.getCapacity() >= req.getCapacity())
				result.add(c);
		return result;
	}

	public List<Period> freeHoursInAnAssignableItemInAGivenWeek(AssignableItem item, Calendar start) {
		Calendar current = (Calendar) start.clone();
		List<Period> result = new ArrayList<Period>();
		for (int i = 0; i < 7; i++) {
			result.add(this.freeHoursInADay(item, current));
			current.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result;
	}

	public SimplePeriod freeHoursInADay(AssignableItem item, Calendar day) {
		Calendar copy = (Calendar) day.clone();
		day.set(Calendar.HOUR_OF_DAY, 0);	// horarios iniciales
		day.set(Calendar.MINUTE, 0);
		List<HourInterval> intervals = new LinkedList<HourInterval>();
		Timestamp currentStart = null;
		// de 0 hs hasta 23:30 (porque las 24 hs ya es el otro día)
		for (int j = 0; j < (24 * 60 / Period.MIN_HOUR_BLOCK) - 1; j++) {
			if (item.isFreeAt(day)) {
				if (currentStart == null) {
					currentStart = new Timestamp(day.get(Calendar.HOUR_OF_DAY),
							day.get(Calendar.MINUTE));
					if (day.get(Calendar.HOUR_OF_DAY) != 0
							|| day.get(Calendar.MINUTE) != 0)
						currentStart = currentStart.substract(Period.MIN_HOUR_BLOCK);
				}
			} else if (currentStart != null) {
				// cerrar el HourInterval y agregarlo a la lista
				Timestamp end = new Timestamp(day.get(Calendar.HOUR_OF_DAY),
						day.get(Calendar.MINUTE));
				intervals.add(new HourInterval(currentStart, end));
				currentStart = null;
			}
			day.add(Calendar.MINUTE, Period.MIN_HOUR_BLOCK);
		}
		day = copy;	// restauro el parámetro a su valor original
		if (intervals.isEmpty())
			return null;	// cuando todo está ocupado
		else {
			SimplePeriod res = new SimplePeriod(intervals.get(0), day);
			if (intervals.size() > 1) // Más de un hour interval, combinar con Or
				for (int x = 1; x < intervals.size(); x++)
					res.addHourCondition(intervals.get(x));
			return res;
		}
	}
}
