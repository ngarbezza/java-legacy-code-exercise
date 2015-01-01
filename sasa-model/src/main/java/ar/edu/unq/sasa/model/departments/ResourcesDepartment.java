package ar.edu.unq.sasa.model.departments;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class ResourcesDepartment extends Department {

	// TODO kill this
	private static int idCount = 0;

	public ResourcesDepartment(University university) {
		super(university);
	}

	public MobileResource createMobileResource(String name) {
		MobileResource res = new MobileResource(name, ++idCount);
		getUniversity().addResource(res);

		getPublisher().changed("mobileResources", getMobileResources());
		return res;
	}

	public FixedResource createFixedResource(String name, int amount) {
		if (amount < 0)
			throw new ResourceException("La cantidad del recurso debe ser positiva");
		else
			return new FixedResource(name, amount);
	}

	public void deleteResource(MobileResource resource) {
		for (Assignment assignment : resource.getAssignments().values())
			getAssignmentsDepartment().deleteAssignment(assignment);
		getUniversity().deleteResource(resource);

		getPublisher().changed("mobileResources", getMobileResources());
	}

	public void modifyResource(Resource searchedResource, String newName) {
		searchedResource.setName(newName);

		getPublisher().changed("mobileResources", getMobileResources());
	}

	public void modifyResource(FixedResource searchedResource, int newAmount) {
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
		for (Resource mr : getMobileResources())
			if (mr.getName().contains(name))
				result.add((MobileResource) mr);
		return result;
	}

	public void deleteMobileResource(MobileResource mr) {
		getMobileResources().remove(mr);

		getPublisher().changed("mobileResources", getUniversity().getMobileResources());
	}

	public List<Resource> getAllResources() {
		return getUniversity().getResources();
	}

	public List<MobileResource> getMobileResources() {
		return getUniversity().getMobileResources();
	}
}