package ar.edu.unq.sasa.model.assignments;

import junit.framework.TestCase;
import ar.edu.unq.sasa.model.items.MobileResource;

/**
 * Test Case para la clase {@link BookedAssignment}.
 * 
 * @author Cristian
 *
 */
public class TestBookedAssignment extends TestCase {
	
	public void test_shouldConstructCorrectly(){
		String motivo = "Reparaciones";
		MobileResource item = new MobileResource("Proyector", 0);
		
		BookedAssignment asignacion = new BookedAssignment(motivo, item);
		boolean motivoIgual = asignacion.getCause().equals(motivo);
		boolean itemIgual = asignacion.getAssignableItem().equals(item);
		
		assertTrue(motivoIgual && itemIgual);
	}
}
