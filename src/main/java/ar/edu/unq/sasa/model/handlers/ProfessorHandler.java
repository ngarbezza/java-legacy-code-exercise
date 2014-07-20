package ar.edu.unq.sasa.model.handlers;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.exceptions.handlers.ProfessorException;

/**
 * Es un handler específico de Professor; realiza ABMC.
 */
public class ProfessorHandler extends Handler {
	
	private static ProfessorHandler instance;
	
	public static ProfessorHandler getInstance() {
		if (instance == null)
			instance = new ProfessorHandler();
		return instance;
	}
	
	private static long idPool = 0;
	
	private ProfessorHandler() {}
	
	public Professor createProfessor(String name, String phone, String mail) {
		return createProfessor(name, phone, mail, new LinkedList<Subject>());
	}
	
	public Professor createProfessor(String name, String phone, String mail, List<Subject> subjects) {
		Professor professor = new Professor(name, idPool++, phone, mail, subjects);
		this.getInformationManager().addProfessor(professor);
		
		this.getPublisher().changed("professorsChanged", 
				this.getInformationManager().getProfessors());
		
		return professor;
	}

	public void deleteProfessor(Professor professor) {
		this.getInformationManager().getProfessors().remove(professor);
		
		this.getPublisher().changed("professorsChanged", 
				this.getInformationManager().getProfessors());	
	}

	@SuppressWarnings("unused")
	private void verificar(Professor professor) throws ProfessorException {
		if (professor == null)
			throw new ProfessorException("El profe no esta, el profe no esta!!");
	}
	
	public List<Professor> searchProfessor(String text) {
		List<Professor> res = new LinkedList<Professor>();
		for (Professor p : this.getInformationManager().getProfessors())
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
		
		this.getPublisher().changed("professorsChanged", this.getInformationManager().getProfessors());
	}

	public List<Professor> getProfessors() {
		return getInformationManager().getProfessors();
	}
}