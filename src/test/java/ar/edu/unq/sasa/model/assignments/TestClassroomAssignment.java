package ar.edu.unq.sasa.model.assignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class TestClassroomAssignment {

	@Test
	public void test_shouldConstructCorrectly() {
		Classroom classroom = new Classroom("Aula 1", 10);
		List<ResourceAssignment> listaRes = new ArrayList<ResourceAssignment>();
		Period desHours = null;
		Subject subject = new Subject("Creacion de Shortcuts", 12);
		Professor professor = new Professor("Pable", "42244556", "pablo@gmail.com");
		Map<Resource, Integer> reqResources = new HashMap<Resource, Integer>();
		Map<Resource, Integer> optResources = new HashMap<Resource, Integer>();
		ClassroomRequest classReq = new ClassroomRequest(desHours, subject, professor, 10, reqResources, optResources, 20);
		ClassroomAssignment asig = new ClassroomAssignment(classReq, classroom, listaRes);

		boolean listaResIgual = asig.getResourcesAssignments().equals(listaRes);
		boolean classroomIgual = asig.getAssignableItem().equals(classroom);
		boolean classReqIgual = asig.getRequest().equals(classReq);

		assertTrue(classroomIgual && classReqIgual && listaResIgual);
	}

	@Test
	public void test_createSatisfaction() {
		Map<Resource, Integer> requiredResources = new HashMap<Resource, Integer>();
		Map<Resource, Integer> optionalResources = new HashMap<Resource, Integer>();
		Period desiredHours = null;
		Subject subject = new Subject("Matematica", 0);
		Professor professor = new Professor("Pablo", "44445555", "pablo@gmail.com");
		ClassroomRequest classroomRequest = new ClassroomRequest(desiredHours, subject, professor, 0, requiredResources, optionalResources, 25);
		Classroom classroom = new Classroom("Aula 1", 20);
		FixedResource fr1 = new FixedResource("Proyector", 1);
		FixedResource fr2 = new FixedResource("Pizarron", 2);
		classroom.addResource(fr1);
		classroom.addResource(fr2);
		List<ResourceAssignment> resourcesAssignmentsList = new ArrayList<ResourceAssignment>();
		ClassroomAssignment classroomAssignment1 = new ClassroomAssignment(classroomRequest, classroom, resourcesAssignmentsList);
		ClassroomAssignment classroomAssignment2 = new ClassroomAssignment(classroomRequest, classroom, resourcesAssignmentsList);
		Satisfaction satisfaction1 = classroomAssignment1.createSatisfaction();
		Satisfaction satisfaction2 = classroomAssignment2.createSatisfaction();

		assertEquals(satisfaction1, satisfaction2);
	}
}
