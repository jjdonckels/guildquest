import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PlayableCharacter extends Entity {
    private static final int DEFAULT_MAX_HEALTH = 200;

    private final String name;           // player name (the person)
    private final String characterName;
    private final int attackModifier;
    private int strengthBoostTurns;
    private List<InventoryItem> inventory; //taken from Amanda's implementation amanda-implementation/INF122Assignment3/src/Character.java

    public PlayableCharacter(String name, String characterName, Position position, String symbol, int attackModifier) {
        super(position, DEFAULT_MAX_HEALTH, symbol);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or blank.");
        if (characterName == null || characterName.isBlank())
            throw new IllegalArgumentException("Character name cannot be null or blank.");
        if (attackModifier < 0)
            throw new IllegalArgumentException("Attack power cannot be negative.");
        this.name = name;
        this.characterName = characterName;
        this.attackModifier = attackModifier;
        this.strengthBoostTurns = 0;
        this.inventory = new ArrayList<>();
        //populate with default inventory items
        this.inventory.add(new Weapon("Wooden Sword", "A basic wooden sword.", 1, 4));
        this.inventory.add(new Armor("Leather Armor", "Basic leather armor. Basically just clothing.", 1, 5));
        this.inventory.add(new HealingItem("Apple", "A fresh apple! They say one a day keeps the doctor away.", 1, 5));
    }

    // Backwards-compatible constructor (character name defaults to player name)
    public PlayableCharacter(String name, Position position, String symbol, int attackPower) {
        this(name, name, position, symbol, attackPower);
    }

    public int getDefense() {
        return inventory.stream()
                .filter(item -> item instanceof Armor)
                .map(item -> (Armor) item)
                .mapToInt(Armor::getDefense)
                .max()
                .orElse(0);
    }


    public int getAttackPower() {
        if (strengthBoostTurns > 0)
            return (inventory.stream()
                    .filter(item -> item instanceof Weapon)
                    .map(item -> (Weapon) item)
                    .mapToInt(Weapon::getDamage)
                    .max()
                    .orElse(0) * attackModifier * 2);
        return inventory.stream()
                .filter(item -> item instanceof Weapon)
                .map(item -> (Weapon) item)
                .mapToInt(Weapon::getDamage)
                .max()
                .orElse(0) * attackModifier;
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

    public void pickupItem(InventoryItem item){
        if(inventory.contains(item)){ item.pickup(); }
        else { inventory.add(item); }
    }

    public void dropItem(InventoryItem item){
        if(inventory.contains(item)){ item.drop(); }
        else { System.out.println("You do not have this item."); }
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
                + ", attackPower=" + getAttackPower()
                + ", defense=" + getDefense()
                + ", strengthBoostTurns=" + strengthBoostTurns
                + ", symbol='" + getSymbol() + "'}";
    }
}