package ar.edu.unq.sasa.model.items;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.exceptions.handlers.AssignmentException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.mocks.assignments.MockClassroomAssignment;
import ar.edu.unq.sasa.model.mocks.time.MockPeriod;
import ar.edu.unq.sasa.model.time.Period;

public class TestMobileResource extends TestCase{

    public void testConstructor(){
    	MobileResource mobileResource = new MobileResource("Proyector", 0) ;
		String name = mobileResource.getName();
     	Map<Period, Assignment> assigments = mobileResource.getAssignments();
		Map<Period, Assignment> emptyMap = new HashMap<Period, Assignment>();
		Assert.assertEquals("Proyector", name);
		Assert.assertEquals(emptyMap, assigments);
    }

	public void testGetAssigment() throws AssignmentException,ResourceException {
		MockPeriod period = new MockPeriod();
		MockClassroomAssignment assignment = null;
		MobileResource mobileResource = new MobileResource("Proyector", 0) ;
		mobileResource.addAssignment(period, assignment);
		Map<Period, Assignment> assignments = new HashMap<Period, Assignment>();
		assignments.put(period, assignment);
		Assert.assertEquals( assignments,  mobileResource.getAssignments() );
     }
	
}
