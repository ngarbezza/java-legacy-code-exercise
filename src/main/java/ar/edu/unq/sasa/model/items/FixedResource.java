package ar.edu.unq.sasa.model.items;

/**
 * Representa aquellos recursos que pertenecen a un aula, de manera fija.
 * 
 * @author Diego Campos
 *
 */
public class FixedResource implements Resource{
	
	private String name;
	
	private int amount;
	
	public FixedResource(String name, int amount) {
		this.name = name;
		this.amount = amount;
	}
	
	/**
	 * Constructor que sirve para los {@link Request}
	 * @param name
	 */
	public FixedResource(String name){}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		this.name = newName;
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
	public String toString(){
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

	public boolean equalsName(Resource anotherResource){
		return this.getName().equals(anotherResource.getName());
	}
}
