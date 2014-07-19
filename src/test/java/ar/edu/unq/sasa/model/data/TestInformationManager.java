package ar.edu.unq.sasa.model.data;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.assignments.Assignment;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.MobileResource;

public class TestInformationManager extends TestCase {

	@Override
	protected void setUp() throws Exception {
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
	
	public void testConstructor() {
		InformationManager info = InformationManager.getInstance();

		List<Request> requests = new LinkedList<Request>();
		LinkedList<Professor> professors = new LinkedList<Professor>();
		List<Classroom> classrooms = new LinkedList<Classroom>();
		List<MobileResource> mobileResources = new LinkedList<MobileResource>();
		List<Assignment> emptyAssignments = new LinkedList<Assignment>();

		Assert.assertEquals(emptyAssignments, info.getAssignments());
		Assert.assertEquals(classrooms, info.getClassrooms());
		Assert.assertEquals(mobileResources, info.getMobileResources());
		Assert.assertEquals(professors, info.getProfessors());
		Assert.assertEquals(requests, info.getRequests());
	}

	public void testAddClassroom(){
		InformationManager info = InformationManager.getInstance();
         Classroom classroom = new Classroom("Aula 3", 20);
		 info.addClassroom(classroom ) ;
		 List<Classroom> classrooms = new LinkedList<Classroom>();
		 classrooms.add(classroom) ;
 		Assert.assertEquals(classrooms  , info.getClassrooms()  );
	}
	
	public void testAddProfessor(){
		InformationManager info = InformationManager.getInstance();
         Professor professor = new Professor("Aula 3", 2000, "1200258", "lala@lolo.com");
		 info.addProfessor(professor);
		 List<Professor> professors = new LinkedList<Professor>();
		 professors.add(professor) ;
 		Assert.assertEquals( professors  , info.getProfessors() ); 
	}
	
	public void testAddResource(){
		InformationManager info = InformationManager.getInstance();
		MobileResource res = new MobileResource("Proyector", 0);
		 info.addResource(res);
		 List<MobileResource> resources = new LinkedList<MobileResource>();
		 resources.add(res) ;
 		Assert.assertEquals( resources  , info.getMobileResources() ); 
	}
}
