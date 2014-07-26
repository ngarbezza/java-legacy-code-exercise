package ar.edu.unq.sasa.model.departments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.University;

public class TestProfessorsDepartment {

	private ProfessorsDepartment professorsDepartment;
	private University university;

	@Before
	public void setUp() {
		university = new University();
		professorsDepartment = new ProfessorsDepartment(university);
	}

	@Test
	public void testCreateProfessor() {
		Professor professor = professorsDepartment.createProfessor("Pepe", "42158787", "a@pepe.com");
		assertNotNull(professor.getId());
		assertEquals("a@pepe.com", professor.getMail());
		assertEquals("Pepe", professor.getName());
		assertEquals("42158787", professor.getPhoneNumber());
	}

	@Test
	public void testCreateProfessorConMismoID() {
		professorsDepartment.createProfessor("Pepe", "42158787", "a@pepe.com");
		professorsDepartment.createProfessor("Jose", "42564798", "a@jose.com");
	}
}
