import java.util.List;
import static java.lang.Math.min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Skeleton implements Enemy{
    private int health = 20;
    private int maxHealth = 20;
    private int attackStrength = 3;
    private boolean alive = true;

    public Skeleton() {
    }

    @Override
    public void takeDamage(int damage) {
        if(alive) {
            this.health = min(this.health -= damage, 0);
            if (this.health == 0) {
                this.alive = false; //remove from parent container?)
            }
        }
    }

    @Override
    public void healDamage(int damage) {
        if(alive){
            this.health = min(this.health += damage, this.maxHealth);
        }
    }

    @Override
    public void hit(List<Mob> victims){
        for(Mob victim : victims){ victim.takeDamage(this.attackStrength); }
    }
}
