package dnd.Backend;

import java.util.Random;

public abstract class Unit {

    protected enum UnitType{
        PLAYER,
        ENEMY
    }

    protected UnitType unitType;
    protected char tileChar;
    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected int expValue;
    protected boolean isDead;
    protected Position2D position;
    protected GameTile gameTile;

    protected Random rand = new Random();

    public Unit(String _name, char _tile, int _maxHealth, int _attack, int _defense, Position2D _position, UnitType _type) {
        name = _name;
        tileChar = _tile;
        maxHealth = _maxHealth;
        attack = _attack;
        defense = _defense;
        currentHealth = maxHealth;
        position = _position;
        isDead = false;
        unitType = _type;
    }

    public int takeDamage(int incomingDamage) {
        int defenceRoll = rand.nextInt(defense);
        int damage = Math.max(incomingDamage - defenceRoll, 0);
        currentHealth = Math.max(currentHealth - damage, 0);
        if(currentHealth <= 0)
            isDead = true;
        return damage;
    }

    public abstract void die();

    public void tick() {
        //Do nothing
    };

    public abstract Integer accept(UnitVisitor visitor);

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setMaxHealth(int _maxHealth) {
        maxHealth = _maxHealth;
    }

    public void setCurrentHealth(int _currentHealth) {
        assert _currentHealth <= maxHealth;
        assert _currentHealth >= 0;
        currentHealth = _currentHealth;
    }

    public int heal(int amount){
        assert amount >= 0;
        int h = currentHealth;
        currentHealth = Math.min(currentHealth + amount, maxHealth);
        return currentHealth - h;
    }

    public void setAttack(int _attack) {
        attack = _attack;
    }

    public void setDefense(int _defense) {
        defense = _defense;
    }

    public boolean isDead(){
        return isDead;
    }

    public void setPosition(Position2D pos){
        position = pos;
    }

    public Position2D getPosition(){
        return position;
    }
    
    public abstract Integer interact(Unit target);

    public abstract int getExpValue();

    public abstract String getDescription();

    @Override
    public String toString(){
        return String.valueOf(tileChar);
    }
}
