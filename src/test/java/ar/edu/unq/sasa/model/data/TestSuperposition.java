package ar.edu.unq.sasa.model.data;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.assignments.AssignmentByRequest;
import ar.edu.unq.sasa.model.time.hour.HourInterval;

public class TestSuperposition {

	public Superposition superposition1; //Rightly coded superposition
	public HourInterval hourInterval1 = createMock(HourInterval.class);
	public AssignmentByRequest abr11 = createMock(AssignmentByRequest.class);
	public AssignmentByRequest abr12 = createMock(AssignmentByRequest.class);
	public List<AssignmentByRequest> ls1 = new LinkedList<AssignmentByRequest>();

	public HourInterval hourInterval2 = createMock(HourInterval.class);
	public AssignmentByRequest abr21 = createMock(AssignmentByRequest.class);
	public AssignmentByRequest abr22 = createMock(AssignmentByRequest.class);
	public List<AssignmentByRequest> ls2 = new LinkedList<AssignmentByRequest>();

	Superposition superposition2; //Non-rightly coded superposition
	AssignmentByRequest abr31 = createMock(AssignmentByRequest.class);
	List<AssignmentByRequest> ls4 = new LinkedList<AssignmentByRequest>();

	@Before
	public void setUp(){
		this.ls1.add(abr11);this.ls1.add(abr12); //Adding elements to each list.
		this.ls2.add(abr21);this.ls2.add(abr22);
		this.ls4.add(abr31);
		
		this.superposition1 = new Superposition();
		this.superposition2 = new Superposition();
		
		this.superposition1.getSuperpositionData().put(hourInterval1,ls1);
		this.superposition1.getSuperpositionData().put(hourInterval2,ls2);
		this.superposition2.getSuperpositionData().put(hourInterval2,ls4);
		//Adding elements to each superposition
	}

	@Test
	public void test_shouldConstructCorrectly(){
		assertNotNull("Fail - traceback: constructor",this.superposition1.getSuperpositionData());
		assertNotNull("Fail - traceback: constructor",this.superposition2.getSuperpositionData());
	}

	@Test
	public void test_allElementsHaveSuperposition(){
		Map<HourInterval,List<AssignmentByRequest> > m1 = this.superposition1.getSuperpositionData();

		Map<HourInterval,List<AssignmentByRequest> > m2 = this.superposition2.getSuperpositionData();

		List<List<AssignmentByRequest>> l_m1 = new LinkedList<List<AssignmentByRequest>>(m1.values());
		for(List<AssignmentByRequest> it : l_m1){
			assertTrue("There are elements without superpositions",it.size()>1);
		}
		
		List<List<AssignmentByRequest>> l_m2 = new LinkedList<List<AssignmentByRequest>>(m2.values());
		for(List<AssignmentByRequest> it : l_m2){
			assertFalse("There are elements without superpositions",it.size()>1);
		}
	}
}
