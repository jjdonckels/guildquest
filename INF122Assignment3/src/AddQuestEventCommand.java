import java.util.Scanner;

public class AddQuestEventCommand implements Command{

    private Campaign campaign;

    public AddQuestEventCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        String name;
        WorldTime start_time;
        WorldTime end_time = new WorldTime();
        Realm realm = GuildQuest.getInstance().hub(); //hub realm default
        Scanner scanner = new Scanner(System.in);

        //title
        System.out.println("What would you like to title this QuestEvent?: ");
        name = scanner.nextLine();

        //start time
        System.out.println("When would you like this QuestEvent to take place? Enter a time in minutes.");
        System.out.printf("For reference, the current time in minutes is %s.\n", WorldClock.getInstance().getCurrentTime().getMinutes_passed());
        int start_time_minutes = scanner.nextInt();
        start_time = new WorldTime(start_time_minutes);

        //end time
        System.out.println("How long would you like this QuestEvent to last? Enter a time in minutes.");
        int end_time_minutes = scanner.nextInt() + start_time_minutes;
        end_time = new WorldTime(end_time_minutes);

        //realm
        boolean valid_realm = false;
        while(!valid_realm) {
            System.out.println("What Realm would you like this QuestEvent to take place in? Please enter its name.");
            GuildQuest.getInstance().displayRealms();
            realm = GuildQuest.getInstance().findRealm(scanner.nextLine());
            if(realm != null) { valid_realm = true; }
            else System.out.println("Invalid input. Please try again.");
        }

        campaign.addQuestEvent(new QuestEvent(name, start_time, end_time, realm));
    }
}
