package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessorsDepartment extends Department {

    private List<Professor> professors;

    public ProfessorsDepartment(University university) {
        super(university);
        professors = new LinkedList<>();
    }

    public Professor createProfessor(String name, String phone, String mail) {
        return createProfessor(name, phone, mail, new LinkedList<>());
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
        return getProfessors().stream()
                .filter(p -> p.getName().contains(text))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void modifyProfessor(Professor aProfessor, String aName, String aPhoneNumber, String anEmail, List<Subject> subjects) {
        aProfessor.setName(aName);
        aProfessor.setPhoneNumber(aPhoneNumber);
        aProfessor.setMail(anEmail);
        aProfessor.getSubjects().clear();
        subjects.forEach(aProfessor::addNewSubject);

        getPublisher().changed("professorsChanged", professors);
    }
}
