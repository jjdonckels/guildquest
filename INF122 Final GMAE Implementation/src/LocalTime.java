import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalTime
{
    private int day;
    private int hour;
    private int minute;

    public  LocalTime(int day, int hour, int minute)
    {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public LocalTime(int minutes)
    {
        day = minutes / 1440;
        hour = (minutes % 1440) / 60;
        minute = minutes % 60;
    }

    public String toString()
    {
        return "Day " + day + ", " + hour + ":" + minute;
    }

    public int getMinutesPassed()
    {
        return (day * 1440) + (hour * 60) + minute;
    }
}