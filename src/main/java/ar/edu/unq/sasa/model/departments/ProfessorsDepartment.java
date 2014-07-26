package ar.edu.unq.sasa.model.departments;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.data.University;
import ar.edu.unq.sasa.model.exceptions.departments.ProfessorException;

public class ProfessorsDepartment extends Department {

	// TODO kill this
	private static long idPool = 0;

	public ProfessorsDepartment(University university) {
		super(university);
	}

	public Professor createProfessor(String name, String phone, String mail) {
		return createProfessor(name, phone, mail, new LinkedList<Subject>());
	}

	public Professor createProfessor(String name, String phone, String mail, List<Subject> subjects) {
		Professor professor = new Professor(name, idPool++, phone, mail, subjects);
		this.getUniversity().addProfessor(professor);

		this.getPublisher().changed("professorsChanged", this.getProfessors());

		return professor;
	}

	public void deleteProfessor(Professor professor) {
		this.getUniversity().getProfessors().remove(professor);

		this.getPublisher().changed("professorsChanged",
				this.getUniversity().getProfessors());
	}

	@SuppressWarnings("unused")
	private void verificar(Professor professor) throws ProfessorException {
		if (professor == null)
			throw new ProfessorException("El profe no esta, el profe no esta!!");
	}

	public List<Professor> searchProfessor(String text) {
		List<Professor> res = new LinkedList<Professor>();
		for (Professor p : this.getUniversity().getProfessors())
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

		this.getPublisher().changed("professorsChanged", this.getUniversity().getProfessors());
	}

	@Override
	public List<Professor> getProfessors() {
		return getUniversity().getProfessors();
	}
}