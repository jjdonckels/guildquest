public abstract class PowerUp extends Entity {

    public PowerUp(Position position, String symbol) {
        super(position, 0, symbol);
    }
    public abstract void applyEffect(PlayableCharacter character);
    @Override
    public String toString() {
        return "{PowerUp "
                + "id=" + getId()
                + ", position=" + getPosition()
                + ", symbol='" + getSymbol() + "'}";
    }
}

class StrengthPowerUp extends PowerUp {
    private static final int BOOST_TURNS = 3;
    public StrengthPowerUp(Position position) {
        super(position, "💪");
    }
    @Override
    public void applyEffect(PlayableCharacter character) {
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null.");
        character.setStrengthBoostTurns(BOOST_TURNS);
    }
}

class HealthPowerUp extends PowerUp {
    private static final int HEAL_AMOUNT = 50;
    public HealthPowerUp(Position position) {
        super(position, "❤️");
    }
    @Override
    public void applyEffect(PlayableCharacter character) {
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null.");
        character.heal(HEAL_AMOUNT);
    }
}
