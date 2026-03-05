package guildquest.core;

import guildquest.campaign.Campaign;
import guildquest.campaign.QuestEvent;
import guildquest.enums.SharePermission;
import guildquest.enums.TimeDisplayPreference;
import guildquest.enums.Visibility;
import guildquest.realm.LocalTimeRule;
import guildquest.realm.Realm;
import guildquest.realm.RealmRegistry;
import guildquest.settings.SettingsScreen;
import guildquest.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main CLI entry point.
 *
 * REFACTORING: Replace Nested Conditional with Guard Clauses (Fowler / lecture slide 73).
 * MOTIVATION: The original selectUser() and selectCampaign() methods had nested
 * if-else blocks with "happy path" code deeply indented inside validity checks.
 * Guard clauses make the exceptional exits happen at the top, leaving the normal
 * flow flat and easy to read.
 *
 * REFACTORING: Extract Method applied throughout.
 * MOTIVATION: The original addQuestEvent() and viewQuestEvents() were long
 * methods mixing input parsing, object creation, and display formatting. Each
 * concern is now its own method, reducing method length and improving
 * understandability.
 */
public class GuildQuestCLI {

    private static final Scanner      scanner      = new Scanner(System.in);
    private static final List<User>   users        = new ArrayList<>();
    private static final RealmRegistry realmRegistry = new RealmRegistry();
    private static final SettingsScreen settings   = new SettingsScreen();

