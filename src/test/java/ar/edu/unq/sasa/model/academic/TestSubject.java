package ar.edu.unq.sasa.model.academic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestSubject {

	public Subject subject;

	@Before
	public void setUp() {
		subject = new Subject("Álgebra y Geometría Analítica");
	}

	@Test
	public void test_GetName() {
		assertEquals("Álgebra y Geometría Analítica", subject.getName());
	}
}
