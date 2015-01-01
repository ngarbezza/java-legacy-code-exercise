package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	public void setUp() {
		subject1 = new Subject("Filosofía");
		subject2 = new Subject("Pensamiento científico");
		subjects = new LinkedList<Subject>();
		subjects.add(subject1);
		subjects.add(subject2);
		professor = new Professor("John Doe", "230934", "jdoe@gmail.com", subjects);
	}

	@Test
	public void theProfessorTeachesSomeSubjects() {
		assertTrue(professor.teaches(subject1));
		assertTrue(professor.teaches(subject2));
	}

	@Test
	public void addingASubjectToAProfessor() {
		Subject newSubject = new Subject("Análisis Matemático 1");
		professor.addNewSubject(newSubject);
		assertTrue(professor.teaches(newSubject));
	}

	@Test
	public void addingTheSameSubjectToAProfessorTwiceLeavesJustOne() {
		Subject newSubject = new Subject("Análisis Matemático 1");
		professor.addNewSubject(newSubject);
		professor.addNewSubject(newSubject);
		assertEquals(3, professor.getSubjects().size());
	}
}
