package ar.edu.unq.sasa.model.items;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.exceptions.departments.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.time.Period;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Classroom extends AssignableItem {

    private int capacity;

    private List<FixedResource> resources;

    public Classroom(String aName, int aCapacity) {
        super(aName);
        capacity = aCapacity;
        resources = new LinkedList<>();
    }

    public List<FixedResource> getResources() {
        return resources;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int aCapacity) {
        capacity = aCapacity;
    }

    public void addResource(FixedResource resource) {
        resources.add(resource);
    }

    public boolean hasResource(String aName) {
        return resources.stream().anyMatch(res -> res.getName().equals(aName));
    }

    public FixedResource getResource(String name) {
        FixedResource recursoBuscado = null;
        for (int i = 0; i < resources.size(); i++)
            if (resources.get(i).getName() == name) {
                recursoBuscado = resources.get(i);
                i = resources.size();
            }
        this.verify(recursoBuscado);
        return recursoBuscado;
    }

    public void verify(FixedResource resource) {
        if (resource == null)
            throw new ResourceException("Recurso no encontrado");
    }

    public void verify(Assignment assignment) {
        if (assignment == null)
            throw new AssignmentException("Asignación no encontrada");
    }

    public ClassroomAssignment getAssignment(Period period) {
        ClassroomAssignment assignment = (ClassroomAssignment) this.getAssignments().get(period);
        this.verify(assignment);
        return assignment;
    }

    public boolean satisfyFixedResources(Map<FixedResource, Integer> someResources) {
        for (Entry<FixedResource, Integer> res : someResources.entrySet())
            if (!this.satisfyFixedResource(res.getKey(), res.getValue()))
                return false;
        return true;
    }

    public boolean satisfyFixedResource(FixedResource aRequestedResource, Integer quantity) {
        return resources.stream().anyMatch(resource ->
                resource.getName().equals(aRequestedResource.getName()) && resource.getAmount() >= quantity);
    }

    public List<ClassroomAssignment> getClassroomAssignments() {
        return getAssignments().values().stream()
                .filter(Assignment::isClassroomAssignment)
                .map(a -> (ClassroomAssignment) a)
                .collect(Collectors.toList());
    }

    public List<Booking> getBookings() {
        return getAssignments().values().stream()
                .filter(Assignment::isBookedAssignment)
                .map(a -> (Booking) a)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + capacity;
        result = prime * result
                + ((resources == null) ? 0 : resources.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Classroom other = (Classroom) obj;
        if (capacity != other.capacity)
            return false;
        if (resources == null) {
            if (other.resources != null)
                return false;
        } else if (!resources.equals(other.resources))
            return false;
        return true;
    }
}
