import java.util.ArrayList;
import java.util.List;

//manages all game history entries
public class GameHistoryManager {
    private final List<GameHistoryEntry> history;

    //make empty history list
    public GameHistoryManager() {
        this.history = new ArrayList<>();
    }

    public void record(GameHistoryEntry entry) {
        if (entry == null)
            throw new IllegalArgumentException("History entry cannot be null.");
        history.add(entry);
    }

    public List<GameHistoryEntry> getHistory() {
        return new ArrayList<>(history);
    }

    public void printHistory() {
        if (history.isEmpty()) {
            System.out.println("No game history yet.");
            return;
        }

        //print all past games to console
        System.out.println("=== Game History ===");
        for (GameHistoryEntry entry : history)
            System.out.println(entry);
    }
}