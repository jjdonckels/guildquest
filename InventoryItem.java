package guildquest.user;

public class InventoryItem {

    private String name;
    private String type;
    private int    value;

    public InventoryItem(String name, String type, int value) {
        this.name  = name;
        this.type  = type;
        this.value = value;
    }

    public String getName()  { return name; }
    public String getType()  { return type; }
    public int    getValue() { return value; }

    public void setName(String name)   { this.name  = name; }
    public void setType(String type)   { this.type  = type; }
    public void setValue(int value)    { this.value = value; }
}
