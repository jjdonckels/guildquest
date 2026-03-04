public class Realm extends GameElement
{
    private static int idCounter = 1;
    private String description;
    private LocalTimeRule localTimeRule;

    public  Realm(String name, String description, LocalTimeRule localTimeRule)
    {
        super(idCounter++, name);
        this.description = description;
        this.localTimeRule = localTimeRule;
    }

    public String toString()
    {
        return "Realm id: " + getElemId() +
                "\nName: " + getElemName() +
                "\nDescription: " + description +
                "\nLocal Time: " + toLocalTime(GuildQuest.getWorldClock().getCurrentTime());
    }

    // converts world time to local time
    public LocalTime toLocalTime(WorldTime worldTime)
    {
        return localTimeRule.worldToLocal(worldTime);
    }

    // Getters
    public int getId()
    {
        return getElemId();
    }
    public String getName()
    {
        return getElemName();
    }
    public String getDescription()
    {
        return description;
    }
    public LocalTimeRule getLocalTimeRule()
    {
        return localTimeRule;
    }
    // Setters
    public void setName(String name)
    {
        super.setName(name);
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public void setLocalTimeRule(LocalTimeRule localTimeRule)
    {
        this.localTimeRule = localTimeRule;
    }
}
