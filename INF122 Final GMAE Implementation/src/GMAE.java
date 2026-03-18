import lombok.Getter;
import lombok.Setter;
//singleton class & facade

@Getter
@Setter
public class GMAE {

    //private static UserController userController;
    //private static CampaignController campaignController;
    //private static RealmController realmController;
    private static GMAE instance = null;

    private GMAE(){
        
    }

    public static GMAE getInstance(){
        if(instance == null)
            instance = new GMAE();
        return instance;
    }

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


    public static void Main(String args[]){
        //launch menu of mini-adventures and let them choose

    }
}