import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Character {
    private UUID character_id;
    private String name;
    private String class_type;
    private int level;
    private List<InventoryItem> inventory;

    public Character(String name, String class_type){
        this.character_id = UUID.randomUUID();
        this.name = name;
        this.class_type = class_type;
        this.level = 0;
        this.inventory = new ArrayList<>();
        inventory.add(new InventoryItem("Wooden Sword", "A basic wooden sword. Does 5 damage per hit.", "common", 1));
    }

    public void levelUp(){
        this.level++;
    }

    public void addItem(InventoryItem item){
        this.inventory.add(item);
    }

    public InventoryItem findItem(String name){
        for (InventoryItem i : this.inventory){
            if(i.getName().equalsIgnoreCase(name)){
                return i;
            }
        }
        return null;
    }

    public boolean removeItem(String name){
        InventoryItem item = findItem(name);
        if(item != null){
            if(item.getQuantity() > 1){
                item.setQuantity(item.getQuantity() - 1);
            }
            else this.inventory.remove(item);
            return true;
        }
        return false;
    }

    public String toString(){
        return String.format("%s, %s\n Level: %s", this.name, this.class_type, this.level);
    }

    public void displayInventory(){
        if(this.inventory.isEmpty()) System.out.println("Your inventory is empty!");
        for(InventoryItem item : this.inventory){
            System.out.println(item);
        }
    }
}
