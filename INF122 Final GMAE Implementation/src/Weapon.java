import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weapon extends InventoryItem {
    private final int damage;

    public Weapon(String name, String description, int quantity, int damage) {
        super(name, description, ItemType.WEAPON, quantity);
        this.damage = damage;
    }

    @Override
    public String toString(){
        return String.format("""
                \n%s (x%s)
                %s
                Does %s damage.
                """, super.getName(), super.getQuantity(), super.getDescription(), this.damage);
    }
}
