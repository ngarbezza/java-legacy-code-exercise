package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.items.MobileResource;

public class TestUniversity {

	private University university;

	@Before
	public void setUp() {
		university = new University();
	}

	@Test
	public void testAddResource() {
		MobileResource res = new MobileResource("Proyector", 0);
		university.addResource(res);
		List<MobileResource> resources = new LinkedList<MobileResource>();
		resources.add(res);
 		assertEquals(resources, university.getMobileResources());
	}
}
