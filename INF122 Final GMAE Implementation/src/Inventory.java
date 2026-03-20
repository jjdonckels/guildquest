import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Inventory {
    private List<Item> items;
    private int capacity;
    private ItemType itemType;

    public Inventory(int capacity, ItemType itemType) {
        this.capacity = capacity;
        this.itemType = itemType;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (this.items.size() >= this.capacity) {
            return false;
        }
        // enforce type safety by itemType in this inventory
        if (item.getItemType() != this.itemType) {
            return false;
        }
        this.items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return this.items.remove(item);
    }


}
