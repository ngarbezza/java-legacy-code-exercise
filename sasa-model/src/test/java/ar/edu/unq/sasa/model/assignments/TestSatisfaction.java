package ar.edu.unq.sasa.model.assignments;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ar.edu.unq.sasa.model.items.FixedResource;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

public class TestSatisfaction {

	@Test
	public void testShouldConstructCorrectly() {
		Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
		Map<Period, Float> timeDifference = new HashMap<Period, Float>();
		int capacity = 10;

		Satisfaction satis = new Satisfaction(resources, timeDifference, capacity);
		boolean resourcesIgual = satis.getResources().equals(resources);
		boolean timeDifferenceIgual = satis.getTimeDifference() == timeDifference;
		assertTrue(resourcesIgual && timeDifferenceIgual);
	}

	@Test
	public void itIsSatisfiedIfTheCapacityDifferenceIsPossitiveAndResourcesAndTimeDifferencesAreEmpty() {
		Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
		Map<Period, Float> timeDifference = new HashMap<Period, Float>();
		int capacity = 1;

		Satisfaction satisfaction = new Satisfaction(resources, timeDifference, capacity);
		assertTrue(satisfaction.isSatisfied());
	}

	@Test
	public void itIsNotSatisfiedIfThereAreResourcesPendingToAssign() {
		Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
		resources.put(new FixedResource("Proyector"), 1);
		Map<Period, Float> timeDifference = new HashMap<Period, Float>();
		int capacity = 10;

		Satisfaction satisfaction = new Satisfaction(resources, timeDifference, capacity);
		assertFalse(satisfaction.isSatisfied());
	}

	@Test
	public void itIsNotSatisfiedIfTheCapacityDifferenceIsNegative() {
		Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
		Map<Period, Float> timeDifference = new HashMap<Period, Float>();
		int capacity = -1;

		Satisfaction satisfaction = new Satisfaction(resources, timeDifference, capacity);
		assertFalse(satisfaction.isSatisfied());
	}
}
