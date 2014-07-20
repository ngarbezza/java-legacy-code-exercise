package ar.edu.unq.sasa.model.assignments;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ar.edu.unq.sasa.model.items.MobileResource;

public class TestBookedAssignment {

	@Test
	public void test_shouldConstructCorrectly(){
		String motivo = "Reparaciones";
		MobileResource item = new MobileResource("Proyector", 0);

		BookedAssignment asignacion = new BookedAssignment(motivo, item);
		boolean motivoIgual = asignacion.getCause().equals(motivo);
		boolean itemIgual = asignacion.getAssignableItem().equals(item);

		assertTrue(motivoIgual && itemIgual);
	}
}
