import java.util.Scanner;

public class ChangeCampaignVisibilityCommand implements Command{

    private final Campaign campaign;
    private final Scanner scanner = new Scanner(System.in);

    public ChangeCampaignVisibilityCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        System.out.println("What would you like the new visibility to be? Type private or public.");
        boolean valid_input = false;

        while(!valid_input){
            String publicity =  scanner.nextLine();
            if(publicity.equalsIgnoreCase("public")) { campaign.set_public(true); valid_input = true; }
            else if(publicity.equalsIgnoreCase("private")) { campaign.set_public(false); valid_input = true; }
            else System.out.println("Invalid input. Please try again.");
        }
    }

}
