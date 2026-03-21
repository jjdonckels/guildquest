import java.util.Random;
public class Main {
    public static boolean debug = true;
    public static void main(String[] args) {
        new MenuSystem().run();
        EscortGame game = EscortGame.builder()
                .setSeed(12345L)
                .setDifficulty(Difficulty.EASY)
                .setRealm(new FireRealm())
                .build();

        game.start();
        game.getBoard().render();
        System.out.println();

        for (int i = 0; i < 500 && !game.isGameOver(); i++) {
            PlayableCharacter current = game.getPlayers().get(game.getCurrentTurn() % game.getPlayers().size());
            game.processTurn(current);

            if (game.checkWinCondition()) {
                game.getBoard().render();
                System.out.println("Players win!");
                break;
            }

            if (game.checkLoseCondition()) {
                game.getBoard().render();
                System.out.println("Players lose!");
                break;
            }

            game.getBoard().render();
            System.out.println();
        }
    }
}

// public class Main {
//     public static void main(String[] args) {
//         EscortGame game = EscortGame.builder()
//                 .setSeed(12345L)
//                 .setDifficulty(Difficulty.MEDIUM)
//                 .setRealm(new FireRealm())
//                 .build();

//         game.start();

//         System.out.println("Realm: " + game.getRealm().getName());
//         System.out.println("Seed: " + game.getSeed());
//         System.out.println("Start: " + game.getStartPosition());
//         System.out.println("End: " + game.getEndPosition());
//         System.out.println();

//         game.getBoard().render();
//     }
// }

//public class Main {
//    public static void main(String[] args) {
//        EscortGame game = EscortGame.builder()
//                .setSeed(12345L)
//                .setDifficulty(Difficulty.MEDIUM)
//                .setRealm(new FireRealm())
//                .build();
//
//        game.start();
//
//        System.out.println("Realm: " + game.getRealm().getName());
//        System.out.println("Seed: " + game.getSeed());
//        System.out.println("Start: " + game.getStartPosition());
//        System.out.println("End: " + game.getEndPosition());
//        System.out.println();
//
//        game.getBoard().render();
//    }
//}

//public class Main {
//    public static void main(String[] args) {
//        PlayableCharacter player = new PlayableCharacter("Player 1", new Position(0, 0), "😀", 20);
//        Enemy enemy = new Enemy(new Position(1, 0), "🐉", 100, 15);
//        CombatSystem combatSystem = new CombatSystem();
//
//        player.setStrengthBoostTurns(3);
//
//        combatSystem.resolveAttack(player, enemy);
//        System.out.println("Enemy health after boosted hit: " + enemy.getHealth());
//        System.out.println("Remaining boost turns: " + player.getStrengthBoostTurns());
//    }
//}

//public class Main {
//    public static void main(String[] args) {
//        Board board = new Board(8, 5);
//
//        PlayableCharacter p1 = new PlayableCharacter("Player 1", new Position(0, 0), "😀", 20);
//        PlayableCharacter p2 = new PlayableCharacter("Player 2", new Position(1, 0), "😎", 18);
//        Enemy enemy = new Enemy(new Position(4, 2), "🐉", 30, 15);
//        Hazard hazard = new Hazard(new Position(3, 3), "🔥", 20);
//        Relic relic = new Relic(new Position(6, 1));
//
//        board.addEntity(p1);
//        board.addEntity(p2);
//        board.addEntity(enemy);
//        board.addEntity(hazard);
//        board.addEntity(relic);
//
//        System.out.println("Initial board:");
//        board.render();
//
//        System.out.println("\nMove Player 1 to the right:");
//        board.updatePosition(p1, p1.attemptMove(Direction.RIGHT));
//        board.render();
//
//        System.out.println("\nMove Player 2 to the right:");
//        board.updatePosition(p2, p2.attemptMove(Direction.RIGHT));
//        board.render();
//    }
//}