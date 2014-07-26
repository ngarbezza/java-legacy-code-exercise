package ar.edu.unq.sasa.model.departments;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.exceptions.departments.RequestException;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class RequestsDepartment extends Department {

	// TODO move requests list from university to here

	// TODO kill this
	private static long idPool = 0;

	public RequestsDepartment(University university) {
		super(university);
	}

	public ClassroomRequest createClassroomRequest(Map<Resource, Integer> requiredResources, Map<Resource, Integer> optionalResources, Period desiredHours, Subject subject, Professor professor, int capacity) {
		if (capacity < 0)
			throw new RequestException("La capacidad del aula tiene que ser positiva");
		else {
			ClassroomRequest classReq = new ClassroomRequest(desiredHours, subject, professor,  idPool++, optionalResources, requiredResources,  capacity);
			getUniversity().addRequest(classReq);

			getPublisher().changed("requestsChanged", getUniversity().getRequests());
			return classReq;
		}
	}

	public void deleteRequest(Request searchedRequest) {
		getUniversity().deleteRequest(searchedRequest);

		getPublisher().changed("requestsChanged", getUniversity().getRequests());
	}

	public List<Request> searchByProfessor(Professor p) {
		List<Request> result = new LinkedList<Request>();
		for (Request r : getUniversity().getRequests())
			if (r.getProfessor().equals(p) || p == null)
				result.add(r);
		return result;
	}
}
