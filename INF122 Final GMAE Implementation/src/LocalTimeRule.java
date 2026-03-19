public interface LocalTimeRule
{
    public LocalTime worldToLocal(WorldTime worldTime);
    public WorldTime localToWorld(LocalTime localTime);
}