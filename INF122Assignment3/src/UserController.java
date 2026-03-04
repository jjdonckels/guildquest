import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter
public class UserController {

    private List<User> users;

    public UserController(){
        users = new ArrayList<>();
    }



    //login & logout functionalities

    public void login(){
        System.out.println("Please enter your username to login or create an account by entering 0.\n");
        boolean logged_in = false;
        String username = "";
        while(!logged_in){
            Scanner scanner =  new Scanner(System.in);
            username = scanner.nextLine();

            if(username.equals("0")){
                //creating a user
                System.out.println("What would you like your username to be?\n");
                username = scanner.nextLine();
                createUser(username);
                logged_in = true;
            }
            else{
                //checking to see if user exists
                logged_in = login(username);
                if(!logged_in){System.out.println("Incorrect username. Enter 0 if you would like to create an account or try another username.\n");}
            }
        }

        user_home(findUser(username));
    }

    boolean login(String username){
        User user = findUser(username);
        return user != null;
    }

    void createUser(String username){
        users.add(new User(username));
    }

    User findUser(String username){
        for(User user : users){
            if(user.getUsername().equalsIgnoreCase(username)){ return user;}
        }
        return null;
    }

    void logout(){
        boolean selected = false;
        while(!selected){

            System.out.println("Would you like to exit or log into another account?");
            String selection = scanInput("""
                1. Exit
                2. Log into another account""", Arrays.asList("1", "2"));
            if(selection.equals("1")){ System.exit(0);}
            else if(selection.equals("2")){ selected = true; login();}
        }
    }


    //homepage functionalities

    void user_home(User user){
        boolean selected = false;
        while(!selected){
            System.out.printf("Welcome back %s! What would you like to do?\n", user.getUsername());
            String selection = scanInput("""
                    1. Select/Delete Character
                    2. View Campaigns
                    3. Settings
                    4. Logout""", Arrays.asList("1", "2", "3", "4"));
            if(selection.equals("1")){
                character_selection(user);
            }
            else if(selection.equals("2")){
                display_campaigns(user);
            } else if (selection.equals("3")) {
                display_settings(user);
            }
            else if(selection.equals("4")){
                logout();
            }
        }
    }

    private void character_home(User user, Character character){
        boolean selected = false;
        while(!selected){
            System.out.printf("Hello %s! What would you like to do?\n", character.getName());
            String selection = scanInput("""
                    1. Move Realms
                    2. View Inventory
                    3. Check Time
                    4. Change Character
                    5. Settings
                    6. Logout
                    """, Arrays.asList("1", "2", "3", "4", "5", "6"));
            switch(selection){
                case "1":
                    move_realms(user);
                    break;
                case "2":
                    selected = true;
                    view_inventory(user, character);
                    break;
                case "3":
                    display_time(user);
                    break;
                case "4":
                    selected = true;
                    character_selection(user);
                    break;
                case "5":
                    display_settings(user);
                    break;
                case "6":
                    selected = true;
                    logout();
            }
        }
    }

    private void display_campaigns(User user){
        Scanner scanner = new Scanner(System.in);
        boolean selected = false;
        while(!selected){
            System.out.println("Your campaigns: ");
            user.displayCampaigns();
            String selection = scanInput("""
                What would you like to do?
                1. Create New Campaign
                2. Delete Campaign
                3. Select Campaign
                4. View Global Campaigns
                5. Back
                """, Arrays.asList("1", "2", "3", "4", "5"));
            switch(selection){
                case "1":
                    System.out.println("What would you like to name your campaign?");
                    user.createCampaign(scanner.nextLine());
                    break;
                case "2":
                    System.out.println("Which campaign would you like to delete? Note: This action is irreversible. If you would like to change your mind, enter 0.");
                    user.displayCampaigns();
                    int campaign =  Integer.parseInt(scanner.nextLine());
                    try{
                        user.deleteCampaign(campaign);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Invalid selection, please try again.");
                    }
                    break;
                case "3":
                    System.out.println("Which campaign would you like to select?");
                    user.displayCampaigns();
                    int choice = Integer.parseInt(scanner.nextLine());
                    if(choice - 1 < user.getCampaigns().size()){
                        GuildQuest.view_campaign(user, user.getCampaigns().get(choice - 1));
                    }
                    break;
                case "4":
                    GuildQuest.getInstance().viewGlobalCampaigns();
                    break;
                case "5":
                    selected = true;
                    user_home(user);
                    break;
            }
        }
    }


