public class OffsetAndMultiplierRule implements LocalTimeRule
{
    // multiplier is always processed before offset, so going from world to local will multiply world then offset
    // and going from local to world will undo offset then undo multiplier
    private int offsetMin;
    private int multiplier;

    public OffsetAndMultiplierRule(int offsetMin, int multiplier)
    {
        this.offsetMin = offsetMin;
        this.multiplier = multiplier;
    }

    public int getOffsetMin()
    {
        return offsetMin;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

    public void setOffsetMin(int offsetMin)
    {
        this.offsetMin = offsetMin;
    }

    public void setMultiplier(int multiplier)
    {
        this.multiplier = multiplier;
    }

    public LocalTime worldToLocal(WorldTime worldTime)
    {
        return new LocalTime((worldTime.getNumMinutesPassed() * multiplier) + offsetMin);
    }

    public WorldTime localToWorld(LocalTime localTime)
    {
        return new WorldTime((localTime.getMinutesPassed() - offsetMin) / multiplier);
    }
}
