package ar.edu.unq.sasa.model.academic;

import static org.easymock.EasyMock.createMock;

import java.util.List;

import junit.framework.TestCase;

public class TestProfessor extends TestCase {
	/** TEST PROFESSOR
	 * Test class according to the Professor Class.
	 * @author MEKODA
	 * CONSIDERATIONS
	 * Simple test denoting a professor with some subjects.
	 */
	public Subject s1;
	public Subject s2;
	public List<Subject> ls; 
	public Professor p;
	
	public void setUp(){	
		this.s1 = createMock(Subject.class); 
		this.s2 = createMock(Subject.class);
		this.p = new Professor("John Doe",267765,"230934","jdoe@gmail.com");
	}
	
	public void test_hasInitializedSubjects(){
		/**
		 * Determining the right initialization of the subjects of the professor.
		 */
		assertNotNull("Professor subjects not initialized",this.p.getSubjects());
	}
	public void test_hasInitializedName(){
		/**
		 * Determining the right initialization of the name of the professor.
		 */
		assertNotNull("Professor name not initialized",this.p.getName());
	}
	public void test_hasInitializedPhoneNumber(){
		/**
		 * Determining the right initialization of the phone number of the professor.
		 */
		assertNotNull("Professor phone number not initialized",this.p.getPhoneNumber());
	}
	public void test_hasInitializedID(){
		/**
		 * Determining the right initialization of the ID of the professor.
		 */
		assertNotNull("Professor ID not initialized",this.p.getId());
	}
	public void test_hasInitializedMail(){
		/**
		 * Determining the right initialization of the mail of the professor.
		 */
		assertNotNull("Professor mail not initialized",this.p.getMail());
	}
}
