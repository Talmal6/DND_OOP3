package dnd.Backend.PlayerCharacters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dnd.Backend.GameManager;
import dnd.Backend.Player;
import dnd.Backend.Position2D;
import dnd.Backend.Report;
import dnd.Backend.Unit;

public class Hunter extends Player {
    protected int range;
    protected int arrowCount;
    protected int tickCount;

    protected final int ARROWS_PER_LEVEL = 10;
    protected final int EXTRA_ATK_PER_LVL = 2;
    protected final int EXTRA_DEF_PER_LVL = 1;
    protected final int ARROW_REGEN_TICKS = 10;
    protected final int ARROW_REGEN_PER_LEVEL = 1;

    public Hunter(String _name, int _maxHealth, int _attack, int _defense, int _range, Position2D _position) {
        super(_name, _maxHealth, _attack, _defense, _position);
        range = _range;
        arrowCount = 0;
        tickCount = 0;
        type = Type.HUNTER;
    }

    public HashMap<StatPerLevel, Integer> getStatsPerLevel() {
        HashMap<StatPerLevel, Integer> stats = super.getStatsPerLevel();
        stats.put(StatPerLevel.EXTRA_ATK, EXTRA_ATK_PER_LVL);
        stats.put(StatPerLevel.EXTRA_DEF, EXTRA_DEF_PER_LVL);
        stats.put(StatPerLevel.ARROWS, ARROWS_PER_LEVEL);
        stats.put(StatPerLevel.ARROW_REGEN, ARROW_REGEN_PER_LEVEL);
        return stats;
    }

    public void levelUp() {
        super.levelUp();
        setAttack(attack + EXTRA_ATK_PER_LVL * level);
        setDefense(defense + EXTRA_DEF_PER_LVL * level);
        setArrowCount(arrowCount + ARROWS_PER_LEVEL * level);
    }

    public void tick() {
        if (tickCount == ARROW_REGEN_TICKS) {
            setArrowCount(arrowCount + ARROW_REGEN_PER_LEVEL * level);
            tickCount = 0;
        }
        tickCount++;
    }

    public void setArrowCount(int arrowCount) {
        assert arrowCount >= 0;
        this.arrowCount = arrowCount;
    }

    @Override
    public Report castAbility(List<Unit> targets) {
        if (arrowCount <= 0) {
            GameManager.addMessage("Ability is on cooldown");
            return null;
        }

        arrowCount--;
        List<Unit> targetsInRange = new ArrayList<Unit>();
        for (Unit unit : targets) {
            if (Position2D.getRange(unit.getPosition(), position) <= range) {
                targetsInRange.add(unit);
            }
        }

        if (targetsInRange.size() <= 0) {
            GameManager.addMessage(name + " used Shoot, but there were no targets in range");
            return new Report();
        }

        Unit target = null;
        for (Unit t : targetsInRange) {
            double distance = Position2D.getRange(t.getPosition(), position);
            if (target == null || distance < Position2D.getRange(target.getPosition(), position)) {
                target = t;
                break;
            }
        }

        int damage = target.takeDamage(attack);
        GameManager.addMessage(name + " used Shoot, hitting " + target.getName() + " for " + damage + " damage");
        return new Report(target);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + String.format(" | Arrow Count: %d", arrowCount);
    }

    public int getArrows() {
        return arrowCount;
    }

    public int getArrowRegen() {
        return ARROW_REGEN_PER_LEVEL * level;
    }
}
