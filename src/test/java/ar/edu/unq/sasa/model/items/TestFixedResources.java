package ar.edu.unq.sasa.model.items;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestFixedResources {

	@Test
	public void testFixedResource() {
		FixedResource resource = new FixedResource("PC", 10);
        assertEquals("PC", resource.getName());
        assertEquals(10, resource.getAmount());
	}

}
