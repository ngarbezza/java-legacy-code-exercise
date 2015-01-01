package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.requests.MobileResourcesRequest;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class TestResourceAssignment {

    @Test
    public void shouldConstructCorrectly() {
        String name = "Proyector";
        MobileResource resource = new MobileResource(name, 0);
        Subject subject = new Subject("Creación de Shortcuts");
        Professor professor = new Professor("Pablo", "42244556", "pablo@gmail.com");

        MobileResourcesRequest request = new MobileResourcesRequest(null, subject, professor, 12, new HashSet<>());
        ResourceAssignment assignment = new ResourceAssignment(request, resource);

        assertTrue(assignment.getAssignableItem().equals(resource));
        assertTrue(assignment.getRequest().equals(request));
    }
}
