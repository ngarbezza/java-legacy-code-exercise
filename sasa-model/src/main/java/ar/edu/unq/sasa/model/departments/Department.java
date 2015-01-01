package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.util.Publisher;

import java.util.List;

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

    public Department(University anUniversity) {
        university = anUniversity;
    }

    protected University getUniversity() {
        return university;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public List<Assignment> getAssignments() {
        return university.getAssignments();
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

    public ProfessorsDepartment getProfessorsDepartment() {
        return university.getProfessorsDepartment();
    }

    public SubjectsDepartment getSubjectsDepartment() {
        return university.getSubjectsDepartment();
    }

    public RequestsDepartment getRequestsDepartment() {
        return university.getRequestsDepartment();
    }
}
