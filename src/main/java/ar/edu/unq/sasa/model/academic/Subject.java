package ar.edu.unq.sasa.model.academic;

/** 
 * SUBJECT
 * Class denoting a Subject entity, containing his name and his ID.
 * 
 * CONSIDERATIONS
 * The id instance variable is going to be final, because no changes are allowed.
 * 
 * @author Gaston Charkiewicz
 * 
 */
public class Subject {
	
	/**
	 * ID of the subject.
	 */
	public final long id;
	
	/**
	 * Name of the Subject.
	 */
	public String name;
	
	public Subject(String aName,long anID){
		this.id = anID;
		this.name = aName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	/** 
	 * hashCode redefinition
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/** 
	 * equals redefinition
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}