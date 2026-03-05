package guildquest.sharing;

import guildquest.enums.SharePermission;
import guildquest.user.User;

/**
 * Value object pairing a user with their permission level for a shared resource.
 */
public class ShareEntry {

    private final User           user;
    private       SharePermission permission;

    public ShareEntry(User user, SharePermission permission) {
        this.user       = user;
        this.permission = permission;
    }

    public User            getUser()       { return user; }
    public SharePermission getPermission() { return permission; }
    public void setPermission(SharePermission p) { this.permission = p; }

    public boolean canEdit() {
        return permission == SharePermission.COLLABORATIVE;
    }
}
