import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuildQuest {

    private static UserController userController;
    private static CampaignController campaignController;
    private static RealmController realmController;
    private static GuildQuest instance = null;

    private GuildQuest(){
        userController = new UserController();
        campaignController = new CampaignController();
        realmController = new RealmController();
        }

    public static GuildQuest getInstance(){
        if(instance == null)
            instance = new GuildQuest();
        return instance;
    }

    //user methods
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

}
