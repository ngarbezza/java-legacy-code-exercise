package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.time.Period;

import java.util.Set;

public class MobileResourcesRequest extends Request {

    public MobileResourcesRequest(Period aPeriod,
                                  Subject aSubject, Professor aProfessor, long anID,
                                  Set<Requirement> listOfRequirements) {
        super(aPeriod, aSubject, aProfessor, anID, listOfRequirements);
    }

    @Override
    public Boolean isClassroomRequest() {
        return false;
    }
}
