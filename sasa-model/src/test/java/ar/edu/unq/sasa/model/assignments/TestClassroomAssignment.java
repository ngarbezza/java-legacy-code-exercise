package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.requests.ClassroomRequest;
import ar.edu.unq.sasa.model.time.Period;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestClassroomAssignment {

    @Test
    public void shouldConstructCorrectly() {
        Classroom classroom = new Classroom("Aula 1", 10);
        List<ResourceAssignment> listaRes = new ArrayList<>();
        Period desHours = null;
        Subject subject = new Subject("Creación de Shortcuts");
        Professor professor = new Professor("Pable", "42244556", "pablo@gmail.com");
        ClassroomRequest classReq = new ClassroomRequest(desHours, subject, professor, 10, new HashSet<>(), 20);
        ClassroomAssignment asig = new ClassroomAssignment(classReq, classroom, listaRes);

        assertTrue(asig.getResourcesAssignments().equals(listaRes));
        assertTrue(asig.getAssignableItem().equals(classroom));
        assertTrue(asig.getRequest().equals(classReq));
    }

    @Test
    public void createSatisfaction() {
        Subject subject = new Subject("Matemática");
        Professor professor = new Professor("Pablo", "44445555", "pablo@gmail.com");
        ClassroomRequest classroomRequest = new ClassroomRequest(null, subject, professor, 0, new HashSet<>(), 25);
        Classroom classroom = new Classroom("Aula 1", 20);
        FixedResource fr1 = new FixedResource("Proyector", 1);
        FixedResource fr2 = new FixedResource("Pizarrón", 2);
        classroom.addResource(fr1);
        classroom.addResource(fr2);
        List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<>();
        ClassroomAssignment classroomAssignment1 =
                new ClassroomAssignment(classroomRequest, classroom, resourcesAssignmentsList);
        ClassroomAssignment classroomAssignment2 =
                new ClassroomAssignment(classroomRequest, classroom, resourcesAssignmentsList);
        Satisfaction satisfaction1 = classroomAssignment1.createSatisfaction();
        Satisfaction satisfaction2 = classroomAssignment2.createSatisfaction();

        assertEquals(satisfaction1, satisfaction2);
    }
}
