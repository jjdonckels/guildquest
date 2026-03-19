import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player implements Entity{
    //player profile
    private String name;
    private List<Character> characters;
    private int lifetimeWins;
    private int totalGamesPlayed;
    private int health = 100;

    public Player(String name){
        this.name=name;
        this.characters = new ArrayList<Character>();
        this.lifetimeWins = 0;
        this.totalGamesPlayed = 0;
        createCharacter();
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println(name + " now has " + health + " HP.");
    }

    public int getHealth() {
        return health;
    }

    public void createCharacter(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Creating a character...
                Please enter your character's name: """);
        String name =  scanner.nextLine(); //saved in local variable for when we add more attributes to characters
        characters.add(new Character(name));
    }
}
