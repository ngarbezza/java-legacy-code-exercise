package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Request {

    public Period desiredHours;

    public Subject subject;

    public Professor professor;

    public final long id;

    public Set<Requirement> requirements;

    // TODO model status?
    private Boolean assigned = false;

    public Request(Period aPeriod, Subject aSubject, Professor aProfessor, long anID,
                   Map<Resource, Integer> reqResources, Map<Resource, Integer> optResources) {
        desiredHours = aPeriod;
        subject = aSubject;
        professor = aProfessor;
        id = anID;
        requirements = new HashSet<>();
        parseRequiredResources(reqResources);
        parseOptionalResources(optResources);
    }

    // TODO remove after requirements refactor
    private void parseRequiredResources(Map<Resource, Integer> reqResources) {
        if (reqResources != null) {
            reqResources.forEach((res, qty) -> requirements.add(new Requirement(res, qty, false)));
        }
    }

    private void parseOptionalResources(Map<Resource, Integer> optResources) {
        if (optResources != null) {
            optResources.forEach((res, qty) -> requirements.add(new Requirement(res, qty, true)));
        }
    }

    public void setDesiredHours(Period aPeriod) {
        desiredHours = aPeriod;
    }

    public void setRequiredResources(Map<Resource, Integer> reqResources) {
        parseRequiredResources(reqResources);
    }

    public void setOptionalResources(Map<Resource, Integer> optResources) {
        parseOptionalResources(optResources);
    }

    public Period getDesiredHours() {
        return desiredHours;
    }

    public Subject getSubject() {
        return subject;
    }

    public Professor getProfessor() {
        return professor;
    }

    public long getId() {
        return id;
    }

    public Map<Resource, Integer> getRequiredResources() {
        return unparseRequiredResources();
    }

    private Map<Resource, Integer> unparseRequiredResources() {
        Map<Resource, Integer> reqResources = new HashMap<>();
        requirements.stream().filter(Requirement::isRequired).forEach(req ->
                reqResources.put(req.getResource(), req.getQuantity()));
        return reqResources;
    }

    public Map<Resource, Integer> getOptionalResources() {
        Map<Resource, Integer> reqResources = new HashMap<>();
        requirements.stream().filter(Requirement::isOptional).forEach(req ->
                reqResources.put(req.getResource(), req.getQuantity()));
        return reqResources;
    }

    public Boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean aState) {
        assigned = aState;
    }

    public abstract Boolean isClassroomRequest();

    public Boolean getAssigned() {
        // do not remove, it is invoked by reflection
        return assigned;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + (int) (id ^ (id >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        Request other = (Request) obj;
        return getClass() == other.getClass() && id == other.id;
    }
}
