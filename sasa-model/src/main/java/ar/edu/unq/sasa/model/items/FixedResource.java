package ar.edu.unq.sasa.model.items;

/**
 * Representa aquellos recursos que pertenecen a un aula, de manera fija.
 * TODO make a difference between what we request and we have already in a classroom
 */
public class FixedResource implements Resource {

	private String name;

	private int amount;

	public FixedResource(String aName, int anAmount) {
		name = aName;
		amount = anAmount;
	}

	public FixedResource(String aName) {
		name = aName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int anAmount) {
		amount = anAmount;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public boolean isFixedResource() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FixedResource other = (FixedResource) obj;
		if (amount != other.amount)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean equalsName(Resource anotherResource) {
		return name.equals(anotherResource.getName());
	}
}
