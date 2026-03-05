package guildquest.core;

import guildquest.realm.Realm;

/**
 * DESIGN PATTERN: Strategy (GoF).
 *
 * MOTIVATION: The original code had a TimeDisplayPreference enum and then
 * scattered switch/if-else logic wherever time needed to be displayed
 * (e.g., in the CLI's viewQuestEvents). This is the "Repeated Switch
 * Statements" code smell from the lecture. Every new display mode would
 * require hunting down every switch and adding a case.
 *
 * By turning each display mode into a Strategy object, the decision of
 * *how* to format time is encapsulated in the strategy and the calling
 * code is reduced to a single method call. New display modes are added
 * by writing a new class, not by modifying existing code (Open/Closed).
 */
public interface TimeDisplayStrategy {

    /**
     * Formats the given time for display, optionally using the Realm's
     * local time rule.
     *
     * @param time  the World Clock time to format
     * @param realm the realm associated with the event (may be null)
     * @return a human-readable time string
     */
    String format(WorldClockTime time, Realm realm);
}
