public class Hazard extends Entity {
    private final int damage;

    public Hazard(Position position, String symbol, int damage) {
        super(position, 0, symbol);
        if (Main.debug)
            super.setSymbol("H");
        if (damage < 0)
            throw new IllegalArgumentException("Damage cannot be negative.");
        this.damage = damage;
    }

    public int getDamage() {return damage;}

    public void applyEffect(Entity target) {
        if (target == null)
            throw new IllegalArgumentException("Target cannot be null.");
        if (!target.isAlive()) {return;}
        target.takeDamage(damage);
    }

    @Override
    public String toString() {
        return "{Hazard "
                + "id=" + getId()
                + ", position=" + getPosition()
                + ", damage=" + damage
                + ", symbol='" + getSymbol() + "'"
                + "}";
    }
}