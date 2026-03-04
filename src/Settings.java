public class Settings
{
    private String currentRealmName;
    private Theme theme;
    private TimeRepresentation timeRep;

    public Settings()
    {
        currentRealmName = "Home";
        theme = Theme.CLASSIC;
        timeRep = TimeRepresentation.LOCAL_TIME;
    }
    public Settings(String name)
    {
        currentRealmName = "Home";
        if (GuildQuest.getRealms().containsKey(name))
            currentRealmName = name;
        theme = Theme.CLASSIC;
        timeRep = TimeRepresentation.LOCAL_TIME;
    }
    public Settings(String name, Theme  theme, TimeRepresentation timeRep)
    {
        currentRealmName = "Home";
        if (GuildQuest.getRealms().containsKey(name))
            currentRealmName = name;
        this.theme = theme;
        this.timeRep = timeRep;
    }

    public String toString()
    {
        return "Current Realm Name: " + currentRealmName + ", Theme: " + theme.toString() +
                ", Time: " + timeRep.toString();
    }

    // Getters
    public String getCurrentRealmName()
    {
        return currentRealmName;
    }
    public Theme getTheme()
    {
        return theme;
    }
    public TimeRepresentation getTimeRep()
    {
        return timeRep;
    }
    // Setters
    public void setCurrentRealmName(String currentRealmName)
    {
        if (GuildQuest.getRealms().containsKey(currentRealmName))
            this.currentRealmName = currentRealmName;
    }
    public void setTheme(Theme theme)
    {
        this.theme = theme;
    }
    public void setTimeRep(TimeRepresentation timeRep)
    {
        this.timeRep = timeRep;
    }
}
