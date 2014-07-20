package ar.edu.unq.sasa.model.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.handlers.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;

/**
 * Es el punto de acceso a toda la información del sistema.
 */
public class InformationManager {
	private static InformationManager instance = null;

	private List<Request> requests;
	private LinkedList<Professor> professors;
	private List<Classroom> classrooms;
	private List<MobileResource> mobileResources;
	private List<Assignment> assignments;
	private List<Subject> subjects;

	private InformationManager() {
		this.classrooms = new LinkedList<Classroom>();
		this.professors = new LinkedList<Professor>();
		this.requests = new LinkedList<Request>();
		this.mobileResources = new LinkedList<MobileResource>();
		this.assignments = new ArrayList<Assignment>();
		this.subjects = new LinkedList<Subject>();
	}

	public static InformationManager getInstance() {
		if (instance == null)
			instance = new InformationManager();
		return instance;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public List<Professor> getProfessors() {
		return professors;
	}

	public List<Classroom> getClassrooms() {
		return classrooms;
	}

	public List<MobileResource> getMobileResources() {
		return mobileResources;
	}

	public List<Resource> getResources() {
		List<Resource> resources = new ArrayList<Resource>();
		for (MobileResource mobileResource : mobileResources)
			if (!containsResource(resources, mobileResource))
				resources.add(mobileResource);
		for (Classroom classroom : classrooms)
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
		this.getRequests().add(request);
	}

	public void addSubject(Subject s) {
		this.getSubjects().add(s);
	}

	public void addProfessor(Professor professor) {
		this.getProfessors().add(professor);
	}

	public void addClassroom(Classroom classroom) {
		this.getClassrooms().add(classroom);
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

	public void deleteClassroom(Classroom classroom) {
		this.getClassrooms().remove(classroom);
	}

	/**
	 * Borra un {@link MobileResource} buscándolo por nombre.
	 * 
	 * @param name
	 *            el nombre del {@link MobileResource} a borrar.
	 * @throws ResourceException
	 *             si el recurso no está.
	 */
	public void deleteResource(String name) throws ResourceException {
		for (MobileResource mobRes : this.getMobileResources())
			if (mobRes.getName().equals(name)) {
				this.getMobileResources().remove(mobRes);
				return;
			}
		throw new ResourceException("Recurso inexistente");
	}

	/**
	 * Obtiene la asignación correspondiente a un pedido.
	 * 
	 * @param request
	 *            el pedido que se utiliza para buscar.
	 * @return la asignación correspondiente.
	 * @throws AssignmentException
	 *             si no existe una asignación para ese pedido.
	 */
	public Assignment getAssignmentByRequest(Request request)
			throws AssignmentException {
		Assignment resultado = null;
		for (Assignment a : this.getAssignments())
			if (a.getRequest().equals(request))
				resultado = a;
		if (resultado == null)
			throw new AssignmentException("No existe una asignacion para ese pedido");
		return resultado;
	}

	public Classroom getClassroom(String nameClassroom) {
		Classroom wantedClassroom = null;
		for (Classroom currentClassroom : this.getClassrooms())
			if (currentClassroom.getName().equals(nameClassroom))
				wantedClassroom = currentClassroom;
		return wantedClassroom;
	}

	public MobileResource getResource(String name) {
		for (MobileResource r : this.getMobileResources())
			if (r.getName().equals(name))
				return r;
		return null;
	}

	public Request getRequest(Professor professor, Subject subject)
			throws RequestException {
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
}