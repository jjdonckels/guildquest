package guildquest.core;

import guildquest.realm.Realm;

/**
 * Concrete Strategy: show only World Clock time.
 */
public class WorldClockDisplayStrategy implements TimeDisplayStrategy {
    @Override
    public String format(WorldClockTime time, Realm realm) {
        return "World: " + time;
    }
}
