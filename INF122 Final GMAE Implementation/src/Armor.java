public class Armor extends InventoryItem {
    private final int defense;


    public Armor(String name, String description, int quantity, int defense) {
        super(name, description, ItemType.ARMOR, quantity);
        this.defense = defense;
    }

    @Override
    public String toString(){
        return String.format("""
                \n%s (x%s)
                %s
                + %s Defense.
                """, super.getName(), super.getQuantity(), super.getDescription(), this.defense);
    }
}
