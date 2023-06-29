package dnd.Backend;

public class Position2D {

    public static final Position2D ZERO = new Position2D(0, 0);
    public static final Position2D UP = new Position2D(0, -1);
    public static final Position2D DOWN = new Position2D(0, 1);
    public static final Position2D LEFT = new Position2D(-1, 0);
    public static final Position2D RIGHT = new Position2D(1, 0);
    public static Position2D[] Directions = { UP, DOWN, LEFT, RIGHT };

    public int x;
    public int y;

    public Position2D(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public static Position2D add(Position2D pos1, Position2D pos2) {
        return new Position2D(pos1.x + pos2.x, pos1.y + pos2.y);
    }

    public static double getRange(Position2D pos1, Position2D pos2) {
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position2D))
            return false;
        Position2D other = (Position2D) o;
        return o == this || (x == other.x && y == other.y);
    }
}