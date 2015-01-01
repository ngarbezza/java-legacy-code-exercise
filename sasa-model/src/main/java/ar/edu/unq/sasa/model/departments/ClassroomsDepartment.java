package ar.edu.unq.sasa.model.departments;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.exceptions.departments.ClassroomException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassroomsDepartment extends Department {

    private List<Classroom> classrooms;

    public ClassroomsDepartment(University university) {
        super(university);
        classrooms = new LinkedList<>();
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void addClassroom(Classroom classroom) {
        classrooms.add(classroom);
    }

    public Classroom createClassroom(String name, int capacity) {
        if (hasClassroomNamed(name))
            throw new ClassroomException("Ya existe un aula con ese nombre");
        Classroom addedClassroom = new Classroom(name, capacity);
        addClassroom(addedClassroom);
        getPublisher().changed("classroomsChanged", classrooms);
        return addedClassroom;
    }

    public boolean hasClassroomNamed(String aName) {
        for (Classroom classroom : classrooms)
            if (classroom.getName().equals(aName))
                return true;
        return false;
    }

    public void deleteClassroom(Classroom aClassroom) {
        classrooms.remove(aClassroom);

        getPublisher().changed("classroomsChanged", classrooms);
    }

    public Classroom searchClassroom(String aName) {
        for (Classroom currentClassroom : classrooms)
            if (currentClassroom.getName().equals(aName))
                return currentClassroom;
        throw new ClassroomException("No existe el aula");
    }

    public void modifyClassroom(String aName, int aCapacity) {
        searchClassroom(aName).setCapacity(aCapacity);
    }

    public void modifyClassroomAddResource(String aName, FixedResource aResource) {
        searchClassroom(aName).addResource(aResource);
    }

    public List<Classroom> searchClassroomByName(String aName) {
        return classrooms.stream()
                .filter(c -> c.getName().contains(aName))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void modifyClassroomProperties(Classroom aClassroom, String aName, int aCapacity,
                                          List<FixedResource> resources) {
        aClassroom.setName(aName);
        aClassroom.setCapacity(aCapacity);
        aClassroom.getResources().clear();
        resources.forEach(aClassroom::addResource);

        getPublisher().changed("classroomsChanged", classrooms);
    }
}
