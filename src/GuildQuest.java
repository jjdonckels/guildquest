import java.util.*;

public class GuildQuest
{
    static void print(String s)
    {
        System.out.println(s);
    }

    private static Clock worldClock = new WorldClock(new WorldTime());
    private static Map<String, Realm> realms = new HashMap<String, Realm>();

    private Map<String, User> users;

    private static GuildQuest guildquest;
    private GuildQuest()
    {
        realms.put("Home", new Realm("Home",
                "Home base and starting point for all players",
                new FixedOffsetRule(0)));
        users = new HashMap<>();
    }
    public static GuildQuest getInstance()
    {
        if (guildquest == null)
        {
            guildquest = new GuildQuest();
        }
        return guildquest;
    }

    public static Map<String, Realm> getRealms()
    {
        return Collections.unmodifiableMap(realms);
    }
    // may return null if realmName isn't found in Realm map
    public static Realm getRealm(String realmName)
    {
        return realms.get(realmName);
    }
    public void addRealm(Realm realm)
    {
        if (!realms.containsKey(realm.getName()))
        {
            realms.put(realm.getName(), realm);
        }
    }
    public static Clock getWorldClock()
    {
        return worldClock;
    }
    public WorldTime getWorldTime()
    {
        return worldClock.getCurrentTime();
    }
    public void advanceTime(int minutes)
    {
        for (User u : users.values())
        {
            for (Campaign c : u.getCampaigns().values())
            {
                for (QuestEvent qe : c.getEvents())
                {
                    ((WorldClock) worldClock).addObserver(qe);
                }
            }
        }
        for (int i = 0; i < minutes; i++)
            ((WorldClock) worldClock).tick();
    }

    // may return null if username isn't found in User map
    public User getUser(String username)
    {
        return users.get(username);
    }
    public void addUser(User user)
    {
        if (!users.containsKey(user.getUsername()))
            users.put(user.getUsername(), user);
    }

    // return a list of all public campaigns
    public List<Campaign> getPublicCampaigns()
    {
        List<Campaign> publicCampaigns = new ArrayList<>();
        for (User u :  users.values())
        {
            for (Campaign c : u.getCampaigns().values())
            {
                if (c.getVisibility() == Visibility.PUBLIC)
                {
                    publicCampaigns.add(c);
                }
            }
        }
        return publicCampaigns;
    }

