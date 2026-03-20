public abstract class Entity {
    private static int nextId = 1;

    private final int id;
    private Position position;
    private Position spawnPosition;
    private int health;
    private final int maxHealth;
    private String symbol;

    public Entity(Position position, int maxHealth, String symbol) {
        if (position == null)
            throw new IllegalArgumentException("Position cannot be null.");
        if (maxHealth < 0)
            throw new IllegalArgumentException("Max health cannot be negative.");
        if (symbol == null || symbol.isBlank())
            throw new IllegalArgumentException("Symbol cannot be null or blank.");

        this.id = nextId++;
        this.position = position;
        this.spawnPosition = position;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.symbol = symbol;
    }

    public int getId() {return id;}

    public Position getPosition() {return position;}

    public Position getSpawnPosition() {return spawnPosition;}

    public int getHealth() {return health;}

    public int getMaxHealth() {return maxHealth;}

    public String getSymbol() {return symbol;}

    public void setSymbol(String symbol) {
        if (symbol == null || symbol.isBlank())
            throw new IllegalArgumentException("Symbol cannot be null or blank.");
        this.symbol = symbol;
    }

    public void setSpawnPosition(Position spawnPosition) {
        if (spawnPosition == null)
            throw new IllegalArgumentException("Spawn position cannot be null.");
        this.spawnPosition = spawnPosition;
    }

    public void move(Position newPos) {
        if (newPos == null)
            throw new IllegalArgumentException("New position cannot be null.");
        this.position = newPos;
    }

    public void takeDamage(int damage) {
        if (damage < 0)
            throw new IllegalArgumentException("Damage cannot be negative.");
        health = Math.max(0, health - damage);
    }

    public void heal(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Heal amount cannot be negative.");
        health = Math.min(maxHealth, health + amount);
    }

    public boolean isAlive() {return health > 0;}

    public void reset() {
        this.position = spawnPosition;
        this.health = maxHealth;
    }

    @Override
    public String toString() {
        return "{Entity "
                + "id: " + id
                + ", position=" + position
                + ", health=" + health + "/" + maxHealth
                + ", symbol='" + symbol + "'}";
    }
}

class Destination extends Entity {
    public Destination(Position position) {
        super(position, 0, "\ud83c\udfc1");
    }
}