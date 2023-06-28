package dnd.Backend.Enemies;

import java.util.Arrays;

import dnd.Backend.Enemy;
import dnd.Backend.GameManager;
import dnd.Backend.Position2D;
import dnd.Backend.Unit;

public class Monster extends Enemy {
    protected int visionRange;
    protected int expValue;
    protected int attackRange;
    protected int attackPoints;
    protected int defensePoints;

    public Monster(String _name, char _char, int _maxHealth, int _attack, int _defense, int _visionRange, int _expValue,
            Position2D _position) {
        super(_name, _char, _maxHealth, _attack, _defense, _expValue, _position, EnemyType.MONSTER);
        visionRange = _visionRange;
    }

    public Position2D turn(Unit player) {
        Position2D playerPosition = player.getPosition();
        if (Position2D.getRange(position, playerPosition) < visionRange) {
            int dx = playerPosition.x - position.x;
            int dy = playerPosition.y - position.y;
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    if (GameManager.isTileEmpty(Position2D.add(position, Position2D.RIGHT))
                            || Position2D.add(position, Position2D.RIGHT).equals(playerPosition))
                        return Position2D.RIGHT;
                    else
                        return null;
                } else {
                    if (GameManager.isTileEmpty(Position2D.add(position, Position2D.LEFT))
                            || Position2D.add(position, Position2D.LEFT).equals(playerPosition))
                        return Position2D.LEFT;
                    else
                        return null;
                }
            } else {
                if (dy > 0) {
                    if (GameManager.isTileEmpty(Position2D.add(position, Position2D.DOWN))
                            || Position2D.add(position, Position2D.DOWN).equals(playerPosition))
                        return Position2D.DOWN;
                    else
                        return null;
                } else {
                    if (GameManager.isTileEmpty(Position2D.add(position, Position2D.UP))
                            || Position2D.add(position, Position2D.UP).equals(playerPosition))
                        return Position2D.UP;
                    else
                        return null;
                }
            }
        } else {
            // Return a random direction
            Position2D[] directions = { Position2D.UP, Position2D.DOWN, Position2D.LEFT, Position2D.RIGHT };
            return Arrays.stream(directions).skip(rand.nextInt(directions.length)).findFirst().orElse(null);
        }
    }
}