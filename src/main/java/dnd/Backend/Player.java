package dnd.Backend;

import java.util.HashMap;

import dnd.Backend.GameTile.TileType;

public abstract class Player extends Unit implements HeroicUnit {

    public enum Type {
        MAGE,
        HUNTER,
        WARRIOR,
        ROUGE
    }

    public enum StatPerLevel {
        HEALTH,
        ATTACK,
        DEFENSE,
        EXTRA_HEALTH,
        EXTRA_ATK,
        EXTRA_DEF,
        POWER,
        MANA,
        ARROWS,
        ARROW_REGEN
    }

    protected int exp;
    protected int level;
    protected Type type;

    protected final int EXP_PER_LEVEL = 50;
    protected final int HEALTH_PER_LEVEL = 10;
    protected final int ATTACK_PER_LEVEL = 4;
    protected final int DEFENSE_PER_LEVEL = 1;

    public Player(String _name, int _maxHealth, int _attack, int _defense, Position2D _position) {
        super(_name, '@', _maxHealth, _attack, _defense, _position, UnitType.PLAYER);
        exp = 0;
        level = 1;
        gameTile = new GameTile(TileType.PLAYER, this);
    }

    protected class PlayerVisitor implements UnitVisitor {

        @Override
        public Integer visit(Enemy enemy) {
            return attack(enemy);
        }

        @Override
        public Integer visit(Player player) {
            // Do nothing
            return null;
        }
    }

    public HashMap<StatPerLevel, Integer> getStatsPerLevel() {
        HashMap<StatPerLevel, Integer> stats = new HashMap<>();
        stats.put(StatPerLevel.HEALTH, HEALTH_PER_LEVEL);
        stats.put(StatPerLevel.ATTACK, ATTACK_PER_LEVEL);
        stats.put(StatPerLevel.DEFENSE, DEFENSE_PER_LEVEL);
        stats.put(StatPerLevel.EXTRA_HEALTH, 0);
        stats.put(StatPerLevel.EXTRA_ATK, 0);
        stats.put(StatPerLevel.EXTRA_DEF, 0);
        stats.put(StatPerLevel.POWER, 0);
        stats.put(StatPerLevel.MANA, 0);
        stats.put(StatPerLevel.ARROWS, 0);
        stats.put(StatPerLevel.ARROW_REGEN, 0);
        return stats;
    }

    public Type getType() {
        return type;
    }

    public Integer interact(Unit otherUnit) {
        return otherUnit.accept(new PlayerVisitor());
    }

    @Override
    public Integer accept(UnitVisitor visitor) {
        return visitor.visit(this);
    }

    public void gainExp(int amount) {
        exp += amount;
        if (exp >= EXP_PER_LEVEL * level) {
            levelUp();
        }
    }

    public Integer attack(Enemy target) {
        int attackRoll = rand.nextInt(attack);
        return target.takeDamage(attackRoll);
    }

    protected void levelUp() {
        exp -= EXP_PER_LEVEL * level;
        level++;
        setMaxHealth(maxHealth + HEALTH_PER_LEVEL * level);
        setCurrentHealth(maxHealth);
        setAttack(attack + ATTACK_PER_LEVEL * level);
        setDefense(defense + DEFENSE_PER_LEVEL * level);
        GameManager.addMessage(name + " leveled up!");
    }

    public abstract void tick();

    public int getExpValue() {
        return -1;
    }

    public GameTile getGameTile() {
        return gameTile;
    }

    public String getInfo() {
        return String.format("%s\t %s   \tHealth: %d\tAttack: %d\tDefense: %d", name, type, maxHealth, attack,
                defense);
    }

    @Override
    public String getDescription() {
        return String.format("%s | %s | Level: %d | Health: %d/%d | Attack: %d | Defense: %d | Experience: %d/%d",
                name,
                type, level, currentHealth, maxHealth, attack, defense, exp, EXP_PER_LEVEL * level);
    }

    @Override
    public void die() {
        tileChar = 'X';
    }

    public int getLevel() {
        return level;
    }
}