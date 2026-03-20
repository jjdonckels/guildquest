public class RelicItem extends Item {
    private final String lore;
    private final String specialEffect;
    private int charge;
    private final int maxCharge;

    public RelicItem(String name, String lore, String specialEffect, int maxCharge) {
        super(name, ItemType.RELIC);
        this.lore = lore;
        this.specialEffect = specialEffect;
        this.maxCharge = maxCharge;
        this.charge = maxCharge;
    }

    public String getLore() {
        return lore;
    }

    public String getSpecialEffect() {
        return specialEffect;
    }

    public boolean hasCharge() {
        return charge > 0;
    }

    public String activate() {
        if (charge <= 0) {
            return String.format("%s has no charge left and must be recharged.", getName());
        }
        charge--;
        return String.format("You activate %s. %s (charge left: %d/%d).", getName(), specialEffect, charge, maxCharge);
    }

    public void recharge(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Recharge amount cannot be negative");
        }
        charge = Math.min(maxCharge, charge + amount);
    }

    @Override
    public String use() {
        return activate();
    }

    @Override
    public String getDescription() {
        return String.format("Relic: %s (charge %d/%d)", specialEffect, charge, maxCharge);
    }
}