    public static void main (String[] args)
    {
        // this should be a looping menu to show updates to GuildQuest

        GuildQuest gq = GuildQuest.getInstance();
        Scanner input = new Scanner(System.in);
        String userInput = "";
        print("Welcome to the Guild Quest RPG campaign manager.");

        do {
            print("\nThe current world time is:\n" + gq.getWorldTime());
            print("Choose a menu option:" +
                    "\na: advance time" +
                    "\nb: add user" +
                    "\nc: select user" +
                    "\nd: add realm" +
                    "\ne: display realm info" +
                    "\nf: display all public campaign info" +
                    "\ng: display all info for all users" +
                    "\nx: exit");
            userInput = input.nextLine();
            if (userInput.equals("a"))
                gq.advance_time_menu(input);
            else if (userInput.equals("b"))
                gq.add_user_menu(input);
            else if (userInput.equals("c"))
                gq.select_user_menu(input);
            else if (userInput.equals("d"))
                gq.add_realm_menu(input);
            else if (userInput.equals("e"))
                gq.display_realm_info(input);
            else if (userInput.equals("f"))
                gq.display_public_campaign_info(input);
            else if (userInput.equals("g"))
                gq.display_all_user_info(input);
//            else
//                print("Invalid input");
        } while (!userInput.equals("x"));

        print("Exiting GuildQuest.");
    }
    public void display_realm_info(Scanner input)
    {
        for (Realm r : realms.values())
            print(r.toString());
        buffer_screen(input);
    }
    public void display_public_campaign_info(Scanner input)
    {
        for (Campaign c : getPublicCampaigns())
            print(c.toString());
        buffer_screen(input);
    }
    public void display_all_user_info(Scanner input)
    {
        for (User u : users.values())
            print(u.toString());
        buffer_screen(input);
    }
    public void add_realm_menu(Scanner input)
    {
        print("Enter realm name: ");
        String realmName = input.nextLine();
        print("Enter realm description: ");
        String realmDescription = input.nextLine();
        print("Enter realm time offset: ");
        int offset = input.nextInt();
        input.nextLine(); // clear buffer
        Realm newRealm = new Realm(realmName, realmDescription, new FixedOffsetRule(offset));
        addRealm(newRealm);
        print(newRealm.toString());
        buffer_screen(input);
    }
    public void buffer_screen(Scanner input)
    {
        do {
            print("Press c to continue");
        } while (!input.nextLine().equals("c"));
    }
    public void advance_time_menu(Scanner input)
    {
        print("You chose to advance time. Please enter the number of minutes you would like to advance.");
        int minutes = input.nextInt();
        advanceTime(minutes);
        input.nextLine(); // consume newline character left in buffer from getting next int
    }
    public void select_user_menu(Scanner input)
    {
        print("Enter username: ");
        String name = input.nextLine();
        if  (!users.containsKey(name))
        {
            print("User does not exist");
            buffer_screen(input);
            return;
        }
        User currentUser = users.get(name);
        String i = "";
        do {
            print("Choose a menu option for user " + name +
                    "\na: add campaign" +
                    "\nb: archive campaign" +
                    "\nc: update campaign" +
                    "\nd: display campaign info for " + name +
                    "\ne: add character" +
                    "\nf: remove character" +
                    "\ng: update character" +
                    "\nh: display a character's info" +
                    "\ni: display all character info" +
                    "\nj: user settings" +
                    "\nk: display all user info for " + name +
                    "\nz: back");
            i = input.nextLine();
            if (i.equals("a"))
                add_campaign_menu(currentUser, input);
            else if (i.equals("b"))
                archive_campaign_menu(currentUser, input);
            else if (i.equals("c"))
                update_campaign_menu(currentUser, input);
            else if (i.equals("d"))
                display_user_campaigns(currentUser, input);
            else if (i.equals("e"))
                add_character_menu(currentUser, input);
            else if (i.equals("f"))
                remove_character_menu(currentUser, input);
            else if (i.equals("g"))
                update_character_menu(currentUser, input);
            else if (i.equals("h"))
                display_character_info(currentUser, input);
            else if (i.equals("i"))
                display_all_character_info(currentUser, input);
            else if (i.equals("j"))
                user_settings_menu(currentUser, input);
            else if (i.equals("k"))
                display_all_info_for_one_user(currentUser, input);
        } while (!i.equals("z"));
    }
    public void display_all_info_for_one_user(User u, Scanner input)
    {
        print(u.toString());
        buffer_screen(input);
    }
    public void user_settings_menu(User u, Scanner input)
    {
        String i = "";
        do {
            print("Choose a menu option:" +
                    "\na: change Realm" +
                    "\nb: change Theme" +
                    "\nc: change Time Representation" +
                    "\nd: display current settings" +
                    "\nz: back");
            i = input.nextLine();
            if (i.equals("a"))
                change_realm_menu(u, input);
            if (i.equals("b"))
                change_theme_menu(u, input);
            if (i.equals("c"))
                change_time_rep_menu(u, input);
            if (i.equals("d"))
                display_current_settings(u, input);
        } while (!i.equals("z"));
    }
    public void display_current_settings(User u, Scanner input)
    {
        print(u.getSettings().toString());
        buffer_screen(input);
    }
    public void change_time_rep_menu(User u, Scanner input)
    {
        print("Choose a Time Representation (WORLD_TIME / LOCAL_TIME / BOTH");
        String i = input.nextLine();
        if (i.equalsIgnoreCase("WORLD_TIME"))
        {
            u.getSettings().setTimeRep(TimeRepresentation.WORLD_TIME);
            print("Time representation changed to WORLD_TIME");
        }
        else if (i.equalsIgnoreCase("LOCAL_TIME"))
        {
            u.getSettings().setTimeRep(TimeRepresentation.LOCAL_TIME);
            print("Time representation changed to LOCAL_TIME");
        }
        else if (i.equalsIgnoreCase("BOTH"))
        {
            u.getSettings().setTimeRep(TimeRepresentation.BOTH);
            print("Time representation changed to BOTH");
        }
        else
            print("Invalid Time Representation selection, no chnages made");
        buffer_screen(input);
    }
    public void change_theme_menu(User u, Scanner input)
    {
        print("Choose a theme (CLASSIC / MODERN)");
        String i = input.nextLine();
        if (i.equalsIgnoreCase("classic"))
        {
            u.getSettings().setTheme(Theme.CLASSIC);
            print("Theme changed to CLASSIC");
        }
        else if (i.equalsIgnoreCase("modern"))
        {
            u.getSettings().setTheme(Theme.MODERN);
            print("Theme changed to MODERN");
        }
        else
            print("Invalid Theme selection, no changes made");
        buffer_screen(input);
    }
    public void change_realm_menu(User u, Scanner input)
    {
        print("Enter realm name: ");
        String name = input.nextLine();
        if (!realms.containsKey(name))
        {
            print("Realm " + name + " doesn't exist");
            buffer_screen(input);
            return;
        }
        u.getSettings().setCurrentRealmName(name);
        print("Current Realm updated");
        buffer_screen(input);
    }
    public void display_all_character_info(User u, Scanner input)
    {
        print(u.getCharacters().values().toString());
        buffer_screen(input);
    }
    public void display_character_info(User u, Scanner input)
    {
        print("Enter character name: ");
        String name = input.nextLine();
        if (!u.containsCharacter(name))
        {
            print("Character " + name + " does not exist");
            buffer_screen(input);
            return;
        }
        print(u.getCharacter(name).toString());
        buffer_screen(input);
    }
    public void update_character_menu(User u, Scanner input)
    {
        print("Enter character name: ");
        String name = input.nextLine();
        if (!u.containsCharacter(name))
        {
            print("Character does not exist");
            buffer_screen(input);
            return;
        }
        Character curr =  u.getCharacter(name);
        String i = "";
        do {
            print("Choose a menu option: " +
                    "\na: change name" +
                    "\nb: change class" +
                    "\nc: change level" +
                    "\nd: update Inventory" +
                    "\ne: display character Inventory" +
                    "\nz: back");
            i = input.nextLine();
            if (i.equals("a"))
                change_character_name_menu(u, curr, input);
            else if (i.equals("b"))
                change_character_class_menu(u, curr, input);
            else if (i.equals("c"))
                change_character_level_menu(u, curr, input);
            else if (i.equals("d"))
                update_inventory_menu(u, curr, input);
            else if (i.equals("e"))
                display_character_inventory(curr, input);
        } while (!i.equals("z"));
    }
    public void display_character_inventory(Character ch, Scanner input)
    {
        print(ch.getInventory().toString());
        buffer_screen(input);
    }
    public void update_inventory_menu(User u, Character ch, Scanner input)
    {
        String i = "";
        do {
            print("Choose a menu option: " +
                    "\na: add Item" +
                    "\nb: remove Item" +
                    "\nc: update Item" +
                    "\nd: display Item details" +
                    "\nz: back");
            i = input.nextLine();
            if (i.equals("a"))
                add_item_menu(ch, input);
            else if (i.equals("b"))
                remove_item_menu(ch, input);
            else if (i.equals("c"))
                update_item_menu(ch, input);
            else if (i.equals("d"))
                display_item_menu(ch, input);
        } while (!i.equals("z"));
    }
    public void display_item_menu(Character ch, Scanner input)
    {
        print("Enter item name: ");
        String name = input.nextLine();
        if (!ch.getInventory().getItems().containsKey(name))
        {
            print("Item does not exist");
            buffer_screen(input);
            return;
        }
        print(ch.getInventory().getItems().get(name).toString());
        buffer_screen(input);
    }
    public void update_item_menu(Character ch, Scanner input)
    {
        print("Enter item name: ");
        String name = input.nextLine();
        if (ch.getInventory().getItems().containsKey(name))
        {
            Item currItem = ch.getInventory().getItems().get(name);
            String i = "";
            do {
                print("Choose a menu option:" +
                        "\na: change name" +
                        "\nb: change type" +
                        "\nc: change rarity" +
                        "\nd: change quantity" +
                        "\nz: back");
                i = input.nextLine();
                if (i.equals("a"))
                    change_item_name_menu(currItem, ch, input);
                else if (i.equals("b"))
                    change_item_type_menu(currItem, input);
                else if (i.equals("c"))
                    change_item_rarity_menu(currItem, input);
                else if (i.equals("d"))
                    change_item_quantity_menu(currItem, input);
            } while (!i.equals("z"));
        }
        else
        {
            print("Item does not exist");
            buffer_screen(input);
        }
    }
    public void change_item_quantity_menu(Item item, Scanner input)
    {
        print("Enter the new quantity:");
        int newQuantity = input.nextInt();
        input.nextLine(); // clear newline buffer
        item.setQuantity(newQuantity);
        print("Item quantity updated");
        buffer_screen(input);
    }
    public void change_item_rarity_menu(Item item, Scanner input)
    {
        print("Enter the new rarity of the item:");
        String rarity = input.nextLine();
        item.setRarity(rarity);
        print("Item rarity updated");
        buffer_screen(input);
    }
    public void change_item_type_menu(Item item, Scanner input)
    {
        print("Enter the new type of the item:");
        String type = input.nextLine();
        item.setType(type);
        print("Item type updated");
        buffer_screen(input);
    }
    public void change_item_name_menu(Item item, Character ch, Scanner input)
    {
        // we need to prompt for new item name, remove it from the map of items in
        // Inventory, then change the Item name and add it back into the map
        print("Enter new item name: ");
        String newName = input.nextLine();
        String oldName = item.getName();
        ch.getInventory().getItems().remove(oldName);
        item.setName(newName);
        ch.getInventory().addItem(item);
        print("Item name updated");
        buffer_screen(input);
    }
    public void remove_item_menu(Character ch, Scanner input)
    {
        print("Enter the name of the item to remove:");
        String name = input.nextLine();
        if (ch.getInventory().getItems().containsKey(name))
        {
            print(name + " quantity: " + ch.getInventory().getItems().get(name).getQuantity());
            print("How many of these would you like to remove? Enter a number:");
            int quantity = input.nextInt();
            input.nextLine(); // clear newline buffer
            ch.getInventory().removeItem(name, quantity);
            print("Item removal complete");
        }
        else
        {
            print("Item does not exist");
        }
        buffer_screen(input);
    }
    public void add_item_menu(Character ch, Scanner input)
    {
        print("Enter the name of the item to add: ");
        String name = input.nextLine();
        if (ch.getInventory().getItems().containsKey(name))
        {
            print("This item already exists. Please enter the number of how many you'd like to add:");
            int toAdd = input.nextInt();
            input.nextLine(); // clear newline buffer
            ch.getInventory().getItems().get(name).add(toAdd);
        }
        else
        {
            print("Enter the type of the item:");
            String type = input.nextLine();
            print("Enter the rarity of the item:");
            String rarity = input.nextLine();
            print("Enter the quantity to add:");
            int quantity = input.nextInt();
            input.nextLine(); // clear newline buffer
            ch.getInventory().addItem(new Item(name, type, rarity, quantity));
        }
        print("Item has been added");
        buffer_screen(input);
    }
    public void change_character_level_menu(User u, Character character, Scanner input)
    {
        print("Enter the new level number for Character " + character.getName() + ": ");
        int level = input.nextInt();
        input.nextLine(); // clear newline buffer
        character.setLevel(level);
        print("Character level has been updated");
        buffer_screen(input);
    }
    public void change_character_class_menu(User u, Character character, Scanner input)
    {
        print("Enter the new class details for Character " + character.getName() + ": ");
        String i = input.nextLine();
        character.setCharacterClass(i);
        print("Character class details have been updated");
        buffer_screen(input);
    }
    public void change_character_name_menu(User u, Character c, Scanner input)
    {
        // we need to prompt for the new name, make sure it's not a duplicate in
        // the other characters for this user, then we need to remove the current
        // character from the user's map of characters, remove the character
        // from any quest events it participated in while also logging those quest events,
        // change the character's name
        // then re-add the character to the map and re-add the character as a participant in
        // all the quest events it was a part of
        print("Enter new character name: ");
        String newName = input.nextLine();
        String oldName = c.getName();
        if (u.containsCharacter(newName))
        {
            print("A Character already goes by that name");
            buffer_screen(input);
            return;
        }
        u.removeCharacter(oldName);
        List<QuestEvent> affected = new ArrayList<>();
        for (Campaign campaign : u.getCampaigns().values())
        {
            for (QuestEvent q : campaign.getEvents())
            {
                if (q.containsCharacter(oldName)) {
                    q.removeParticipant(oldName);
                    affected.add(q);
                }
            }
        }
        c.setName(newName);
        u.addCharacter(c);
        for (QuestEvent q : affected)
            q.addParticipant(c);
        print("Character name has been updated to " + newName);
        buffer_screen(input);
    }
    public void remove_character_menu(User u, Scanner input)
    {
        print("Enter character name: ");
        String name = input.nextLine();
        if (!u.containsCharacter(name))
        {
            print("No character exists with name " + name + " to be removed");
            buffer_screen(input);
            return;
        }
        // we need to remove the character from the user's list of characters
        // and any quest events that character participated in
        u.removeCharacter(name);
        // go through each campaign's quest events, removing the desired character
        // from any participant lists it was a part of
        for (Campaign c : u.getCampaigns().values())
        {
            for (QuestEvent q : c.getEvents())
            {
                if (q.containsCharacter(name))
                    q.removeParticipant(name);
            }
        }
        print("Character " + name + " has been removed");
        buffer_screen(input);
    }
    public void add_character_menu(User u, Scanner input)
    {
        print("Enter character name: ");
        String name = input.nextLine();
        if (u.getCharacterNames().contains(name))
        {
            print("Character already exists");
            buffer_screen(input);
            return;
        }
        print("Enter character class: ");
        String characterClass = input.nextLine();
        print("Enter character level: ");
        int level = input.nextInt();
        input.nextLine(); // clear buffer of newline character
        u.addCharacter(new Character(name, u.getUsername(), characterClass, level));
        print(name + " added");
        buffer_screen(input);
    }
    public void update_campaign_menu(User u, Scanner input)
    {
        print("Enter campaign name: ");
        String cname = input.nextLine();
        if (!u.getCampaigns().containsKey(cname))
        {
            print("Campaign does not exist");
            buffer_screen(input);
            return;
        }
        Campaign currCampaign = u.getCampaigns().get(cname);
        String i = "";
        do {
            print("Choose a menu option for " + cname +
                    "\na: make PUBLIC" +
                    "\nb: make PRIVATE" +
                    "\nc: add QuestEvent" +
                    "\nd: remove QuestEvent" +
                    "\ne: update QuestEvent" +
                    "\nf: display QuestEvent" +
                    "\ng: Display QuestEvents in a timeline" +
                    "\nh: Display all QuestEvents in this campaign" +
                    "\nz: back");
            i = input.nextLine();
            if (i.equals("a"))
                currCampaign.setVisibility(Visibility.PUBLIC);
            else if (i.equals("b"))
                currCampaign.setVisibility(Visibility.PRIVATE);
            else if (i.equals("c"))
                add_quest_event_menu(currCampaign, input);
            else if (i.equals("d"))
                remove_quest_event_menu(currCampaign, input);
            else if (i.equals("e"))
                update_quest_event_menu(currCampaign, input, u);
            else if (i.equals("f"))
                display_quest_event_menu(currCampaign, u, input);
            else if (i.equals("g"))
                display_quest_event_timeline_menu(currCampaign, u, input);
            else if (i.equals("h"))
                display_all_quest_events_menu(currCampaign, u, input);
        } while (!i.equals("z"));
    }
    public void display_all_quest_events_menu(Campaign c, User u, Scanner input)
    {
        for (QuestEvent q : c.getEvents())
        {
            if (u.getSettings().getTimeRep() == TimeRepresentation.WORLD_TIME)
                print(q.toString());
            else if (u.getSettings().getTimeRep() == TimeRepresentation.LOCAL_TIME)
                print(q.toStringLocalTime());
            else
                print(q.toStringBothTimes());
            buffer_screen(input);
        }
    }
    public void display_quest_event_timeline_menu(Campaign c, User u, Scanner input)
    {
        print("Choose a menu option: " +
                "\na: display QuestEvents for the day" +
                "\nb: display QuestEvents for the week" +
                "\nc: display QuestEvents for the month" +
                "\nd: display QuestEvents for the year");
        String i = input.nextLine();
        if (i.equals("a"))
            print(c.getDailyEvents(worldClock.getCurrentTime()).toString());
        else if (i.equals("b"))
            print(c.getWeekEvents(worldClock.getCurrentTime()).toString());
        else if (i.equals("c"))
            print(c.getMonthEvents(worldClock.getCurrentTime()).toString());
        else if (i.equals("d"))
            print(c.getYearEvents(worldClock.getCurrentTime()).toString());
        else
            print("Returning to previous menu");
        buffer_screen(input);
    }
    public void display_quest_event_menu(Campaign c, User u, Scanner input)
    {
        print("Enter quest event name: ");
        String name = input.nextLine();
        if (!c.containsQuestEvent(name))
        {
            print("Quest event does not exist in this campaign");
            buffer_screen(input);
            return;
        }
        if (u.getSettings().getTimeRep() == TimeRepresentation.WORLD_TIME)
            print(c.getQuestEvent(name).toString());
        else if (u.getSettings().getTimeRep() == TimeRepresentation.LOCAL_TIME)
            print(c.getQuestEvent(name).toStringLocalTime());
        else
            print(c.getQuestEvent(name).toStringBothTimes());
        buffer_screen(input);
    }
    public void update_quest_event_menu(Campaign c, Scanner input, User u)
    {
        print("Enter quest event name: ");
        String questEventName = input.nextLine();
        if (!c.containsQuestEvent(questEventName))
        {
            print("Quest event does not exist");
            buffer_screen(input);
            return;
        }
        String i = "";
        do {
            QuestEvent qe = c.getQuestEvent(questEventName);
            print("Choose a menu option for QuestEvent " + questEventName +
                    "\na: rename QuestEvent" +
                    "\nb: set QuestEvent realm" +
                    "\nc: add participant Character to QuestEvent" +
                    "\nd: remove participant Character from QuestEvent" +
                    "\nz: back");
            i = input.nextLine();
            if (i.equals("a"))
                questEventName = rename_quest_event_menu(qe, c, input);
            else if (i.equals("b"))
                set_quest_event_realm_menu(qe, c, input);
            else if (i.equals("c"))
                add_participant_to_quest_event_menu(qe, u, input);
            else if (i.equals("d"))
                remove_participant_from_quest_event_menu(qe, u, input);
        } while (!i.equals("z"));
    }
    public void remove_participant_from_quest_event_menu(QuestEvent q, User u, Scanner input)
    {
        print("Enter the name of the participant to remove");
        String name = input.nextLine();
        if (!q.containsCharacter(name))
        {
            print(name + " is not a participant in this QuestEvent");
            buffer_screen(input);
            return;
        }
        q.removeCharacter(name);
        print(name + " has been removed");
        buffer_screen(input);
    }
    public void add_participant_to_quest_event_menu(QuestEvent q, User u, Scanner input)
    {
        print("Character names: " + u.getCharacterNames().toString());
        print("Enter the name of the character to add to the QuestEvent: ");
        String toAdd = input.nextLine();
        if (!u.containsCharacter(toAdd))
        {
            print(toAdd + " is not a character for the current user");
            buffer_screen(input);
            return;
        }
        if (q.containsCharacter(toAdd))
        {
            print(toAdd + " is already a QuestEvent participant");
            buffer_screen(input);
            return;
        }
        q.addParticipant(u.getCharacter(toAdd));
        print(toAdd + " has been added");
        buffer_screen(input);
    }
    public void set_quest_event_realm_menu(QuestEvent q, Campaign c, Scanner input)
    {
        print("Enter realm name: ");
        String realmName = input.nextLine();
        if (!realms.containsKey(realmName))
        {
            print("Realm does not exist");
            buffer_screen(input);
            return;
        }
        q.setRealm(realms.get(realmName));
        print("Realm updated");
        buffer_screen(input);
    }
    public String  rename_quest_event_menu(QuestEvent q, Campaign c, Scanner input)
    {
        // we want to prompt for a new name, then remove the quest event from the campaign's map
        // so the key of the old name is removed, then we want to call the setname method
        // for the quest event to change the quest event's name,
        // then we want to re-add the event to the campaign, mapping the new name to the same event
        print("Enter new quest event name: ");
        String newName = input.nextLine();
        if (c.containsQuestEvent(newName))
        {
            print("Name already used");
            buffer_screen(input);
            return q.getName();
        }
        String oldName = q.getName();
        c.removeEvent(oldName);
        q.setName(newName);
        c.addEvent(q);
        print("Name updated");
        buffer_screen(input);
        return newName;
    }
    public void remove_quest_event_menu(Campaign c, Scanner input)
    {
        print("Enter quest event name: ");
        String questEventName = input.nextLine();
        if (!c.containsQuestEvent(questEventName))
        {
            print("Quest event does not exist");
            buffer_screen(input);
            return;
        }
        c.removeEvent(questEventName);
        print(questEventName + " has been removed");
    }
    public void add_quest_event_menu(Campaign c, Scanner input)
    {
        print("Enter quest event name: ");
        String name = input.nextLine();
        print("Enter start time day number: ");
        int startDay = input.nextInt();
        input.nextLine();
        print("Enter start time hour number: ");
        int startHour = input.nextInt();
        input.nextLine();
        print("Enter start time minute number: ");
        int startMinute = input.nextInt();
        input.nextLine();
        print("Enter end time day number: ");
        int endDay = input.nextInt();
        input.nextLine();
        print("Enter end time hour number: ");
        int endHour = input.nextInt();
        input.nextLine();
        print("Enter end time minute number: ");
        int endMinute = input.nextInt();
        input.nextLine();
        String realmName = "";
        do {
            print("Enter the name of the realm where this quest event takes place: ");
            realmName = input.nextLine();
            if (!realms.containsKey(realmName))
            {
                print("Realm does not exist");
            }
        } while (!realms.containsKey(realmName));
        c.addEvent(new QuestEvent(name, new WorldTime(startDay, startHour, startMinute),
                new WorldTime(endDay, endHour, endMinute), realms.get(realmName)));
        print("Quest event " + name + " added.");
        buffer_screen(input);
    }
    public void display_user_campaigns(User u, Scanner input)
    {
        for (Campaign c : u.getCampaigns().values())
            print(c.toString());
        buffer_screen(input);
    }
    public void archive_campaign_menu(User u, Scanner input)
    {
        print("Enter campaign name: ");
        String name = input.nextLine();
        if (u.getCampaigns().containsKey(name))
        {
            u.archiveCampaign(name);
            return;
        }
        else
        {
            print("Campaign does not exist");
        }
    }
    public void add_campaign_menu(User u, Scanner input)
    {
        print("Enter campaign name: ");
        String name = input.nextLine();
        if (u.getCampaigns().containsKey(name))
        {
            print("User campaign already exists");
            return;
        }
        u.addCampaign(name);
    }
    public void add_user_menu(Scanner input)
    {
        print("Enter the new username:");
        String name =  input.nextLine();
        addUser(new User(name));
        print("New user added.");
    }

}