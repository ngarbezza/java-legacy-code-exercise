package ar.edu.unq.sasa.model.handlers;

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
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.Satisfaction;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.exceptions.time.TimestampException;
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
 * los handlers (nota: es el anfitrión de las futuras consultas).
 * 
 * @author Todos
 * 
 */
public class QueryManager {

	public InformationManager getInformationManager() {
		return InformationManager.getInstance();
	}

	public Asignator getAsignator() {
		return Asignator.getInstance();
	}

	/**
	 * Devuelve una lista de Satisfactions entre un ClassroomRequest y un Classroom
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest
	 * @param classroom
	 * @return Lista de Satisfaction
	 * @throws PeriodException
	 */
	public Satisfaction satisfactionsFromClassroomAndRequest(
			ClassroomRequest classroomRequest, Classroom classroom)
			throws PeriodException {
		
		ClassroomAssignment classroomAssignment = getAsignator().asignateRequestInAClassroom(classroomRequest, classroom);
		
		return classroomAssignment.getSatisfaction();
	}

	/**
	 * Retorna aquellas {@link Classroom} que satisfacen la totalidad de un
	 * pedido del tipo {@link ClassroomRequest}. Es decir, que se tendrán en 
	 * cuenta los requerimientos de tiempo, capacidad del aula y recursos.
	 * 
	 * @author Nahuel Garbezza
	 * 
	 * @param req el pedido {@link ClassroomRequest} del cual se obtienen los 
	 * 		requerimientos.
	 * @return una colección de aulas.
	 * @throws PeriodException
	 */
	public Collection<Classroom> classroomsThatSatisfyTheWholeRequest(
			ClassroomRequest req) throws PeriodException {
		return this.classroomsThatSatisfyTheWholeRequest(req, false);
	}

	/**
	 * Retorna aquellas {@link Classroom} que satisfacen la totalidad de un
	 * pedido del tipo {@link ClassroomRequest}, pero sin importar que los
	 * requerimientos del tiempo se superpongan con asignaciones comunes.
	 * 
	 * @author Nahuel Garbezza
	 *  
	 * @param req el pedido {@link ClassroomRequest} del cual se obtienen los 
	 * 		requerimientos.
	 * @return una colección de aulas.
	 * @throws PeriodException
	 */
	public Collection<Classroom> classroomsThatSatisfyARequestRegardlessOfTheirAssignments(
			ClassroomRequest req) throws PeriodException {
		return this.classroomsThatSatisfyTheWholeRequest(req, true);
	}

	protected Collection<Classroom> classroomsThatSatisfyTheWholeRequest(
			ClassroomRequest req, boolean ignoreCommonAssignments)
			throws PeriodException {
		Collection<Classroom> result = this
				.classroomsThatSatisfyCapacityRequirement(req);
		result.retainAll(this.classroomsThatSatisfyTimeRequirements(req,
				ignoreCommonAssignments));

		Map<FixedResource, Integer> resources = new HashMap<FixedResource, Integer>();
		Map<Resource, Integer> totalResources = req.getRequiredResources();
		totalResources.putAll(req.getOptionalResources());
		for (Entry<Resource, Integer> res : totalResources.entrySet())
			if (res.getKey().isFixedResource())
				resources.put((FixedResource) res.getKey(), res.getValue());

		result.retainAll(this.classroomsThatSatisfyFixedResources(resources));
		return result;
	}

	/**
	 * Retorna aquellas aulas que satisfacen los requerimientos de tiempo
	 * especificados en un {@link ClassroomRequest}.
	 * 
	 * @author Nahuel Garbezza
	 * 
	 * @param req el {@link ClassroomRequest} de donde se obtienen los 
	 * 		requerimientos.
	 * @param ignoreCommonAssignments indica si se deben tener en cuenta o no  
	 * 		asignaciones comunes. Cuando vale true, sólo tendrá en cuenta para
	 * 		el cálculo las {@link BookedAssignment}.
	 * @return
	 * @throws PeriodException
	 */
	public Collection<Classroom> classroomsThatSatisfyTimeRequirements(
			ClassroomRequest req, boolean ignoreCommonAssignments)
			throws PeriodException {
		Set<Classroom> result = new HashSet<Classroom>();
		for (Classroom c : this.getInformationManager().getClassrooms())
			if (c.satisfyTimeRequirements(req, ignoreCommonAssignments))
				result.add(c);
		return result;
	}

	/**
	 * Retorna aquellas aulas que pueden satisfacer los requerimientos de 
	 * {@link FixedResource}s. 
	 * 
	 * @author Nahuel Garbezza
	 * @param resources
	 * @return
	 */
	private Collection<Classroom> classroomsThatSatisfyFixedResources(
			Map<FixedResource, Integer> resources) {
		Set<Classroom> result = new HashSet<Classroom>();
		for (Classroom c : this.getInformationManager().getClassrooms())
			if (c.satisfyFixedResources(resources))
				result.add(c);
		return result;
	}

	/**
	 * Retorna aquellas aulas que satisfacen los requerimientos de tiempo
	 * especificados en un {@link ClassroomRequest}.
	 * 
	 * @author Nahuel Garbezza
	 * @param req el {@link ClassroomRequest} de donde se obtienen los 
	 * 		requerimientos.
	 * @return una colección con todas las aulas que satisfacen la condición.
	 * @throws PeriodException
	 */
	public Collection<Classroom> classroomsThatSatisfyTimeRequirements(
			ClassroomRequest req) throws PeriodException {
		return this.classroomsThatSatisfyTimeRequirements(req, false);
	}

