import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User extends GameElement
{
    private static int idCounter = 1;
    private Settings settings;
    private Map<String, Campaign> campaigns;
    private Map<String, Character> characters;

    public User(String username)
    {
        super(idCounter++, username);
        settings = new Settings();
        campaigns = new HashMap<>();
        characters = new HashMap<>();
    }

    public String toString()
    {
        return "Username: " + getElemName() +
                "\nSettings:\n" + settings.toString() +
                "\nCampaigns:\n" + campaigns.toString() +
                "\nCharacters:\n" + characters.toString();
    }

    public String getCurrentRealmName()
    {
        return settings.getCurrentRealmName();
    }

    public void addCampaign(String campaignName)
    {
        if (!campaigns.containsKey(campaignName))
            campaigns.put(campaignName, new Campaign(campaignName, getElemName()));
    }

    public void archiveCampaign(String campaignName)
    {
        if (campaigns.containsKey(campaignName))
            campaigns.get(campaignName).setIsArchived(true);
    }

    public void addCharacter(Character character)
    {
        if (!characters.containsKey(character.getName()))
            characters.put(character.getName(), character);
    }

    public boolean containsCharacter(String name)
    {
        return characters.containsKey(name);
    }

    public void removeCharacter(String name)
    {
        characters.remove(name);
    }

    // return the character with the desired name
    // PRECONDITION: the user's map of characters contains this character
    public Character getCharacter(String name)
    {
        return characters.get(name);
    }

    // Getters
    public String getUsername()
    {
        return getElemName();
    }
    public Settings getSettings()
    {
        return settings;
    }
    public Map<String, Campaign> getCampaigns()
    {
        return campaigns;
    }
    public Map<String, Character> getCharacters()
    {
        return characters;
    }
    public List<String> getCharacterNames()
    {
        List<String> names = new ArrayList<>();
        for (Character character : characters.values())
            names.add(character.getName());
        return names;
    }

    // no setter methods in this class because campaigns and characters will be handled in their own way
    // and changing settings can be accomplished by calling getSettings() and then using the setters for the
    // Settings class
}
