package ar.edu.unq.sasa.model.academic;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.departments.*;
import ar.edu.unq.sasa.model.exceptions.departments.AssignmentException;
import ar.edu.unq.sasa.model.requests.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Represento a una universidad, quien tiene aulas, profesores, materias,
 * recursos, pedidos y asignaciones.
 */
public class University {

    private List<Assignment> assignments;

    private ClassroomsDepartment classroomsDepartment;
    private ProfessorsDepartment professorsDepartment;
    private AssignmentsDepartment assignmentsDepartment;
    private ResourcesDepartment resourcesDepartment;
    private RequestsDepartment requestsDepartment;
    private SubjectsDepartment subjectsDepartment;

    public University() {
        assignments = new ArrayList<>();
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

    public void addAssignment(Assignment anAssignment) {
        getAssignments().add(anAssignment);
    }

    public void deleteAssignment(Assignment searchedAssignment) {
        getAssignments().remove(searchedAssignment);
    }

    public Assignment getAssignmentByRequest(Request request) {
        Assignment resultado = null;
        for (Assignment assignment : getAssignments())
            if (assignment.getRequest().equals(request))
                resultado = assignment;
        if (resultado == null)
            throw new AssignmentException("No existe una asignación para ese pedido");
        return resultado;
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
