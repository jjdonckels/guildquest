import lombok.Getter;

@Getter
public abstract class Item {
    private final String name;
    private final ItemType itemType;

    protected Item(String name, ItemType itemType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be null or blank");
        }
        this.name = name;
        this.itemType = itemType;
    }

    // Every item must define what happens when a player uses it
    public abstract String use();

    // Every item must describe itself for UI/display
    public abstract String getDescription();

    @Override
    public String toString() {
        return "[" + itemType + "] " + name + " — " + getDescription();
    }
}