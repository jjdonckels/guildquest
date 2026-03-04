import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Inventory
{
    private Map<String, Item> items;

    public Inventory()
    {
        items = new HashMap<>();
    }

    public String toString()
    {
        return items.toString();
    }

    public void addItem(Item item)
    {
        if (items.containsKey(item.getName()))
        {
            // if item already exists in the map, just increase that currently existing item's quantity
            items.get(item.getName()).add(item.getQuantity());
        }
        else
        {
            items.put(item.getName(), item);
        }
    }

    public void removeItem(String name, int quantity)
    {
        if (items.containsKey(name))
        {
            items.get(name).remove(quantity);
            if (items.get(name).getQuantity() == 0)
            {
                items.remove(name);
            }
        }
    }

    // Getter
    public Map<String, Item>  getItems()
    {
        return Collections.unmodifiableMap(items);
    }
}
