package guildquest.user;

public class Character {

    private String    name;
    private String    characterClass;   // renamed from 'role' to match RPG domain
    private int       level;
    private Inventory inventory;

    public Character(String name, String characterClass) {
        this.name           = name;
        this.characterClass = characterClass;
        this.level          = 1;
        this.inventory      = new Inventory();
    }

    public void addItem(InventoryItem item)    { inventory.addItem(item); }
    public void removeItem(InventoryItem item) { inventory.removeItem(item); }
    public Inventory getInventory()            { return inventory; }

    public String getName()          { return name; }
    public String getCharacterClass() { return characterClass; }
    public int    getLevel()          { return level; }
    public void   levelUp()           { level++; }

    public void setName(String name)                        { this.name = name; }
    public void setCharacterClass(String characterClass)    { this.characterClass = characterClass; }
}
