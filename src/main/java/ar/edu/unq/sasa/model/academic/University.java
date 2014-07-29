package ar.edu.unq.sasa.model.academic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.departments.AssignmentsDepartment;
import ar.edu.unq.sasa.model.departments.ClassroomsDepartment;
import ar.edu.unq.sasa.model.departments.ProfessorsDepartment;
import ar.edu.unq.sasa.model.departments.RequestsDepartment;
import ar.edu.unq.sasa.model.departments.ResourcesDepartment;
import ar.edu.unq.sasa.model.departments.SubjectsDepartment;
import ar.edu.unq.sasa.model.exceptions.departments.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.departments.RequestException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;

/**
 * Represento a una universidad, quien tiene aulas, profesores, materias,
 * recursos, pedidos y asignaciones.
 */
public class University {

	private List<Request> requests;
	private List<MobileResource> mobileResources;
	private List<Assignment> assignments;

	private ClassroomsDepartment classroomsDepartment;
	private ProfessorsDepartment professorsDepartment;
	private AssignmentsDepartment assignmentsDepartment;
	private ResourcesDepartment resourcesDepartment;
	private RequestsDepartment requestsDepartment;
	private SubjectsDepartment subjectsDepartment;

	public University() {
		this.requests = new LinkedList<Request>();
		this.mobileResources = new LinkedList<MobileResource>();
		this.assignments = new ArrayList<Assignment>();
		classroomsDepartment = new ClassroomsDepartment(this);
		professorsDepartment = new ProfessorsDepartment(this);
		assignmentsDepartment = new AssignmentsDepartment(this);
		resourcesDepartment = new ResourcesDepartment(this);
		requestsDepartment = new RequestsDepartment(this);
		subjectsDepartment = new SubjectsDepartment(this);
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public List<MobileResource> getMobileResources() {
		// TODO move to ResourcesDepartment
		return mobileResources;
	}

	public List<Resource> getResources() {
		List<Resource> resources = new ArrayList<Resource>();
		for (MobileResource mobileResource : mobileResources)
			if (!containsResource(resources, mobileResource))
				resources.add(mobileResource);
		for (Classroom classroom : getClassroomsDepartment().getClassrooms())
			for (FixedResource fixedResource : classroom.getResources())
				if (!containsResource(resources, fixedResource))
					resources.add(fixedResource);
		return resources;
	}

	private boolean containsResource(List<Resource> resources,
			Resource aResource) {
		for (Resource resource : resources)
			if (aResource.getName().equals(resource.getName()))
				return true;
		return false;
	}

	public void addRequest(Request request) {
		// TODO move to requests department
		this.getRequests().add(request);
	}

	public void addAssignment(Assignment assignment) {
		this.getAssignments().add(assignment);
	}

	public void addResource(MobileResource res) {
		this.getMobileResources().add(res);
	}

	public void deleteAssignment(Assignment searchedAssignment) {
		this.getAssignments().remove(searchedAssignment);
	}

	public void deleteRequest(Request request) {
		this.getRequests().remove(request);
	}

	public void deleteResource(MobileResource resource) {
		this.getMobileResources().remove(resource);
	}

	public Assignment getAssignmentByRequest(Request request) {
		Assignment resultado = null;
		for (Assignment a : this.getAssignments())
			if (a.getRequest().equals(request))
				resultado = a;
		if (resultado == null)
			throw new AssignmentException("No existe una asignacion para ese pedido");
		return resultado;
	}

	public MobileResource getResource(String name) {
		for (MobileResource r : this.getMobileResources())
			if (r.getName().equals(name))
				return r;
		return null;
	}

	public Request getRequest(Professor professor, Subject subject) {
		for (Request request : this.getRequests())
			if (request.getProfessor().equals(professor)
					&& request.getSubject().equals(subject))
				return request;
		throw new RequestException("No existe un pedido con ese ID");
	}

	public List<MobileResource> getNResources(Resource resource, int n) {
		List<MobileResource> listaRecursos = new ArrayList<MobileResource>();
		int cant = n;
		for (MobileResource m : this.getMobileResources())
			if (m.getName().equals(resource.getName()) && cant > 0) {
				listaRecursos.add(m);
				cant--;
			}
		return listaRecursos;
	}

	public AssignmentsDepartment getAssignmentsDepartment() {
		return assignmentsDepartment;
	}

	public ClassroomsDepartment getClassroomsDepartment() {
		return classroomsDepartment;
	}

	public ProfessorsDepartment getProfessorsDepartment() {
		return professorsDepartment;
	}

	public ResourcesDepartment getResourcesDepartment() {
		return resourcesDepartment;
	}

	public RequestsDepartment getRequestsDepartment() {
		return requestsDepartment;
	}

	public SubjectsDepartment getSubjectsDepartment() {
		return subjectsDepartment;
	}
}