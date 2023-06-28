package dnd.Backend;

import dnd.Backend.GameTile.TileType;

public abstract class Enemy extends Unit {

    public enum EnemyType {
        MONSTER,
        TRAP
    }

    protected int expValue;
    protected EnemyType enemyType;

    public Enemy(String _name, Character _tile, int _maxHealth, int _attack, int _defense, int _expValue,
            Position2D _position, EnemyType _enemyType) {
        super(_name, _tile, _maxHealth, _attack, _defense, _position, UnitType.ENEMY);
        expValue = _expValue;
        gameTile = new GameTile(TileType.ENEMY, this);
        enemyType = _enemyType;
    }

    public int getExpValue() {
        return expValue;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    protected class EnemyVisitor implements UnitVisitor {

        @Override
        public Integer visit(Enemy enemy) {
            // Do nothing
            return null;
        }

        @Override
        public Integer visit(Player player) {
            return attack(player);
        }
    }

    public Integer attack(Player target) {
        int attackRoll = rand.nextInt(attack);
        return target.takeDamage(attackRoll);
    }

    @Override
    public Integer interact(Unit otherUnit) {
        return otherUnit.accept(new EnemyVisitor());
    }

    @Override
    public Integer accept(UnitVisitor visitor) {
        return visitor.visit(this);
    }

    public GameTile getGameTile() {
        return gameTile;
    }

    public void die() {
        GameManager.removeEnemy(this);
    }

    @Override
    public void tick() {
        // Do nothing
    }

    @Override
    public String getDescription() {
        return String.format("%s | Health: %d/%d | Attack: %d | Defense: %d | Experience: %d",
                name, currentHealth, maxHealth, attack, defense, expValue);
    }

    public abstract Position2D turn(Unit player);
}