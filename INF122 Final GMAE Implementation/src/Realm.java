import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Realm {
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

}
