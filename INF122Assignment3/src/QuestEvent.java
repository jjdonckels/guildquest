import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class QuestEvent {
    private UUID event_id;
    private String title;
    private WorldTime start_time;
    private WorldTime end_time;
    private Realm realm;
    private List<Character> participants;

    public QuestEvent(String title, WorldTime start_time, WorldTime end_time, Realm realm) {
        this.event_id = UUID.randomUUID();
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.realm = realm;
        this.participants = new ArrayList<>();
    }

    public String display(User user) {

        return String.format("""
                Title: %s
                Realm: %s
                %s""", this.title, this.realm, time_display(user));

    }

    public void addParticipant(Character character) {
        this.participants.add(character);
    }
    public void removeParticipant(Character character) {
        this.participants.remove(character);
    }

    private String time_display(User user){
        String time = "";
        int mode = user.getSettings().getTime_display_mode();
        switch(mode){
            case 0: // world time
                time = String.format("""
                Start Time (World Clock Units): %s
                End Time (World Clock Units): %s""", this.start_time, this.end_time);
                break;
            case 1: //local time
                time = String.format("""
                Start Time (Local Clock): %s
                End Time (Local Clock): %s""", this.realm.toLocalTime(this.start_time.getMinutes_passed()), this.realm.toLocalTime(this.end_time.getMinutes_passed()));
                break;
            case 2:
                time = String.format("""
                Start Time (World Clock Units): %s
                End Time (World Clock Units): %s
                Start Time (Local Clock): %s
                End Time (Local Clock): %s""", this.start_time, this.end_time, this.realm.toLocalTime(this.start_time.getMinutes_passed()), this.realm.toLocalTime(this.end_time.getMinutes_passed()));
                System.out.printf("World Time: %s\n", WorldClock.getInstance());
                System.out.printf("Local Time: %s\n", realm.toLocalTime());
                break;
        }
        return time;
    }

}