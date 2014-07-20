package ar.edu.unq.sasa.gui;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Professor;
import ar.edu.unq.sasa.model.academic.Subject;
import ar.edu.unq.sasa.model.data.InformationManager;
import ar.edu.unq.sasa.model.handlers.Asignator;
import ar.edu.unq.sasa.model.handlers.ClassroomHandler;
import ar.edu.unq.sasa.model.handlers.ProfessorHandler;
import ar.edu.unq.sasa.model.handlers.RequestsHandler;
import ar.edu.unq.sasa.model.handlers.ResourcesHandler;
import ar.edu.unq.sasa.model.handlers.SubjectsHandler;
import ar.edu.unq.sasa.model.items.Classroom;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;
import ar.edu.unq.sasa.model.time.SimplePeriod;
import ar.edu.unq.sasa.model.time.hour.HourInterval;
import ar.edu.unq.sasa.model.time.hour.LogicalHourFulfiller;
import ar.edu.unq.sasa.model.time.hour.Timestamp;

/**
 * Clase que se encarga de iniciar el sistema, con algunos datos de prueba.
 */
public class Main {

	public static void main(String[] args) {
		// MODELO
		InformationManager info = InformationManager.getInstance();
		agregarValoresDePrueba(info);
		
		// INTERFAZ GRÁFICA
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (Exception e) {}
		
		new MainWindow();
	}

