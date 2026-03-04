public class CampaignArchiveCommand implements Command {

    private Campaign campaign;

    public CampaignArchiveCommand(Campaign campaign){
        this.campaign = campaign;
    }

    public void execute(){
        if(campaign.isCan_edit()) campaign.setArchived(true);
        else System.out.println("You cannot archive this campaign!");
    }
}
