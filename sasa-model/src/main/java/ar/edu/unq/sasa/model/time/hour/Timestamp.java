package ar.edu.unq.sasa.model.time.hour;

import static ar.edu.unq.sasa.util.Preconditions.precondition;

public class Timestamp {

    private int hour;

    private int minutes;

    public Timestamp(int anHour) {
        precondition("Hours should be a number between 0 and 23", anHour >= 0 && anHour < 24);
        hour = anHour;
    }

    public Timestamp(int anHour, int someMinutes) {
        this(anHour);
        precondition("Minutes should be between 0 and 59", someMinutes >= 0 && someMinutes < 60);
        minutes = someMinutes;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean lessThan(Timestamp other) {
        return hour < other.getHour() || (hour == other.getHour() && minutes < other.getMinutes());
    }

    public boolean greaterThan(Timestamp other) {
        return !(lessThan(other) || equals(other));
    }

    public boolean greaterEqual(Timestamp other) {
        return !lessThan(other);
    }

    public boolean lessEqual(Timestamp other) {
        return !greaterThan(other);
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

    public int minutesBetween(Timestamp other) {
        return other.totalMinutes() - totalMinutes();
    }

    public int totalMinutes() {
        return hour * 60 + minutes;
    }

    @Override
    public String toString() {
        return hour + ":" + minutes;
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
        Timestamp other = (Timestamp) obj;
        return hour == other.getHour() && minutes == other.getMinutes();
    }
}
