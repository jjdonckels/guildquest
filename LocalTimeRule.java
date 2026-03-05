package guildquest.realm;

import guildquest.core.WorldClockTime;

public class LocalTimeRule {

    private final int timeOffsetMinutes;

    public LocalTimeRule(int offsetMinutes) {
        this.timeOffsetMinutes = offsetMinutes;
    }

    public WorldClockTime toLocalTime(WorldClockTime worldTime) {
        int total = worldTime.toTotalMinutes() + timeOffsetMinutes;
        // WorldClockTime constructor normalises negative values too
        int days    = total / (24 * 60);
        int remainder = total % (24 * 60);
        int hours   = remainder / 60;
        int minutes = remainder % 60;
        return new WorldClockTime(days, hours, minutes);
    }

    public int getOffsetMinutes() { return timeOffsetMinutes; }
}
