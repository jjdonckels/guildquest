import java.util.Scanner;

public class DeleteQuestEventCommand implements Command{

    private Campaign campaign;

    public DeleteQuestEventCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        Scanner scanner = new Scanner(System.in);
        if(!campaign.getEvents().isEmpty()) {
            boolean selected = false;
            while (!selected) {
                System.out.println("Which QuestEvent would you like to delete? Please type its title.");
                System.out.println(campaign.listEvents());
                String quest_event = scanner.nextLine();
                if (campaign.removeQuestEvent(campaign.findQuestEvent(quest_event).getEvent_id()))
                    selected = true;
                else System.out.println("Invalid input. Please try again.");
            }
        }
        else System.out.println("You have no QuestEvents to delete!");
    }
}
