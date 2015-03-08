package ar.edu.unq.sasa.model;

import ar.edu.unq.sasa.model.academic.TestProfessor;
import ar.edu.unq.sasa.model.academic.TestSubject;
import ar.edu.unq.sasa.model.assignments.*;
import ar.edu.unq.sasa.model.departments.*;
import ar.edu.unq.sasa.model.items.TestClassroom;
import ar.edu.unq.sasa.model.items.TestFixedResources;
import ar.edu.unq.sasa.model.items.TestMobileResource;
import ar.edu.unq.sasa.model.requests.TestClassroomRequest;
import ar.edu.unq.sasa.model.requests.TestMobileResourcesRequest;
import ar.edu.unq.sasa.model.time.TestAnd;
import ar.edu.unq.sasa.model.time.TestMinus;
import ar.edu.unq.sasa.model.time.TestSimplePeriod;
import ar.edu.unq.sasa.model.time.hour.TestHourInterval;
import ar.edu.unq.sasa.model.time.hour.TestTimestamp;
import ar.edu.unq.sasa.model.time.repetition.TestDaily;
import ar.edu.unq.sasa.model.time.repetition.TestMonthly;
import ar.edu.unq.sasa.model.time.repetition.TestNone;
import ar.edu.unq.sasa.model.time.repetition.TestWeekly;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

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
        TestSuperposition.class,
        TestAssignmentsDepartment.class,
        TestClassroomsDepartment.class,
        TestProfessorsDepartment.class,
        TestQueryManager.class,
        TestRequestsDepartment.class,
        TestResourcesDepartment.class,
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
public class SasaTestSuite {
}
