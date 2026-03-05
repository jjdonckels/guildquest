package guildquest.campaign;

import guildquest.core.TimeRange;
import guildquest.core.WorldClockTime;
import guildquest.realm.Realm;
import guildquest.sharing.ShareEntry;
import guildquest.user.Character;
import guildquest.user.InventoryItem;
import guildquest.enums.SharePermission;
import guildquest.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single quest event within a campaign.
 *
 * Refactorings applied here:
 *  - Introduce Parameter Object: startTime + endTime collapsed to TimeRange.
 *  - Encapsulate Collection: participants and sharedWith lists are unmodifiable.
 */
public class QuestEvent {

    private String    name;
    private TimeRange timeRange;
    private Realm     realm;

    // Optional participants
    private final List<Character>  participants = new ArrayList<>();

    // Loot granted/removed by this event
    private final List<InventoryItem> lootGranted = new ArrayList<>();
    private final List<InventoryItem> lootRemoved = new ArrayList<>();

    // Sharing
    private final List<ShareEntry> sharedWith = new ArrayList<>();

    /** Full constructor with explicit TimeRange. */
    public QuestEvent(String name, TimeRange timeRange, Realm realm) {
        this.name      = name;
        this.timeRange = timeRange;
        this.realm     = realm;
    }

    /** Convenience constructor for point-in-time events (no end). */
    public QuestEvent(String name, WorldClockTime startTime, Realm realm) {
        this(name, new TimeRange(startTime), realm);
    }

    // ── Basic getters/setters ────────────────────────────────────────────────

    public String    getName()     { return name; }
    public TimeRange getTimeRange() { return timeRange; }
    public Realm     getRealm()    { return realm; }

    /** Convenience shortcut used by the CLI. */
    public WorldClockTime getStartTime() { return timeRange.getStart(); }

    public void setName(String name)          { this.name = name; }
    public void setTimeRange(TimeRange range)  { this.timeRange = range; }
    public void setRealm(Realm realm)          { this.realm = realm; }

    /**
     * REFACTORING: Hide Delegate (Fowler / lecture slide 56-57) — AI-assisted.
     *
     * MOTIVATION: The message chain event.getRealm().getLocalTimeRule().toLocalTime(time)
     * appears wherever local time is needed, coupling callers to the internals of
     * Realm and LocalTimeRule (a Message Chain / Law of Demeter violation). By
     * providing this delegate method, callers need only know QuestEvent — they do
     * not "talk to strangers" two levels deep.
     */
    public WorldClockTime getLocalStartTime() {
        if (realm == null) return timeRange.getStart();
        return realm.getLocalTimeRule().toLocalTime(timeRange.getStart());
    }

    // ── Participants ─────────────────────────────────────────────────────────

    public void addParticipant(Character c)    { participants.add(c); }
    public void removeParticipant(Character c) { participants.remove(c); }
    public List<Character> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    // ── Loot ─────────────────────────────────────────────────────────────────

    public void addLootGranted(InventoryItem item) { lootGranted.add(item); }
    public void addLootRemoved(InventoryItem item) { lootRemoved.add(item); }

    /**
     * Applies loot changes to each participating character's inventory.
     * REFACTORING: Extract Method — isolating loot-application logic from the
     * CLI keeps each method focused on one task.
     */
    public void applyLoot() {
        for (Character c : participants) {
            lootGranted.forEach(c::addItem);
            lootRemoved.forEach(c::removeItem);
        }
    }

    // ── Sharing ───────────────────────────────────────────────────────────────

    public void shareWith(User user, SharePermission permission) {
        sharedWith.add(new ShareEntry(user, permission));
    }

    public boolean isVisibleTo(User user) {
        return sharedWith.stream().anyMatch(e -> e.getUser().equals(user));
    }

    public List<ShareEntry> getSharedWith() {
        return Collections.unmodifiableList(sharedWith);
    }
}
