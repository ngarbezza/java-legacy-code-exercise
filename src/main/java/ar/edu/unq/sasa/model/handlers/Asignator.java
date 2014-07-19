package ar.edu.unq.sasa.model.handlers;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.assignments.Satisfaction;
import ar.edu.unq.sasa.model.data.Superposition;
import ar.edu.unq.sasa.model.exceptions.handlers.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.exceptions.time.PeriodException;
import ar.edu.unq.sasa.model.items.AssignableItem;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;

/**
 * Se encarga de realizar operaciones de Alta, Baja y Modificación sobre 
 * {@link Assignment}s.
 * 
 * @author Cristian Suarez
 * 
 */
public class Asignator extends Handler{
	private static Asignator instance = null;
	
	public static Asignator getInstance(){
		if (instance == null){
			instance = new Asignator();
		}
		return instance;
	}
	
	/** 
	 * Crea una {@link ResourceAssignment} para un {@link MobileResource}
	 * en un determinado {@link Period}.
	 *
	 * @author Cristian Suarez
	 * 
	 * @param request  Request que se va a guardar en la ResourceAssignment.
	 * @param mobileResource MobileResource al cual se le va a asignar el Request.
	 * @param period Period en el cual se va a asignar.
	 * 
	 * @return ResourceAssignment
	 */
	public ResourceAssignment asignateResourceAssignment(Request request, MobileResource mobileResource, Period period){
		ResourceAssignment resourceAssignment = new ResourceAssignment(request, mobileResource);
		mobileResource.addAssignment(period, resourceAssignment);
		getInformationManager().addAssignment(resourceAssignment);
		return resourceAssignment;
	}
	
