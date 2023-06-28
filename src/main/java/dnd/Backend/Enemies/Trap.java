package dnd.Backend.Enemies;

import dnd.Backend.Enemy;
import dnd.Backend.Position2D;
import dnd.Backend.Unit;

public class Trap extends Enemy {
    protected int visibilityRange;
    protected int visibilityTime;
    protected int invisibilityTime;
    protected int ticksCount;
    protected boolean isVisible;

    private final int RANGE = 2;

    public Trap(String _name, char _tile, int _maxHealth, int _attack, int _defense, int _expValue, int _visibilityTime,
            int _invisibilityTime, Position2D _position) {
        super(_name, _tile, _maxHealth, _attack, _defense, _expValue, _position, EnemyType.TRAP);
        // visibilityRange = _visibilityRange;
        visibilityTime = _visibilityTime;
        invisibilityTime = _invisibilityTime;
        ticksCount = 0;
        isVisible = true;
    }

    @Override
    public Position2D turn(Unit player) {
        isVisible = ticksCount < visibilityTime;
        if (ticksCount == (visibilityTime + invisibilityTime)) {
            ticksCount = 0;
        } else {
            ticksCount++;
        }
        if (Position2D.getRange(position, player.getPosition()) < RANGE) {
            interact(player);
        }
        return null;
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String toString() {
        if (isVisible) {
            return super.toString();
        } else {
            return ".";
        }
    }
}