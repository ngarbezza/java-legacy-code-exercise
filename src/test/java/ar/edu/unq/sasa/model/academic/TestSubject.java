package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestSubject {
	/** TEST PROFESSOR
	 * Test class according to the Subject Class.
	 * @author MEKODA
	 * CONSIDERATIONS
	 * Simple test denoting a subject.
	 */
	public Subject subject;

	@Before
	public void setUp(){
		this.subject = new Subject("Mathematical Analysis",((int) (Math.random()*6+1)));
	}

	@Test
	public void test_hasInitializedName(){
		/**
		 * Determining the right initialization of the name of the subject.
		 */
		assertNotNull("Subject name not initialized",this.subject.getName());
	}

	@Test
	public void test_hasInitializedID(){
		/**
		 * Determining the right initialization of the ID of the subject.
		 */
		assertNotNull("Subject ID not initialized",this.subject.getId());
	}
}
