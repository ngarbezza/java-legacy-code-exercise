package ar.edu.unq.sasa.model.departments;

import java.util.List;

import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;

public class SubjectsDepartment extends Department {

	// TODO move list of subjects from university to here

	// TODO kill this
	private static long idPool = 0;

	public SubjectsDepartment(University university) {
		super(university);
	}

	public Subject createSubject(String name) {
		Subject newSubject = new Subject(name, idPool++);
		getUniversity().addSubject(newSubject);
		return newSubject;
	}

	public List<Subject> getSubjects() {
		return this.getUniversity().getSubjects();
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
