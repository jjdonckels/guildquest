package guildquest.realm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REFACTORING: Extract Class (Fowler / lecture).
 *
 * MOTIVATION: In the original CLI, realm creation was embedded directly inside
 * addQuestEvent(), hard-coding a "Default Realm". This is a "Divergent Change"
 * smell — every time realm management logic needed to change, the CLI method
 * had to change too, even though the CLI's job is user interaction, not realm
 * storage. RealmRegistry extracts realm lifecycle management into a dedicated
 * class with a single clear responsibility (SRP), and makes realms available
 * system-wide without repeating the construction logic.
 */
public class RealmRegistry {

    private final List<Realm> realms = new ArrayList<>();

    public void addRealm(Realm realm) {
        realms.add(realm);
    }

    public void removeRealm(Realm realm) {
        realms.remove(realm);
    }

    public List<Realm> getRealms() {
        return Collections.unmodifiableList(realms);
    }

    public Optional<Realm> findByName(String name) {
        return realms.stream()
                     .filter(r -> r.getName().equalsIgnoreCase(name))
                     .findFirst();
    }
}
