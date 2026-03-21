import java.util.List;
import java.util.Scanner;

public class MenuSystem {
    private final Scanner scanner;
    private final GameHistoryManager historyManager;

    public MenuSystem() {
        this.scanner = new Scanner(System.in);
        this.historyManager = new GameHistoryManager();
    }

    public void run() {
        printWelcome();
        PlayableCharacter[] players = setupPlayers();

        while (true) {
            int gameChoice = showGameSelectMenu();
            if (gameChoice == 4) {
                System.out.println("Thanks for playing! Goodbye.");
                break;
            }
            if (gameChoice == 3) {
                System.out.println();
                historyManager.printHistory();
                System.out.println();
                continue;
}

            boolean isCustom = showDefaultOrCustomMenu();
            MiniGame game;

            if (gameChoice == 1) {
                game = buildEscortGame(players, isCustom);
            } else {
                game = buildRelicHunt(players, isCustom);
            }

            if (game instanceof  EscortGame){
                List<GameHistoryEntry> results = ((EscortGame)game).gameLoop();
                historyManager.record(results.get(0));
                historyManager.record(results.get(1));
            }
            if (game instanceof RelicHunt) {
                List<GameHistoryEntry> results = ((RelicHunt)game).gameLoop();
                historyManager.record(results.get(0));
                historyManager.record(results.get(1));
            }
            
            if (!showPlayAgainMenu()) break;
        }

        scanner.close();
    }

    // -------------------------
    // Welcome
    // -------------------------

    private void printWelcome() {
        System.out.println("==============================================");
        System.out.println("   GMAE: GuildQuest Mini-Adventure Environment");
        System.out.println("==============================================");
        System.out.println();
    }

    // -------------------------
    // Player Setup
    // -------------------------

    private PlayableCharacter[] setupPlayers() {
        System.out.println("--- Player Setup ---");
        System.out.println();

        System.out.println("[ Player 1 ]");
        String playerName1 = promptNonBlank("Enter your name: ");
        String charName1   = promptNonBlank("Enter your character's name: ");
        PlayableCharacter player1 = new PlayableCharacter(
                playerName1, charName1, new Position(0, 0), "\u0031\u20e3", 20);

        System.out.println();
        System.out.println("[ Player 2 ]");
        String playerName2 = promptNonBlank("Enter your name: ");
        String charName2   = promptNonBlank("Enter your character's name: ");
        PlayableCharacter player2 = new PlayableCharacter(
                playerName2, charName2, new Position(0, 1), "\u0032\u20e3", 20);

        System.out.println();
        System.out.println("Welcome, " + playerName1 + " (" + charName1 + ")"
                + " and " + playerName2 + " (" + charName2 + ")!");
        System.out.println();

        return new PlayableCharacter[]{player1, player2};
    }

    // -------------------------
    // Game Select Menu
    // -------------------------

    private int showGameSelectMenu() {
        while (true) {
            System.out.println("--- Choose a Mini-Adventure ---");
            System.out.println("1. Escort Service");
            System.out.println("2. Relic Hunt");
            System.out.println("3. View Game History");
            System.out.println("4. Exit");
            System.out.print("Enter choice (1-4): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return 1;
                case "2": return 2;
                case "3": return 3;
                case "4": return 4;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                    System.out.println();
            }
        }
    }

    // -------------------------
    // Default or Custom
    // -------------------------

    private boolean showDefaultOrCustomMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- Game Settings ---");
            System.out.println("1. Default (Medium difficulty, random seed, Fire Realm)");
            System.out.println("2. Custom  (choose difficulty, seed, and realm)");
            System.out.print("Enter choice (1-2): ");

            String input = scanner.nextLine().trim();
            if (input.equals("1")) return false;
            if (input.equals("2")) return true;
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    // -------------------------
    // Build Escort Game
    // -------------------------

    private MiniGame buildEscortGame(PlayableCharacter[] players, boolean isCustom) {
        EscortGame.Builder builder = EscortGame.builder()
                .setPlayers(players[0], players[1]);

        if (isCustom) {
            builder.setDifficulty(promptDifficulty())
                   .setSeed(promptSeed())
                   .setRealm(promptRealm());
        }

        EscortGame game = builder.build();
        game.setHistoryManager(historyManager);
        game.start();
        
        System.out.println();
        System.out.println("Starting Escort Service...");
        System.out.println("Realm:      " + game.getRealm().getName());
        System.out.println("Difficulty: " + game.getDifficulty());
        System.out.println("Seed:       " + game.getSeed());
        System.out.println("Start: " + game.getStartPosition());
        System.out.println("End: " + game.getEndPosition());
        System.out.println();

//        game.getBoard().render();

        return game;
    }

    // -------------------------
    // Build Relic Hunt
    // -------------------------

    private MiniGame buildRelicHunt(PlayableCharacter[] players, boolean isCustom) {
        // TODO: Replace with RelicHunt.builder() once RelicHunt is implemented
        RelicHunt.Builder builder = RelicHunt.builder()
                .setPlayers(players[0], players[1]);

        if (isCustom) {
            builder.setDifficulty(promptDifficulty())
                    .setSeed(promptSeed())
                    .setRealm(promptRealm());
        }

        RelicHunt game = builder.build();
        game.setHistoryManager(historyManager);
        game.start();

        System.out.println();
        System.out.println("Starting Relic Hunt...");
        System.out.println("Realm:      " + game.getRealm().getName());
        System.out.println("Difficulty: " + game.getDifficulty());
        System.out.println("Seed:       " + game.getSeed());
        System.out.println();

//        game.getBoard().render();

        return game;
    }

    // -------------------------
    // Play Again
    // -------------------------

    private boolean showPlayAgainMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- Game Over ---");
            System.out.println("1. Return to game select");
            System.out.println("2. Exit");
            System.out.print("Enter choice (1-2): ");

            String input = scanner.nextLine().trim();
            if (input.equals("1")) return true;
            if (input.equals("2")) return false;
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }

    // -------------------------
    // Custom Settings Prompts
    // -------------------------

    private Difficulty promptDifficulty() {
        while (true) {
            System.out.println();
            System.out.println("Choose difficulty:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.print("Enter choice (1-3): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return Difficulty.EASY;
                case "2": return Difficulty.MEDIUM;
                case "3": return Difficulty.HARD;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private long promptSeed() {
        while (true) {
            System.out.println();
            System.out.print("Enter a seed (number), or press Enter for a random seed: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                long seed = System.currentTimeMillis();
                System.out.println("Using random seed: " + seed);
                return seed;
            }

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid seed. Please enter a whole number or press Enter for random.");
            }
        }
    }

    private Realm promptRealm() {
        while (true) {
            System.out.println();
            System.out.println("Choose a realm:");
            System.out.println("1. Fire Realm  (Volcano)");
            System.out.println("2. Water Realm (Ocean)");
            System.out.println("3. Earth Realm (Land)");
            System.out.print("Enter choice (1-3): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return new FireRealm();
                case "2": return new WaterRealm();
                case "3": return new EarthRealm();
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    // -------------------------
    // Helpers
    // -------------------------

    private String promptNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isBlank()) return input;
            System.out.println("This field cannot be blank. Please try again.");
        }
    }
}