package ar.edu.unq.sasa.model.handlers;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unq.sasa.model.exceptions.handlers.ClassroomException;
import ar.edu.unq.sasa.model.exceptions.handlers.ProfessorException;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.FixedResource;

/**
 * Es un handler específico de Classroom; realiza ABMC.
 * 
 * @author Diego Campos
 */
public class ClassroomHandler extends Handler {

	private static ClassroomHandler instance;
	
	public static ClassroomHandler getInstance() {
		if (instance == null)
			instance = new ClassroomHandler();
		return instance;
	}
	
	private ClassroomHandler() {}

	/**
	 * Crea una {@link Classroom} y la guarda en el sistema.
	 * 
	 * @param name el nombre de la nueva aula.
	 * @param capacity la capacidad del aula.
	 * @throws ClassroomException
	 */
	public Classroom createClassroom(String name, int capacity) throws ClassroomException{
		Classroom classroom = null;
		if (verificarUnicidad(name)) {
			classroom = new Classroom(name, capacity);	
	        this.getInformationManager().addClassroom(classroom);
		} else
			throw new ClassroomException("Ya existe un aula con ese nombre");

		this.getPublisher().changed("classroomsChanged", this.getInformationManager().getClassrooms());

		return classroom;
	}

	private boolean verificarUnicidad(String name) {
		boolean unicidad = true;
		for (Classroom currentClassrooom : getInformationManager().getClassrooms())
			if (currentClassrooom.getName().equals(name))
			  unicidad = false;
		return unicidad;			
	}

	/**
	 * Borra una {@link Classroom} del sistema.
	 * 
	 * @param classroom la {@link Classroom} a borrar.
	 */
	public void deleteClassroom(Classroom classroom) {
		getInformationManager().deleteClassroom(classroom);

		getPublisher().changed("classroomsChanged", getInformationManager().getClassrooms());
	}

	/**
	 * Borra una {@link Classroom} buscándola por nombre. 
	 * 
	 * @param name el nombre de la {@link Classroom} a buscar.
	 * @throws ClassroomException
	 */
	public void deleteClassroom(String name) throws ClassroomException{
		this.deleteClassroom(this.searchClassroom(name));
		
		getPublisher().changed("classroomsChanged", getInformationManager().getClassrooms());
	}

	/**
	 * Método de búsqueda de {@link Classroom} a partir del nombre y
	 * la capacidad.
	 * 
	 * @param name el nombre de la {@link Classroom} a buscar.
	 * @return la {@link Classroom} encontrada, o <code>null</code>
	 * 		si no la encuentra.
	 * @throws ClassroomException
	 */
	public Classroom searchClassroom(String name)throws ClassroomException{
		Classroom wantedClassroom = null;
		for (Classroom currentClassroom : this.getInformationManager().getClassrooms())
			if(currentClassroom.getName().equals(name))
				wantedClassroom  = currentClassroom ;
		this.verificar(wantedClassroom);
		return wantedClassroom;
	}
	
	/**
	 * Verifica que una instancia de classroom no sea null
	 * @param professor recibe una instancia de professor
	 * @throws ProfessorException si o que recibe es null lanza una excepcion
	 */
	private void verificar(Classroom classroom) throws ClassroomException {
		if (classroom == null)
			throw new ClassroomException("No existe el aula");
	}
	
	/**
	 * Busca el classroom y le Modifica la capacidad 
	 * @param name
	 * @param capacity
	 * @throws ClassroomException
	 */
	public void modifyClassroom(String name,int capacity) throws  ClassroomException{
	 	this.searchClassroom(name).setCapacity(capacity);
	}
	
	public void modifyClassroomAddResource(String name, FixedResource resource) throws ClassroomException{
		this.searchClassroom(name).addResource(resource);
	}
	
	public List<Classroom> searchClassroomByName(String name) {
		List<Classroom> res = new LinkedList<Classroom>();
		for (Classroom c : getInformationManager().getClassrooms())
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
		
		getPublisher().changed("classroomsChanged", getInformationManager().getClassrooms());
	}
}
