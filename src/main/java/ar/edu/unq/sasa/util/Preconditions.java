package ar.edu.unq.sasa.util;

public final class Preconditions {

	private Preconditions() { }

	public static void precondition(String description, Boolean condition) {
		if (!condition)
			throw new PreconditionNotMetException("PRECONDITION NOT MET: " + description);
	}
}
