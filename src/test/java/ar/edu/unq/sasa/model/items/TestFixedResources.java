package ar.edu.unq.sasa.model.items;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestFixedResources extends TestCase {

	public void testFixedResource() {
		FixedResource resource = new FixedResource("PC" , 10);
        Assert.assertEquals("PC", resource.getName()) ;
        Assert.assertEquals(10, resource.getAmount()) ;
	}
	
}
