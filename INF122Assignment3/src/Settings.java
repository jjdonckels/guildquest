import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Settings {
    private Realm current_realm;
    private int theme;
    private int time_display_mode;
    private static final List<String> THEME_OPTIONS = List.of("Classic");
    private static final List<String> TIME_DISPLAY_MODES = List.of("World Clock", "Local Time", "Both World and Local Times");

    public Settings(){
        this.theme = 0;
        this.time_display_mode = 0;
        //when a new User is created, their starting Realm defaults to the Hub
        this.current_realm = GuildQuest.getInstance().hub();
    }

    @Override
    public String toString(){
        return String.format("""
                Current Settings:
                Realm: %s
                Theme: %s
                Time Display: %s""", current_realm, THEME_OPTIONS.get(theme), TIME_DISPLAY_MODES.get(time_display_mode));
    }

    public boolean changeTheme(int choice){
        if(!(choice - 1 < THEME_OPTIONS.size())){ return false;}
        else{ this.theme = choice - 1; return true; }
    }

    public boolean changeTimeDisplayMode(int choice){
        if(!(choice - 1 < TIME_DISPLAY_MODES.size())){ return false;}
        else{ this.time_display_mode = choice - 1; return true;}
    }

    public void displayThemes(){
        for(int i = 0; i < THEME_OPTIONS.size(); i++){
            System.out.printf("%s. %s\n", i + 1,  THEME_OPTIONS.get(i));
        }
    }

    public void displayTimeDisplayModes(){
        for(int i = 0; i < TIME_DISPLAY_MODES.size(); i++){
            System.out.printf("%s. %s\n", i + 1,  TIME_DISPLAY_MODES.get(i));
        }
    }
}
