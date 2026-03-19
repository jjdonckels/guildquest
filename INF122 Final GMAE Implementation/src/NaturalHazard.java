import java.util.List;

public interface NaturalHazard extends Hazard{
    public boolean diceRollForDamage(int maxRoll);
    public void doDamage(List<Mob> victims);
}
