package ar.edu.unq.sasa.model.handlers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.assignments.BookedAssignment;
import ar.edu.unq.sasa.model.assignments.ResourceAssignment;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.RequestException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.MobileResource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Testing para la clase {@link ResourceHandler}.
 * 
 * @author Cristian
 * 
 */

public class TestResourcesHandler {
	private ResourcesHandler resHand;
	private FixedResource fixedResource;
	private MobileResource mobileResource;
	
	@Before
	public void setUp(){
		resHand = ResourcesHandler.getInstance();
		fixedResource = createMock(FixedResource.class);
		mobileResource = createMock(MobileResource.class);
 	}
	
	@Test
	public void testShouldConstructCorrectly() {
		assertSame(resHand.getInformationManager(), InformationManager.getInstance());
	}
	
	@Test
	public void testCreateMobileResource() {
		MobileResource res2 = resHand.createMobileResource("Luz");
		assertTrue(InformationManager.getInstance().getMobileResources().contains(res2));
	}
	
	@Test
	public void testCreateFixedResource() throws ResourceException{
		String name = "Proyector";
		int amount = 10;
		FixedResource res1 = new FixedResource(name, amount);
		FixedResource res2 = resHand.createFixedResource(name, amount);
		assertEquals(res1, res2);
	}
	
	@Test
	public void testCreatePositiveAmountFixedResource() throws ResourceException {
		String name = "Proyector";
		int amount = -10;
		try {
			@SuppressWarnings("unused")
			FixedResource res2 = resHand.createFixedResource(name, amount);
			fail("La cantidad del recurso debe ser positiva");
		} 
		
		catch (ResourceException e) {}
	}
	
	@Test
	public void testDeleteMobileResource() throws ResourceException {
		resHand.deleteMobileResource(mobileResource);
		assertFalse(InformationManager.getInstance().getMobileResources().contains(mobileResource));
	}
	
	@Test
	public void testModifyResourceName() {
		String name = "Proyector";
		fixedResource.setName(name);
		expectLastCall();
		replay(fixedResource);
		resHand.modifyResource(fixedResource, name);
		verify(fixedResource);
	}
	
	@Test
	public void testModifyResourceAmount() throws ResourceException {
		int amount = 10;
		fixedResource.setAmount(amount);
		expectLastCall();
		replay(fixedResource);
		resHand.modifyResource(fixedResource, amount);
		verify(fixedResource);
	}
	
	@Test
	public void testModifyPositiveResourceAmount() throws ResourceException {
		int amount = -10;
		try {
			resHand.modifyResource(fixedResource, amount);
			fail("La cantidad debe ser positiva");
		}
		catch (ResourceException e){}
	}
	
	@Test
	public void testAddMobileResourceAssignment() throws RequestException {
		Period period = null;
		ClassroomRequest mobileResourcesRequest = new ClassroomRequest(null, null, null, 0, null, null, 10);
		ResourceAssignment resAsig = new ResourceAssignment(mobileResourcesRequest, mobileResource);
		mobileResource.addAssignment(period, resAsig);
		expectLastCall();
		replay(mobileResource);
		resHand.addMobileResourceAssignment(mobileResource, period, resAsig);
		verify(mobileResource);
	}
	
	@Test
	public void testAddMobileResourceBookedAssignment() {
		Period period = null;
		String cause = "Necesita mantenimiento";
		BookedAssignment resAsig = new BookedAssignment(cause, mobileResource);
		mobileResource.addAssignment(period, resAsig);
		expectLastCall();
		replay(mobileResource);
		resHand.addMobileResourceAssignment(mobileResource, period, resAsig);
		verify(mobileResource);
	}
	
	@Test
	public void testRemoveMobileResourceAssignment() {
		Period period = null;
		mobileResource.removeAssignment(period);
		expectLastCall();
		replay(mobileResource);
		resHand.removeMobileResourceAssignment(mobileResource, period);
		verify(mobileResource);
	}
	
}
