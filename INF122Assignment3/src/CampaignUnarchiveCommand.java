public class CampaignUnarchiveCommand implements Command{

    private Campaign campaign;

    public CampaignUnarchiveCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        if(campaign.isCan_edit()) campaign.setArchived(false);
        else System.out.println("You cannot archive this campaign!");
    }
}
