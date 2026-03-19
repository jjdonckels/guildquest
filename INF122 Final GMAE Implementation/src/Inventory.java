import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Inventory {
    private List<ItemType> items;
    private int capacity;
    private ItemType itemType;

    public Inventory(int capacity, ItemType itemType) {
        this.capacity = capacity;
        this.itemType = itemType;
        this.items = new ArrayList<>();
    }

    public boolean addItem(ItemType item) {
        if (this.items.size() >= this.capacity) {
            return false;
        }
        this.items.add(item);
        return true;
    }

    public boolean removeItem(ItemType item) {
        return this.items.remove(item);
    }


}