    //characters

    private void character_selection(User user){
        Scanner scanner = new Scanner(System.in);
        boolean selected = false;
        while(!selected){
            System.out.println("Select your character.");
            int i = 0;
            for(; i < user.getCharacters().size(); i++){
                System.out.printf("%s. %s\n", (i + 1), user.getCharacters().get(i));
            }
            System.out.printf("%s. Create new Character\n", ++i);
            System.out.printf("%s. Delete Character\n", ++i);
            System.out.printf("%s. Back\n", ++i);

            String selection =  scanner.nextLine();
            try{
                int n = Integer.parseInt(selection);
                if(n == i){ user_home(user); }
                else if(n == i - 2){ create_character(user); }
                else if(n == i - 1){ delete_character(user); }
                else if(n > user.getCharacters().size()){ System.out.println("Invalid selection, please try again.\n");}
                else{ character_home(user, user.getCharacters().get(n - 1)); }
            } catch (NumberFormatException e){
                System.out.println("Invalid selection, please try again.\n");
            }
        }
    }

    private void create_character(User user){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter character name: ");
        String character_name = scanner.nextLine();
        System.out.print("\nEnter character class: ");
        String character_class = scanner.nextLine();
        Character character = user.createCharacter(character_name, character_class);
        character_home(user, character);
    }

    private void delete_character(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the character you would like to delete.");
        int i = 0;
        for (; i < user.getCharacters().size(); i++) {
            System.out.printf("%s. %s\n", (i + 1), user.getCharacters().get(i));
        }
        try {
            int n = Integer.parseInt(scanner.nextLine());
            if (n > user.getCharacters().size()) {
                System.out.println("Invalid selection, please try again.\n");
            } else {
                user.deleteCharacter(user.getCharacters().get(n - 1));
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection, please try again.\n");
        }

    }



    //inventories & items

    void view_inventory(User user, Character character){
        boolean done = false;
        while(!done){
            character.displayInventory();
            String selection = scanInput("""
                    What would you like to do?
                    1. Pick up item
                    2. Drop item
                    3. Update item
                    4. Done""", Arrays.asList("1", "2", "3", "4"));
            switch(selection){
                case "1":
                    pickup_item(user, character);
                    break;
                case "2":
                    drop_item(user, character);
                    break;
                case "3":
                    update_item_prompt(user, character);
                    break;
                case "4":
                    done = true;
                    break;
            }
        }
        character_home(user, character);
    }

    private void pickup_item(User user, Character character) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name: ");
        String item_name = scanner.nextLine();
        System.out.println("Enter item description: ");
        String item_description = scanner.nextLine();
        System.out.println("Enter item rarity: ");
        String item_rarity = scanner.nextLine();
        System.out.println("Enter item quantity: ");
        String item_quantity = scanner.nextLine();
        boolean done = false;
        while(!done){
            try{
                character.addItem(new InventoryItem(item_name, item_description, item_rarity, Integer.parseInt(item_quantity)));
                done = true;
            } catch (NumberFormatException e){
                System.out.println("Invalid input. Please input a number.");
            }
        }
    }

