package ar.edu.unq.sasa.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

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
import ar.edu.unq.sasa.model.handlers.TestClassroomHandler;
import ar.edu.unq.sasa.model.handlers.TestProfessorHandler;
import ar.edu.unq.sasa.model.handlers.TestQueryManager;
import ar.edu.unq.sasa.model.handlers.TestRequestsHandler;
import ar.edu.unq.sasa.model.handlers.TestResourcesHandler;
import ar.edu.unq.sasa.model.items.TestClassroom;
import ar.edu.unq.sasa.model.items.TestFixedResources;
import ar.edu.unq.sasa.model.items.TestMobileResource;
import ar.edu.unq.sasa.model.time.TestAnd;
import ar.edu.unq.sasa.model.time.TestMinus;
import ar.edu.unq.sasa.model.time.TestSimplePeriod;
import ar.edu.unq.sasa.model.time.hour.TestHourInterval;
import ar.edu.unq.sasa.model.time.hour.TestTimestamp;
import ar.edu.unq.sasa.model.time.repetition.TestDaily;
import ar.edu.unq.sasa.model.time.repetition.TestMonthly;
import ar.edu.unq.sasa.model.time.repetition.TestNone;
import ar.edu.unq.sasa.model.time.repetition.TestWeekly;

@RunWith(Suite.class)
@SuiteClasses({
	TestClassroomRequest.class,
	TestMobileResourcesRequest.class,
	TestProfessor.class,
	TestSubject.class,
	TestBookedAssignment.class,
	TestClassroomAssignment.class,
	TestResourceAssignment.class,
	TestSatisfaction.class,
	TestInformationManager.class,
	TestSuperposition.class,
	TestWeeklySchedule.class,
	TestAsignator.class,
	TestClassroomHandler.class,
	TestProfessorHandler.class,
	TestQueryManager.class,
	TestRequestsHandler.class,
	TestResourcesHandler.class,
	TestClassroom.class,
	TestFixedResources.class,
	TestMobileResource.class,
	TestAnd.class,
	TestMinus.class,
	ar.edu.unq.sasa.model.time.TestOr.class,
	TestSimplePeriod.class,
	TestHourInterval.class,
	ar.edu.unq.sasa.model.time.hour.TestOr.class,
	TestTimestamp.class,
	TestDaily.class,
	TestMonthly.class,
	TestNone.class,
	TestWeekly.class
})
public class SasaTestSuite {}