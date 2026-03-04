import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID uuid;
    private String username;
    private List<Campaign> campaigns;
    private List<Character> characters;
    private Settings settings;

    public User(String username) {
        //constructor for if the User has a starting Realm
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.settings = new Settings();
        this.campaigns = new ArrayList<>();
        this.characters = new ArrayList<>();
    }

    public void createCampaign(String name){
        Campaign campaign = new Campaign(name, this);
        campaigns.add(campaign);
        GuildQuest.getInstance().createCampaign(campaign);
    }

    public void deleteCampaign(int index) throws IndexOutOfBoundsException{
        if(!(index - 1 < campaigns.size())) throw new IndexOutOfBoundsException();
        else if(!campaigns.get(index - 1).isCan_edit()) System.out.println("You cannot delete this campaign!");
        else {
            Campaign campaign = campaigns.get(index - 1);
            campaigns.remove(index - 1);
            GuildQuest.getInstance().deleteCampaign(campaign);
        }
    }

    public void addCampaign(Campaign campaign, boolean can_edit){
        campaign.setCan_edit(can_edit);
        campaigns.add(campaign);
    }

    public Character createCharacter(String name, String class_type){
        Character character = new Character(name, class_type);
        this.characters.add(character);
        return character;
    }

    public void deleteCharacter(Character character){
        characters.remove(character);
    }

    public void shareCampaign(Campaign campaign, User user, boolean can_edit){
        user.addCampaign(campaign, can_edit);
    }

    public void displayTime(){
        int mode = settings.getTime_display_mode();
        switch(mode){
            case 0: //world time
                System.out.println(WorldClock.getInstance());
                break;
            case 1: //local time
                System.out.println(settings.getCurrent_realm().toLocalTime());
                break;
            case 2:
                System.out.printf("World Time: %s\n", WorldClock.getInstance());
                System.out.printf("Local Time: %s\n", settings.getCurrent_realm().toLocalTime());
        }
    }

    public void displayCampaigns(){
        for(int i = 0; i < campaigns.size(); i++){
            System.out.printf("%s. %s\n", i + 1,  campaigns.get(i).getName());
        }
    }

    public List<Campaign> getCampaigns(){
        return Collections.unmodifiableList(this.campaigns);
    }
}
