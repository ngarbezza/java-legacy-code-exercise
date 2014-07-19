package ar.edu.unq.sasa.model.handlers;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * {@link Handler} que realiza Alta Baja Modificación y Consulta 
 * de {@link Resource}s.
 * 
 * @author Cristian Suarez
 *
 */
public class ResourcesHandler extends Handler {

	private static ResourcesHandler instance;
	
	public static ResourcesHandler getInstance() {
		if (instance == null)
			instance = new ResourcesHandler();
		return instance;
	}
	
	private static int idCount = 0;
	
	private ResourcesHandler() {}

	/**
	 * Crea un {@link MobileResource} y lo almacena en el sistema.
	 * 
	 * @param name el nombre del nuevo {@link MobileResource}.
	 * @return el nuevo {@link MobileResource}.
	 */
	public MobileResource createMobileResource(String name) {
		MobileResource res = new MobileResource(name, ++idCount);
		getInformationManager().addResource(res);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
		return res;
	}
	
	/**
	 * Crea un {@link FixedResource} a partir de parámetros dados.
	 * 
	 * @param name el nombre del nuevo recurso.
	 * @param amount la cantidad del nuevo recurso.
	 * @return el nuevo {@link FixedResource}.
	 * @throws ResourceException
	 */
	public FixedResource createFixedResource(String name, int amount) throws ResourceException {
		if (amount < 0)
			throw new ResourceException("La cantidad del recurso debe ser positiva");
		else {
			FixedResource res = new FixedResource(name, amount);
			return res;		
		}
	}

	/**
	 * Elimina del sistema un {@link Resource} buscándolo por nombre.
	 * 
	 * @param name el nombre del {@link Resource} a eliminar.
	 * @throws ResourceException
	 */
	public void deleteResource(String name) throws ResourceException {
		MobileResource mobileResource = getInformationManager().getResource(name);
		for (Assignment assignment : mobileResource.getAssignments().values()){
			Asignator.getInstance().deleteAssignment(assignment);
		}
		getInformationManager().deleteResource(name);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
	}

	/**
	 * Modifica el nombre de un {@link Resource}.
	 * 
	 * @param searchedResource el {@link Resource} que se quiere modificar.
	 * @param newName el nuevo nombre.
	 */
	public void modifyResource(Resource searchedResource, String newName) {
		searchedResource.setName(newName);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
	}
	
	/**
	 * Modifica la cantidad de un {@link Resource}. 
	 * 
	 * @param searchedResource el {@link Resource} que se quiere modificar.
	 * @param newAmount la nueva cantidad.
	 * @throws ResourceException
	 */
	public void modifyResource(FixedResource searchedResource, int newAmount) throws ResourceException {
		if (newAmount < 0)
			throw new ResourceException("La cantidad del recurso debe ser positiva");
		else
			searchedResource.setAmount(newAmount);
	}
	
	/**
	 * Agrega una asignación a un {@link MobileResource}.
	 * 
	 * @param searchedResource el {@link MobileResource} al cual se lo va a asignar.
	 * @param period {@link Period} que representa el tiempo que durará esa asignación.
	 * @param assignment la asignación ({@link Assignment}).
	 */
	public void addMobileResourceAssignment(MobileResource searchedResource, Period period, Assignment assignment) {
		searchedResource.addAssignment(period, assignment);
	}

	/**
	 * Elimina una asignación para un {@link MobileResource}.
	 * 
	 * @param searchedResource el {@link MobileResource} al cual se le va a eliminar la asignación.
	 * @param period el {@link Period} que corresponde a la asignación deseada.
	 */
	public void removeMobileResourceAssignment(MobileResource searchedResource, Period period) {
		searchedResource.removeAssignment(period);
	}

	public List<MobileResource> searchResources(String name) {
		List<MobileResource> result = new LinkedList<MobileResource>();
		for (Resource mr : getInformationManager().getMobileResources())
			if (mr.getName().contains(name))
				result.add((MobileResource) mr);
		return result;
	}

	public void deleteMobileResource(MobileResource mr) {
		getInformationManager().getMobileResources().remove(mr);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
	}

	public List<Resource> getAllResources() {
		return getInformationManager().getResources();
	}
}