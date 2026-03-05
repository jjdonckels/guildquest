package guildquest.core;

import guildquest.enums.TimeDisplayPreference;

/**
 * DESIGN PATTERN: Factory Method (GoF).
 *
 * MOTIVATION: Without a factory, every place that needs a TimeDisplayStrategy
 * would contain its own if/switch block mapping a TimeDisplayPreference enum
 * value to a concrete strategy class. This is "Repeated Switch Statements"
 * (a lecture code smell). The factory centralises this mapping so it exists
 * in exactly one place. Adding a new display preference only requires adding
 * a new Strategy class and one line in this factory — no other class changes.
 *
 * This also keeps the CLI and SettingsScreen decoupled from concrete strategy
 * classes; they only depend on the TimeDisplayStrategy interface.
 */
public class TimeDisplayStrategyFactory {

    private TimeDisplayStrategyFactory() { /* utility class */ }

    public static TimeDisplayStrategy create(TimeDisplayPreference preference) {
        switch (preference) {
            case WORLD_CLOCK:  return new WorldClockDisplayStrategy();
            case REALM_LOCAL:  return new RealmLocalDisplayStrategy();
            case BOTH:         return new BothDisplayStrategy();
            default:
                throw new IllegalArgumentException("Unknown preference: " + preference);
        }
    }
}
