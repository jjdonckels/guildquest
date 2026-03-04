public class Character extends GameElement
{
    private static int idCounter = 1;
    private String ownerUsername;
    private String characterClass;
    private int level;
    private Inventory inventory;

    public Character(String name, String ownerUsername, String characterClass, int level)
    {
        super(idCounter++, name);
        this.ownerUsername = ownerUsername;
        this.characterClass = characterClass;
        this.level = level;
        inventory = new Inventory();
    }

    public Character(String name, String ownerUsername, String characterClass, int level, Inventory inventory)
    {
        super(idCounter++, name);
        this.ownerUsername = ownerUsername;
        this.characterClass = characterClass;
        this.level = level;
        this.inventory = inventory;
    }

    public String toString()
    {
        return "Character id: " + getElemId() +
                "\nName: " + getElemName() +
                "\nOwner: " + ownerUsername +
                "\nClass: " +  characterClass +
                "\nLevel: " + level +
                "\nInventory: " + inventory.toString() + "\n";
    }

    // Getters
    public int  getId()
    {
        return getElemId();
    }
    public String getName()
    {
        return getElemName();
    }
    public String getOwnerUsername()
    {
        return ownerUsername;
    }
    public String getCharacterClass()
    {
        return characterClass;
    }
    public int getLevel()
    {
        return level;
    }
    public Inventory getInventory()
    {
        return inventory;
    }
    // Setters
    public void setName(String name)
    {
        super.setName(name);
    }
    public void setOwnerUsername(String ownerUsername)
    {
        this.ownerUsername = ownerUsername;
    }
    public void setCharacterClass(String characterClass)
    {
        this.characterClass = characterClass;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }

}
