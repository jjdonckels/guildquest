import java.util.List;

public class Escortee extends Entity {
    private static final int DEFAULT_MAX_HEALTH = 150;

    public Escortee(Position position, String symbol) {
        super(position, DEFAULT_MAX_HEALTH, symbol);
    }

    public void followClosestPlayer(List<PlayableCharacter> players) {
        if (players == null || players.isEmpty())
            throw new IllegalArgumentException("Players list cannot be null or empty.");
        PlayableCharacter closestPlayer = getClosestLivingPlayer(players);
        if (closestPlayer != null)
            moveToward(closestPlayer.getPosition());
    }

    public void followEndPoint(Position endPosition) {
        if (endPosition == null)
            throw new IllegalArgumentException("End position cannot be null.");
        moveToward(endPosition);
    }

    public void followClosestPlayerOrEndPoint(List<PlayableCharacter> players, Position endPosition) {
        if (endPosition == null)
            throw new IllegalArgumentException("End position cannot be null.");
        if (players == null || players.isEmpty()) {
            moveToward(endPosition);
            return;
        }
        PlayableCharacter closestPlayer = getClosestLivingPlayer(players);
        if (closestPlayer == null) {
            moveToward(endPosition);
            return;
        }
        int distanceToPlayer = linearDistance(getPosition(), closestPlayer.getPosition());
        int distanceToEnd = linearDistance(getPosition(), endPosition);
        if (distanceToEnd <= distanceToPlayer)
            moveToward(endPosition);
        else
            moveToward(closestPlayer.getPosition());
    }

    private PlayableCharacter getClosestLivingPlayer(List<PlayableCharacter> players) {
        PlayableCharacter closestPlayer = null;
        int bestDistance = Integer.MAX_VALUE;
        for (PlayableCharacter player : players) {
            if (player == null || !player.isAlive())
                continue;
            int distance = linearDistance(getPosition(), player.getPosition());
            if (distance < bestDistance) {
                bestDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    private void moveToward(Position target) {
        Position current = getPosition();
        int newX = current.getX();
        int newY = current.getY();
        if (target.getX() > current.getX())
            newX++;
        else if (target.getX() < current.getX())
            newX--;
        else if (target.getY() > current.getY())
            newY++;
        else if (target.getY() < current.getY())
            newY--;
        move(new Position(newX, newY));
    }

    private int linearDistance(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    @Override
    public String toString() {
        return "{Escortee "
                + "id: " + getId()
                + ", position=" + getPosition()
                + ", health=" + getHealth() + "/" + getMaxHealth()
                + ", symbol='" + getSymbol() + "'}";
    }
}