import java.util.Scanner;

public class ChangeQuestEventRuntimeCommand implements Command{

    private QuestEvent qe;

    public ChangeQuestEventRuntimeCommand(QuestEvent qe) {
        this.qe = qe;
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How long would you like this QuestEvent to last? Enter a time in minutes.");
        int end_time_minutes = scanner.nextInt() + qe.getStart_time().getMinutes_passed();
        qe.setEnd_time(new WorldTime(end_time_minutes));
    }
}
