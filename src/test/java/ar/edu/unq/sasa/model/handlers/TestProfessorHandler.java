package ar.edu.unq.sasa.model.handlers;

import junit.framework.Assert;
import junit.framework.TestCase;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.exceptions.handlers.ProfessorException;

public class TestProfessorHandler extends TestCase {
	
	public void test_ShouldConstructCorrectly(){
		ProfessorHandler pHandler = ProfessorHandler.getInstance();
		assertSame(pHandler.getInformationManager(), InformationManager.getInstance());
	}
	
	public void testCreateProfessor() throws ProfessorException{
		ProfessorHandler pHandler = ProfessorHandler.getInstance(); 
		
		Professor professor = pHandler.createProfessor("Pepe", "42158787", "a@pepe.com");
		Assert.assertEquals( 0 , professor.getId()) ;
		Assert.assertEquals( "a@pepe.com" , professor.getMail()) ;
		Assert.assertEquals( "Pepe" , professor.getName()) ;
		Assert.assertEquals( "42158787" , professor.getPhoneNumber()) ;
	}
	
	public void testCreateProfessorConMismoID() throws ProfessorException{
		ProfessorHandler pHandler = ProfessorHandler.getInstance(); 
		
		@SuppressWarnings("unused")
		Professor professor = pHandler.createProfessor("Pepe", "42158787", "a@pepe.com");
		@SuppressWarnings("unused")
		Professor professor2 = pHandler.createProfessor("Jose", "42564798", "a@jose.com");
	}
}
