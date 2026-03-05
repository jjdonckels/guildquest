package guildquest.campaign;

import guildquest.enums.SharePermission;
import guildquest.user.User;

/**
 * DESIGN PATTERN: Observer (GoF) — AI-assisted identification.
 *
 * MOTIVATION: The requirements explicitly call for "Notifications when another
 * user shares a campaign/event or updates a shared campaign." Without Observer,
 * a future NotificationService would have to modify Campaign directly every time
 * a new notification type was added — a classic Shotgun Surgery smell.
 *
 * Observer decouples the event source (Campaign) from the event handlers
 * (future NotificationService, audit log, real-time sync layer, etc.). Each
 * handler registers itself; Campaign fires notifications without knowing who
 * is listening. This is the highest-leverage pattern for future-proofing
 * because it is required by the written requirements and creates the hook
 * for multiple planned features simultaneously.
 */
public interface CampaignObserver {

    /** Called whenever a new QuestEvent is added to the campaign. */
    void onQuestEventAdded(Campaign campaign, QuestEvent event);

    /** Called whenever the campaign is shared with a new user. */
    void onCampaignShared(Campaign campaign, User recipient, SharePermission permission);
}
