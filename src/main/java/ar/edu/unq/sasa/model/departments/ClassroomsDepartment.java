package ar.edu.unq.sasa.model.departments;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.academic.University;
import ar.edu.unq.sasa.model.exceptions.departments.ClassroomException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

public class ClassroomsDepartment extends Department {

	private List<Classroom> classrooms;

	public ClassroomsDepartment(University university) {
		super(university);
		classrooms = new LinkedList<Classroom>();
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

	public boolean hasClassroomNamed(String name) {
		for(Classroom classroom: classrooms)
			if (classroom.getName().equals(name))
				return true;
		return false;
	}

	public void deleteClassroom(Classroom classroom) {
		classrooms.remove(classroom);

		getPublisher().changed("classroomsChanged", classrooms);
	}

	public Classroom searchClassroom(String name) {
		for (Classroom currentClassroom : classrooms)
			if(currentClassroom.getName().equals(name))
				return currentClassroom;
		throw new ClassroomException("No existe el aula");
	}

	public void modifyClassroom(String name, int capacity) {
	 	searchClassroom(name).setCapacity(capacity);
	}

	public void modifyClassroomAddResource(String name, FixedResource resource) {
		searchClassroom(name).addResource(resource);
	}

	public List<Classroom> searchClassroomByName(String name) {
		List<Classroom> res = new LinkedList<Classroom>();
		for (Classroom c : classrooms)
			if (c.getName().contains(name))
				res.add(c);
		return res;
	}

	public void modifyClassroomProperties(Classroom c, String name, int cap, List<FixedResource> resources) {
		c.setName(name);
		c.setCapacity(cap);
		c.getResources().clear();
		for (FixedResource r : resources)
			c.addResource(r);

		getPublisher().changed("classroomsChanged", classrooms);
	}
}
