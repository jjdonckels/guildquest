public class WorldTime
{
    private int numMinutesPassed;

    // 1440 minutes in a day, so numMinutesPassed is offset by 1 day so days start counting at 1 rather than 0
    public  WorldTime()
    {
        numMinutesPassed = 1440;
    }
    public WorldTime(int day, int hour, int minute)
    {
        numMinutesPassed = 1440 + (day * 1440) + (hour * 60) + minute;
    }
    public WorldTime(int numMinutesPassed)
    {
        this.numMinutesPassed = 1440 + numMinutesPassed;
    }

    public WorldTime addMinutes(int minutes)
    {
        numMinutesPassed += minutes;
        return this;
    }

    public int calculateDay()
    {
        return numMinutesPassed / 1440;
    }

    public int calculateHour()
    {
        int temp = numMinutesPassed % 1440; // number of mins in current day
        return temp / 60;
    }

    public int calculateMinute()
    {
        return numMinutesPassed % 60; // number of mins in current hour
    }

    public int getNumMinutesPassed()
    {
        return numMinutesPassed;
    }

    public String toString()
    {
        return "Day " + calculateDay() + ", " + calculateHour() + ":" + calculateMinute();
    }
}
