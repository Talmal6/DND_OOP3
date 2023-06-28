package dnd.Backend;

public interface UnitVisitor {
    public Integer visit(Enemy enemy);
    public Integer visit(Player player);
}
