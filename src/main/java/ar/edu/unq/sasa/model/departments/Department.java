package ar.edu.unq.sasa.model.departments;

import java.util.List;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.util.Publisher;

/**
 * Represento a un departamento de una universidad. Ejemplos de departamentos
 * son: el departamento de profesores, el departamento de aulas o el de
 * pedidos.
 */
public abstract class Department {

	// TODO refactor: try to not know the entire university, just the
	// departments that make sense for each department
	private University university;

	private final Publisher publisher = new Publisher();

	public Department(University university) {
		this.university = university;
	}

	protected University getUniversity() {
		return university;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public List<Professor> getProfessors() {
		return university.getProfessors();
	}

	public List<Assignment> getAssignments() {
		return university.getAssignments();
	}

	public List<Request> getRequests() {
		return university.getRequests();
	}

	public List<Classroom> getClassrooms() {
		return university.getClassrooms();
	}

	public ClassroomsDepartment getClassroomsDepartment() {
		return university.getClassroomsDepartment();
	}

	public AssignmentsDepartment getAssignmentsDepartment() {
		return university.getAssignmentsDepartment();
	}

	public ResourcesDepartment getResourcesDepartment() {
		return university.getResourcesDepartment();
	}

	public SubjectsDepartment getSubjectsDepartment() {
		return university.getSubjectsDepartment();
	}
}
