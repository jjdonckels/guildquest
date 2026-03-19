public interface NaturalHazard extends Hazard {
    int getSeverity();
    void applyToPlayers(Player player1, Player player2, String choice);
}