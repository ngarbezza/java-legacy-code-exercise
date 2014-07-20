package ar.edu.unq.sasa.model.assignments;

import ar.edu.unq.sasa.model.academic.Request;

/**
 * Clase abstracta, que representa las {@link Assignment} que fueron hechas 
 * por medio de un {@link Request}.
 */
public abstract class AssignmentByRequest implements Assignment {
	
	private Request request;
	
	public AssignmentByRequest(Request req) {
		req.setAsignated(true);
		request = req;
	}
	
	@Override
	public Request getRequest() {
		return request;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((request == null) ? 0 : request.hashCode());
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
		AssignmentByRequest other = (AssignmentByRequest) obj;
		if (request == null) {
			if (other.request != null)
				return false;
		} else if (!request.equals(other.request))
			return false;
		return true;
	}
}