package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.time.Period;

import java.util.Set;

import static ar.edu.unq.sasa.util.Preconditions.precondition;

public class ClassroomRequest extends Request {

    public Integer capacity;

    public ClassroomRequest(Period aPeriod, Subject aSubject, Professor aProfessor, long anID,
                            Set<Requirement> listOfRequirements, Integer aCapacity) {
        super(aPeriod, aSubject, aProfessor, anID, listOfRequirements);

        precondition("Capacity should be positive", aCapacity > 0);
        capacity = aCapacity;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer aCapacity) {
        capacity = aCapacity;
    }

    @Override
    public Boolean isClassroomRequest() {
        return true;
    }
}
