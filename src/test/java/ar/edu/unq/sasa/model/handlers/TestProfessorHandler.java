package ar.edu.unq.sasa.model.handlers;

import static org.junit.Assert.*;

import org.junit.Test;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.ProfessorException;

public class TestProfessorHandler {

	@Test
	public void test_ShouldConstructCorrectly(){
		ProfessorHandler pHandler = ProfessorHandler.getInstance();
		assertSame(pHandler.getInformationManager(), InformationManager.getInstance());
	}
	
	@Test
	public void testCreateProfessor() throws ProfessorException{
		ProfessorHandler pHandler = ProfessorHandler.getInstance(); 
		
		Professor professor = pHandler.createProfessor("Pepe", "42158787", "a@pepe.com");
		assertEquals( 0 , professor.getId()) ;
		assertEquals( "a@pepe.com" , professor.getMail()) ;
		assertEquals( "Pepe" , professor.getName()) ;
		assertEquals( "42158787" , professor.getPhoneNumber()) ;
	}
	
	@Test
	public void testCreateProfessorConMismoID() throws ProfessorException{
		ProfessorHandler pHandler = ProfessorHandler.getInstance(); 
		
		@SuppressWarnings("unused")
		Professor professor = pHandler.createProfessor("Pepe", "42158787", "a@pepe.com");
		@SuppressWarnings("unused")
		Professor professor2 = pHandler.createProfessor("Jose", "42564798", "a@jose.com");
	}
}
