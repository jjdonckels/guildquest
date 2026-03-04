import java.util.Scanner;

public class ShareCampaignCommand implements Command {
    private User user;
    private Campaign campaign;

    public ShareCampaignCommand(User user, Campaign campaign) {
        this.user = user;
        this.campaign = campaign;
    }

    public void execute(){
        Scanner scanner = new Scanner(System.in);
        User u = null;
        boolean can_edit = false;
        boolean valid_input = false;
        while(!valid_input){
            System.out.println("Please enter the username of the user you would like to share with.");
            String username = scanner.nextLine();
            u = GuildQuest.getInstance().findUser(username);
            if(u != null){
                valid_input = true;
            }
            else{
                System.out.println("Invalid username, please try again.");
            }
        }
        valid_input = false;
        while(!valid_input){
            System.out.println("Please enter 0 if you would like the user to be able to edit your campaign and 1 otherwise.");
            String editable =  scanner.nextLine();
            can_edit = editable.equals("0");
            if((editable.equals("0") || editable.equals("1"))){
                valid_input = true;
            }
        }
        user.shareCampaign(campaign, u, can_edit);
    }
}
