import java.util.ArrayList;
import java.util.List;

public class Campaign extends GameElement
{
    private static int idCounter = 1;
    private String ownerUsername;
    private Visibility visibility;
    private boolean isArchived;
    private List<QuestEvent> events;

    public Campaign(String name, String ownerUsername)
    {
        super(idCounter++, name);
        this.ownerUsername = ownerUsername;
        this.visibility = Visibility.PRIVATE;
        this.isArchived = false;
        events = new ArrayList<>();
    }

    public String toString()
    {
        if (isArchived) return "Archived";
        return "Campaign id: " + getElemId() +
                "\nName: " + getElemName() +
                "\nOwner: "  + ownerUsername +
                "\nVisibility: " + visibility +
                "\nIsArchived: " + isArchived +
                "\nQuest Events:\n" + events + "\n";
    }

    // return a list of quest events with times that start and finish in the given range
    private List<QuestEvent> getEventsInDayRange(int startDay, int length)
    {
        List<QuestEvent> result = new ArrayList<>();
        int endDay = startDay + length;

        for (QuestEvent e : events)
        {
            int sDay = e.getStartTime().calculateDay();
            int eDay = e.getEndTime().calculateDay();
            if (sDay >= startDay && eDay <= endDay)
                result.add(e);
        }
        return result;
    }
    // return a list of quest events in range of the current WorldTime day
    public List<QuestEvent> getDailyEvents(WorldTime anchor)
    {
        return getEventsInDayRange(anchor.calculateDay(), 0); // all events on the same day
    }
    // return a list of quest events in range of the current WorldTime week (7 days)
    public List<QuestEvent> getWeekEvents(WorldTime anchor)
    {
        return getEventsInDayRange(anchor.calculateDay(), 7); // all events within the week
    }
    // return a list of quest events in range of the current WorldTime month (31 days)
    public List<QuestEvent> getMonthEvents(WorldTime anchor)
    {
        return getEventsInDayRange(anchor.calculateDay(), 31); // all events within the month
    }
    // return a list of quest events in range of the current WorldTime year (365 days)
    public List<QuestEvent> getYearEvents(WorldTime anchor)
    {
        return getEventsInDayRange(anchor.calculateDay(), 365); // all events within the year
    }

    public void addEvent(QuestEvent event)
    {
        if  (!events.contains(event))
            events.add(event);
    }
    public void  removeEvent(String name)
    {
        events.removeIf(event -> event.getName().equals(name));
    }

    // Getters
    public int  getId()
    {
        return getElemId();
    }
    public String getName()
    {
        return getElemName();
    }
    public String getOwnerUsername()
    {
        return ownerUsername;
    }
    public Visibility getVisibility()
    {
        return visibility;
    }
    public boolean getIsArchived()
    {
        return isArchived;
    }
    public List<QuestEvent> getEvents()
    {
        return events;
    }
    public boolean containsQuestEvent(String name)
    {
        for  (QuestEvent e : events)
        {
            if (e.getName().equals(name))
                return true;
        }
        return false;
    }
    // return the QuestEvent with the given name
    // PRECONDITION: events contains the desired QuestEvent
    public QuestEvent getQuestEvent(String name)
    {
        for (QuestEvent e : events)
        {
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }
    // Setters
    public void setName(String name)
    {
        super.setName(name);
    }
    public void setOwnerUsername(String ownerUsername)
    {
        this.ownerUsername = ownerUsername;
    }
    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }
    public void setIsArchived(boolean archived)
    {
        isArchived = archived;
    }
}
