package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestSubject {
	public Subject subject;

	@Before
	public void setUp(){
		this.subject = new Subject("Mathematical Analysis",((int) (Math.random()*6+1)));
	}

	@Test
	public void test_hasInitializedName(){
		assertNotNull("Subject name not initialized",this.subject.getName());
	}

	@Test
	public void test_hasInitializedID(){
		assertNotNull("Subject ID not initialized",this.subject.getId());
	}
}
