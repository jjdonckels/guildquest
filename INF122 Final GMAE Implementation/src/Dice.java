import java.util.Random;

public class Dice {
    private final int sides;

    public Dice(int sides) {
        if (sides < 2) {
            throw new IllegalArgumentException("Dice must have at least 2 sides.");
        }
        this.sides = sides;
    }

    public int getSides() {return sides;}

    public int roll(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null.");
        }
        return random.nextInt(sides) + 1; // shift off of 0
    }

    @Override
    public String toString() {
        return "Dice " + sides;
    }
}
