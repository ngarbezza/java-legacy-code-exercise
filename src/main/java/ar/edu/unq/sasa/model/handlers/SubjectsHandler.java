package ar.edu.unq.sasa.model.handlers;

import java.util.List;

import ar.edu.unq.sasa.model.academic.Subject;

/**
 * @author Nahuel Garbezza
 *
 */
public class SubjectsHandler extends Handler {
	
	private static SubjectsHandler instance;
	
	public static SubjectsHandler getInstance() {
		if (instance == null)
			instance = new SubjectsHandler();
		return instance;
	}
	
	private static long idPool = 0;
	
	private SubjectsHandler() {}
	
	public Subject createSubject(String name) {
		Subject newSubject = new Subject(name, idPool++);
		getInformationManager().addSubject(newSubject);
		return newSubject;
	}

	public List<Subject> getSubjects() {
		return this.getInformationManager().getSubjects();
	}

	public boolean existSubjectNamed(String name) {
		return existSubjectNamed(getSubjects(), name);
	}

	public Subject getSubjectNamed(String name) {
		for (Subject s : getSubjects())
			if (name.equals(s.getName()))
				return s;
		return null;
	}

	public boolean existSubjectNamed(List<Subject> subjectList, String name) {
		for (Subject s : subjectList)
			if (name.equals(s.getName()))
				return true;
		return false;
	}
}
