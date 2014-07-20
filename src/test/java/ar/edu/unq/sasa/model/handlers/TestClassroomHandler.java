package ar.edu.unq.sasa.model.handlers;

import org.junit.Assert;
import org.junit.Test;

import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.ClassroomException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

/**
 * @author Campos Diego
 *
 */
public class TestClassroomHandler {

	@Test
	public void testConstructor(){
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		Assert.assertSame(pHandler.getInformationManager(), InformationManager.getInstance());
	}

	@Test
	public void testCreateClassroom() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance();
		Classroom classroom =  pHandler.createClassroom("Aula 1", 10) ;
		Assert.assertEquals("Aula 1", classroom.getName()) ;
		Assert.assertEquals(10, classroom.getCapacity()) ;
	}

	@Test
	public void testSearchClassroom() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		try {
	    	pHandler.searchClassroom("Aula 2");
		} catch (ClassroomException e) {
			System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
		}
	}

	@Test
	public void testSearchClassroomInexistente() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		try {
	    	pHandler.searchClassroom("Aula 5");
		} catch (ClassroomException e) {
			System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
		}
	}

	@Test	
	public void testDeleteClassroom() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
	    try {
	    	pHandler.searchClassroom("Aula 2")  ;
		} catch (ClassroomException e) {
			System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
		}
	}

	@Test
	public void testDeleteClassroomErroneo() throws ClassroomException {
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
	    try {
	    	pHandler.searchClassroom("Aula 3")  ;
		} catch (ClassroomException e) {
			System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
		}
	}

	@Test
	public void testModificarClassroomCapacity() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		try {
		       pHandler.modifyClassroom("Aula 2", 30) ;	
			} catch (ClassroomException e) {
				System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
			}
	}

	@Test
	public void testModificarClassroomCapacityConNameErroneo() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		try {
		       pHandler.modifyClassroom("Aula 5", 30) ;	
			} catch (ClassroomException e) {
				System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
			}
	}

	@Test
	public void testModificarClassroomAddResoruce() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		FixedResource resource = new FixedResource("PC", 5);
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		try {
		       pHandler.modifyClassroomAddResource("Aula 2", resource) ;	
			} catch (ClassroomException e) {
				System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
			}
	}

	@Test
	public void testModificarClassroomAddResoruceConNameErroneo() throws ClassroomException{
		ClassroomHandler pHandler = ClassroomHandler.getInstance(); 
		FixedResource resource = new FixedResource("PC", 5);
		@SuppressWarnings("unused")
		Classroom classroom = pHandler.createClassroom("Aula 2", 40);
		try {
			  pHandler.modifyClassroomAddResource("Aula 5", resource) ;	
			} catch (ClassroomException e) {
				System.out.println("Exception capturada, se paso como parametro: un nombre incorrecto");
			}
	}
}