	/**
	 * Crea una {@link BookedAssignment} para un {@link AssignableItem} en 
	 * un determinado {@link Period}, con una causa determinada. 
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroom AssignableItem al que se va a asignar.
	 * @param cause Motivo por el cual se hace la asignación.
	 * @param period Period en el cual se va a asignar.
	 * 
	 * @return BookedAssignment
	 * @throws PeriodException 
	 */
	public BookedAssignment asignateBookedAssignment(Classroom classroom, String cause, Period period) throws PeriodException {
		BookedAssignment bookedAssignment = new BookedAssignment(cause, classroom);
		classroom.addAssignment(period, bookedAssignment);
		getInformationManager().addAssignment(bookedAssignment);
		updateAssignmentsSatisfactionSuperpositions(period, bookedAssignment);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", 
				this.getInformationManager().getAssignments());
		this.getPublisher().changed("requestsChanged",
                this.getInformationManager().getRequests());
		///////////////////////////////////////////////////////////
		return bookedAssignment;
	}
	
	/**
	 * Crea una {@link ClassroomAssignment} para un {@link Classroom}.
	 * Además, crea también en los {@link MobileResource} necesarios 
	 * la misma {@link ClassroomAssignment}. Si algún {@link MobileResource} 
	 * de los que necesito tiene {@link Superposition} al asignar,
	 * no se asigna, y se busca otro. Puede que se hayan asignado menos
	 * {@link MobileResource} de los que originalmente se pidieron.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest Pedido de un Classroom hecho por un Professor.
	 * @param classroom El Classroom a donde se va a asignar el classroomRequest.
	 * @param period El Period en el cual se va a asignar el classroomRequest.
	 *
	 * @return ClassroomAssignment
	 * @throws PeriodException 
	 */
	public ClassroomAssignment asignateClassroomAssignment(ClassroomRequest classroomRequest, Classroom classroom, Period period) throws PeriodException {
		ClassroomAssignment classroomAssignment = asignateClassroomAssignmentWithoutSatisfaction(classroomRequest, classroom, period);
		classroomAssignment.createSatisfaction();
		updateAssignmentsSatisfactionSuperpositions(period, classroomAssignment);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", 
				this.getInformationManager().getAssignments()) ;
		this.getPublisher().changed("requestsChanged",
                this.getInformationManager().getRequests());
		///////////////////////////////////////////////////////////
		return classroomAssignment;
	}
	
	
	/**
	 * Subtarea de asignateClassroomAssignment
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest
	 * @param classroom
	 * @param period
	 * 
	 * @return classroomAssignment
	 * 
	 * @throws PeriodException
	 */
	private ClassroomAssignment asignateClassroomAssignmentWithoutSatisfaction(ClassroomRequest classroomRequest, Classroom classroom, Period period) throws PeriodException {
		List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<ResourceAssignment>();
		Set<Entry<Resource, Integer>> requiredResources = classroomRequest.getRequiredResources().entrySet();
		Set<Entry<Resource, Integer>> optionalResources = classroomRequest.getOptionalResources().entrySet();		
		
		asignateInFreeResources(requiredResources, classroomRequest, resourcesAssignmentsList, period);
		asignateInFreeResources(optionalResources, classroomRequest, resourcesAssignmentsList, period);
		
		ClassroomAssignment classroomAssignment = new ClassroomAssignment(classroomRequest, classroom, resourcesAssignmentsList);
		classroom.addAssignment(period, classroomAssignment);
		getInformationManager().addAssignment(classroomAssignment);
		
		
		return classroomAssignment;
	}
	
	/**
	 * Subtarea, para actualizar las {@link Satisfaction} de todas las {@link Assignment}
	 * que haya en un {@link Classroom}
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param period
	 * @param assignment
	 * @throws PeriodException
	 */
	private void updateAssignmentsSatisfactionSuperpositions(Period period, Assignment assignment) throws PeriodException {
		for (Entry<Period, Assignment> entry : assignment.getAssignableItem().getAssignments().entrySet()){
			if (! entry.getValue().equals(assignment)) {
				if (entry.getKey().intersectsWith(period)) {
					if (entry.getValue().isClassroomAssignment()) {
						float minutesShared = entry.getKey().minutesSharedWithPeriod(period) / 60;
						((ClassroomAssignment) entry.getValue()).getSatisfaction().addPeriodSuperposition(period, minutesShared);
					}
				}
			}
		}
	}

	/**
	 * Utilizado para separar en subtareas. No es parte de la interfaz pública.
	 * Crea ResourceAssignments para cada recurso, utilizando al método
	 * asignateResourceAssignment. además, guarda los ResourceAssignments en la
	 * resourcesAssignmentsList.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param resources Recursos en los cuales se necesita asignar.
	 * @param classroomRequest ClassroomRequest de donde provienen las cosas pedidas.
	 * @param resourcesAssignmentsList Guarda las resourcesAssignments hechas.
	 * @param period Period en el que se va a asignar.
	 * @throws PeriodException 
	 */
	private void asignateInFreeResources(Set<Entry<Resource, Integer>> resources, ClassroomRequest classroomRequest, List<ResourceAssignment> resourcesAssignmentsList, Period period) throws PeriodException {
		for (Entry<Resource, Integer> entry : resources){
			Integer cant = entry.getValue();
			for (MobileResource resource : getInformationManager().getMobileResources()){
				if (resource.getName().equals(entry.getKey().getName()) &&
						cant > 0){
					boolean isUsefullPeriod = true;
					for (Period resourcePeriod : resource.getAssignments().keySet()){
						if (resourcePeriod.intersectsWith(period))
							isUsefullPeriod = false;
					}
					if (isUsefullPeriod){
						ResourceAssignment asig = asignateResourceAssignment(classroomRequest, resource, period);
						resourcesAssignmentsList.add(asig);
						cant--;
					}
				}
			}
		}
	}
	
	/**
	 * Asigna un {@link ClassroomRequest} en el {@link Classroom} especificado.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest ClassroomRequest a asignar.
	 * @param classroom Classroom al cual se le va a asignar el ClassroomRequest.
	 * 
	 * @return una colección de ClassroomAssignment. 
	 * 
	 * @throws PeriodException
	 */
	public ClassroomAssignment asignateRequestInAClassroom(ClassroomRequest classroomRequest, Classroom classroom) throws PeriodException {
		Period freePeriod = (Period) getPercentageAndPeriod(classroomRequest, classroom).get(1);
		ClassroomAssignment classroomAssignment = asignateClassroomAssignment(classroomRequest, classroom, freePeriod);
		return classroomAssignment;
	}

	/**
	 * Asigna un {@link ClassroomRequest} en el {@link Classroom} más satisfactorio 
	 * que encuentre, teniendo en cuenta los {@link MobileResources} disponibles 
	 * para asignarlos, si son requeridos en el ClassroomRequest. Si no hay algún 
	 * MobileResource requerido, simplemente no se asigna.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest ClassroomRequest que se va a asignar.
	 * @return 
	 * 
	 * @throws PeriodException
	 * @throws ResourceException
	 */
	public ClassroomAssignment asignateRequestInMostSatisfactoryClassroom(ClassroomRequest classroomRequest) throws PeriodException, ResourceException {
		Classroom bestClassroom = null;
		Float bestRequiredPercentage = null;
		Float bestOptionalPercentage = null;
		int bestCapacityDifference = 0;
		int lowestMinutesSuperposed = 0;
		Period freePeriod = null;
		
		for (Classroom c : getInformationManager().getClassrooms()){
			float cRequiredPercentage = getPercentage(classroomRequest.getRequiredResources(), c);
			float cOptionalPercentage = getPercentage(classroomRequest.getOptionalResources(), c);
			Map<Integer, Object> percentageAndPeriod = getPercentageAndPeriod(classroomRequest, c);
			int cMinutesSuperposed = (Integer) percentageAndPeriod.get(0);
			Period cFreePeriod = (Period) percentageAndPeriod.get(1);
			int cCapacityDifference = c.getCapacity() - classroomRequest.getCapacity();
			
			if (bestClassroom == null){
				bestClassroom = c;
				bestRequiredPercentage = cRequiredPercentage;
				bestOptionalPercentage = cOptionalPercentage;
				bestCapacityDifference = cCapacityDifference;
				lowestMinutesSuperposed = cMinutesSuperposed;
				freePeriod = cFreePeriod;
			}
			else if (cMinutesSuperposed < lowestMinutesSuperposed){
					bestClassroom = c;
					bestRequiredPercentage = cRequiredPercentage;
					bestOptionalPercentage = cOptionalPercentage;
					bestCapacityDifference = cCapacityDifference;
					lowestMinutesSuperposed = cMinutesSuperposed;
					freePeriod = cFreePeriod;
				}
				else if (cMinutesSuperposed == lowestMinutesSuperposed &&
						cRequiredPercentage > bestRequiredPercentage){
						bestClassroom = c;
						bestRequiredPercentage = cRequiredPercentage;
						bestOptionalPercentage = cOptionalPercentage;
						bestCapacityDifference = cCapacityDifference;
						lowestMinutesSuperposed = cMinutesSuperposed;
						freePeriod = cFreePeriod;
					}
					else if (cRequiredPercentage == bestRequiredPercentage &&
							cCapacityDifference < bestCapacityDifference &&
							cCapacityDifference > 0){
							bestClassroom = c;
							bestRequiredPercentage = cRequiredPercentage;
							bestOptionalPercentage = cOptionalPercentage;
							lowestMinutesSuperposed = cMinutesSuperposed;
							freePeriod = cFreePeriod;
						}
						else if (cCapacityDifference == bestCapacityDifference &&
								cOptionalPercentage > bestOptionalPercentage){
								bestClassroom = c;
								bestRequiredPercentage = cRequiredPercentage;
								bestOptionalPercentage = cOptionalPercentage;
								lowestMinutesSuperposed = cMinutesSuperposed;
								freePeriod = cFreePeriod;
						}
		}
		
		return asignateClassroomAssignment(classroomRequest, bestClassroom, freePeriod);		
	}
	
	
	/**
	 * Crea una ClassroomAssignment en determinada Classroom, en determinado Period, 
	 * con determinados Resource.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest
	 * @param classroom
	 * @param period
	 * @param resources
	 * 
	 * @return classroomAssignment
	 * 
	 * @throws PeriodException
	 */
	public ClassroomAssignment asignateClassroomAssignmentWithDesiredPeriodAndRequiredResources(ClassroomRequest classroomRequest, Classroom classroom, Period period, Map<Resource, Integer> resources) throws PeriodException {
		Map<Resource, Integer> oldRequiredResources = classroomRequest.getRequiredResources();
		Map<Resource, Integer> oldOptionalResources = classroomRequest.getOptionalResources();
		classroomRequest.setRequiredResources(resources);
		classroomRequest.setOptionalResources(new HashMap<Resource, Integer>());
		
		ClassroomAssignment classroomAssignment = asignateClassroomAssignmentWithoutSatisfaction(classroomRequest, classroom, period);
		classroomAssignment.getRequest().setRequiredResources(oldRequiredResources);
		classroomAssignment.getRequest().setOptionalResources(oldOptionalResources);
		
		classroomAssignment.createSatisfaction();
		updateAssignmentsSatisfactionSuperpositions(period, classroomAssignment);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", 
				this.getInformationManager().getAssignments());
		this.getPublisher().changed("requestsChanged",
                this.getInformationManager().getRequests());
		///////////////////////////////////////////////////////////
		return classroomAssignment;
	}	
	
	/**
	 * Utilizado para separar en subtareas. No es parte de la interfaz pública.
	 * Crea y retorna una estructura (Map) que guarda:
	 * En la clave 0, minutos superpuestos.
	 * En la clave 1, Period disponible para asignar.
	 * Este método trabaja de esta manera para ser más eficiente, y poder hacer
	 * buscar ambas cosas (el porcentaje y los Periods disponibles) juntas, ya que
	 * sino se tendría q hacer otro método que volviera a recorrer todo de nuevo, 
	 * porque la lógica de búsqueda de ambas cosas es la misma.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param classroomRequest ClassroomRequest del cual provienen los Periods requeridos.
	 * @param classroom Classroom de la cual se fija los Periods disponibles.
	 *
	 * @return Map<Integer, Object> 
	 * 
	 * @throws PeriodException
	 */
	private Map<Integer, Object> getPercentageAndPeriod(ClassroomRequest classroomRequest, Classroom classroom) throws PeriodException {
		Map<Integer, Object> percentageAndPeriods = new HashMap<Integer, Object>();
		Period bestPeriod = null;
		int minutesShared = -1;
		
		List<Period> periodDivisions = classroomRequest.getDesiredHours().convertToConcrete();
		
		boolean isUsefullPeriod = true;
		for (Period reqPeriod : periodDivisions){
			if (bestPeriod == null){
				bestPeriod = reqPeriod;
				minutesShared = searchForHighestSuperposedMinutes(reqPeriod, classroom);
				if (minutesShared > 0){
					isUsefullPeriod = false;
				}
				else {
					isUsefullPeriod = true;
				}
			}
			else {
				int currentMinutesShared = searchForHighestSuperposedMinutes(reqPeriod, classroom);
				if (currentMinutesShared > 0){
					if (currentMinutesShared < minutesShared){
						bestPeriod = reqPeriod;
						minutesShared = 0;
						isUsefullPeriod = true;
					}
					else {
						isUsefullPeriod = false;
					}
				}
				else {
					bestPeriod = reqPeriod;
					minutesShared = 0;
					isUsefullPeriod = true;
				}
			}
				
			if (isUsefullPeriod){
				break;
			}
		}
		
		percentageAndPeriods.put(0, minutesShared);
		percentageAndPeriods.put(1, bestPeriod);
		
		return percentageAndPeriods;		
	}
	
	/**
	 * Busca la peor superposicion de un {@link Period} con respecto a un {@link Clasroom}
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param period
	 * @param classroom
	 * @return int
	 * @throws PeriodException
	 */
	private int searchForHighestSuperposedMinutes(Period period, Classroom classroom) throws PeriodException {
		int highestMinutesSuperposed = 0;
		for (Period classroomPeriod : classroom.getAssignments().keySet()){
			int minutesSharedWithPeriod = classroomPeriod.minutesSharedWithPeriod(period);
			if (minutesSharedWithPeriod > highestMinutesSuperposed){
				highestMinutesSuperposed = minutesSharedWithPeriod;
			}
		}
		return highestMinutesSuperposed;
	}

	/**
	 * Utilizado para separar en subtareas. No es parte de la interfaz pública.
	 * Obtiene el porcentaje de Resources disponibles para asignar, pedidos en el
	 * Request. El porcentaje lo retorna con el tipo float.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param requestResources Recursos pedidos en el Request.
	 * @param classroom Classroom la cual usa para obtener el porcentaje.
	 * 
	 * @return float
	 * 
	 * @throws ResourceException
	 */
	private float getPercentage(Map<Resource, Integer> requestResources, Classroom classroom) throws ResourceException {
		int cantMatched = 0;
		int cantTotal = 0;
		
		for (Entry<Resource, Integer> entry : requestResources.entrySet()){
			if (classroom.hasResource(entry.getKey().getName())) {
				int classAmount = classroom.getResource(entry.getKey().getName()).getAmount();
				if (entry.getValue() > classAmount){
					cantMatched += classAmount;
				}
				else {
					cantMatched += entry.getValue();
				}
			}
			cantTotal += entry.getValue();
		}
		
		if (cantTotal == 0){
			return 1;
		}
		else {
			return cantMatched * 100 / cantTotal;
		}
	}
	
	/**
	 * Modifica la causa por la cual se hizo una BookedAssignment.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param searchedAssignment BookedAssignment a la cual se le va a modificar la causa.
	 * @param newCause nueva causa, la que reemplaza la anterior.
	 */
	public void modifyBookedAssignmentCause(BookedAssignment searchedAssignment, String newCause){
		searchedAssignment.setCause(newCause);
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", 
				this.getInformationManager().getAssignments());
		this.getPublisher().changed("requestsChanged",
                this.getInformationManager().getRequests());
		///////////////////////////////////////////////////////////
	}
	
	/**
	 * Borra un {@link ResourceAssignment}.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param resourceAssignment ResourceAssignment que se va a eliminar.
	 */
	public void deleteResourceAssignment(ResourceAssignment resourceAssignment) {
		getInformationManager().deleteAssignment(resourceAssignment);
		Map<Period, Assignment> resourceAssignments = resourceAssignment.getAssignableItem().getAssignments();
		for (Entry<Period, Assignment> entry : resourceAssignments.entrySet())
			if (entry.getValue().equals(resourceAssignment))
				resourceAssignment.getAssignableItem().removeAssignment(entry.getKey());
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", 
				this.getInformationManager().getAssignments());
		this.getPublisher().changed("requestsChanged",
                this.getInformationManager().getRequests());
		///////////////////////////////////////////////////////////
	}
	
	/**
	 * Borra un {@link ClassroomAssignment} y todos sus 
	 * {@link ResourceAssignments}.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param request Request utilizado para buscar la ClassroomAssignment
	 * 
	 * @throws AssignmentException
	 */
	public void deleteClassroomAssignmentFromARequest(Request request) throws AssignmentException{
		ClassroomAssignment classroomAssignment = null;
		for (Assignment assignment : getInformationManager().getAssignments()){
			if (assignment.isClassroomAssignment()){
				if (assignment.getRequest().equals(request)){
					classroomAssignment = (ClassroomAssignment) assignment;
					getInformationManager().deleteAssignment(assignment);
					break;
				}
			}
		}
				
		for (ResourceAssignment resourceAssignment : ((ClassroomAssignment)classroomAssignment).getResourcesAssignments())
			deleteResourceAssignment(resourceAssignment);
		
		Map<Period, Assignment> classroomAssignments = classroomAssignment.getAssignableItem().getAssignments();
		for (Entry<Period, Assignment> entry : classroomAssignments.entrySet()){
			if (entry.getValue().equals(classroomAssignment)){
				classroomAssignment.getAssignableItem().removeAssignment(entry.getKey());
				break;
			}
		}
		
		if (! request.isAsignated())
			throw new AssignmentException("No existe una asignacion para ese pedido");
		///////////////////////////////////////////////////////////
		this.getPublisher().changed("assignmentsChanged", 
				this.getInformationManager().getAssignments());
		this.getPublisher().changed("requestsChanged",
                this.getInformationManager().getRequests());
		///////////////////////////////////////////////////////////
	}
	
	/**
	 * Elimina un {@link Assignment}.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param searchedAssignment Assignment que se va a borrar.
	 */
	public void deleteAssignment(Assignment searchedAssignment){
		try {
			if (searchedAssignment.getRequest() != null){
				searchedAssignment.getRequest().setAsignated(false);
			}
			AssignableItem assignableItem = searchedAssignment.getAssignableItem();
			Map<Period, Assignment> assignments = assignableItem.getAssignments();
			
			for (Entry<Period, Assignment> entry : assignments.entrySet())
				if (entry.getValue().equals(searchedAssignment)){
					assignableItem.removeAssignment(entry.getKey());
					break;
				}
			getInformationManager().deleteAssignment(searchedAssignment);
			
			///////////////////////////////////////////////////////////
			this.getPublisher().changed("assignmentsChanged", 
					this.getInformationManager().getAssignments());
			this.getPublisher().changed("requestsChanged",
	                this.getInformationManager().getRequests());
			///////////////////////////////////////////////////////////
		}
		catch (ConcurrentModificationException e){
			deleteAssignment(searchedAssignment);
		}
	}
	
	/**
	 * Busca un {@link Assignment} por medio de un {@link AssignableItem} 
	 * y un {@link Period}.
	 * 
	 * @author Cristian Suarez
	 * 
	 * @param assignableItem AssignableItem requerido para buscar la Assignment.
	 * @param period Period requerido para buscar la Assignment.
	 * 
	 * @return Assignment
	 */
	public Assignment searchForAssignment(AssignableItem assignableItem, Period period){
		return assignableItem.getAssignments().get(period);
	}	
	
	public List<BookedAssignment> searchForBookedAssignment(String text) {
		List<BookedAssignment> bookedAssignments = new ArrayList<BookedAssignment>();
		
		for (Assignment assignment : getInformationManager().getAssignments()){
			if (assignment instanceof BookedAssignment){
				if (((BookedAssignment) assignment).getCause().contains(text)){
					bookedAssignments.add((BookedAssignment) assignment);
				}
			}
		}
		
		return bookedAssignments;
	}
	
	/**
	 * Este método dada una asignación cambia el aula que estaba asignada por una nueva aula.
	 * @author Campos Diego
	 * @param assigment Asignación que se desea modificar
	 * @param classroom Aula que suplantara a la anterior asignada
	 * @param periodPeriodo de tiempo en el cual esta asignada	
	 * @return 
	 * @throws PeriodException 
	 */
	public ClassroomAssignment moveAssignmentOfClassroom(ClassroomAssignment assigment, Classroom classroom)throws AssignmentException, PeriodException{
		Period period = null;
		for (Entry<Period, Assignment> currentEntry : (assigment.getAssignableItem().getAssignments().entrySet())  ) {
		   if (currentEntry.getValue().equals(assigment)){
			   period =  currentEntry.getKey();
		       break; 
		    } 
		}
		ClassroomRequest request = assigment.getRequest();
		this.deleteClassroomAssignmentFromARequest(request);
		return this.asignateClassroomAssignment(request,classroom,period);
	} 

    /**
     * Este método dada una asignación cambia el horario que estaba asignado
     * @author Campos Diego
	 * @param assigment Asignación que se desea modificar
	 * @param periodPeriodo de tiempo en el cual esta asignada
	 * @param hour LogicalHourFulfiller que suplantara al anterior
     * @throws AssignmentException 
     * @throws PeriodException 
	 */
	public void moveAssignmentOfHour(ClassroomAssignment assignment ,LogicalHourFulfiller hour) throws AssignmentException, PeriodException{
		Period period = null;
		for (Entry<Period, Assignment> currentEntry : (assignment.getAssignableItem().getAssignments().entrySet())  ) {
		   if (currentEntry.getValue().equals(assignment)){
			   period =  currentEntry.getKey();
		       break; 
		    } 
		}
		deleteClassroomAssignmentFromARequest(assignment.getRequest());
		period.setHourFulfiller(hour);
		asignateClassroomAssignment(assignment.getRequest(), assignment.getAssignableItem(), period);
	}
	
	/**
	 * Este método dada una asignación y un period, rearma la asignacion con el nuevo period
	 * @author Campos Diego
	 * @param assigment Asignación que se desea modificar
	 * @param periodPeriodo de tiempo en el cual esta asignada
	 * @param hour LogicalDateFulfiller que suplantara al anterior
	 * @throws AssignmentException 
	 * @throws PeriodException 
	 * @throws AssignmentException 
	 */
	public void moveAssignmentOfPeriod( ClassroomAssignment assignment , Period newPeriod) throws PeriodException, AssignmentException{
		deleteClassroomAssignmentFromARequest(assignment.getRequest());
		asignateClassroomAssignment(assignment.getRequest(), assignment.getAssignableItem(), newPeriod);
		}
}