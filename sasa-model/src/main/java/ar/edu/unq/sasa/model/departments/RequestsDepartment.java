package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.requests.Request;
import ar.edu.unq.sasa.model.requests.Requirement;
import ar.edu.unq.sasa.model.time.Period;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestsDepartment extends Department {

    private List<Request> requests;

    private static long idPool = 0;

    public RequestsDepartment(University university) {
        super(university);
        requests = new LinkedList<>();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public ClassroomRequest createClassroomRequest(Set<Requirement> listOfRequirements, Period desiredHours,
                                                   Subject subject, Professor professor, Integer capacity) {
        ClassroomRequest classroomRequest = new ClassroomRequest(desiredHours, subject, professor,
                idPool++, listOfRequirements, capacity);
        addRequest(classroomRequest);
        return classroomRequest;
    }

    // TODO implement if necessary
//	public Request getRequest(Professor professor, Subject subject) {
//		for (Request request : getRequestsDepartment().getRequests())
//			if (request.getProfessor().equals(professor)
//					&& request.getSubject().equals(subject))
//				return request;
//		throw new RequestException("No existe un pedido con ese ID");
//	}

    public void addRequest(Request requestToAdd) {
        requests.add(requestToAdd);
        getPublisher().changed("requestsChanged", requests);
    }

    public void deleteRequest(Request searchedRequest) {
        requests.remove(searchedRequest);

        getPublisher().changed("requestsChanged", requests);
    }

    public List<Request> searchByProfessor(Professor aProfessor) {
        return requests.stream()
                .filter(aRequest -> aRequest.getProfessor().equals(aProfessor) || aProfessor == null)
                .collect(Collectors.toList());
    }
}
