import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightningStorm implements NaturalHazard{
     private int severity;

    @Override
    public String getDescription() {
        return "A lightning storm is coming!";
    }

    @Override
    public void applyToPlayers(Player player1, Player player2, String choice) {
        int roll1 = Dice.getInstance().roll(20);
        int roll2 = Dice.getInstance().roll(20);
        int finalRoll = Math.max(roll1, roll2);

        System.out.println(player1.getName() + " rolled: " + roll1);
        System.out.println(player2.getName() + " rolled: " + roll2);
        System.out.println("Using higher roll: " + finalRoll);

        if (choice.equalsIgnoreCase("house")) {
            resolveHouseChoice(player1, player2, finalRoll);
        } else {
            resolveTreeChoice(player1, player2, finalRoll);
        }

        giveRandomReward(player1);
        giveRandomReward(player2);
    }

    private void resolveHouseChoice(Player p1, Player p2, int roll) {
        if (roll < 8) {
            p1.takeDamage(10);
            p2.takeDamage(10);
        } else if (roll <= 15) {
            int attackChance = 16 - roll;
            int attackRoll = Dice.getInstance().roll(20);

            if (attackRoll <= attackChance) {
                p1.takeDamage(8);
                p2.takeDamage(8);
            }
        }
    }

    private void resolveTreeChoice(Player p1, Player p2, int roll) {
        if (roll < 5) {
            p1.takeDamage(15);
            p2.takeDamage(15);
        } else if (roll <= 12) {
            triggerMiniHazard(p1);
            triggerMiniHazard(p2);
        }
    }

    private void triggerMiniHazard(Player player) {
        int miniRoll = Dice.getInstance().roll(2);

        if (miniRoll == 1) {
            player.takeDamage(5);
        } else {
            int poisonRoll = Dice.getInstance().roll(2);
            player.takeDamage(poisonRoll == 1 ? 10 : 5);
        }
    }

    private void giveRandomReward(Player player) {
        int rewardRoll = Dice.getInstance().roll(100);

        if (rewardRoll <= 25) {
            System.out.println(player.getName() + " found food.");
        } else if (rewardRoll <= 40) {
            System.out.println(player.getName() + " found a potion.");
        } else if (rewardRoll <= 50) {
            System.out.println(player.getName() + " found a weapon.");
        }
    }
}