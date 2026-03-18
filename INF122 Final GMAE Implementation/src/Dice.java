import java.util.Random;

public class Dice {
    private Random rand = new Random();

    public int roll(int max){
        return rand.nextInt(1, max + 1);
    }
}
