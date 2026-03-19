import java.util.Random;

//singleton class (+1 design pattern)

public class Dice {
    private final Random rand = new Random();
    public static Dice instance;

    public Dice(){}

    public static Dice getInstance(){
       if(instance == null){ instance = new Dice(); }
       return instance;
    }

    public int roll(int max){
        return rand.nextInt(1, max + 1);
    }
}
