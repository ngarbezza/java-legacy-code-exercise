package ar.edu.unq.sasa.model.academic;

import junit.framework.TestCase;

public class TestSubject extends TestCase{
	/** TEST PROFESSOR
	 * Test class according to the Subject Class.
	 * @author MEKODA
	 * CONSIDERATIONS
	 * Simple test denoting a subject.
	 */
	public Subject subject;
	
	public void setUp(){

		this.subject = new Subject("Mathematical Analysis",((int) (Math.random()*6+1)));
	}
	
	public void test_hasInitializedName(){
		/**
		 * Determining the right initialization of the name of the subject.
		 */
		assertNotNull("Subject name not initialized",this.subject.getName());
	}
	
	public void test_hasInitializedID(){
		/**
		 * Determining the right initialization of the ID of the subject.
		 */
		assertNotNull("Subject ID not initialized",this.subject.getId());
	}
}
