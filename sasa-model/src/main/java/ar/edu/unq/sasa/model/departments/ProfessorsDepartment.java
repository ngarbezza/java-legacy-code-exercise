package ar.edu.unq.sasa.model.departments;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;

public class ProfessorsDepartment extends Department {

	private List<Professor> professors;

	public ProfessorsDepartment(University university) {
		super(university);
		professors = new LinkedList<Professor>();
	}

	public Professor createProfessor(String name, String phone, String mail) {
		return createProfessor(name, phone, mail, new LinkedList<Subject>());
	}

	public Professor createProfessor(String name, String phone, String mail, List<Subject> subjects) {
		Professor professor = new Professor(name, phone, mail, subjects);
		addProfessor(professor);

		getPublisher().changed("professorsChanged", professors);

		return professor;
	}

	public List<Professor> getProfessors() {
		return professors;
	}

	public void addProfessor(Professor professor) {
		professors.add(professor);
	}

	public void deleteProfessor(Professor professor) {
		professors.remove(professor);

		getPublisher().changed("professorsChanged", professors);
	}

	public List<Professor> searchProfessor(String text) {
		List<Professor> res = new LinkedList<Professor>();
		for (Professor p : getProfessors())
			if (p.getName().contains(text))
				res.add(p);
		return res;
	}

	public void modifyProfessor(Professor p, String name, String phone, String mail, List<Subject> subjects) {
		p.setName(name);
		p.setPhoneNumber(phone);
		p.setMail(mail);
		p.getSubjects().clear();
		for (Subject s : subjects)
			p.addNewSubject(s);

		getPublisher().changed("professorsChanged", professors);
	}
}