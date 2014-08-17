package ar.edu.unq.sasa.util;

public class PreconditionNotMetException extends RuntimeException {

	private static final long serialVersionUID = 6932836946083515828L;

	public PreconditionNotMetException(String aMessage) {
		super(aMessage);
	}

}
