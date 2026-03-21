import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class InventoryItem {
    private UUID item_id;
    private String name;
    private String description;
    private int quantity;
    private ItemType type;

    public InventoryItem(String name, String description, ItemType type, int quantity) {
        this.item_id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return String.format("""
                \n%s (x%s)
                %s
                """, name, quantity, description);
    }
}