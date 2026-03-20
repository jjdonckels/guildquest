import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EscortGame implements MiniGame {
    private static final int DEFAULT_BOARD_WIDTH = 12;
    private static final int DEFAULT_BOARD_HEIGHT = 8;
    private static final String DEFAULT_ESCORTEE_SYMBOL = "\ud83d\ude48";

    private Board board;
    private final List<PlayableCharacter> players;
    private Escortee escortee;
    private final Realm realm;
    private final Difficulty difficulty;
    private final long seed;
    private Random random;
    private final Dice dice;
    private final CombatSystem combatSystem;

    private Position startPosition;
    private Position endPosition;
    private Destination destination;

    private boolean isGameOver;
    private int currentTurn;

    private EscortGame(Builder builder) {
        this.seed = builder.seed;
        this.difficulty = builder.difficulty;
        this.realm = builder.realm;
        this.players = new ArrayList<>(builder.players);
        this.dice = new Dice(20);
        this.combatSystem = new CombatSystem();

        this.board = null;
        this.escortee = null;
        this.startPosition = null;
        this.endPosition = null;

        this.random = new Random(seed);
        this.isGameOver = false;
        this.currentTurn = 0;
    }

    public static Builder builder() {return new Builder();}

    public Board getBoard() {return board;}

    public List<PlayableCharacter> getPlayers() {return new ArrayList<>(players);}

    public Escortee getEscortee() {return escortee;}

    public Realm getRealm() {return realm;}

    public Difficulty getDifficulty() {return difficulty;}

    @Override
    public long getSeed() {return seed;}

    public Dice getDice() {return dice;}

    public CombatSystem getCombatSystem() {return combatSystem;}

    public Position getStartPosition() {return startPosition;}

    public Position getEndPosition() {return endPosition;}

    public boolean isGameOver() {return isGameOver;}

    public int getCurrentTurn() {return currentTurn;}

    @Override
    public void start() {
        random = new Random(seed);
        isGameOver = false;
        currentTurn = 0;
        initializeBoard();
        initializeObjectivePositions();
        initializePlayers();
        initializeEscortee();
        spawnEntities();
        board.rebuildEntityMap();
    }

    @Override
    public void reset() {start();}

    @Override
    public boolean checkWinCondition() {
        return escortee != null
                && endPosition != null
                && escortee.getPosition().equals(endPosition);
    }

    @Override
    public boolean checkLoseCondition() {return escortee != null && !escortee.isAlive();}

    public void gameLoop() {
        while (!isGameOver) {
            board.render();
            PlayableCharacter currentPlayer = players.get(currentTurn % players.size());
            processTurn(currentPlayer);
            if (checkWinCondition()) {
                isGameOver = true;
                System.out.println("Victory! Successfully escorted " + escortee.getSymbol() + " to the destination!");
                break;
            }
            if (checkLoseCondition()) {
                isGameOver = true;
                System.out.println("Game Over! You couldn't successfully escort " + escortee.getSymbol()
                        + " to the destination.");
                break;
            }
            currentTurn++;
        }
    }

    public void processTurn(PlayableCharacter player) {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null.");
        System.out.println("\n--- Turn " + currentTurn + " (" + player.getName() + ") ---");
        if (!player.isAlive())
            return;
        int roll = dice.roll(random);
        System.out.println(player.getName() + " roll: " + roll + ".");
        boolean moveSucceeded = roll >= 10;
        if (moveSucceeded) {
            Direction direction = getRandomDirection();
            Position attemptedPosition = player.attemptMove(direction);
            System.out.println(player.getName() + " attempts to move " + direction + " to " + attemptedPosition + ".");
            if (board.isValidMove(attemptedPosition)) {
                board.updatePosition(player, attemptedPosition);
                System.out.println(player.getName() + " moved successfully.");
                handleTileInteractions(player);
                attackEnemyOnCurrentTile(player);
            } else {
                System.out.println(player.getName() + " could not move out of bounds.");
            }
        } else {
            System.out.println(player.getName() + " failed the movement roll.");
        }
        if (escortee != null && escortee.isAlive() && !checkWinCondition()) {
            escortee.followClosestPlayerOrEndPoint(players, endPosition);
            if (board.isValidMove(escortee.getPosition())) {
                board.rebuildEntityMap();
                handleEscorteeTileInteractions();
            }
        }
        currentTurn++;

        // Placeholder for full turn logic.
        // Later this will:
        // - roll dice
        // - ask for movement direction
        // - move player if allowed
        // - handle combat
        // - apply hazards/powerups
        // - move escortee
        // - run enemy turns
    }

    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    private void handleTileInteractions(PlayableCharacter player) {
        List<Entity> entitiesAtTile = board.getEntitiesAt(player.getPosition());
        List<Entity> toRemove = new ArrayList<>();
        for (Entity entity : entitiesAtTile) {
            if (entity == player)
                continue;
            if (entity instanceof Hazard) {
                Hazard hazard = (Hazard) entity;
                hazard.applyEffect(player);
                System.out.println(player.getName() + " took " + hazard.getDamage() + " damage from a hazard.");
            }
            if (entity instanceof PowerUp ) {
                PowerUp powerUp = (PowerUp) entity;
                powerUp.applyEffect(player);
                toRemove.add(powerUp);
                System.out.println(player.getName() + " picked up " + powerUp.getSymbol() + ".");
            }
        }
        for (Entity entity : toRemove)
            board.removeEntity(entity);
    }

    private void handleEscorteeTileInteractions() {
        List<Entity> entitiesAtTile = board.getEntitiesAt(escortee.getPosition());
        for (Entity entity : entitiesAtTile) {
            if (entity == escortee)
                continue;
            if (entity instanceof Hazard ) {
                Hazard hazard = (Hazard) entity;
                hazard.applyEffect(escortee);
                System.out.println("Escortee took " + hazard.getDamage() + " damage from a hazard.");
            }
        }
    }

    private void attackEnemyOnCurrentTile(PlayableCharacter player) {
        List<Entity> entitiesAtTile = board.getEntitiesAt(player.getPosition());
        for (Entity entity : entitiesAtTile) {
            if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;
                if (enemy.isAlive()) {
                    int damage = player.getAttackPower();
                    combatSystem.resolveAttack(player, enemy);
                    System.out.println(player.getName() + " attacked an enemy for " + damage + " damage.");
                }
                if (!enemy.isAlive()) {
                    board.removeEntity(enemy);
                    System.out.println("Enemy defeated.");
                }
                return;
            }
        }
    }

    public void spawnEntities() {
        spawnEnemies();
        spawnHazards();
    }

    public void spawnEnemies() {
        for (int i = 0; i < difficulty.getEnemyCount(); i++) {
            Position pos = getRandomEmptyPosition();
            Enemy enemy = realm.createEnemy(pos, difficulty);
            board.addEntity(enemy);
        }
    }

    public void spawnHazards() {
        for (int i = 0; i < difficulty.getHazardCount(); i++) {
            Position pos = getRandomEmptyPosition();
            Hazard hazard = realm.createHazard(pos);
            board.addEntity(hazard);
        }
    }

    public Position getRandomEmptyPosition() {
        int maxAttempts = 100000;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = random.nextInt(board.getWidth());
            int y = random.nextInt(board.getHeight());
            Position pos = new Position(x, y);
            if (isValidSpawn(pos))
                return pos;
        }
        throw new IllegalStateException("Could not find a valid empty position after many attempts.");
    }

    public boolean isValidSpawn(Position position) {
        // null pos or not on board
        if (position == null || !board.isValidMove(position))
            return false;
        // reserved spot for escortee
        if (startPosition != null && position.equals(startPosition))
            return false;
        // reserved destination for escortee
        if (endPosition != null && position.equals(endPosition))
            return false;
        return board.getEntitiesAt(position).isEmpty();
    }

    private void initializeBoard() {board = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);}

    private void initializeObjectivePositions() {
        startPosition = new Position(0, board.getHeight() / 2);
        endPosition = new Position(board.getWidth() - 1, board.getHeight() / 2);

        destination = new Destination(endPosition);
        board.addEntity(destination);
    }

    private void initializePlayers() {
        if (players.size() >= 1) {
            Position p1Start = new Position(0, 0);
            players.get(0).setSpawnPosition(p1Start);
            players.get(0).reset();
        }
        if (players.size() >= 2) {
            Position p2Start = new Position(0, 1);
            players.get(1).setSpawnPosition(p2Start);
            players.get(1).reset();
        }
        for (PlayableCharacter player : players)
            board.addEntity(player);
    }

    private void initializeEscortee() {
        escortee = new Escortee(startPosition, DEFAULT_ESCORTEE_SYMBOL);
        board.addEntity(escortee);
    }

    public static class Builder {
        private long seed = System.currentTimeMillis();
        private Difficulty difficulty = Difficulty.MEDIUM;
        private Realm realm = new FireRealm();
        private List<PlayableCharacter> players = new ArrayList<>();

        public Builder setSeed(long seed) {
            this.seed = seed;
            return this;
        }

        public Builder setDifficulty(Difficulty difficulty) {
            if (difficulty == null)
                throw new IllegalArgumentException("Difficulty cannot be null.");
            this.difficulty = difficulty;
            return this;
        }

        public Builder setRealm(Realm realm) {
            if (realm == null)
                throw new IllegalArgumentException("Realm cannot be null.");
            this.realm = realm;
            return this;
        }

        public Builder setPlayers(PlayableCharacter... players) {
            if (players == null)
                throw new IllegalArgumentException("Players cannot be null.");
            this.players = new ArrayList<>(Arrays.asList(players));
            validatePlayers(this.players);
            return this;
        }

        public Builder setPlayers(List<PlayableCharacter> players) {
            if (players == null)
                throw new IllegalArgumentException("Players cannot be null.");
            this.players = new ArrayList<>(players);
            validatePlayers(this.players);
            return this;
        }

        public EscortGame build() {
            if (players.isEmpty())
                players = createDefaultPlayers();
            validatePlayers(players);
            return new EscortGame(this);
        }

        private void validatePlayers(List<PlayableCharacter> players) {
            if (players.size() != 2)
                throw new IllegalArgumentException("EscortGame needs 2 players.");
            for (PlayableCharacter player : players) {
                if (player == null)
                    throw new IllegalArgumentException("Player list cannot contain null.");
            }
        }

        private List<PlayableCharacter> createDefaultPlayers() {
            List<PlayableCharacter> defaults = new ArrayList<>();
            defaults.add(new PlayableCharacter("Player 1", new Position(0, 0), "\u0031\u20e3", 20));
            defaults.add(new PlayableCharacter("Player 2", new Position(1, 0), "\u0032\u20e3", 20));
            return defaults;
        }
    }
}
