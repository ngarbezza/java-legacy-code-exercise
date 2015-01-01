package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.academic.University;

import java.util.LinkedList;
import java.util.List;

public class SubjectsDepartment extends Department {

    private List<Subject> subjects;

    public SubjectsDepartment(University university) {
        super(university);
        subjects = new LinkedList<>();
    }

    public Subject createSubject(String name) {
        Subject newSubject = new Subject(name);
        addSubject(newSubject);
        return newSubject;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public boolean existSubjectNamed(String name) {
        for (Subject s : subjects)
            if (name.equals(s.getName()))
                return true;
        return false;
    }

    public Subject getSubjectNamed(String name) {
        for (Subject s : getSubjects())
            if (name.equals(s.getName()))
                return s;
        return null;
    }

    private void addSubject(Subject s) {
        subjects.add(s);
    }
}
