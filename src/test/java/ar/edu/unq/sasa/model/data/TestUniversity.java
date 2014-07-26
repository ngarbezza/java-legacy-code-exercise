package ar.edu.unq.sasa.model.data;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.MobileResource;

public class TestUniversity {

	private University university;

	@Before
	public void setUp() throws Exception {
		university = new University();
	}

	@Test
	public void testConstructor() {
		List<Request> requests = new LinkedList<Request>();
		LinkedList<Professor> professors = new LinkedList<Professor>();
		List<Classroom> classrooms = new LinkedList<Classroom>();
		List<MobileResource> mobileResources = new LinkedList<MobileResource>();
		List<Assignment> emptyAssignments = new LinkedList<Assignment>();

		assertEquals(emptyAssignments, university.getAssignments());
		assertEquals(classrooms, university.getClassrooms());
		assertEquals(mobileResources, university.getMobileResources());
		assertEquals(professors, university.getProfessors());
		assertEquals(requests, university.getRequests());
	}

	@Test
	public void testAddClassroom() {
        Classroom classroom = new Classroom("Aula 3", 20);
		university.addClassroom(classroom);
		List<Classroom> classrooms = new LinkedList<Classroom>();
		classrooms.add(classroom);
 		assertEquals(classrooms, university.getClassrooms());
	}

	@Test
	public void testAddProfessor(){
        Professor professor = new Professor("Aula 3", 2000, "1200258", "lala@lolo.com");
		university.addProfessor(professor);
		List<Professor> professors = new LinkedList<Professor>();
		professors.add(professor);
 		assertEquals(professors, university.getProfessors());
	}

	@Test
	public void testAddResource(){
		MobileResource res = new MobileResource("Proyector", 0);
		university.addResource(res);
		List<MobileResource> resources = new LinkedList<MobileResource>();
		resources.add(res) ;
 		assertEquals(resources, university.getMobileResources());
	}
}
