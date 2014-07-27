package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestProfessor {

	private Subject subject1;
	private Subject subject2;
	private List<Subject> subjects;
	private Professor professor;

	@Before
	public void setUp(){
		subject1 = new Subject("Filosofía", 1);
		subject2 = new Subject("Pensamiento científico", 2);
		subjects = new LinkedList<Subject>();
		subjects.add(subject1);
		subjects.add(subject2);
		professor = new Professor("John Doe", "230934", "jdoe@gmail.com", subjects);
	}

	@Test
	public void test_hasInitializedSubjects(){
		assertNotNull("Professor subjects not initialized", professor.getSubjects());
	}

	@Test
	public void test_hasInitializedName(){
		assertNotNull("Professor name not initialized",this.professor.getName());
	}

	@Test
	public void test_hasInitializedPhoneNumber(){
		assertNotNull("Professor phone number not initialized",this.professor.getPhoneNumber());
	}

	@Test
	public void test_hasInitializedMail(){
		assertNotNull("Professor mail not initialized",this.professor.getMail());
	}
}
