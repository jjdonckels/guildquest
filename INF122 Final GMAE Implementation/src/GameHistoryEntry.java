import java.util.ArrayList;
import java.util.List;

//a single completed game
//stores info so we can track past games
public class GameHistoryEntry {
    private final String gameName;
    private final long seed;
    private final List<String> players;
    private final GameResult result;

    //constructor for creating game history after game ends
    public GameHistoryEntry(String gameName, long seed, List<String> players, GameResult result) {
        if (gameName == null || gameName.isBlank())
            throw new IllegalArgumentException("Game name cannot be null or blank.");
        if (players == null || players.isEmpty())
            throw new IllegalArgumentException("Players cannot be null or empty.");
        if (result == null)
            throw new IllegalArgumentException("Result cannot be null.");

        this.gameName = gameName;
        this.seed = seed;
        this.players = new ArrayList<>(players);
        this.result = result;
    }
    //getter methods
    public String getGameName() { return gameName; }

    public long getSeed() { return seed; }

    public List<String> getPlayers() { return new ArrayList<>(players); }

    public GameResult getResult() { return result; }

    //converts to readble string 
    @Override
    public String toString() {
        return gameName
                + " | Seed: " + seed
                + " | Players: " + players
                + " | Result: " + result;
    }
}