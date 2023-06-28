package dnd.Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import dnd.Backend.CharacterFactory.PlayerCharacter;
import dnd.FrontEnd.GameCLI;

public class GameManager {
    private Scanner scanner;

    private static GameBoard board;
    private GameCLI gameCLI;
    private CharacterFactory cf;
    private static Stack<String> messageStack = new Stack<>();
    private static List<Enemy> allEnemies;

    private Player player;
    private Unit currentEnemy;

    private boolean gameStarted;
    private boolean isOver;

    // Input bindings
    private final String UP = "w";
    private final String DOWN = "s";
    private final String LEFT = "a";
    private final String RIGHT = "d";
    private final String WAIT = "q";
    private final String SPECIAL_ABILITY = "e";

    public GameManager() {
        isOver = false;
        gameStarted = false;
        allEnemies = new ArrayList<>();

        cf = new CharacterFactory();
        scanner = new Scanner(System.in);
        gameCLI = new GameCLI();
    }

    public void startGame() {
        CharacterFactory.PlayerCharacter playerCharacter = characterSelection();
        player = cf.choosePlayer(playerCharacter);
        allEnemies = new ArrayList<>();
        board = new GameBoard(cf, player);
        board.parseLevel();
        gameLoop();
    }

    private PlayerCharacter characterSelection() {
        PlayerCharacter character = null;
        int input = 0;
        do {
            gameCLI.clearConsole();
            gameCLI.printOpeningScreen(cf.getCharacters());
            try {
                input = scanner.nextInt();
                character = CharacterFactory.PlayerCharacter.values()[input - 1];
                gameStarted = true;
            } catch (Exception e) {
                scanner.nextLine();
                gameCLI.addMessage("Invalid input try again");
            }
        } while (!gameStarted);
        return character;
    }

    private void gameLoop() {
        while (!isOver) {
            // Check level end
            if (allEnemies.isEmpty() && !isOver) {
                levelEnd();
            }

            // Player turn
            boolean isValid;
            do {
                isValid = false;
                renderUI();
                gameCLI.render("Choose an action: ");
                String input = scanner.next();
                isValid = handleInput(input);
            } while (!isValid);

            // Enemy turn
            for (Enemy enemy : allEnemies) {
                Boolean outcome = false;
                do {
                    Position2D dir = enemy.turn(player);
                    if (dir != null) {
                        Position2D pos = Position2D.add(enemy.getPosition(), dir);
                        outcome = interaction(enemy.getGameTile(), pos);
                    }
                } while (outcome == null);
            }

            if (player.isDead()) {
                player.die();
                gameCLI.addMessage(player.getName() + " died");
                isOver = true;
                renderUI();
            }

            board.tick();
        }
        scanner.nextLine();
        renderUI();
        gameCLI.render("Press Enter key to continue...");
        scanner.nextLine();
    }

    private void renderUI() {
        gameCLI.clearConsole();
        gameCLI.renderPlayerBar(player);
        gameCLI.renderEnemyBar(currentEnemy);
        gameCLI.renderBoard(board);
        popMessages();
        gameCLI.printMessages();
    }

    public boolean handleInput(String input) {
        Position2D dir = null;
        switch (input) {
            case UP:
                dir = Position2D.UP;
                break;
            case DOWN:
                dir = Position2D.DOWN;
                break;
            case LEFT:
                dir = Position2D.LEFT;
                break;
            case RIGHT:
                dir = Position2D.RIGHT;
                break;
            case WAIT:
                return true;
            case SPECIAL_ABILITY:
                List<Unit> monsters = new ArrayList<>();
                for (Enemy enemy : allEnemies) {
                    if (enemy.getEnemyType() == Enemy.EnemyType.MONSTER) {
                        monsters.add(enemy);
                    }
                }
                Report report = player.castAbility(monsters);
                popMessages();
                if (report == null)
                    return false;
                if (report.units == null)
                    return true;
                for (Unit unit : report.units) {
                    if (unit.isDead())
                        enemyDied(unit);
                }
                return true;
            default:
                gameCLI.addMessage("Invalid input try again");
                return false;
        }

        Position2D newPosition = Position2D.add(player.getPosition(), dir);
        return processOutcome(interaction(player.getGameTile(), newPosition), newPosition);
    }

    public boolean processOutcome(Boolean outcome, Position2D newPosition) {
        // Invalid move
        if (outcome == null) {
            gameCLI.addMessage("Invalid move");
            return false;
        } else {
            // Interaction
            if (outcome) {
                currentEnemy = board.getTileAt(newPosition).getUnit();
                if (currentEnemy.isDead()) {
                    enemyDied(currentEnemy);
                    board.move(player.gameTile, newPosition);
                }
            } else // Move
                currentEnemy = null;
            return true;
        }
    }

    public Boolean interaction(GameTile actor, Position2D pos) {
        GameTile target = board.getTileAt(pos);
        // Empty => Move
        if (target == null) {
            board.move(actor, pos);
            return false;
        }

        // Wall => Invalid move
        if (target.getUnit() == null) {
            return null;
        }

        // Unit => Interact
        Unit actorUnit = actor.getUnit();
        Unit targetUnit = target.getUnit();
        Integer outcome = actorUnit.interact(targetUnit);
        if (outcome != null) {
            gameCLI.addMessage(actorUnit.getName() + " attacked " + targetUnit.getName() + " for " + outcome.toString()
                    + " damage");
            return true;
        }
        return null;
    }

    public void levelEnd() {
        gameCLI.addMessage("All enemies are dead");
        gameCLI.addMessage("Press Enter key to continue...");
        renderUI();
        scanner = new Scanner(System.in);
        scanner.nextLine();
        if (!board.advanceLevel()) {
            gameCLI.addMessage("You have reached the end of the dungeon!");
            isOver = true;
            System.exit(0);
        } else {
            gameCLI.addMessage("You have entered to the next level of the dungeon");
        }
    }

    private void enemyDied(Unit dead) {
        dead.die();
        player.gainExp(dead.getExpValue());
        board.setTileAt(null, dead.getPosition());
        gameCLI.addMessage(
                player.getName() + " killed " + dead.getName() + " and gained " + dead.getExpValue() + " experience");
        currentEnemy = null;
    }

    public static boolean isTileEmpty(Position2D pos) {
        return board.getTileAt(pos) == null;
    }

    private void popMessages() {
        while (!messageStack.isEmpty())
            gameCLI.addMessage(messageStack.pop());
    }

    public static void addMessage(String message) {
        messageStack.push(message);
    }

    public static void removeEnemy(Enemy enemy) {
        allEnemies.remove(enemy);
    }

    public static void addEnemy(Enemy enemy) {
        allEnemies.add(enemy);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isOver() {
        return isOver;
    }

    // for testing
    public void setPlayer(Player player) {
        this.player = player;
    }
}
