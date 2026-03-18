import java.util.List;

public class Player implements Entity{
    //player profile
    String name;
    List<Character> characters;
    Realm preferred_realm;

    public Player(String name){
        this.name=name;
    }
}
