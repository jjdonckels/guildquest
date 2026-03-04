import java.util.Scanner;

public class ChangeQuestEventRealmCommand implements Command{

    private QuestEvent qe;

    public ChangeQuestEventRealmCommand(QuestEvent qe){
        this.qe = qe;
    }

    public void execute(){
        Scanner scanner = new Scanner(System.in);
        Realm realm = null;
        boolean valid_realm = false;
        while(!valid_realm) {
            System.out.println("What Realm would you like this QuestEvent to take place in? Please enter its name.");
            GuildQuest.getInstance().displayRealms();
            realm = GuildQuest.getInstance().findRealm(scanner.nextLine());
            if(realm != null) { valid_realm = true; }
            else System.out.println("Invalid input. Please try again.");
        }
        qe.setRealm(realm);
    }
}
