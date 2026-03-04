import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Realm {
    private UUID realm_id;
    private String name;
    private String description;
    private int timeOffset; //measured in minutes

    public Realm(){
        this.realm_id = UUID.randomUUID();
        this.name = "Hub";
        this.description = "The Hub of all Realms. QuestEvents rarely occur here, but it is a good place to start your journey or take a break between journeys.";
        this.timeOffset = 0;
    }

    public Realm(String name, String description, int timeOffset) {
        this.realm_id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.timeOffset = timeOffset;
    }

    public WorldTime toLocalTime(){
        return new WorldTime(WorldClock.getInstance().now().getMinutes_passed() + timeOffset);
    }

    public WorldTime toLocalTime(int minutes){
        return new WorldTime(minutes + timeOffset);
    }

    @Override
    public String toString(){
        return this.name;
    }
}
