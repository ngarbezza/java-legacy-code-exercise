package ar.edu.unq.sasa.model.assignments;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

public class TestSuperposition {

	private Superposition superposition1; //Rightly coded superposition
	private HourInterval hourInterval1;
	private AssignmentByRequest abr11 = createMock(AssignmentByRequest.class);
	private AssignmentByRequest abr12 = createMock(AssignmentByRequest.class);
	private List<AssignmentByRequest> ls1 = new LinkedList<AssignmentByRequest>();

	private HourInterval hourInterval2;
	private AssignmentByRequest abr21 = createMock(AssignmentByRequest.class);
	private AssignmentByRequest abr22 = createMock(AssignmentByRequest.class);
	private List<AssignmentByRequest> ls2 = new LinkedList<AssignmentByRequest>();

	Superposition superposition2; //Non-rightly coded superposition
	AssignmentByRequest abr31 = createMock(AssignmentByRequest.class);
	List<AssignmentByRequest> ls4 = new LinkedList<AssignmentByRequest>();

	@Before
	public void setUp() {
		hourInterval1 = new HourInterval(new Timestamp(12), new Timestamp(13));
		hourInterval2 = new HourInterval(new Timestamp(8), new Timestamp(9));
		ls1.add(abr11);
		ls1.add(abr12);
		ls2.add(abr21);
		ls2.add(abr22);
		ls4.add(abr31);

		superposition1 = new Superposition();
		superposition2 = new Superposition();

		superposition1.getSuperpositionData().put(hourInterval1, ls1);
		superposition1.getSuperpositionData().put(hourInterval2, ls2);
		superposition2.getSuperpositionData().put(hourInterval2, ls4);
	}

	@Test
	public void allElementsHaveSuperposition() {
		Map<HourInterval, List<AssignmentByRequest> > m1 = superposition1.getSuperpositionData();

		Map<HourInterval, List<AssignmentByRequest> > m2 = superposition2.getSuperpositionData();

		List<List<AssignmentByRequest>> supElements1 = new LinkedList<List<AssignmentByRequest>>(m1.values());
		for (List<AssignmentByRequest> it : supElements1)
			assertTrue("There are elements without superpositions", it.size() > 1);

		List<List<AssignmentByRequest>> supElements2 = new LinkedList<List<AssignmentByRequest>>(m2.values());
		for (List<AssignmentByRequest> it : supElements2)
			assertFalse("There are elements without superpositions", it.size() > 1);
	}
}