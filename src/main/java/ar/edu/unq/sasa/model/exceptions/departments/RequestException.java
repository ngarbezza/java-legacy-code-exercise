package ar.edu.unq.sasa.model.exceptions.departments;

public class RequestException extends RuntimeException {

	private static final long serialVersionUID = -6896894711717621482L;

	public RequestException(String message) {
		super(message);
	}
}
