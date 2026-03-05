package guildquest.core;

import guildquest.realm.Realm;

/**
 * Concrete Strategy: show only Realm-local time.
 */
public class RealmLocalDisplayStrategy implements TimeDisplayStrategy {
    @Override
    public String format(WorldClockTime time, Realm realm) {
        if (realm == null) return "Local: (no realm)";
        WorldClockTime local = realm.getLocalTimeRule().toLocalTime(time);
        return "Local (" + realm.getName() + "): " + local;
    }
}
