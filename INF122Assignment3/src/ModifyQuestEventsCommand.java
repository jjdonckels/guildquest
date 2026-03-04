public class ModifyQuestEventsCommand implements Command{

    private Campaign campaign;
    private User user;

    public ModifyQuestEventsCommand(Campaign campaign, User user){
        this.campaign = campaign;
        this.user = user;
    }

    public void execute(){
        if(campaign.isCan_edit()) CampaignController.modify_quest_events(user, campaign);
        else {
            System.out.println("You cannot modify this campaign! Here are the QuestEvents:\n");
            campaign.displayAllEventDetails(user);
        }
    }

}
