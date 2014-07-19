package ar.edu.unq.sasa.model;

import junit.framework.Test;
import junit.framework.TestSuite;
import ar.edu.unq.sasa.model.academic.TestClassroomRequest;
import ar.edu.unq.sasa.model.academic.TestMobileResourcesRequest;
import ar.edu.unq.sasa.model.academic.TestProfessor;
import ar.edu.unq.sasa.model.academic.TestSubject;
import ar.edu.unq.sasa.model.assignments.TestBookedAssignment;
import ar.edu.unq.sasa.model.assignments.TestClassroomAssignment;
import ar.edu.unq.sasa.model.assignments.TestResourceAssignment;
import ar.edu.unq.sasa.model.assignments.TestSatisfaction;
import ar.edu.unq.sasa.model.data.TestInformationManager;
import ar.edu.unq.sasa.model.data.TestSuperposition;
import ar.edu.unq.sasa.model.data.TestWeeklySchedule;
import ar.edu.unq.sasa.model.handlers.TestAsignator;
import ar.edu.unq.sasa.model.handlers.TestClassroomhandler;
import ar.edu.unq.sasa.model.handlers.TestProfessorHandler;
import ar.edu.unq.sasa.model.handlers.TestQueryManager;
import ar.edu.unq.sasa.model.handlers.TestRequestsHandler;
import ar.edu.unq.sasa.model.handlers.TestResourcesHandler;
import ar.edu.unq.sasa.model.items.TestClassroom;
import ar.edu.unq.sasa.model.items.TestFixedResources;
import ar.edu.unq.sasa.model.time.TestAnd;
import ar.edu.unq.sasa.model.time.TestMinus;
import ar.edu.unq.sasa.model.time.TestSimplePeriod;
import ar.edu.unq.sasa.model.time.hour.TestHourInterval;
import ar.edu.unq.sasa.model.time.hour.TestTimestamp;
import ar.edu.unq.sasa.model.time.repetition.TestDaily;
import ar.edu.unq.sasa.model.time.repetition.TestMonthly;
import ar.edu.unq.sasa.model.time.repetition.TestNone;
import ar.edu.unq.sasa.model.time.repetition.TestWeekly;

/**
 * Test Suite que contiene todos los tests del proyecto.
 * 
 * @author Todos
 * 
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("SASA - Test Suite");
		// Clases de Nahuel
		suite.addTestSuite(TestSimplePeriod.class);
		suite.addTestSuite(TestNone.class);
		suite.addTestSuite(TestDaily.class);
		suite.addTestSuite(TestWeekly.class);
		suite.addTestSuite(TestMonthly.class);
		suite.addTestSuite(TestAnd.class);
		suite.addTestSuite(TestMinus.class);
		suite.addTestSuite(ar.edu.unq.sasa.model.time.TestOr.class);
		suite.addTestSuite(TestTimestamp.class);
		suite.addTestSuite(TestHourInterval.class);
		suite.addTestSuite(ar.edu.unq.sasa.model.time.hour.TestOr.class);
		// Clases de Cristian
		suite.addTestSuite(TestBookedAssignment.class);
		suite.addTestSuite(TestClassroomAssignment.class);
		suite.addTestSuite(TestResourceAssignment.class);
		suite.addTestSuite(TestSatisfaction.class);
		suite.addTestSuite(TestRequestsHandler.class);
		suite.addTestSuite(TestResourcesHandler.class);
		// Clases de Gaston
		suite.addTestSuite(TestClassroomRequest.class);
		suite.addTestSuite(TestMobileResourcesRequest.class);
		suite.addTestSuite(TestProfessor.class);
		suite.addTestSuite(TestSubject.class);
		suite.addTestSuite(TestSuperposition.class);
		suite.addTestSuite(TestWeeklySchedule.class);
		// Clases de Diego
		suite.addTestSuite(TestClassroom.class);
		suite.addTestSuite(TestClassroomhandler.class);
		suite.addTestSuite(TestClassroom.class);
		suite.addTestSuite(TestFixedResources.class);
		suite.addTestSuite(TestInformationManager.class);
		suite.addTestSuite(TestMobileResourcesRequest.class);
		suite.addTestSuite(TestProfessorHandler.class);
		// Clases de Todos
		suite.addTestSuite(TestQueryManager.class);
		suite.addTestSuite(TestAsignator.class);
		return suite;
	}
}
