package dnd.Backend.PlayerCharacters;

import dnd.Backend.GameManager;
import dnd.Backend.Player;
import dnd.Backend.Position2D;
import dnd.Backend.Report;
import dnd.Backend.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Warrior extends Player {

    protected int abilityCooldown;
    protected int currentCooldown;

    protected final int ABILITY_RANGE = 3;
    protected final float ABILITY_DAMAGE = 0.1f;
    protected final int ABILITY_HEAL = 10;
    protected final int EXTRA_HP_PER_LVL = 5;
    protected final int EXTRA_ATK_PER_LVL = 2;
    protected final int EXTRA_DEF_PER_LVL = 1;

    public Warrior(String _name, int _maxHealth, int _attack, int _defense, int _abilityCooldown,
            Position2D _position) {
        super(_name, _maxHealth, _attack, _defense, _position);
        abilityCooldown = _abilityCooldown;
        type = Type.WARRIOR;
    }

    public HashMap<StatPerLevel, Integer> getStatsPerLevel() {
        HashMap<StatPerLevel, Integer> stats = super.getStatsPerLevel();
        stats.put(StatPerLevel.EXTRA_HEALTH, EXTRA_HP_PER_LVL);
        stats.put(StatPerLevel.EXTRA_ATK, EXTRA_ATK_PER_LVL);
        stats.put(StatPerLevel.EXTRA_DEF, EXTRA_DEF_PER_LVL);
        return stats;
    }

    public Report castAbility(List<Unit> targets) {
        if (currentCooldown > 0) {
            GameManager.addMessage("Ability is on cooldown");
            return null;
        }

        currentCooldown = abilityCooldown;
        List<Unit> targetsInRange = new ArrayList<>();
        for (Unit target : targets) {
            if (Position2D.getRange(position, target.getPosition()) < ABILITY_RANGE)
                targetsInRange.add(target);
        }

        if (targets.size() <= 0) {
            GameManager.addMessage(name + " used Avenger's Shield, but there were no targets in range");
            return new Report();
        }

        Unit target = targets.get(rand.nextInt(targets.size()));
        int heal = heal(ABILITY_HEAL * defense);
        int damage = target.takeDamage((int) (ABILITY_DAMAGE * maxHealth));
        GameManager.addMessage(name + " used Avenger's Shield, healing for " + heal +
                " and hitting " + target.getName() + " for " + damage + " damage");
        return new Report(target);
    }

    public void levelUp() {
        super.levelUp();
        currentCooldown = 0;
        setMaxHealth(maxHealth + EXTRA_HP_PER_LVL * level);
        setAttack(attack + EXTRA_ATK_PER_LVL * level);
        setDefense(defense + EXTRA_DEF_PER_LVL * level);
    }

    public void tick() {
        if (currentCooldown > 0)
            currentCooldown--;
    }

    public int getAbilityRange() {
        return ABILITY_RANGE;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + String.format(" | Ability Cooldown: %d/%d", currentCooldown, abilityCooldown);
    }
}