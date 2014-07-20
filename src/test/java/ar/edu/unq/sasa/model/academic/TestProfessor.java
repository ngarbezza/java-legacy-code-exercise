package ar.edu.unq.sasa.model.academic;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestProfessor {

	public Subject s1;
	public Subject s2;
	public List<Subject> ls; 
	public Professor p;

	@Before
	public void setUp(){	
		this.s1 = createMock(Subject.class); 
		this.s2 = createMock(Subject.class);
		this.p = new Professor("John Doe",267765,"230934","jdoe@gmail.com");
	}

	@Test
	public void test_hasInitializedSubjects(){
		assertNotNull("Professor subjects not initialized",this.p.getSubjects());
	}

	@Test
	public void test_hasInitializedName(){
		assertNotNull("Professor name not initialized",this.p.getName());
	}

	@Test
	public void test_hasInitializedPhoneNumber(){
		assertNotNull("Professor phone number not initialized",this.p.getPhoneNumber());
	}

	@Test
	public void test_hasInitializedID(){
		assertNotNull("Professor ID not initialized",this.p.getId());
	}

	@Test
	public void test_hasInitializedMail(){
		assertNotNull("Professor mail not initialized",this.p.getMail());
	}
}
