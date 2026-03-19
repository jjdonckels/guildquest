import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Character implements Entity {
    private String name;
    private String characterClass;
    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int speed;
    private Inventory inventory;
    private ItemType itemType;

    public Character(String name, String characterClass, int maxHealth, int attack, int defense, int speed, ItemType itemType) {
        this.name = name;
        this.characterClass = characterClass;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.itemType = itemType;
        this.inventory = new Inventory(20, itemType);
    }

    public void takeDamage(int amount) {
        int effective = Math.max(0, amount - this.defense);
        this.currentHealth = Math.max(0, this.currentHealth - effective);
    }

    public void heal(int amount) {
        this.currentHealth = Math.min(this.maxHealth, this.currentHealth + amount);
    }

    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    public String getSummary() {
        return String.format("%s (%s) HP: %d/%d ATK: %d DEF: %d SPD: %d", this.name, this.characterClass,
                this.currentHealth, this.maxHealth, this.attack, this.defense, this.speed);
    }
}
