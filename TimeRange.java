package guildquest.core;

/**
 * REFACTORING: Introduce Parameter Object (Fowler).
 *
 * MOTIVATION: QuestEvent previously stored startTime and endTime as two separate
 * WorldClockTime fields, and any method that cared about duration had to
 * individually read both. This is a classic "Data Clump" smell — two values
 * that always travel together should become one object. TimeRange encapsulates
 * the pair and provides derived behaviour (contains, duration) in one place,
 * eliminating the clump in QuestEvent and any future callers.
 */
public class TimeRange {

    private final WorldClockTime start;
    private final WorldClockTime end;   // may be null for open-ended events

    public TimeRange(WorldClockTime start, WorldClockTime end) {
        this.start = start;
        this.end   = end;
    }

    /** Convenience constructor for point-in-time (no end). */
    public TimeRange(WorldClockTime start) {
        this(start, null);
    }

    public WorldClockTime getStart() { return start; }
    public WorldClockTime getEnd()   { return end; }
    public boolean hasEnd()          { return end != null; }

    /** Returns duration in minutes, or -1 if open-ended. */
    public int durationMinutes() {
        if (end == null) return -1;
        return end.toTotalMinutes() - start.toTotalMinutes();
    }

    /**
     * True if the given time falls within [start, end).
     * Always false for open-ended ranges.
     */
    public boolean contains(WorldClockTime t) {
        if (end == null) return false;
        return t.compareTo(start) >= 0 && t.compareTo(end) < 0;
    }

    @Override
    public String toString() {
        return hasEnd() ? start + " → " + end : start + " (open-ended)";
    }
}
