package ar.edu.unq.sasa.model.time.hour;

import ar.edu.unq.sasa.model.exceptions.time.TimestampException;

/**
 * Entidad que modela una hora particular, con minutos. Las horas se representan
 * en formato 24 hs.
 */
public class Timestamp {

	// TODO try to not have setters

	private int hour;

	private int minutes;

	public Timestamp(int anHour) {
		setHour(anHour);
	}

	public Timestamp(int anHour, int someMinutes) {
		this(anHour);
		setMinutes(someMinutes);
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setHour(int anHour) {
		if (anHour >= 0 && anHour < 24)
			hour = anHour;
		else
			throw new TimestampException("Hour out of range");
	}

	public void setMinutes(int someMinutes) {
		if (someMinutes >= 0 && someMinutes < 60)
			minutes = someMinutes;
		else
			throw new TimestampException("Minutes out of range");
	}

	public boolean lessThan(Timestamp other) {
		if (this.getHour() < other.getHour())
			return true;
		else if (this.getHour() == other.getHour())
			return this.getMinutes() < other.getMinutes();
		else
			return false;
	}

	public boolean greaterThan(Timestamp other) {
		return !(this.lessThan(other) || this.equals(other));
	}

	public boolean greaterEqual(Timestamp other) {
		return !this.lessThan(other);
	}

	public boolean lessEqual(Timestamp other) {
		return !this.greaterThan(other);
	}

	public Timestamp add(int someMinutes) {
		int newMinutes = someMinutes + getMinutes();
		int newHours = getHour();
		while (newMinutes >= 60) {
			newMinutes = newMinutes - 60;
			newHours++;
		}
		return new Timestamp(newHours, newMinutes);
	}

	public Timestamp substract(int someMinutes) {
		int newMinutes = getMinutes() - someMinutes;
		int newHours = getHour();
		while (newMinutes < 0) {
			newMinutes = newMinutes + 60;
			newHours--;
		}
		return new Timestamp(newHours, newMinutes);
	}

	public int minutesBetween(Timestamp t) {
		// TODO have a message #totalMinutes
		int myMinutes = getHour() * 60 + getMinutes();
		int tMinutes = t.getHour() * 60 + t.getMinutes();
		return tMinutes - myMinutes;
	}

	@Override
	public String toString() {
		return getHour() + ":" + getMinutes();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + minutes;
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
		Timestamp other = (Timestamp) obj;
		if (hour != other.hour)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}