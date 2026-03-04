public interface Clock
{
    public WorldTime getCurrentTime();
    public WorldTime addMinutes(WorldTime time, int minutes);
}
