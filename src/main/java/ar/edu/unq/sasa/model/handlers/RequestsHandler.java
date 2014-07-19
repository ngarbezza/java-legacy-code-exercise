package ar.edu.unq.sasa.model.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * {@link Handler} que realiza Alta Baja Modificación y Consulta 
 * de {@link Request}s.
 * 
 * @author Cristian Suarez
 *
 */
public class RequestsHandler extends Handler {
	
	private static RequestsHandler instance;
	
	public static RequestsHandler getInstance() {
		if (instance == null)
			instance = new RequestsHandler();
		return instance;
	}
	
	private static long idPool = 0;
	
	private RequestsHandler() {}

	/**
	 * Crea un {@link ClassroomRequest} con todos los parámetros necesarios.
	 * 
	 * @param requiredResources recursos obligatorios.
	 * @param optionalResources recursos opcionales.
	 * @param desiredHours requerimientos horarios.
	 * @param subject la materia.
	 * @param professor el profesor.
	 * @param capacity la capacidad de aula deseada.
	 * @return un nuevo {@link ClassroomRequest} con todos los parámetros anteriores.
	 * @throws RequestException
	 */
	public ClassroomRequest createClassroomRequest(Map<Resource, Integer> requiredResources, Map<Resource, Integer> optionalResources, Period desiredHours, Subject subject, Professor professor, int capacity) throws RequestException {
		if (capacity < 0)
			throw new RequestException("La capacidad del aula tiene que ser positiva");
		else {
			ClassroomRequest classReq = new ClassroomRequest(desiredHours, subject, professor,  idPool++, optionalResources, requiredResources,  capacity);
			getInformationManager().addRequest(classReq);
			
			getPublisher().changed("requestsChanged", getInformationManager().getRequests());
			return classReq;
		}
	}
	
	/**
	 * Borra un {@link Request} del sistema.
	 * 
	 * @param searchedRequest el {@link Request} a eliminar.
	 */
	public void deleteRequest(Request searchedRequest) {
		getInformationManager().deleteRequest(searchedRequest);
		
		getPublisher().changed("requestsChanged", getInformationManager().getRequests());
	}

	/**
	 * Modifica los parámetros de tiempo de un {@link Request}.
	 * 
	 * @param searchedRequest el {@link Request} que se desea modificar.
	 * @param hours {@link Period} con los nuevos requerimientos horarios.
	 */
	public void modifyRequest(Request searchedRequest, Period hours) {
		searchedRequest.setDesiredHours(hours);
	}
	
	/**
	 * Modifica la capacidad de un {@link ClassroomRequest}.
	 * 
	 * @param searchedRequest el {@link Request} que se desea modificar.
	 * @param capacity la nueva capacidad del {@link Request}.
	 * @throws RequestException
	 */
	public void modifyRequest(ClassroomRequest searchedRequest, int capacity) throws RequestException {
		if (capacity < 0)
			throw new RequestException("La capacidad del aula tiene que ser positiva");
		searchedRequest.setCapacity(capacity);
	}
	
	/**
	 * Modifica los recursos obligatorios de un {@link Request}.
	 * 
	 * @param searchedRequest el {@link Request} que se desea modificar.
	 * @param requiredResources el {@link Map} de recursos obligatorios.
	 */
	public void modifyRequestRequiredResources(Request searchedRequest, Map<Resource, Integer> requiredResources) {
		searchedRequest.setRequiredResources(requiredResources);
	}
	
	/**
	 * Modifica los recursos opcionales de un {@link Request}.
	 * 
	 * @param searchedRequest el {@link Request} que se desea modificar.
	 * @param optionalResources el {@link Map} de recursos opcionales.
	 */
	public void modifyRequestOptionalResources(Request searchedRequest, Map<Resource, Integer> optionalResources) {
		searchedRequest.setOptionalResources(optionalResources);
	}
	
	public List<Request> searchByProfessor(Professor p) {
		List<Request> result = new LinkedList<Request>();
		for (Request r : getInformationManager().getRequests())
			if (r.getProfessor().equals(p) || p == null)
				result.add(r);
		return result;
	}
}
