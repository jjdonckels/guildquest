import java.util.Scanner;

public class ChangeCampaignNameCommand implements Command{

    private final Campaign campaign;
    private final Scanner scanner = new Scanner(System.in);

    public ChangeCampaignNameCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        System.out.println("What would you like the new name to be?");
        campaign.setName(scanner.nextLine());
    }
}