	private static void agregarValoresDePrueba(InformationManager informationManager) {
		
		try {
		
		ClassroomHandler classroomHandler = ClassroomHandler.getInstance();
		ProfessorHandler professorHandler = ProfessorHandler.getInstance();
		ResourcesHandler resourcesHandler = ResourcesHandler.getInstance();
		RequestsHandler requestsHandler = RequestsHandler.getInstance();
		SubjectsHandler subjectsHandler = SubjectsHandler.getInstance();
		Asignator asignator = Asignator.getInstance();
		
		//Creación de 10 aulas de capacidad 30
		classroomHandler.createClassroom("Aula 1", 30);
		classroomHandler.createClassroom("Aula 2", 30);
		classroomHandler.createClassroom("Aula 3", 30);
		classroomHandler.createClassroom("Aula 4", 30);
		classroomHandler.createClassroom("Aula 5", 30);
		classroomHandler.createClassroom("Aula 6", 30);
		classroomHandler.createClassroom("Aula 7", 30);
		classroomHandler.createClassroom("Aula 8", 30);
		classroomHandler.createClassroom("Aula 9", 30);
		classroomHandler.createClassroom("Aula 10",30);
	
		//creacion de 10 aulas de capacidad 15
		classroomHandler.createClassroom("Aula 11", 15);
		classroomHandler.createClassroom("Aula 12", 15);
		classroomHandler.createClassroom("Aula 13", 15);
		classroomHandler.createClassroom("Aula 14", 15);
		classroomHandler.createClassroom("Aula 15", 15);
		classroomHandler.createClassroom("Aula 16", 15);
		classroomHandler.createClassroom("Aula 17", 15);
		classroomHandler.createClassroom("Aula 18", 15);
		classroomHandler.createClassroom("Aula 19", 15);
		classroomHandler.createClassroom("Aula 20", 15);
		
		
		//creacion de 5 aulas de capacidad 25
		classroomHandler.createClassroom("Aula 21", 25);
		classroomHandler.createClassroom("Aula 22", 25);
		classroomHandler.createClassroom("Aula 23", 25);
		classroomHandler.createClassroom("Aula 24", 25);
		classroomHandler.createClassroom("Aula 25", 25);
		
		//creacion de 5 aulas de capacidad 40
		classroomHandler.createClassroom("Aula 26", 40);
		classroomHandler.createClassroom("Aula 27", 40);
		classroomHandler.createClassroom("Aula 28", 40);
		classroomHandler.createClassroom("Aula 29", 40);
		classroomHandler.createClassroom("Aula 30", 40);
		
	
		//************************************************************************\\
		//---------------------Creacion de fixedResources-------------------------\\
		//************************************************************************\\
		
		//Creacion de 30 Computadoras y las agrego a 3 aulas de 30
		classroomHandler.modifyClassroomAddResource("Aula 1", resourcesHandler.createFixedResource("Computadora", 30)   );
	    classroomHandler.modifyClassroomAddResource("Aula 2", resourcesHandler.createFixedResource("Computadora", 30)   );
	    classroomHandler.modifyClassroomAddResource("Aula 3", resourcesHandler.createFixedResource("Computadora", 30)   );
	  
	    //Agrego 1 Proyector a 2 aulas de 30
	    classroomHandler.modifyClassroomAddResource("Aula 2", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 3", resourcesHandler.createFixedResource("Proyector", 1)   );
	    
	    //Agrego a 5 aulas de 15, 15 computadoras
	    classroomHandler.modifyClassroomAddResource("Aula 11", resourcesHandler.createFixedResource("Computadora", 30) );
	    classroomHandler.modifyClassroomAddResource("Aula 12", resourcesHandler.createFixedResource("Computadora", 30) );
	    classroomHandler.modifyClassroomAddResource("Aula 13", resourcesHandler.createFixedResource("Computadora", 30) );
	    classroomHandler.modifyClassroomAddResource("Aula 14", resourcesHandler.createFixedResource("Computadora", 30) );
	    classroomHandler.modifyClassroomAddResource("Aula 15", resourcesHandler.createFixedResource("Computadora", 30) );
	    
	    //Agrego a 8 aulas de 15, 1 proyector
	    classroomHandler.modifyClassroomAddResource("Aula 11", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 12", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 13", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 14", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 16", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 17", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 18", resourcesHandler.createFixedResource("Proyector", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 19", resourcesHandler.createFixedResource("Proyector", 1) );
	    
	    //Agrego luz natural a las aulas de 25 y de 40
	    classroomHandler.modifyClassroomAddResource("Aula 20", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 21", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 22", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 23", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 24", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 25", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 26", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 27", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 28", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    classroomHandler.modifyClassroomAddResource("Aula 29", resourcesHandler.createFixedResource("Luz Natural", 1) );
	    
	    
	    //************************************************************************\\
		//---------------------Creacion de MobileResources------------------------\\
		//************************************************************************\\
	    
	    resourcesHandler.createMobileResource("Proyector");
	    resourcesHandler.createMobileResource("Proyector");
	    resourcesHandler.createMobileResource("Pizarron Mobil");
	    resourcesHandler.createMobileResource("Cortina");
	    resourcesHandler.createMobileResource("Borrador");
	    resourcesHandler.createMobileResource("Fibron");
	    
	    //**********************************************************************\\
		//------------------------Creacion de profesores y Materias-------------\\
		//**********************************************************************\\
	    
	    Professor profesorTamara    = professorHandler.createProfessor("Tamara", "42585325" , "tamara@zaza.com");
	    Professor profesorPepe      = professorHandler.createProfessor("Pepe", "42583575", "pepe@zaza.com");
	    Professor profesorJose      = professorHandler.createProfessor("Jose", "42583575", "jose@zaza.com");
	    Professor profesorJuan      = professorHandler.createProfessor("Juan", "42583575", "juan@zaza.com");
	    Professor profesorErnesto   = professorHandler.createProfessor("Ernesto", "42583575", "kingOfTheNight@zaza.com");
	    Professor profesorEsteban   = professorHandler.createProfessor("Esteban", "42583575", "Mr@zaza.com");
	    Professor profesorJulieta	= professorHandler.createProfessor("Julieta", "42583575", "julieta@zaza.com");
	    Professor profesorAlejandra	= professorHandler.createProfessor("alejandra", "42583575", "alejandra@zaza.com");
	    Professor profesorCesar		= professorHandler.createProfessor("Cesar", "42583575", "cesar@zaza.com");
	    Professor profesorElizabeth = professorHandler.createProfessor("Elizabeth", "42583575", "elizabeth@zaza.com");
	    
	    Subject am1 = subjectsHandler.createSubject("Analisis Matematico 1");
	    Subject am2 = subjectsHandler.createSubject("Analisis Matematico 2");
	    Subject ha = subjectsHandler.createSubject("Historia Argentina");
	    Subject hu = subjectsHandler.createSubject("Historia Argentina");
	    Subject ha2 = subjectsHandler.createSubject("Historia Argentina 2");
	    Subject i1 = subjectsHandler.createSubject("Ingles 1");
	    Subject i2 = subjectsHandler.createSubject("Ingles 2");
	    Subject ppp = subjectsHandler.createSubject("Principios de la Psicologia Postmoderna");
	    Subject pc = subjectsHandler.createSubject("Psicologia Contemporanea");
	    Subject f1 = subjectsHandler.createSubject("Fisica 1");
	    Subject f2 = subjectsHandler.createSubject("Fisica 2");
	    profesorTamara.addNewSubject(am1);
	    profesorTamara.addNewSubject(am2);
	    profesorPepe.addNewSubject(am1);
	    profesorPepe.addNewSubject(am2);
	    profesorJose.addNewSubject(ha);
	    profesorJose.addNewSubject(hu);
	    profesorJuan.addNewSubject(ha);
	    profesorJuan.addNewSubject(ha2);
	    profesorErnesto.addNewSubject(ha);
	    profesorErnesto.addNewSubject(ha2);
	    profesorEsteban.addNewSubject(i1);
	    profesorEsteban.addNewSubject(i2);
	    profesorJulieta.addNewSubject(ppp);
	    profesorJulieta.addNewSubject(pc);
	    profesorAlejandra.addNewSubject(f1);
	    profesorAlejandra.addNewSubject(f2);
	    profesorCesar.addNewSubject(f1);
	    profesorCesar.addNewSubject(f2);
	    profesorElizabeth.addNewSubject(f1);
	    profesorElizabeth.addNewSubject(f2);
	    profesorElizabeth.addNewSubject(am1);
	    profesorElizabeth.addNewSubject(am2);
	    
	    //Pedido 1
	    //crear requisitos para los pedidos
	    	//obligatorios
	    Map<Resource,Integer> requiredResources = new HashMap<Resource,Integer>();
	    requiredResources.put( resourcesHandler.createFixedResource("Computadora", 20) , 20) ;
	    	//opcionales
	    Map<Resource,Integer> optionalResources = new HashMap<Resource,Integer>();
	    optionalResources.put( resourcesHandler.createFixedResource("Luz Natural", 1) , 1) ;
	    	//fecha y hora
	    GregorianCalendar calendar = new GregorianCalendar(2010, Calendar.MARCH, 3);
	    Timestamp timeStart1 = new Timestamp(8);
	    Timestamp timeEnd1  = new Timestamp(12);
	    LogicalHourFulfiller hour1 = new HourInterval(timeStart1,timeEnd1);
	    Period desiredHours = new SimplePeriod(hour1, calendar);
	    
	    //crear pedido
	    requestsHandler.createClassroomRequest(requiredResources, optionalResources, desiredHours, am1, profesorTamara , 20);

	    /************************************************************************************************************/

	    //Pedido 2
	    //crear requisitos para los pedidos
	    	//obligatorios
	    Map<Resource,Integer> requiredResources2 = new HashMap<Resource,Integer>();
	    requiredResources2.put( resourcesHandler.createFixedResource("Computadora", 20) , 20) ;
	    	//opcionales
	    Map<Resource,Integer> optionalResources2 = new HashMap<Resource,Integer>();
	    optionalResources2.put( resourcesHandler.createFixedResource("Luz Natural", 1) , 1) ;
	    	//fecha y hora
	    GregorianCalendar calendar2 = new GregorianCalendar(2010, Calendar.MARCH, 3);
	    Timestamp timeStart2 = new Timestamp(8);
	    Timestamp timeEnd2  = new Timestamp(12);
	    LogicalHourFulfiller hour2 = new HourInterval(timeStart2,timeEnd2);
	    Period desiredHours2 = new SimplePeriod(hour2, calendar2);
        
	    //crear pedido
	    requestsHandler.createClassroomRequest(requiredResources2, optionalResources2, desiredHours2, am1, profesorPepe , 20);
        
	    /******************************************************************************************************************/
	    
	    //Pedido 3
	    //crear requisitos para los pedidos
	    	//obligatorios
	    Map<Resource,Integer> requiredResources3 = new HashMap<Resource,Integer>();
	    requiredResources3.put( resourcesHandler.createFixedResource("Computadora", 20) , 20) ;
	    	//opcionales
	    Map<Resource,Integer> optionalResources3 = new HashMap<Resource,Integer>();
	    optionalResources3.put( resourcesHandler.createFixedResource("Luz Natural", 1) , 1) ;
	    	//fecha y hora
	    GregorianCalendar calendar3 = new GregorianCalendar(2010, Calendar.MARCH, 3);
	    Timestamp timeStart3 = new Timestamp(8);
	    Timestamp timeEnd3  = new Timestamp(12);
	    LogicalHourFulfiller hour3 = new HourInterval(timeStart3,timeEnd3);
	    Period desiredHours3 = new SimplePeriod(hour3, calendar3);

	    //crear pedido
	    requestsHandler.createClassroomRequest(requiredResources3, optionalResources3, desiredHours3, ha2, profesorJose , 20);

	    /******************************************************************************************************************/

	    
	  //Pedido 4
	    //crear requisitos para los pedidos
	    	//obligatorios
	    Map<Resource,Integer> requiredResources4 = new HashMap<Resource,Integer>();
	    	//opcionales
	    Map<Resource,Integer> optionalResources4 = new HashMap<Resource,Integer>();
	    optionalResources4.put( resourcesHandler.createFixedResource("Computadora", 15) , 15) ;
	    optionalResources4.put( resourcesHandler.createFixedResource("Luz Natural", 1) , 1) ;
	    	//fecha y hora
	    GregorianCalendar calendar4 = new GregorianCalendar(2010, Calendar.MARCH, 3);
	    Timestamp timeStart4 = new Timestamp(8);
	    Timestamp timeEnd4  = new Timestamp(12);
	    LogicalHourFulfiller hour4 = new HourInterval(timeStart4,timeEnd4);
	    Period desiredHours4 = new SimplePeriod(hour4, calendar4);

	    //crear pedido
	    requestsHandler.createClassroomRequest(requiredResources4, optionalResources4, desiredHours4, ha2, profesorJuan , 20);

	    /************************************************************************************************************/

	    //Pedido 5
	    //crear requisitos para los pedidos
	    	//obligatorios
	    Map<Resource,Integer> requiredResources5 = new HashMap<Resource,Integer>();
	    requiredResources5.put( resourcesHandler.createFixedResource("Computadora", 20) , 20) ;
	    	//opcionales
	    Map<Resource,Integer> optionalResources5 = new HashMap<Resource,Integer>();
	    optionalResources5.put( resourcesHandler.createFixedResource("Luz Natural", 20) , 20) ;
	    	//fecha y hora	    
	    GregorianCalendar calendar5 = new GregorianCalendar(2010, Calendar.MARCH, 3);
	    Timestamp timeStart5 = new Timestamp(8);
	    Timestamp timeEnd5  = new Timestamp(12);
	    LogicalHourFulfiller hour5 = new HourInterval(timeStart5,timeEnd5);
	    Period desiredHours5 = new SimplePeriod(hour5, calendar5);
        
	    //crear pedido
	    ClassroomRequest classReq5 = requestsHandler.createClassroomRequest(requiredResources5, optionalResources5, desiredHours5, pc, profesorEsteban , 20);

	    /******************************************************************************************************************/
	    
	    //Pedido 6
	    //crear requisitos para los pedidos
	    	//obligatorios
	    Map<Resource,Integer> requiredResources6 = new HashMap<Resource,Integer>();
	    requiredResources6.put( resourcesHandler.createFixedResource("Proyector", 1) , 1) ;
	    	//opcionales
	    Map<Resource,Integer> optionalResources6 = new HashMap<Resource,Integer>();
	    optionalResources6.put( resourcesHandler.createFixedResource("Luz Natural", 1) , 1) ;
	    	//fecha y hora
	    GregorianCalendar calendar6 = new GregorianCalendar(2010, Calendar.MARCH, 3);
	    Timestamp timeStart6 = new Timestamp(8);
	    Timestamp timeEnd6  = new Timestamp(10);
	    LogicalHourFulfiller hour6 = new HourInterval(timeStart6,timeEnd6);
	    Period desiredHours6 = new SimplePeriod(hour6, calendar6);

	    //crear pedido
	    ClassroomRequest classReq6 = requestsHandler.createClassroomRequest(requiredResources6, optionalResources6, desiredHours6, i1, profesorErnesto, 25);
	    
	    
	    /******************************************************************************************************************/

	    // Asignacion 5
	    asignator.asignateRequestInMostSatisfactoryClassroom(classReq5);
	    
	    /******************************************************************************************************************/
	    
	    // Asignacion 6
	    asignator.asignateRequestInMostSatisfactoryClassroom(classReq6);
	    
	    /******************************************************************************************************************/
	    
	    // Reserva
	    Classroom classroom = classroomHandler.searchClassroom("Aula 20");
	    asignator.asignateBookedAssignment(classroom, "Reparaciones", desiredHours2);
	    
		} catch (Exception e) {}
	}
}
