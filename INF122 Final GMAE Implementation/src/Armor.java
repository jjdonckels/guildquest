public class Armor extends InventoryItem {
    private final int defense;


    public Armor(String name, String description, int quantity, int defense) {
        super(name, description, ItemType.ARMOR, quantity);
        this.defense = defense;
    }
}
