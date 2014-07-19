package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.items.AssignableItem;

/**
 * Assignment que se utiliza para representar asignaciones hechas sin un 
 * pedido, que representan reservas.
 *
 * @author Cristian Suarez
 */
public class BookedAssignment implements Assignment {
	private AssignableItem assignableItem;
	private String cause;
	
	public BookedAssignment(String aCause, AssignableItem anAssignableItem) {
		cause = aCause;
		assignableItem = anAssignableItem;
	}
	
	public String getCause(){
		return cause;
	}
	
	public void setCause(String c) {
		cause = c;
	}

	public AssignableItem getAssignableItem() {
		return assignableItem;
	}
	
	/** 
	 * Aclaración: no es un getter, sirve para hacer más polimórficos algunos 
	 * métodos utilizados.
	 */
	public Request getRequest() {
		return null;
	}
		
	/** 
	 * Para hacer Double Dispatching.
	 */
	@Override
	public boolean isBookedAssignment() {
		return true;
	}
	
	public boolean isClassroomAssignment() {
		return false;		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cause == null) ? 0 : cause.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookedAssignment other = (BookedAssignment) obj;
		if (assignableItem == null) {
			if (other.assignableItem != null)
				return false;
		} else if (!assignableItem.equals(other.assignableItem))
			return false;
		if (cause == null) {
			if (other.cause != null)
				return false;
		} else if (!cause.equals(other.cause))
			return false;
		return true;
	}

	@Override
	public boolean isResourceAssignment() {
		return false;
	}
}