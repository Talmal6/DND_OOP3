package dnd.Backend;

public class GameTile {

    public enum TileType {
        WALL,
        PLAYER,
        ENEMY
    }

    protected final TileType type;
    protected final Unit unit;

    public GameTile(TileType _type, Unit _unit) {
        type = _type;
        unit = _unit;
    }

    public TileType getType() {
        return type;
    }

    public Position2D getPosition() {
        if (unit != null) {
            return unit.getPosition();
        } else {
            return null;
        }
    }

    public void setPosition(Position2D pos) {
        unit.setPosition(pos);
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return unit.toString();
    }
}
