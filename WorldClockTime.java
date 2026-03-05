package guildquest.core;

/**
 * Immutable value object representing a point in World Clock time.
 * Refactoring applied: Introduce Parameter Object - instead of passing
 * (int day, int hour, int minute) triples everywhere, callers use this class.
 */
public class WorldClockTime implements Comparable<WorldClockTime> {

    private final int days;
    private final int hours;
    private final int minutes;

    public WorldClockTime(int days, int hours, int minutes) {
        // Normalize on construction so the object is always valid
        int totalMinutes = days * 24 * 60 + hours * 60 + minutes;
        this.days    = totalMinutes / (24 * 60);
        int remainder = totalMinutes % (24 * 60);
        this.hours   = remainder / 60;
        this.minutes = remainder % 60;
    }

    public int getDays()    { return days; }
    public int getHours()   { return hours; }
    public int getMinutes() { return minutes; }

    public int toTotalMinutes() {
        return days * 24 * 60 + hours * 60 + minutes;
    }

    @Override
    public int compareTo(WorldClockTime other) {
        return Integer.compare(this.toTotalMinutes(), other.toTotalMinutes());
    }

    @Override
    public String toString() {
        return "Day " + days + " " + hours + ":" + String.format("%02d", minutes);
    }
}
