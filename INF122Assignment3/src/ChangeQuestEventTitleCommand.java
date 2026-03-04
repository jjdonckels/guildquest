import java.util.Scanner;

public class ChangeQuestEventTitleCommand implements Command{

    private QuestEvent qe;

    public ChangeQuestEventTitleCommand(QuestEvent qe){
        this.qe = qe;
    }

    public void execute(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("What would you like to title this QuestEvent?: ");
        qe.setTitle(scanner.nextLine());
    }
}
