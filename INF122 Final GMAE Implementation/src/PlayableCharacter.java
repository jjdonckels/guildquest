public class PlayableCharacter extends Entity {
    private static final int DEFAULT_MAX_HEALTH = 200;

    private final String name;           // player name (the person)
    private final String characterName;  // in-game character name
    private final int attackPower;
    private int strengthBoostTurns;

    public PlayableCharacter(String name, String characterName, Position position, String symbol, int attackPower) {
        super(position, DEFAULT_MAX_HEALTH, symbol);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or blank.");
        if (characterName == null || characterName.isBlank())
            throw new IllegalArgumentException("Character name cannot be null or blank.");
        if (attackPower < 0)
            throw new IllegalArgumentException("Attack power cannot be negative.");
        this.name = name;
        this.characterName = characterName;
        this.attackPower = attackPower;
        this.strengthBoostTurns = 0;
    }

    // Backwards-compatible constructor (character name defaults to player name)
    public PlayableCharacter(String name, Position position, String symbol, int attackPower) {
        this(name, name, position, symbol, attackPower);
    }

    public String getName() { return name; }

    public String getCharacterName() { return characterName; }

    public int getBaseAttackPower() { return attackPower; }

    public int getStrengthBoostTurns() { return strengthBoostTurns; }

    public int getAttackPower() {
        if (strengthBoostTurns > 0)
            return attackPower * 2;
        return attackPower;
    }

    public void setStrengthBoostTurns(int strengthBoostTurns) {
        if (strengthBoostTurns < 0)
            throw new IllegalArgumentException("Strength boost turns cannot be negative.");
        this.strengthBoostTurns = strengthBoostTurns;
    }

    public void addStrengthBoost(int turns) {
        if (turns < 0)
            throw new IllegalArgumentException("Strength boost turns cannot be negative.");
        this.strengthBoostTurns += turns;
    }

    public void consumeStrengthBoostTurn() {
        if (strengthBoostTurns > 0)
            strengthBoostTurns--;
    }

    public void attack(Entity target) {
        if (target == null)
            throw new IllegalArgumentException("Target cannot be null.");
        if (!this.isAlive())
            throw new IllegalStateException("A defeated entity cannot attack.");
        if (!target.isAlive())
            throw new IllegalStateException("Cannot attack a defeated target.");
        target.takeDamage(getAttackPower());
        if (strengthBoostTurns > 0)
            consumeStrengthBoostTurn();
    }

    public Position attemptMove(Direction direction) {
        if (direction == null)
            throw new IllegalArgumentException("Direction cannot be null.");
        return getPosition().translate(direction);
    }

    @Override
    public void reset() {
        super.reset();
        strengthBoostTurns = 0;
    }

    @Override
    public String toString() {
        return "{PlayableCharacter "
                + "id: " + getId()
                + ", player: " + name
                + ", character: " + characterName
                + ", position=" + getPosition()
                + ", health=" + getHealth() + "/" + getMaxHealth()
                + ", attackPower=" + attackPower
                + ", strengthBoostTurns=" + strengthBoostTurns
                + ", symbol='" + getSymbol() + "'}";
    }
}