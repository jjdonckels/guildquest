import java.util.Objects;

public class Position
{
    private final int x, y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {return x;}

    public int getY() {return y;}

    public Position translate(Direction direction) {
        return new Position(
                x + direction.getDx(),
                y + direction.getDy()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {return Objects.hash(x, y);}

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
