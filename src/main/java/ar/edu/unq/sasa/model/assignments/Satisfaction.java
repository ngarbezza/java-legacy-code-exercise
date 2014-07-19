package ar.edu.unq.sasa.model.assignments;

import java.util.Map;

import ar.edu.unq.sasa.model.academic.ClassroomRequest;
import ar.edu.unq.sasa.model.academic.Request;
import ar.edu.unq.sasa.model.items.Resource;
import ar.edu.unq.sasa.model.time.Period;

/**
 * Es la manera de comprobar la satisfacción que hubo al asignar un 
 * {@link ClassroomRequest}.
 * 
 * @author Cristian Suarez
 */
public class Satisfaction {
	
	/**
	 * Guarda la información de los {@link Resource}s pedidos en el 
	 * {@link Request} y agrega al costado un número para saber cuántos 
	 * recursos de esos faltan. O sea que si un recurso al lado tiene un 
	 * "0", quiere decir que ese recurso fue asignado.
	 */
	private Map<Resource, Integer> resources;
	
	/**
	 * Representa las superposiciones que hay al haber asignado ese
	 * {@link Request}, y puede haber varias. cada una tiene al lado 
	 * la cantidad de horas que se superponen, representadas en Floats.
	 */
	private Map<Period, Float> timeDifference;
	
	/**
	 * Es la diferencia de capacidad que hay entre la pedida y la capacidad 
	 * real del aula. Si está en positivo quiere decir que sobran lugares, si
	 * está en negativo quiere decir que faltan lugares.
	 */
	private int capacityDifference;
	
	public Satisfaction(Map<Resource, Integer> resourcesMap, Map<Period, Float> aTimeDifference, int aCapacityDifference) {
		resources = resourcesMap;
		timeDifference = aTimeDifference;
		capacityDifference = aCapacityDifference;
	}
	
	public Map<Resource, Integer> getResources() {
		return resources;
	}
	
	public Map<Period, Float> getTimeDifference(){
		return timeDifference;
	}
	
	public int getCapacityDifference(){
		return capacityDifference;
	}
	
	public boolean isSatisfied(){
		boolean isSatisfied = true;
		
		if (! resources.isEmpty()){
			isSatisfied = false;
		}
		
		if (capacityDifference < 0){
			isSatisfied = false;
		}
		
		if (! timeDifference.isEmpty()){
			isSatisfied = false;
		}
		
		return isSatisfied;
	}
	
	public void addPeriodSuperposition(Period period, float minutesShared) {
		timeDifference.put(period, minutesShared);
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacityDifference;
		result = prime * result
				+ ((resources == null) ? 0 : resources.hashCode());
		result = prime * result
				+ ((timeDifference == null) ? 0 : timeDifference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Satisfaction other = (Satisfaction) obj;
		if (capacityDifference != other.capacityDifference)
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (timeDifference == null) {
			if (other.timeDifference != null)
				return false;
		} else if (!timeDifference.equals(other.timeDifference))
			return false;
		return true;
	}

}
