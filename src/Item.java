public class Item extends GameElement
{
    private static int idCounter = 1;
    private String type;
    private String rarity;
    private int quantity;

    public  Item(String name, String type, String rarity, int quantity)
    {
        super(idCounter++, name);
        this.type = type;
        this.rarity = rarity;
        this.quantity = quantity;
    }

    public String toString()
    {
        return "Item name: " + getElemName() +
                ", type: " + type +
                ", rarity: " + rarity +
                ", quantity: " + quantity;
    }

    public void add(int q)
    {
        quantity += q;
    }

    public void remove(int q)
    {
        if (q > quantity)
            quantity = 0;
        else
            quantity -= q;
    }

    // Getters
    public String getName()
    {
        return getElemName();
    }
    public String getType()
    {
        return type;
    }
    public String getRarity()
    {
        return rarity;
    }
    public int getQuantity()
    {
        return quantity;
    }
    // Setters
    public void setName(String name)
    {
        super.setName(name);
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public void setRarity(String rarity)
    {
        this.rarity = rarity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
