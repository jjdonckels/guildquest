public class TimeRange
{
    private WorldTime start;
    private WorldTime end;

    public TimeRange(WorldTime start, WorldTime end)
    {
        this.start = start;
        this.end = end;
    }

    public WorldTime getStart()
    {
        return start;
    }

    public WorldTime getEnd()
    {
        return end;
    }

    public void setStart(WorldTime start)
    {
        this.start = start;
    }

    public void setEnd(WorldTime end)
    {
        this.end = end;
    }

    // return true if t is in the range of start to end, false otherwise
    public boolean contains(WorldTime t)
    {
        int tMins = t.getNumMinutesPassed();
        int startMins = start.getNumMinutesPassed();
        int endMins = end.getNumMinutesPassed();
        return tMins >= startMins && tMins <= endMins;
    }
}
