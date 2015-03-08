package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourcesDepartment extends Department {

    // TODO kill this
    private static int idCount = 0;
    private final List<MobileResource> mobileResources;

    public List<MobileResource> getMobileResources() {
        return mobileResources;
    }

    public ResourcesDepartment(University university) {
        super(university);
        mobileResources = new LinkedList<>();
    }

    public MobileResource createMobileResource(String name) {
        MobileResource mobileResource = new MobileResource(name, ++idCount);
        mobileResources.add(mobileResource);

        getPublisher().changed("mobileResources", mobileResources);
        return mobileResource;
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
        mobileResources.remove(resource);

        getPublisher().changed("mobileResources", mobileResources);
    }

    public void modifyResource(Resource searchedResource, String newName) {
        searchedResource.setName(newName);

        getPublisher().changed("mobileResources", mobileResources);
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

    public MobileResource getResource(String aName) {
        for (MobileResource resource : mobileResources)
            if (resource.getName().equals(aName))
                return resource;
        return null;
    }

    public List<MobileResource> searchResources(String aName) {
        return getMobileResources().stream()
                .filter(mr -> mr.getName().contains(aName))
                .collect(Collectors.toList());
    }

    public void deleteMobileResource(MobileResource mr) {
        getMobileResources().remove(mr);

        getPublisher().changed("mobileResources", mobileResources);
    }

    public List<MobileResource> getNResources(Resource resource, Integer n) {
        List<MobileResource> listaRecursos = new ArrayList<>();
        int cant = n;
        for (MobileResource m : mobileResources)
            if (m.getName().equals(resource.getName()) && cant > 0) {
                listaRecursos.add(m);
                cant--;
            }
        return listaRecursos;
    }

    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        mobileResources.stream()
                .filter(mobileResource -> !containsResource(resources, mobileResource))
                .forEach(resources::add);
        for (Classroom classroom : getClassroomsDepartment().getClassrooms())
            classroom.getResources().stream()
                    .filter(fixedResource -> !containsResource(resources, fixedResource))
                    .forEach(resources::add);
        return resources;
    }

    private boolean containsResource(List<Resource> resources, Resource aResource) {
        return resources.stream().anyMatch(resource -> aResource.getName().equals(resource.getName()));
    }
}
