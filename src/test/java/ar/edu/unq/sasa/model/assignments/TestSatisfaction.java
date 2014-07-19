package ar.edu.unq.sasa.model.assignments;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Test Case para la clase {@link Satisfaction}.
 * 
 * @author Cristian
 *
 */
public class TestSatisfaction extends TestCase {
	
	public void test_shouldConstructCorrectly(){
		Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
		Map<Period, Float> timeDifference = new HashMap<Period, Float>();
		int capacity = 10;
		
		Satisfaction satis = new Satisfaction(resources, timeDifference, capacity);
		boolean resourcesIgual = satis.getResources().equals(resources);
		boolean timeDifferenceIgual = satis.getTimeDifference() == timeDifference;
		assertTrue(resourcesIgual && timeDifferenceIgual);
	}
}