	/**
	 * Retorna aquellas aulas que satisfacen la capacidad especificada en un
	 * pedido.
	 * 
	 * @author Nahuel Garbezza
	 * @param req
	 *            el pedido.
	 * @return una colección de aulas.
	 */
	public Collection<Classroom> classroomsThatSatisfyCapacityRequirement(
			ClassroomRequest req) {
		Set<Classroom> result = new HashSet<Classroom>();
		for (Classroom c : this.getInformationManager().getClassrooms())
			if (c.getCapacity() >= req.getCapacity())
				result.add(c);
		return result;
	}

	/**
	 * Retorna todos los horarios libres para un {@link AssignableItem} para
	 * una semana. 
	 * 
	 * @author Nahuel Garbezza
	 * 
	 * @param item el {@link AssignableItem} a verificar.
	 * @param start la fecha inicial de la semana.
	 * @return una colección con 7 {@link Period}, uno correspondiente
	 * 		a cada día de la semana.
	 * @throws PeriodException
	 * @throws TimestampException
	 */
	public List<Period> freeHoursInAnAssignableItemInAGivenWeek(
			AssignableItem item, Calendar start)
			throws PeriodException, TimestampException {
		Calendar current = (Calendar) start.clone();
		List<Period> result = new ArrayList<Period>();
		for (int i = 0; i < 7; i++) {
			result.add(this.freeHoursInADay(item, current));
			current.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result;
	}

	/**
	 * Retorna todos los horarios libres para un {@link AssignableItem} para
	 * un día particular.
	 * 
	 * @author Nahuel Garbezza
	 * 
	 * @param item el {@link AssignableItem} a verificar.
	 * @param day la fecha que se debe analizar.
	 * @return un {@link SimplePeriod} que representa los horarios libres, y null 
	 * 		si no hay horarios libres.
	 * @throws TimestampException
	 * @throws PeriodException
	 */
	public SimplePeriod freeHoursInADay(AssignableItem item, Calendar day) throws TimestampException, PeriodException {
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
			}
			else if (currentStart != null) {
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

//	/**
//	 * determines the weeklySchedule of a parametered-given classroom. It is
//	 * structured as a Map.Entry<Period,Assignment> run.
//	 * 
//	 * @author Gaston Charkiewicz
//	 * @throws PeriodException
//	 */
//	public WeeklySchedule weeklyScheduleOfAClassroom(Classroom cl, Calendar cal)
//			throws PeriodException {
//		WeeklySchedule ret = new WeeklySchedule(); // the return value
//		Map<HourInterval, Assignment> mha = new HashMap<HourInterval, Assignment>();
//		for (Map.Entry<Period, Assignment> it : cl.getAssignments().entrySet()) {
//			if (it.getKey().contains(cal)) { // if the calendar is contained in
//												// the actual Period
//				if(it.getKey() instanceof SimplePeriod){
//					mha.put((HourInterval) it.getKey().getHourFulfiller(), it.getValue());}
//					else{
//					mha.put((HourInterval) it.getKey().getLeftPeriod().getHourFulfiller(), it.getValue());
//					mha.put((HourInterval) it.getKey().getRightPeriod().getHourFulfiller(), it.getValue());}
//
//
//			} // add it
//		}
//		ret.getSchedule().put(cal, mha); // finally, prepare the return value
//		return ret;
//	}
//
//	/**
//	 * Uses the previous one to determine all the weeklySchedule of classrooms.
//	 * 
//	 * @author Gaston Charkiewicz
//	 * @throws PeriodException
//	 */
//	public Map<Classroom, WeeklySchedule> weeklyScheduleOfAllClassrooms(
//			Calendar c) throws PeriodException {
//		Map<Classroom, WeeklySchedule> ret = new HashMap<Classroom, WeeklySchedule>();
//
//		for (Classroom it : this.getInformationManager().getClassrooms())
//			ret.put(it, this.weeklyScheduleOfAClassroom(it, c));
//
//		return ret;
//	}
//
//	// -----------------------------------------------------------------------//
//
//	/**
//	 * determines the superpositions of a classroom (AssignmentByRequest only)
//	 * 
//	 * @author Gaston Charkiewicz
//	 */
//	public Superposition superpositionFromAClassroom(Classroom c) {
//		Superposition ret = new Superposition();
//
//		for (Map.Entry<Period, Assignment> it1 : c.getAssignments().entrySet()) {
//			List<AssignmentByRequest> la = new LinkedList<AssignmentByRequest>();
//			for (Map.Entry<Period, Assignment> it2 : c.getAssignments()
//					.entrySet()) {
//				if (!(it1.equals(it2))
//						&& (it1.getKey().intersectsWith(it2.getKey()))
//						&& (it1 instanceof AssignmentByRequest)
//						&& it2 instanceof AssignmentByRequest) {
//					la.add((AssignmentByRequest) it1.getValue());
//					la.add((AssignmentByRequest) it2.getValue());
//				}
//			}
//			if(it1.getKey() instanceof SimplePeriod){
//				ret.getSuperpositionData().put((HourInterval) it1.getKey().getHourFulfiller(), la);}
//				else{
//				ret.getSuperpositionData().put((HourInterval) it1.getKey().getRightPeriod().getHourFulfiller(), la);
//				ret.getSuperpositionData().put((HourInterval) it1.getKey().getLeftPeriod().getHourFulfiller(), la);}
//		}
//		return ret;
//	}
//
//	// -----------------------------------------------------------------------//
//	/**
//	 * determines the superpositions of all classrooms (AssignmentByRequest
//	 * only). Uses the previous method.
//	 * 
//	 * @author Gaston Charkiewicz
//	 */
//	public List<Superposition> classroomsSuperpositions() {
//		List<Superposition> ret = new LinkedList<Superposition>();
//
//		for (Classroom it : this.getInformationManager().getClassrooms())
//			ret.add(this.superpositionFromAClassroom(it));
//		return ret;
//	}
}
