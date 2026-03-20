public class ConsumableItem extends Item {
    private final int healAmount;
    private int stock;

    public ConsumableItem(String name, int healAmount, int stock) {
        super(name, ItemType.CONSUMABLE);
        this.healAmount = healAmount;
        this.stock = stock;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public int getStock() {
        return stock;
    }

    public boolean hasStock() {
        return stock > 0;
    }

    public String consume() {
        if (stock <= 0) {
            return String.format("%s is out of stock.", getName());
        }
        stock--;
        return String.format("You use %s and restore %d HP. Remaining: %d.", getName(), healAmount, stock);
    }

    @Override
    public String use() {
        return consume();
    }

    @Override
    public String getDescription() {
        return String.format("Consumable: heals %d HP (stock %d)", healAmount, stock);
    }
}
