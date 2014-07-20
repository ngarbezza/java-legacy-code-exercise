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

	public MobileResource createMobileResource(String name) {
		MobileResource res = new MobileResource(name, ++idCount);
		getInformationManager().addResource(res);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
		return res;
	}
	
	public FixedResource createFixedResource(String name, int amount) throws ResourceException {
		if (amount < 0)
			throw new ResourceException("La cantidad del recurso debe ser positiva");
		else {
			FixedResource res = new FixedResource(name, amount);
			return res;		
		}
	}

	public void deleteResource(String name) throws ResourceException {
		MobileResource mobileResource = getInformationManager().getResource(name);
		for (Assignment assignment : mobileResource.getAssignments().values()){
			Asignator.getInstance().deleteAssignment(assignment);
		}
		getInformationManager().deleteResource(name);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
	}

	public void modifyResource(Resource searchedResource, String newName) {
		searchedResource.setName(newName);
		
		getPublisher().changed("mobileResources", getInformationManager().getMobileResources());
	}
	
	public void modifyResource(FixedResource searchedResource, int newAmount) throws ResourceException {
		if (newAmount < 0)
			throw new ResourceException("La cantidad del recurso debe ser positiva");
		else
			searchedResource.setAmount(newAmount);
	}
	
	public void addMobileResourceAssignment(MobileResource searchedResource, Period period, Assignment assignment) {
		searchedResource.addAssignment(period, assignment);
	}

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