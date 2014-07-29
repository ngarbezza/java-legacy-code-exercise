package ar.edu.unq.sasa.model.items;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.assignments.ClassroomAssignment;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;

public class TestMobileResource {

	@Test
    public void testConstructor() {
    	MobileResource mobileResource = new MobileResource("Proyector", 0);
		String name = mobileResource.getName();
     	Map<Period, Assignment> assigments = mobileResource.getAssignments();
		Map<Period, Assignment> emptyMap = new HashMap<Period, Assignment>();
		assertEquals("Proyector", name);
		assertEquals(emptyMap, assigments);
    }

	@Test
	public void testGetAssigment() {
		Period period = new SimplePeriod(null, null);
		ClassroomAssignment assignment = null;
		MobileResource mobileResource = new MobileResource("Proyector", 0);
		mobileResource.addAssignment(period, assignment);
		Map<Period, Assignment> assignments = new HashMap<Period, Assignment>();
		assignments.put(period, assignment);
		assertEquals(assignments, mobileResource.getAssignments());
     }
}
