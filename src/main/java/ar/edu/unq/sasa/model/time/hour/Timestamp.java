package ar.edu.unq.sasa.model.time.hour;

import ar.edu.unq.sasa.model.exceptions.time.TimestampException;

/**
 * Entidad que modela una hora particular, con minutos. Las horas se representan
 * en formato 24 hs.
 */
public class Timestamp {

	private int hour;

	private int minutes;

	public Timestamp(int h) {
		this.setHour(h);
	}

	public Timestamp(int h, int m) {
		this(h);
		this.setMinutes(m);
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setHour(int hour) {
		if (hour >= 0 && hour < 24)
			this.hour = hour;
		else
			throw new TimestampException("Hour out of range");
	}

	public void setMinutes(int minutes) {
		if (minutes >= 0 && minutes < 60)
			this.minutes = minutes;
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

	public Timestamp add(int min) {
		int minutes = min + this.getMinutes();
		int hours = this.getHour();
		while (minutes >= 60) {
			minutes = minutes - 60;
			hours++;
		}
		return new Timestamp(hours, minutes);
	}

	public Timestamp substract(int min) {
		int minutes = this.getMinutes() - min;
		int hours = this.getHour();
		while (minutes < 0) {
			minutes = minutes + 60;
			hours--;
		}
		return new Timestamp(hours, minutes);
	}

	public int minutesBetween(Timestamp t) {
		int myMinutes = this.getHour() * 60 + this.getMinutes();
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