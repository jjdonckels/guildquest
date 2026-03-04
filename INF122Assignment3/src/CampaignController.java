import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class CampaignController {

    private List<Campaign> campaigns;

    public CampaignController(){
        campaigns = new ArrayList<>();
    }

    public void createCampaign(Campaign campaign){
        campaigns.add(campaign);
    }

    public void deleteCampaign(Campaign campaign){
        campaigns.remove(campaign);
    }

    public void displayCampaigns(){
        int i = 1;
        for(Campaign campaign : campaigns){
            if(campaign.is_public()) {System.out.printf("%s. %s\n", i, campaign.getName()); i++;}
        }
    }

    void view_campaign(User user, Campaign campaign){

        Map<String, Command> commands = new HashMap<>();
        commands.put("1", new EditCampaignCommand(user, campaign));
        commands.put("2", new ShareCampaignCommand(user, campaign));
        commands.put("3", new ViewCampaignDetailsCommand(campaign, user));

        boolean selected = false;
        while(!selected){
            System.out.println("\n" + campaign);
            String selection = scanInput("""
                What would you like to do with this campaign?
                1. Edit Campaign
                2. Share Campaign
                3. View Campaign Details
                4. Back
            """, Arrays.asList("1", "2", "3", "4"));

            if(selection.equals("4")){ selected = true; }
            else{ commands.get(selection).execute(); }
        }
    }

    public static void edit_campaign(User user, Campaign campaign) {

        Map<String, Command> commands = new HashMap<>();
        commands.put("1", new ChangeCampaignNameCommand(campaign));
        commands.put("2", new ChangeCampaignVisibilityCommand(campaign));
        commands.put("3", new ModifyQuestEventsCommand(campaign, user));
        commands.put("4", new CampaignArchiveCommand(campaign));
        commands.put("5", new CampaignUnarchiveCommand(campaign));

        boolean selected = false;
        while(!selected){
            System.out.println("\n" + campaign);
            String selection = scanInput("""
                    What would you like to do with this campaign?
                    1. Change Name
                    2. Change Visibility
                    3. Modify or View QuestEvents
                    4. Archive
                    5. Remove from Archive
                    6. Done
                    """, Arrays.asList("1", "2", "3", "4", "5", "6"));

            if(selection.equals("6")){ selected = true; }
            else{ commands.get(selection).execute(); }
        }
    }

    public static void modify_quest_events(User user, Campaign campaign) {
        Map<String, Command> commands = new HashMap<>();
        commands.put("1", new AddQuestEventCommand(campaign));
        commands.put("2", new DeleteQuestEventCommand(campaign));
        commands.put("3", new UpdateQuestEventCommand(campaign));
        commands.put("4", new ViewQuestEventDetailsCommand( campaign, user));

        boolean done = false;
        while(!done){
            System.out.printf("Events in this campaign: %s\n", campaign.listEvents());
            String selection = scanInput("""
                    What would you like to do with the quest events?
                    1. Add QuestEvent
                    2. Delete QuestEvent
                    3. Update QuestEvent
                    4. View QuestEvent Details
                    5. Back""", Arrays.asList("1", "2", "3", "4", "5"));

            if(selection.equals("5")){ done = true; }
            else{ commands.get(selection).execute(); }
        }
    }


    public static void update_quest_event(QuestEvent qe) {
        Map<String, Command> commands = new HashMap<>();
        commands.put("1", new ChangeQuestEventTitleCommand(qe));
        commands.put("2", new ChangeQuestEventStartCommand(qe));
        commands.put("3", new ChangeQuestEventRuntimeCommand(qe));
        commands.put("4", new ChangeQuestEventRealmCommand(qe));

        boolean done = false;
        while(!done){
            String selection = scanInput("""
                    What would you like to change about this QuestEvent?
                    1. Title
                    2. Start Time
                    3. Runtime
                    4. Realm
                    5. Done""", Arrays.asList("1", "2", "3", "4", "5"));

            if(selection.equals("5")) { done = true; }
            else{ commands.get(selection).execute(); }

        }
    }

    private static String scanInput(String options, List<String> validInputs){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println(options);
            String input = scanner.nextLine();
            if(validInputs.contains(input)){
                return input;
            }
            else{
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

}
