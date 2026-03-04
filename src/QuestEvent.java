import java.util.ArrayList;
import java.util.List;

public class QuestEvent extends GameElement implements TimeObserver
{
    private boolean isLive;
    private static int idCounter = 1;
    private WorldTime startTime;
    private WorldTime endTime;
    private Realm realm;
    private List<Character> participants;

    public void onTimeChanged(WorldTime time)
    {
        if (startTime.getNumMinutesPassed() == time.getNumMinutesPassed())
            isLive = true;
        else if (endTime.getNumMinutesPassed() == time.getNumMinutesPassed())
            isLive = false;
    }
    public boolean getLive()
    {
        return isLive;
    }
    public QuestEvent(String name, WorldTime startTime, WorldTime endTime, Realm realm)
    {
        super(idCounter++, name);
        this.startTime = startTime;
        this.endTime = endTime;
        this.realm = realm;
        participants = new ArrayList<Character>();
        isLive = false;
    }
    public QuestEvent(String name, WorldTime startTime, WorldTime endTime, Realm realm,  List<Character> participants)
    {
        super(idCounter++, name);
        this.startTime = startTime;
        this.endTime = endTime;
        this.realm = realm;
        this.participants = participants;
        isLive = false;
    }

    // default world time
    public String toString()
    {
        return "QuestEvent id: " + getElemId() +
                "\nName: " + getElemName() +
                "\nStart Time (World): " + startTime +
                "\nEnd Time (World): " + endTime +
                "\nHappening Live: " + getLive() +
                "\nRealm: " + realm +
                "\nParticipants: " + participants + "\n";
    }

    public String toStringLocalTime()
    {
        return "QuestEvent id: " + getElemId() +
                "\nName: " + getElemName() +
                "\nStart Time (Local): " + realm.toLocalTime(startTime) +
                "\nEnd Time (Local): " + realm.toLocalTime(endTime) +
                "\nRealm: " + realm +
                "\nParticipants: " + participants + "\n";
    }

    public String toStringBothTimes()
    {
        return "QuestEvent id: " + getElemId() +
                "\nName: " + getElemName() +
                "\nStart Time (Local): " + realm.toLocalTime(startTime) +
                "\nEnd Time (Local): " + realm.toLocalTime(endTime) +
                "\nStart Time (World): " + startTime +
                "\nEnd Time (World): " + endTime +
                "\nRealm: " + realm +
                "\nParticipants: " + participants + "\n";
    }

    public void addParticipant(Character character)
    {
        if (!participants.contains(character))
            participants.add(character);
    }
    public void removeParticipant(String name)
    {
        // remove the character with the given name
        participants.removeIf(character -> character.getName().equals(name));
    }

    // Getters
    public int getId()
    {
        return getElemId();
    }
    public String  getName()
    {
        return getElemName();
    }
    public WorldTime getStartTime()
    {
        return startTime;
    }
    public WorldTime getEndTime()
    {
        return endTime;
    }
    public Realm getRealm()
    {
        return realm;
    }
    public boolean containsCharacter(String name)
    {
        for (Character c : participants)
        {
            if (c.getName().equals(name))
                return true;
        }
        return false;
    }
    // return the character from participants with the given name
    // PRECONDITION: participants contains the desired character
    public Character  getCharacter(String name)
    {
        for (Character c : participants)
        {
            if (c.getName().equals(name))
                return c;
        }
        return null;
    }
    public void removeCharacter(String name)
    {
        participants.removeIf(character -> character.getName().equals(name));
    }
    public List<Character> getParticipants()
    {
        return participants;
    }
    // Setters
    public void setName(String name)
    {
        super.setName(name);
    }
    public void setStartTime(WorldTime startTime)
    {
        this.startTime = startTime;
    }
    public void setEndTime(WorldTime endTime)
    {
        this.endTime = endTime;
    }
    public void setRealm(Realm realm)
    {
        this.realm = realm;
    }

}
