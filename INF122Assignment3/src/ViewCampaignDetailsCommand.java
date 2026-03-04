public class ViewCampaignDetailsCommand implements Command{
    private Campaign campaign;
    private User user;

    public ViewCampaignDetailsCommand(Campaign campaign, User user){
        this.campaign = campaign;
        this.user = user;
    }

    public void execute(){
        System.out.println("\n" + campaign);
        System.out.print("\nIndividual Event Details:\n");
        campaign.displayAllEventDetails(user);
    }

}
