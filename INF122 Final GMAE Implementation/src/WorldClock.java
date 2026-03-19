import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorldClock implements Clock
{
    private List<TimeObserver> observers;
    public void tick()
    {
        currentTime.addMinutes(1);
        notifyObservers();
    }
    public void notifyObservers()
    {
        if (observers == null) return;
        for (TimeObserver curr : observers)
            curr.onTimeChanged(currentTime);
    }
    public void addObserver(TimeObserver observer)
    {
        if  (observers == null) observers = new ArrayList<TimeObserver>();
        if (observers.contains(observer)) return;
        observers.add(observer);
    }
    private WorldTime currentTime;
    public WorldClock(WorldTime currentTime)
    {
        this.currentTime = currentTime;
    }

    public WorldTime addMinutes(WorldTime time, int minutes)
    {
        currentTime.addMinutes(minutes);
        return currentTime;
    }
}