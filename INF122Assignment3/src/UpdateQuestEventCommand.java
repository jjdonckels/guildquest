import java.util.Scanner;

public class UpdateQuestEventCommand implements Command{

    private Campaign campaign;

    public UpdateQuestEventCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        Scanner scanner = new Scanner(System.in);
        if(!campaign.getEvents().isEmpty()) {
            boolean chosen = false;
            while (!chosen) {
                System.out.println("Which QuestEvent would you like to modify? Please type its title.");
                System.out.println(campaign.listEvents());
                String quest_event = scanner.nextLine();
                QuestEvent qe = campaign.findQuestEvent(quest_event);
                if (qe != null) {
                    CampaignController.update_quest_event(qe);
                    chosen = true;
                } else System.out.println("Invalid input. Please try again.");
            }
        }
        else System.out.println("You have no QuestEvents to modify!");
    }

}
