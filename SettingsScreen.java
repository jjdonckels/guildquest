package guildquest.settings;

import guildquest.core.TimeDisplayStrategy;
import guildquest.core.TimeDisplayStrategyFactory;
import guildquest.enums.Theme;
import guildquest.enums.TimeDisplayPreference;
import guildquest.realm.Realm;

/**
 * Holds per-user display settings.
 *
 * DESIGN PATTERN (Strategy): SettingsScreen stores a TimeDisplayStrategy
 * object resolved through the factory, so any downstream code (CLI, future GUI)
 * can call settings.getDisplayStrategy().format(time, realm) without any
 * switch statement on display preference.
 */
public class SettingsScreen {

    private TimeDisplayPreference displayPreference;
    private TimeDisplayStrategy   displayStrategy;
    private Theme                 theme;
    private Realm                 currentRealm;

    public SettingsScreen() {
        setDisplayPreference(TimeDisplayPreference.WORLD_CLOCK);
        this.theme       = Theme.CLASSIC;
        this.currentRealm = null;
    }

    public void setDisplayPreference(TimeDisplayPreference preference) {
        this.displayPreference = preference;
        this.displayStrategy   = TimeDisplayStrategyFactory.create(preference);
    }

    public void selectTheme(Theme theme)  { this.theme = theme; }
    public void changeRealm(Realm realm)  { this.currentRealm = realm; }

    public TimeDisplayPreference getDisplayPreference() { return displayPreference; }
    public TimeDisplayStrategy   getDisplayStrategy()   { return displayStrategy; }
    public Theme                 getTheme()             { return theme; }
    public Realm                 getCurrentRealm()      { return currentRealm; }
}
