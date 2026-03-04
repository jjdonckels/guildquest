import lombok.Getter;

@Getter
public class WorldClock {
    private static WorldClock instance = null;
    private WorldTime currentTime;

    private WorldClock(){
        this.currentTime = new WorldTime();
    }

    public static WorldClock getInstance() {
        if (instance == null) {
            instance = new WorldClock();
        }
        return instance;
    }

    public WorldTime now(){
        return currentTime;
    }

    @Override
    public String toString(){
        return currentTime.toString();
    }

    public boolean compare(WorldTime time1, WorldTime time2){
        return (time1.getMinutes_passed() == (time2.getMinutes_passed()));
    }

}