    private void drop_item(User user, Character character) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while(!done){
            System.out.println("Your inventory contains: ");
            character.displayInventory();
            System.out.println("Please enter the name of the item you would like to drop: ");
            String item_name = scanner.nextLine();
            done = character.removeItem(item_name);
            if(!done) System.out.println("Invalid item name. Please try again.");
        }
    }

    private void update_item_prompt(User user, Character character) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while(!done){
            System.out.println("Your inventory contains: ");
            character.displayInventory();
            System.out.println("Please enter the name of the item you would like to update: ");
            String item_name = scanner.nextLine();
            InventoryItem item = character.findItem(item_name);
            if(item == null) System.out.println("Invalid item name. Please try again.");
            else{
                update_item(character, item);
                done = true;
            }
        }
    }

    private void update_item(Character character, InventoryItem item){
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while(!done){
            System.out.println(item);
            String selection = scanInput("""
                    What would you like to update?
                    1. Title
                    2. Description
                    3. Rarity
                    4. Quantity
                    5. Done
                    """, Arrays.asList("1", "2", "3", "4", "5"));
            switch(selection){
                case "1":
                    System.out.println("Please enter the new title: ");
                    item.setName(scanner.nextLine());
                    break;
                case "2":
                    System.out.println("Please enter the new description: ");
                    item.setDescription(scanner.nextLine());
                    break;
                case "3":
                    System.out.println("Please enter the new rarity: ");
                    item.setRarity(scanner.nextLine());
                    break;
                case "4":
                    boolean valid_input = false;
                    while(!valid_input){
                        System.out.println("Please enter the new quantity: ");
                        try{
                            item.setQuantity(Integer.parseInt(scanner.next()));
                            valid_input = true;
                        } catch (NumberFormatException e){
                            System.out.println("Invalid input. Please try again by entering a number.");
                        }
                    }
                    break;
                case "5":
                    done = true;
                    break;
            }
        }
    }




    //settings

    void display_settings(User user){
        boolean done = false;
        while(!done){
            System.out.println(user.getSettings());
            System.out.println("What would you like to do?");
            String selection = scanInput("""
                    1. Move Realms
                    2. Change Theme
                    3. Change Time Display Mode
                    4. Back
                    """, Arrays.asList("1", "2", "3", "4"));
            switch (selection){
                case "1":
                    move_realms(user);
                    break;
                case "2":
                    change_theme(user);
                    break;
                case "3":
                    change_tdm(user);
                    break;
                case "4":
                    done = true;
                    break;
            }
        }
    }

    private void move_realms(User user){
        Scanner scanner = new Scanner(System.in);
        boolean selected = false;
        while(!selected){
            System.out.println("What is the name of the realm you would like to go to?");
            GuildQuest.getInstance().displayRealms();
            String selection = scanner.nextLine();
            Realm realm = GuildQuest.getInstance().findRealm(selection);
            if(realm != null){ user.getSettings().setCurrent_realm(realm); selected = true;}
            else{System.out.println("Invalid selection. Please try again.");}
        }
    }

    private void change_theme(User user){
        Scanner scanner = new Scanner(System.in);
        boolean selected = false;
        while(!selected){
            System.out.println("What theme you would like to use?");
            user.getSettings().displayThemes();
            String selection = scanner.nextLine();
            selected = user.getSettings().changeTheme(Integer.parseInt(selection));
            if(!selected){System.out.println("Invalid selection. Please try again.");}
        }
    }

    private void change_tdm(User user){
        Scanner scanner = new Scanner(System.in);
        boolean selected = false;
        while(!selected){
            System.out.println("What time display you would like to use?");
            user.getSettings().displayTimeDisplayModes();
            String selection = scanner.nextLine();
            selected = user.getSettings().changeTimeDisplayMode(Integer.parseInt(selection));
            if(!selected){System.out.println("Invalid selection. Please try again.");}
        }
    }

    private void display_time(User user){ user.displayTime(); }

    private String scanInput(String options, List<String> validInputs){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println(options);
            String input = scanner.nextLine();
            if(validInputs.contains(input)){
                return input;
            }
            else{
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

}