    public static void main(String[] args) {
        seedDefaultRealms();

        while (true) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1: createUser();    break;
                case 2: selectUser();    break;
                case 3: openSettings();  break;
                case 4: System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ── Seeding ───────────────────────────────────────────────────────────────

    private static void seedDefaultRealms() {
        realmRegistry.addRealm(new Realm("Aeloria",  "The central continent", new LocalTimeRule(0)));
        realmRegistry.addRealm(new Realm("Duskheim", "A shadowy eastern realm", new LocalTimeRule(60)));
        realmRegistry.addRealm(new Realm("Solaris",  "A sun-scorched desert planet", new LocalTimeRule(-120)));
    }

    // ── Menus ─────────────────────────────────────────────────────────────────

    private static void printMainMenu() {
        System.out.println("\n=== GuildQuest ===");
        System.out.println("1. Create user");
        System.out.println("2. Select user");
        System.out.println("3. Settings");
        System.out.println("4. Exit");
    }

    private static void createUser() {
        System.out.print("Enter username: ");
        users.add(new User(scanner.nextLine()));
        System.out.println("User created.");
    }

    private static void selectUser() {
        // REFACTORING: Guard clause — exit early for empty list
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }
        printNumberedList("Users", users, u -> u.getUsername());
        int index = readInt() - 1;
        if (!isValidIndex(index, users)) {
            System.out.println("Invalid selection.");
            return;
        }
        userMenu(users.get(index));
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n=== User Menu (" + user.getUsername() + ") ===");
            System.out.println("1. Create campaign");
            System.out.println("2. View campaigns");
            System.out.println("3. Select campaign");
            System.out.println("4. Back");
            int choice = readInt();
            switch (choice) {
                case 1: createCampaign(user);  break;
                case 2: viewCampaigns(user);   break;
                case 3: selectCampaign(user);  break;
                case 4: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void createCampaign(User user) {
        System.out.print("Campaign name: ");
        Campaign campaign = new Campaign(scanner.nextLine());
        user.addCampaign(campaign);
        System.out.println("Campaign created.");
    }

    private static void viewCampaigns(User user) {
        if (user.getCampaigns().isEmpty()) {
            System.out.println("No campaigns.");
            return;
        }
        printNumberedList("Campaigns", user.getCampaigns(),
                c -> c.getName() + " [" + c.getVisibility() + (c.isArchived() ? ", ARCHIVED" : "") + "]");
    }

    private static void selectCampaign(User user) {
        if (user.getCampaigns().isEmpty()) {
            System.out.println("No campaigns.");
            return;
        }
        printNumberedList("Campaigns", user.getCampaigns(), Campaign::getName);
        System.out.print("Select campaign: ");
        int index = readInt() - 1;
        if (!isValidIndex(index, user.getCampaigns())) {
            System.out.println("Invalid selection.");
            return;
        }
        campaignMenu(user.getCampaigns().get(index));
    }

    private static void campaignMenu(Campaign campaign) {
        while (true) {
            System.out.println("\n=== Campaign: " + campaign.getName() + " ===");
            System.out.println("1. Add quest event");
            System.out.println("2. View quest events");
            System.out.println("3. View events by day");
            System.out.println("4. Toggle visibility (public/private)");
            System.out.println("5. Back");
            int choice = readInt();
            switch (choice) {
                case 1: addQuestEvent(campaign);     break;
                case 2: viewQuestEvents(campaign);   break;
                case 3: viewEventsOnDay(campaign);   break;
                case 4: toggleVisibility(campaign);  break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ── Campaign actions ──────────────────────────────────────────────────────

    /**
     * REFACTORING: Extract Method — realm selection extracted to selectRealm().
     * MOTIVATION: The original addQuestEvent() hard-coded a single realm inline.
     * Extracting realm selection makes the method shorter and keeps each helper
     * focused on a single concern.
     */
    private static void addQuestEvent(Campaign campaign) {
        System.out.print("Event name: ");
        String name = scanner.nextLine();
        WorldClockTime startTime = promptForTime("Start");
        WorldClockTime endTime   = promptForOptionalTime("End (leave blank to skip)");
        Realm realm = selectRealm();

        TimeRange range = (endTime != null)
                ? new TimeRange(startTime, endTime)
                : new TimeRange(startTime);

        campaign.addQuestEvent(new QuestEvent(name, range, realm));
        System.out.println("Quest event added.");
    }

    /** REFACTORING: Extract Method — time-prompt logic reused for start and end times. */
    private static WorldClockTime promptForTime(String label) {
        System.out.print(label + " day: ");   int day    = readInt();
        System.out.print(label + " hour: ");  int hour   = readInt();
        System.out.print(label + " minute: "); int minute = readInt();
        return new WorldClockTime(day, hour, minute);
    }

    /** Returns null if user skips. */
    private static WorldClockTime promptForOptionalTime(String prompt) {
        System.out.println(prompt + " (enter 0 to skip)");
        System.out.print("Day (0 = skip): ");
        int day = readInt();
        if (day == 0) return null;
        System.out.print("Hour: ");  int hour   = readInt();
        System.out.print("Minute: "); int minute = readInt();
        return new WorldClockTime(day, hour, minute);
    }

    /** REFACTORING: Extract Method — realm selection extracted from addQuestEvent. */
    private static Realm selectRealm() {
        List<Realm> realms = realmRegistry.getRealms();
        printNumberedList("Realms", realms, Realm::getName);
        System.out.print("Select realm: ");
        int index = readInt() - 1;
        if (!isValidIndex(index, realms)) {
            System.out.println("Invalid realm, using default.");
            return realms.get(0);
        }
        return realms.get(index);
    }

    /**
     * DESIGN PATTERN (Strategy): display formatting is delegated entirely to
     * the current TimeDisplayStrategy — no switch on preference here.
     */
    private static void viewQuestEvents(Campaign campaign) {
        if (campaign.getQuestEvents().isEmpty()) {
            System.out.println("No events.");
            return;
        }
        for (QuestEvent event : campaign.getQuestEvents()) {
            printEventSummary(event);
        }
    }

    /** REFACTORING: Extract Method — single-event display reused by multiple views. */
    private static void printEventSummary(QuestEvent event) {
        System.out.println("- " + event.getName());
        System.out.println("  " + settings.getDisplayStrategy()
                .format(event.getStartTime(), event.getRealm()));
        if (event.getTimeRange().hasEnd()) {
            System.out.println("  End: " + settings.getDisplayStrategy()
                    .format(event.getTimeRange().getEnd(), event.getRealm()));
        }
    }

    private static void viewEventsOnDay(Campaign campaign) {
        System.out.print("Enter day number: ");
        int day = readInt();
        List<QuestEvent> events = campaign.eventsOnDay(day);
        if (events.isEmpty()) {
            System.out.println("No events on day " + day + ".");
            return;
        }
        events.forEach(GuildQuestCLI::printEventSummary);
    }

    private static void toggleVisibility(Campaign campaign) {
        campaign.setVisibility(
            campaign.getVisibility() == Visibility.PRIVATE
                ? Visibility.PUBLIC : Visibility.PRIVATE);
        System.out.println("Visibility set to: " + campaign.getVisibility());
    }

    // ── Settings ──────────────────────────────────────────────────────────────

    private static void openSettings() {
        System.out.println("\n=== Settings ===");
        System.out.println("1. Set time display (World/Local/Both)");
        System.out.println("2. Change realm");
        System.out.println("3. Back");
        int choice = readInt();
        switch (choice) {
            case 1: changeDisplayPreference(); break;
            case 2: changeCurrentRealm();      break;
            case 3: return;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void changeDisplayPreference() {
        System.out.println("1. World Clock  2. Realm Local  3. Both");
        int c = readInt();
        TimeDisplayPreference pref =
            c == 2 ? TimeDisplayPreference.REALM_LOCAL :
            c == 3 ? TimeDisplayPreference.BOTH :
                     TimeDisplayPreference.WORLD_CLOCK;
        settings.setDisplayPreference(pref);
        System.out.println("Display preference set to: " + pref);
    }

    private static void changeCurrentRealm() {
        Realm r = selectRealm();
        settings.changeRealm(r);
        System.out.println("Current realm set to: " + r.getName());
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    /** Generic numbered-list printer — Extract Method to avoid copy-paste. */
    private static <T> void printNumberedList(String title,
                                               List<T> items,
                                               java.util.function.Function<T, String> label) {
        System.out.println("--- " + title + " ---");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + label.apply(items.get(i)));
        }
        System.out.print("Choice: ");
    }

    private static boolean isValidIndex(int index, List<?> list) {
        return index >= 0 && index < list.size();
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }
}
