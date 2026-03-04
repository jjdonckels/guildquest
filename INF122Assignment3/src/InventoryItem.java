import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InventoryItem {
    private UUID item_id;
    private String name;
    private String description;
    private String rarity;
    private int quantity;

    public InventoryItem(String name, String description, String rarity, int quantity) {
        this.item_id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return String.format("""
                \n%s (x%s)
                %s
                Rarity: %s""", name, quantity, description, rarity);
    }
}
