import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightningStorm implements NaturalHazard{
    private static final int DAMAGEROLLTHRESHOLD = 10;
    private static final int DAMAGE = 5;

    @Override
    public boolean diceRollForDamage(int maxRoll) {
        return Dice.getInstance().roll(maxRoll) < DAMAGEROLLTHRESHOLD;
    }

    @Override
    public void doDamage(List<Mob> victims){
        for (Mob v : victims){
            v.takeDamage(DAMAGE);
        }
    }
}
