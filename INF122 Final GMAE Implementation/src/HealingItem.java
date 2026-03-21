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

    @Override
    public String toString(){
        return String.format("""
                \n%s (x%s)
                %s
                Heals %s HP.
                """, super.getName(), super.getQuantity(), super.getDescription(), this.health);
    }
}
