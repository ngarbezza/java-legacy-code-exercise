package ar.edu.unq.sasa.model.departments;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.assignments.Booking;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.time.Period;

public class TestResourcesDepartment {

	private ResourcesDepartment resourcesDepartment;
	private FixedResource fixedResource;
	private MobileResource mobileResource;
	private University university;

	@Before
	public void setUp() {
		university = new University();
		resourcesDepartment = new ResourcesDepartment(university);
		fixedResource = createMock(FixedResource.class);
		mobileResource = createMock(MobileResource.class);
 	}

	@Test
	public void testCreateMobileResource() {
		MobileResource res2 = resourcesDepartment.createMobileResource("Luz");
		assertTrue(university.getMobileResources().contains(res2));
	}

	@Test
	public void testCreateFixedResource() {
		String name = "Proyector";
		int amount = 10;
		FixedResource res1 = new FixedResource(name, amount);
		FixedResource res2 = resourcesDepartment.createFixedResource(name, amount);
		assertEquals(res1, res2);
	}

	@Test(expected = ResourceException.class)
	public void testCreatePositiveAmountFixedResource() {
		resourcesDepartment.createFixedResource("Proyector", -10);
	}

	@Test
	public void testDeleteMobileResource() {
		resourcesDepartment.deleteMobileResource(mobileResource);
		assertFalse(university.getMobileResources().contains(mobileResource));
	}

	@Test
	public void testModifyResourceName() {
		String name = "Proyector";
		fixedResource.setName(name);
		expectLastCall();
		replay(fixedResource);
		resourcesDepartment.modifyResource(fixedResource, name);
		verify(fixedResource);
	}

	@Test
	public void testModifyResourceAmount() {
		int amount = 10;
		fixedResource.setAmount(amount);
		expectLastCall();
		replay(fixedResource);
		resourcesDepartment.modifyResource(fixedResource, amount);
		verify(fixedResource);
	}

	@Test(expected = ResourceException.class)
	public void testModifyPositiveResourceAmount() {
		resourcesDepartment.modifyResource(fixedResource, -10);
	}

	@Test
	public void testAddMobileResourceAssignment() {
		Period period = null;
		ClassroomRequest mobileResourcesRequest = new ClassroomRequest(null, null, null, 0, null, null, 10);
		ResourceAssignment resAsig = new ResourceAssignment(mobileResourcesRequest, mobileResource);
		mobileResource.addAssignment(period, resAsig);
		expectLastCall();
		replay(mobileResource);
		resourcesDepartment.addMobileResourceAssignment(mobileResource, period, resAsig);
		verify(mobileResource);
	}

	@Test
	public void testAddMobileResourceBookedAssignment() {
		Period period = null;
		String cause = "Necesita mantenimiento";
		Booking resAsig = new Booking(cause, mobileResource);
		mobileResource.addAssignment(period, resAsig);
		expectLastCall();
		replay(mobileResource);
		resourcesDepartment.addMobileResourceAssignment(mobileResource, period, resAsig);
		verify(mobileResource);
	}

	@Test
	public void testRemoveMobileResourceAssignment() {
		Period period = null;
		mobileResource.removeAssignment(period);
		expectLastCall();
		replay(mobileResource);
		resourcesDepartment.removeMobileResourceAssignment(mobileResource, period);
		verify(mobileResource);
	}

}
