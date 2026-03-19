import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;
//singleton class & facade (+2 design patterns)

@Getter
@Setter
public class GMAE {

    private static PlayerController playerController;
    private static RealmController realmController;
    private static GMAE instance = null;

    private GMAE(){
        playerController = new PlayerController();
        realmController = new RealmController();
    }

    public static GMAE getInstance(){
        if(instance == null)
            instance = new GMAE();
        return instance;
    }

    //commented out cuz they from my og implementation so we can add/remove as necessary
   /* //user methods
    public static void login(){ userController.login(); }

    public User findUser(String username){ return userController.findUser(username); }

    //campaign methods
    public void createCampaign(Campaign campaign){ campaignController.createCampaign(campaign); }

    public void deleteCampaign(Campaign campaign){
        campaignController.deleteCampaign(campaign);
    }

    public void viewGlobalCampaigns(){ campaignController.displayCampaigns(); }

    public static void view_campaign(User user, Campaign campaign) { campaignController.view_campaign(user, campaign); }

    //realm methods
    public Realm findRealm(String realm_name){ return realmController.findRealm(realm_name); }

    public void displayRealms(){ realmController.displayRealms(); }

    public Realm hub(){ return realmController.hub(); }
*/

    public static void createPlayers(){ playerController.createPlayers(); }

    private static int chooseGame(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.printf("""
                    Which Mini-Adventure game would you like to play? Input the number of the game you choose.
                    1. Escort Service
                    2. Relic Hunt
                    3. Exit""");
            String choice = scanner.nextLine();
            if(choice.equals("1")){ return 1; }
            else if(choice.equals("2")){ return 2; }
            else if(choice.equals("3")){ System.exit(0); }
        }
    }

    public static void Main(String args[]){
        System.out.printf("""
                WELCOME TO GMAE: GuildQuest Mini-Adventure Environment!
                """);
        createPlayers();

        //launch menu of mini-adventures and let them choose
        //loops until they end the game (game ending takes place in chooseGame())
        while(true){
            //idk if we like this structure we can change it later
            int choice =  chooseGame();
            AdventureType game;
            if(choice == 1){ game = new EscortService(); } // 1 = Escort Service
            else { game = new RelicHunt(); } //else instead of else if so ide doesn't tweak about var initialization
            game.play();
        }
    }
}