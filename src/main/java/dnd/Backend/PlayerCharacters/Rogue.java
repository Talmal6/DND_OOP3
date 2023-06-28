package dnd.Backend.PlayerCharacters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dnd.Backend.GameManager;
import dnd.Backend.Player;
import dnd.Backend.Position2D;
import dnd.Backend.Report;
import dnd.Backend.Unit;

public class Rogue extends Player {
    protected int energyCost;
    protected int currentEnergy;

    protected final int EXTRA_ATK_PER_LVL = 3;
    protected final int ABILITY_RANGE = 2;
    protected final int MAX_ENERGY = 100;

    public Rogue(String _name, int _maxHealth, int _attack, int _defense, int _cost, Position2D _position) {
        super(_name, _maxHealth, _attack, _defense, _position);
        energyCost = _cost;
        currentEnergy = MAX_ENERGY;
        type = Type.ROUGE;
    }

    public HashMap<StatPerLevel, Integer> getStatsPerLevel() {
        HashMap<StatPerLevel, Integer> statPerLevel = super.getStatsPerLevel();
        statPerLevel.put(StatPerLevel.EXTRA_ATK, EXTRA_ATK_PER_LVL);
        return statPerLevel;
    }

    public void levelUp() {
        super.levelUp();
        setCurrentEnergy(MAX_ENERGY);
        setAttack(attack + EXTRA_ATK_PER_LVL * level);
    }

    public void tick() {
        setCurrentEnergy(Math.min(currentEnergy + 10, MAX_ENERGY));
    }

    public void setCurrentEnergy(int _currentEnergy) {
        currentEnergy = _currentEnergy;
    }

    @Override
    public Report castAbility(List<Unit> targets) {
        if (currentEnergy < energyCost) {
            GameManager.addMessage("Not enough energy to use ability");
            return null;
        }

        currentEnergy -= energyCost;
        List<Unit> targetsInRange = new ArrayList<>();
        for (Unit target : targets) {
            if (Position2D.getRange(position, target.getPosition()) < ABILITY_RANGE) {
                targetsInRange.add(target);
            }
        }

        if (targets.size() <= 0) {
            GameManager.addMessage(name + " used Fan of Knives, but there were no targets in range");
            return new Report();
        }

        List<Unit> hitResult = new ArrayList<>();
        StringBuilder message = new StringBuilder(name + " used Fan of Knives:");
        for (Unit target : targets) {
            int damage = target.takeDamage(attack);
            message.append("\n\tFan of Knives hit " + target.getName() + " for " + damage + " damage");
            hitResult.add(target);
        }
        GameManager.addMessage(message.toString());
        return new Report(hitResult);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + String.format(" | Energy: %d/%d", currentEnergy, MAX_ENERGY);
    }
}
