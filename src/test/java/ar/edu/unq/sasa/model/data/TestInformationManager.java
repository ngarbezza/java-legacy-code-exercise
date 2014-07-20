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

public class TestInformationManager {

	@Before
	public void setUp() throws Exception {
		// resetea todos los valores del information manager
		// ya que es singleton
		InformationManager info = InformationManager.getInstance();
		info.getAssignments().clear();
		info.getClassrooms().clear();
		info.getMobileResources().clear();
		info.getProfessors().clear();
		info.getRequests().clear();
		info.getSubjects().clear();
	}

	@Test
	public void testConstructor() {
		InformationManager info = InformationManager.getInstance();

		List<Request> requests = new LinkedList<Request>();
		LinkedList<Professor> professors = new LinkedList<Professor>();
		List<Classroom> classrooms = new LinkedList<Classroom>();
		List<MobileResource> mobileResources = new LinkedList<MobileResource>();
		List<Assignment> emptyAssignments = new LinkedList<Assignment>();

		assertEquals(emptyAssignments, info.getAssignments());
		assertEquals(classrooms, info.getClassrooms());
		assertEquals(mobileResources, info.getMobileResources());
		assertEquals(professors, info.getProfessors());
		assertEquals(requests, info.getRequests());
	}

	@Test
	public void testAddClassroom(){
		InformationManager info = InformationManager.getInstance();
         Classroom classroom = new Classroom("Aula 3", 20);
		 info.addClassroom(classroom ) ;
		 List<Classroom> classrooms = new LinkedList<Classroom>();
		 classrooms.add(classroom) ;
 		assertEquals(classrooms  , info.getClassrooms()  );
	}

	@Test
	public void testAddProfessor(){
		InformationManager info = InformationManager.getInstance();
         Professor professor = new Professor("Aula 3", 2000, "1200258", "lala@lolo.com");
		 info.addProfessor(professor);
		 List<Professor> professors = new LinkedList<Professor>();
		 professors.add(professor) ;
 		assertEquals(professors, info.getProfessors()); 
	}

	@Test
	public void testAddResource(){
		InformationManager info = InformationManager.getInstance();
		MobileResource res = new MobileResource("Proyector", 0);
		 info.addResource(res);
		 List<MobileResource> resources = new LinkedList<MobileResource>();
		 resources.add(res) ;
 		assertEquals(resources, info.getMobileResources()); 
	}
}
