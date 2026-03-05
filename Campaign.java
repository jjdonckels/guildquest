package guildquest.campaign;

import guildquest.core.WorldClockTime;
import guildquest.enums.SharePermission;
import guildquest.enums.Visibility;
import guildquest.sharing.ShareEntry;
import guildquest.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A campaign belonging to one owner user, containing quest events.
 *
 * REFACTORING: Extract Method applied to timeline filtering.
 * DESIGN PATTERN: Observer — observers are notified on addQuestEvent/shareWith.
 */
public class Campaign {

    private String     name;
    private Visibility visibility;
    private boolean    archived;

    private final List<QuestEvent>      questEvents = new ArrayList<>();
    private final List<ShareEntry>      sharedWith  = new ArrayList<>();
    private final List<CampaignObserver> observers   = new ArrayList<>();

    public Campaign(String name) {
        this.name       = name;
        this.visibility = Visibility.PRIVATE;
        this.archived   = false;
    }

    // ── Basic management ─────────────────────────────────────────────────────

    public String     getName()       { return name; }
    public Visibility getVisibility() { return visibility; }
    public boolean    isArchived()    { return archived; }

    public void setName(String name)             { this.name = name; }
    public void setVisibility(Visibility v)      { this.visibility = v; }
    public void archive()                        { this.archived = true; }
    public void unarchive()                      { this.archived = false; }

    // ── Quest event management ────────────────────────────────────────────────

    // ── Observer management ───────────────────────────────────────────────────

    public void addObserver(CampaignObserver observer)    { observers.add(observer); }
    public void removeObserver(CampaignObserver observer) { observers.remove(observer); }

    // ── Quest event management ────────────────────────────────────────────────

    public void addQuestEvent(QuestEvent event) {
        questEvents.add(event);
        // OBSERVER: notify all registered listeners
        observers.forEach(o -> o.onQuestEventAdded(this, event));
    }
    public void removeQuestEvent(QuestEvent event) { questEvents.remove(event); }

    public List<QuestEvent> getQuestEvents() {
        return Collections.unmodifiableList(questEvents);
    }

    // ── Timeline views ────────────────────────────────────────────────────────
    // REFACTORING: Extract Method — each view is its own query method.

    /** Returns events whose start time falls on the given World Clock day. */
    public List<QuestEvent> eventsOnDay(int day) {
        return questEvents.stream()
                .filter(e -> e.getStartTime().getDays() == day)
                .collect(Collectors.toList());
    }

    /** Returns events in [startDay, startDay + 7). */
    public List<QuestEvent> eventsInWeek(int startDay) {
        return eventsInRange(startDay, startDay + 7);
    }

    /** Returns events in [startDay, startDay + 30). */
    public List<QuestEvent> eventsInMonth(int startDay) {
        return eventsInRange(startDay, startDay + 30);
    }

    /** Returns events in [startDay, startDay + 365). */
    public List<QuestEvent> eventsInYear(int startDay) {
        return eventsInRange(startDay, startDay + 365);
    }

    /**
     * REFACTORING: Extract Method — common range predicate extracted so that
     * eventsInWeek/Month/Year all delegate here rather than duplicating the
     * filter expression (Duplicated Code smell).
     */
    private List<QuestEvent> eventsInRange(int fromDay, int toDay) {
        return questEvents.stream()
                .filter(e -> {
                    int d = e.getStartTime().getDays();
                    return d >= fromDay && d < toDay;
                })
                .collect(Collectors.toList());
    }

    // ── Sharing ───────────────────────────────────────────────────────────────

    public void shareWith(User user, SharePermission permission) {
        sharedWith.add(new ShareEntry(user, permission));
        // OBSERVER: notify listeners
        observers.forEach(o -> o.onCampaignShared(this, user, permission));
    }

    public boolean isVisibleTo(User user) {
        if (visibility == Visibility.PUBLIC) return true;
        return sharedWith.stream().anyMatch(e -> e.getUser().equals(user));
    }

    public List<ShareEntry> getSharedWith() {
        return Collections.unmodifiableList(sharedWith);
    }
}
