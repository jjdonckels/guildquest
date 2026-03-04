public class FixedOffsetRule implements LocalTimeRule
{
    private int offsetMin;

    public  FixedOffsetRule(int offsetMin)
    {
        this.offsetMin = offsetMin;
    }

    public int getOffsetMin()
    {
        return offsetMin;
    }

    public void setOffsetMin(int offsetMin)
    {
        this.offsetMin = offsetMin;
    }

    public LocalTime worldToLocal(WorldTime worldTime)
    {
        return new LocalTime(worldTime.getNumMinutesPassed() + offsetMin);
    }

    public WorldTime localToWorld(LocalTime localTime)
    {
        return new WorldTime(localTime.getMinutesPassed() - offsetMin);
    }
}
