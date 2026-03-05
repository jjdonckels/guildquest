package guildquest.user;

import guildquest.campaign.Campaign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private final String          username;
    private final List<Campaign>  campaigns  = new ArrayList<>();
    private final List<Character> characters = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }

    // ── Campaigns ─────────────────────────────────────────────────────────────

    public void addCampaign(Campaign campaign)    { campaigns.add(campaign); }
    public void removeCampaign(Campaign campaign) { campaigns.remove(campaign); }
    public List<Campaign> getCampaigns() {
        return Collections.unmodifiableList(campaigns);
    }

    // ── Characters ────────────────────────────────────────────────────────────

    public void addCharacter(Character character)    { characters.add(character); }
    public void removeCharacter(Character character) { characters.remove(character); }
    public List<Character> getCharacters() {
        return Collections.unmodifiableList(characters);
    }
}
