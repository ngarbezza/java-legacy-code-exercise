package ar.edu.unq.sasa.model.mocks.items;

import ar.edu.unq.sasa.model.items.FixedResource;

public class MockFixedResource extends FixedResource{

	public MockFixedResource(String name, int amount) {
		super(name, amount);
	}

	public int getAmount() {
		return 0;
	}

	public void setAmount(int amount) {
	}

    
	public void setName(String newName) {
	}
}
