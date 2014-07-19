package ar.edu.unq.sasa.model.assignments;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import ar.edu.unq.sasa.model.academic.MobileResourcesRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Test Case para la clase {@link ResourceAssignment}.
 * 
 * @author Nahuel
 *
 */
public class TestResourceAssignment extends TestCase {
	
	public void test_shouldConstructCorrectly() {
		String name = "Proyector";
		MobileResource resource = new MobileResource(name, 0);
		Period desHours = null;
		Subject subject = new Subject("Creacion de Shortcuts", 12);
		Professor professor = new Professor("Pable", 5, "42244556", "pablo@gmail.com");
		Map<Resource, Integer> reqResources = new HashMap<Resource, Integer>();
		Map<Resource, Integer> optResources = new HashMap<Resource, Integer>();
		
		MobileResourcesRequest request = new MobileResourcesRequest(desHours, subject, professor, 12, reqResources, optResources);
		ResourceAssignment asig = new ResourceAssignment(request, resource);
		
		boolean resourceIgual = asig.getAssignableItem().equals(resource);
		boolean requestIgual = asig.getRequest().equals(request);
		
		assertTrue(resourceIgual && requestIgual);
	}
}
