package guildquest.core;

import guildquest.realm.Realm;

/**
 * Concrete Strategy: show both World Clock and Realm-local time.
 */
public class BothDisplayStrategy implements TimeDisplayStrategy {

    private final TimeDisplayStrategy world = new WorldClockDisplayStrategy();
    private final TimeDisplayStrategy local = new RealmLocalDisplayStrategy();

    @Override
    public String format(WorldClockTime time, Realm realm) {
        return world.format(time, realm) + " | " + local.format(time, realm);
    }
}
