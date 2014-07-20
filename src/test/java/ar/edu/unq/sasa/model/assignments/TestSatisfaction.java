package ar.edu.unq.sasa.model.assignments;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class TestSatisfaction {

	@Test
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
