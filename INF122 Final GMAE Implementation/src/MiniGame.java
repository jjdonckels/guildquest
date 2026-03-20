public interface MiniGame {
    void start();
    void reset();
    boolean checkWinCondition();
    boolean checkLoseCondition();
}
