package ar.edu.unq.sasa.model.handlers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.ClassroomException;
import ar.edu.unq.sasa.model.exceptions.handlers.ResourceException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

public class TestClassroomHandler {

	@Before
	public void setUp() throws Exception {
		ClassroomHandler.getInstance().getInformationManager().getClassrooms().clear();
	}
	
	@Test
	public void testConstructor(){
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		assertSame(pHandler.getInformationManager(), InformationManager.getInstance());
	}

	@Test
	public void testCreateClassroom() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance();
		Classroom classroom =  pHandler.createClassroom("Aula 1", 10) ;
		assertEquals("Aula 1", classroom.getName());
		assertEquals(10, classroom.getCapacity());
	}

	@Test
	public void testSearchClassroom() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		Classroom foundClassroom = pHandler.searchClassroom("Aula 2");
		assertSame(foundClassroom, classroom);
	}

	@Test(expected=ClassroomException.class)
	public void testSearchClassroomInexistente() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
	    pHandler.searchClassroom("Aula 5");
	}

	@Test
	public void testDeleteClassroom() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		pHandler.deleteClassroom("Aula 2");
		assertTrue(pHandler.getInformationManager().getClassrooms().isEmpty());
	}

	@Test(expected=ClassroomException.class)
	public void testDeleteClassroomErroneo() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		pHandler.searchClassroom("Aula 3");
	}

	@Test
	public void testModificarClassroomCapacity() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		pHandler.modifyClassroom("Aula 2", 30);	
		assertEquals(30, classroom.getCapacity());
	}

	@Test(expected=ClassroomException.class)
	public void testModificarClassroomCapacityConNameErroneo() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		pHandler.modifyClassroom("Aula 5", 30);
	}

	@Test
	public void testModificarClassroomAddResoruce() throws ClassroomException, ResourceException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		FixedResource resource = new FixedResource("PC", 5);
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		pHandler.modifyClassroomAddResource("Aula 2", resource);
		assertNotNull(pHandler.searchClassroom("Aula 2").getResource("PC"));
	}

	@Test(expected=ClassroomException.class)
	public void testModificarClassroomAddResourceConNameErroneo() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		FixedResource resource = new FixedResource("PC", 5);
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		pHandler.modifyClassroomAddResource("Aula 5", resource) ;	
	}
}
