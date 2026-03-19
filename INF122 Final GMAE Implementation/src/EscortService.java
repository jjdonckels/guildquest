import java.util.List;

public class EscortService implements AdventureType{
    private List<Map> stages;
    private Mob escortee;

    public EscortService(){
        //creating a random game
        //generate random number of boards each w a random hazard to populate stages
        //generate random npc to escort
    }

    public EscortService(int stages, Mob escortee, List<Hazard> hazards){
        //player generates a custom game -> create stages w hazards
        this.escortee = escortee;
    }

    @Override
    public void play() {

    }
}
