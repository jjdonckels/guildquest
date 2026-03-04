import java.util.Scanner;

public class ViewQuestEventDetailsCommand implements Command{

    private Campaign campaign;
    private User user;

    public ViewQuestEventDetailsCommand(Campaign campaign, User user){
        this.campaign = campaign;
        this.user = user;
    }
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        if(!campaign.getEvents().isEmpty()) {
            boolean viewed = false;
            while (!viewed) {
                System.out.println("Which QuestEvent would you like to view? Please type its title.");
                System.out.println(campaign.listEvents());
                String quest_event = scanner.nextLine();
                QuestEvent qe = campaign.findQuestEvent(quest_event);
                if (qe != null) {
                    System.out.println(qe.display(user));
                    System.out.println("Press enter to continue.");
                    scanner.nextLine();
                    viewed = true;
                } else System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
