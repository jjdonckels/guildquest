public class EditCampaignCommand implements Command{

    private User user;
    private Campaign campaign;

    public EditCampaignCommand(User user, Campaign campaign){
        this.user = user;
        this.campaign = campaign;
    }

    @Override
    public void execute() {
        CampaignController.edit_campaign(user, campaign);
    }
}
