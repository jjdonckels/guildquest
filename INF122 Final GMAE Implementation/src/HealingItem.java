import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealingItem extends InventoryItem {
    private final int health;


    public HealingItem(String name, String description, int quantity, int health) {
        super(name, description, ItemType.HEALING, quantity);
        this.health = health;
    }
}
