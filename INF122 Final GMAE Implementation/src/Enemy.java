public class Enemy extends Entity {
    private final int attackPower;

    public Enemy(Position position, String symbol, int maxHealth, int attackPower) {
        super(position, maxHealth, symbol);
        if (Main.debug)
            super.setSymbol("E");
        if (attackPower < 0)
            throw new IllegalArgumentException("Attack power cannot be negative.");
        this.attackPower = attackPower;
    }

    public int getAttackPower() {return attackPower;}

    public void attack(PlayableCharacter target) {
        if (target == null)
            throw new IllegalArgumentException("Target cannot be null.");
        if (!this.isAlive())
            throw new IllegalStateException("A defeated enemy cannot attack.");
        if (!target.isAlive())
            throw new IllegalStateException("Cannot attack a defeated target.");
        target.takeDamage(attackPower);
    }

    public void takeTurn(Board board) {
        if (board == null)
            throw new IllegalArgumentException("Board cannot be null.");

        // Placeholder for future enemy AI behavior.
        // For now, enemies do nothing on their turn.
    }

    @Override
    public String toString() {
        return "{Enemy "
                + "id: " + getId()
                + ", position=" + getPosition()
                + ", health=" + getHealth() + "/" + getMaxHealth()
                + ", attackPower=" + attackPower
                + ", symbol='" + getSymbol() + "'}";
    }
}
