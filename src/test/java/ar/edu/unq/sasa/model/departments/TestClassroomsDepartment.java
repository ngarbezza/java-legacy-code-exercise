package ar.edu.unq.sasa.model.departments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.exceptions.departments.ClassroomException;
import ar.edu.unq.sasa.model.exceptions.departments.ResourceException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

public class TestClassroomsDepartment {

	private ClassroomsDepartment department;

	@Before
	public void setUp() {
		University university = new University();
		department = new ClassroomsDepartment(university);
	}

	@Test
	public void testCreateClassroom() throws ClassroomException {
		Classroom classroom =  department.createClassroom("Aula 1", 10) ;
		assertEquals("Aula 1", classroom.getName());
		assertEquals(10, classroom.getCapacity());
	}

	@Test
	public void testSearchClassroom() throws ClassroomException {
		Classroom classroom = department.createClassroom("Aula 2", 40);
		Classroom foundClassroom = department.searchClassroom("Aula 2");
		assertSame(foundClassroom, classroom);
	}

	@Test(expected=ClassroomException.class)
	public void testSearchClassroomInexistente() throws ClassroomException {
		department.createClassroom("Aula 2", 40);
		department.searchClassroom("Aula 5");
	}

	@Test
	public void testDeleteClassroom() throws ClassroomException {
		department.createClassroom("Aula 2", 40);
		department.deleteClassroom("Aula 2");
		assertTrue(department.getClassrooms().isEmpty());
	}

	@Test(expected=ClassroomException.class)
	public void testDeleteClassroomErroneo() throws ClassroomException {
		department.createClassroom("Aula 2", 40);
		department.deleteClassroom("Aula 3");
	}

	@Test
	public void testModificarClassroomCapacity() throws ClassroomException {
		Classroom classroom = department.createClassroom("Aula 2", 40);
		department.modifyClassroom("Aula 2", 30);
		assertEquals(30, classroom.getCapacity());
	}

	@Test(expected=ClassroomException.class)
	public void testModificarClassroomCapacityConNameErroneo() throws ClassroomException {
		department.createClassroom("Aula 2", 40);
		department.modifyClassroom("Aula 5", 30);
	}

	@Test
	public void testModificarClassroomAddResoruce() throws ClassroomException, ResourceException {
		FixedResource resource = new FixedResource("PC", 5);
		department.createClassroom("Aula 2", 40);
		department.modifyClassroomAddResource("Aula 2", resource);
		assertEquals(resource, department.searchClassroom("Aula 2").getResource("PC"));
	}

	@Test(expected=ClassroomException.class)
	public void testModificarClassroomAddResourceConNameErroneo() throws ClassroomException {
		FixedResource resource = new FixedResource("PC", 5);
		department.createClassroom("Aula 2", 40);
		department.modifyClassroomAddResource("Aula 5", resource);
	}
}
