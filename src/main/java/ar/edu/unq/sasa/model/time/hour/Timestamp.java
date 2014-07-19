package ar.edu.unq.sasa.model.time.hour;

import ar.edu.unq.sasa.model.exceptions.time.TimestampException;

/**
 * Entidad que modela una hora particular, con minutos. Las horas se representan
 * en formato 24 hs.
 * 
 * @author Nahuel Garbezza
 * 
 */
public class Timestamp {

	/**
	 * Las horas (0 a 23).
	 */
	private int hour;

	/**
	 * Los minutos (0 a 59).
	 */
	private int minutes;

	/**
	 * Constructor de Timestamp que sólo recibe las horas, y setea los minutos
	 * en 0.
	 * 
	 * @param h
	 *            la hora.
	 * @throws TimestampException
	 *             si la hora está fuera de rango.
	 */
	public Timestamp(int h) throws TimestampException {
		this.setHour(h);
	}

	/**
	 * Constructor de Timestamp.
	 * 
	 * @param h
	 *            las horas.
	 * @param m
	 *            los minutos.
	 * @throws TimestampException
	 *             si horas o minutos están fuera de rango.
	 */
	public Timestamp(int h, int m) throws TimestampException {
		this(h);
		this.setMinutes(m);
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

	/**
	 * Asigna una hora al receptor.
	 * 
	 * @param hour
	 *            la hora.
	 * @throws TimestampException
	 *             si hour es mayor que 23 o menor que 0.
	 */
	public void setHour(int hour) throws TimestampException {
		if (hour >= 0 && hour < 24)
			this.hour = hour;
		else
			throw new TimestampException("Hour out of range");
	}

	/**
	 * Asigna los minutos al receptor.
	 * 
	 * @param minutes
	 *            los minutos.
	 * @throws TimestampException
	 *             si minutes es mayor que 59 o menor que 0.
	 */
	public void setMinutes(int minutes) throws TimestampException {
		if (minutes >= 0 && minutes < 60)
			this.minutes = minutes;
		else
			throw new TimestampException("Minutes out of range");
	}


	/**
	 * Compara el receptor con el timestamp recibido como parámetro.
	 * 
	 * @param other
	 *            el timestamp para comparar.
	 * @return true si el receptor es menor que el parámetro, false en caso
	 *         contrario.
	 */
	public boolean lessThan(Timestamp other) {
		if (this.getHour() < other.getHour())
			return true;
		else if (this.getHour() == other.getHour())
			return this.getMinutes() < other.getMinutes();
		else
			return false;
	}

	/**
	 * Compara el receptor con el timestamp recibido como parámetro.
	 * 
	 * @param other
	 *            el timestamp para comparar.
	 * @return true si el receptor es mayor que el parámetro, false en caso
	 *         contrario.
	 */
	public boolean greaterThan(Timestamp other) {
		return !(this.lessThan(other) || this.equals(other));
	}

	/**
	 * Compara el receptor con el timestamp recibido como parámetro.
	 * 
	 * @param other
	 *            el timestamp para comparar.
	 * @return true si el receptor es mayor o igual que el parámetro, false en
	 *         caso contrario.
	 */
	public boolean greaterEqual(Timestamp other) {
		return !this.lessThan(other);
	}

	/**
	 * Compara el receptor con el timestamp recibido como parámetro.
	 * 
	 * @param other
	 *            el timestamp para comparar.
	 * @return true si el receptor es menor o igual que el parámetro, false en
	 *         caso contrario.
	 */
	public boolean lessEqual(Timestamp other) {
		return !this.greaterThan(other);
	}

	/**
	 * Agrega minutos a un Timestamp, retornando como resultado un nuevo
	 * Timestamp.
	 * 
	 * @param min
	 *            la cantidad de minutos a agregar.
	 * @return el nuevo Timestamp.
	 * @throws TimestampException
	 *             si el nuevo Timestamp creado no es válido.
	 */
	public Timestamp add(int min) throws TimestampException {
		int minutes = min + this.getMinutes();
		int hours = this.getHour();
		while (minutes >= 60) {
			minutes = minutes - 60;
			hours++;
		}
		return new Timestamp(hours, minutes);
	}

	/**
	 * Resta minutos a un Timestamp, retornando como resultado un nuevo
	 * Timestamp.
	 * 
	 * @param min
	 *            la cantidad de minutos a restar.
	 * @return el nuevo Timestamp.
	 * @throws TimestampException
	 *             si el nuevo Timestamp creado no es válido.
	 */
	public Timestamp substract(int min) throws TimestampException {
		int minutes = this.getMinutes() - min;
		int hours = this.getHour();
		while (minutes < 0) {
			minutes = minutes + 60;
			hours--;
		}
		return new Timestamp(hours, minutes);
	}
	
	/**
	 * Retorna la cantidad de minutos entre el receptor y un {@link Timestamp}
	 * recibido como parámetro.
	 * 
	 * @param t
	 *            el {@link Timestamp} usado para calcular contra el receptor.
	 * @return la diferencia en minutos, positiva si el receptor es anterior al
	 *         parámetro, y negativa en caso contrario, y 0 si son iguales.
	 */
	public int minutesBetween(Timestamp t) {
		int myMinutes = this.getHour() * 60 + this.getMinutes();
		int tMinutes = t.getHour() * 60 + t.getMinutes();
		return tMinutes - myMinutes;
	}
	
	@Override
	public String toString() {
		return getHour() + ":" + getMinutes();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + minutes;
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timestamp other = (Timestamp) obj;
		if (hour != other.hour)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}