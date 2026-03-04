import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class WorldTime {
    private static final int MINUTES_IN_YEAR = 525600;
    private static final int MINUTES_IN_MONTH = 43800;
    private static final int MINUTES_IN_DAY = 1440;
    private static final int MINUTES_IN_HOUR = 60;
    private int minutes_passed;

    public WorldTime(){
        this.minutes_passed = 0;
        tick_cycle();
    }

    public WorldTime(int minutes_passed){
        this.minutes_passed = minutes_passed;
        tick_cycle();
    }

    private void tick_cycle(){
        TimerTask task = new TimerTask() {
            public void run() {
                minutes_passed += 1;
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 60000);
    }

    @Override
    public String toString(){
        int year = minutes_passed / MINUTES_IN_YEAR;
        int month = (minutes_passed / MINUTES_IN_MONTH) % 12;
        int day = (minutes_passed / MINUTES_IN_DAY);
        int hour = (minutes_passed / MINUTES_IN_HOUR) % 24;
        int minute = minutes_passed % 60;

        //not the most ideal implementation of weekdays since it is limited to the same 7 days for all realms
        //however, the given design did not support unique week/month/year lengths, only day lengths, hence this implementation

        String weekday = switch (day % 7) {
            case 0 -> "Sunday";
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> "";
        };

        //same comment as above but months
        String month_name = switch (month){
            case 0 -> "January";
            case 1 -> "February";
            case 2 -> "March";
            case 3 -> "April";
            case 4 -> "May";
            case 5 -> "June";
            case 6 -> "July";
            case 7 -> "August";
            case 8 -> "September";
            case 9 -> "October";
            case 10 -> "November";
            case 11 -> "December";
            default -> "";
        };

        return String.format("%s:%02d on %s, %s %s, Year %s", hour, minute, weekday, month_name, (day % 30 + 1), year);
    }
}
