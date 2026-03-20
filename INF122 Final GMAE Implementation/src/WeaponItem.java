public class WeaponItem extends Item {
    private int damage;
    private int durability;
    private int maxDurability;

    public WeaponItem(String name, int damage, int durability) {
        super(name, ItemType.WEAPON);
        this.damage = damage;
        this.durability = durability;
        this.maxDurability = durability;
    }

    public int getDamage() {
        return damage;
    }

    public int getDurability() {
        return durability;
    }

    public boolean isBroken() {
        return durability <= 0;
    }

    public String attack(String targetName) {
        if (isBroken()) {
            return String.format("%s is broken and cannot be used.", getName());
        }
        durability = Math.max(0, durability - 1);
        return String.format("You swing %s at %s for %d damage. Durability: %d/%d.", getName(), targetName, damage, durability, maxDurability);
    }

    public void repair(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Repair amount must be non-negative");
        }
        this.durability = Math.min(maxDurability, this.durability + amount);
    }

    @Override
    public String use() {
        if (isBroken()) {
            return String.format("%s is broken and cannot be used.", getName());
        }
        durability = Math.max(0, durability - 1);
        return String.format("You brandish %s and deal %d damage. Durability is now %d/%d.", getName(), damage, durability, maxDurability);
    }

    @Override
    public String getDescription() {
        return String.format("Weapon: +%d damage, durability %d/%d", damage, durability, maxDurability);
    }
}
