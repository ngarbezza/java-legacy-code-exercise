package ar.edu.unq.sasa.model.items;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.departments.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.mocks.assignments.MockClassroomAssignment;
import ar.edu.unq.sasa.model.mocks.time.MockPeriod;
import ar.edu.unq.sasa.model.time.Period;

public class TestMobileResource {

	@Test
    public void testConstructor(){
    	MobileResource mobileResource = new MobileResource("Proyector", 0) ;
		String name = mobileResource.getName();
     	Map<Period, Assignment> assigments = mobileResource.getAssignments();
		Map<Period, Assignment> emptyMap = new HashMap<Period, Assignment>();
		assertEquals("Proyector", name);
		assertEquals(emptyMap, assigments);
    }

	@Test
	public void testGetAssigment() throws AssignmentException,ResourceException {
		MockPeriod period = new MockPeriod();
		MockClassroomAssignment assignment = null;
		MobileResource mobileResource = new MobileResource("Proyector", 0) ;
		mobileResource.addAssignment(period, assignment);
		Map<Period, Assignment> assignments = new HashMap<Period, Assignment>();
		assignments.put(period, assignment);
		assertEquals( assignments,  mobileResource.getAssignments() );
     }
	
}
