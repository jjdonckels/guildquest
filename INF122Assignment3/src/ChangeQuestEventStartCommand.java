import java.util.Scanner;

public class ChangeQuestEventStartCommand implements Command{

    private QuestEvent qe;

    public ChangeQuestEventStartCommand(QuestEvent qe){
        this.qe = qe;
    }

    public void execute(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("When would you like this QuestEvent to take place? Enter a time in minutes.");
        System.out.printf("For reference, the current time in minutes is %s.\n", WorldClock.getInstance().getCurrentTime().getMinutes_passed());
        int start_time_minutes = scanner.nextInt();
        qe.setStart_time(new WorldTime(start_time_minutes));
    }

}
